package org.nodes.wms.biz.task;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.systemParam.SystemParamBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.putway.PutwayStrategyActuator;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.task.factory.PublishJobFactory;
import org.nodes.wms.biz.task.factory.WmsTaskFactory;
import org.nodes.wms.biz.task.util.SendToScheduleUtil;
import org.nodes.wms.dao.application.dto.scheduling.SchedulingGlobalResponse;
import org.nodes.wms.dao.application.dto.scheduling.SchedulingResponse;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.task.WmsTaskDao;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 天宜定制：agv调度任务
 *
 * @author caiyun
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AgvTask {

	private static final String POST_JOB_API = "/api/wms/publishJob";

	private final LocationBiz locationBiz;
	private final WmsTaskFactory wmsTaskFactory;
	private final PutwayStrategyActuator putwayStrategyActuator;
	private final WmsTaskDao wmsTaskDao;
	private final StockBiz stockBiz;
	private final PublishJobFactory publishJobFactory;
	private final SystemParamBiz systemParamBiz;
	private final SendToScheduleUtil sendToScheduleUtil;
	private final StockQueryBiz stockQueryBiz;

	/**
	 * 生成AGV上架任务
	 *
	 * @param stocks 需要上架的库存
	 */
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void putwayToSchedule(List<Stock> stocks) {
		// 判断是否是库位是否是自动化临时区，如果是则生成上架任务
		if (!locationBiz.isAgvTempOfZoneType(stocks.get(0).getLocId())) {
			return;
		}

		WmsTask putwayTask = wmsTaskFactory.createPutwayTask(stocks);
		wmsTaskDao.save(putwayTask);
		// 调用上架策略生成目标库位，并把目标库位保存到任务表中
		Location targetLoc = putwayStrategyActuator.run(BigDecimal.ZERO, stocks);
		if (!targetLoc.getLocId().equals(locationBiz.getUnknowLocation(stocks.get(0).getWhId()).getLocId())) {
			putwayTask.setToLocId(targetLoc.getLocId());
			putwayTask.setToLocCode(targetLoc.getLocCode());
			// 如果计算得到了目标库位，则发送到调度系统
			if (sendToSchedule(Collections.singletonList(putwayTask))) {
				putwayTask.setTaskState(WmsTaskStateEnum.ISSUED);
			}
			// 调度系统接收成功之后冻结目标库位和冻结原库位的库存
			locationBiz.freezeLocByTask(targetLoc.getLocId(), putwayTask.getTaskId().toString());
			stockBiz.freezeStockByTask(stocks, false, putwayTask.getTaskId());
		}

		// 更新任务
		wmsTaskDao.updateById(putwayTask);
	}

	/**
	 * 发送任务到调度系统
	 *
	 * @param putwayTask agv任务
	 * @return true:发送成功
	 */
	public boolean sendToSchedule(List<WmsTask> putwayTask) {
		String url = systemParamBiz.findScheduleUrl().concat(POST_JOB_API);

		SchedulingGlobalResponse schedulingGlobalResponse = sendToScheduleUtil.sendPost(
				url, publishJobFactory.createPublishJobRequestList(putwayTask));
		SchedulingResponse schedulingResponse = schedulingGlobalResponse.getSchedulingResponse();
		return schedulingResponse.hasFailed();
	}

	/**
	 * AGV库内移动,只有原和目标库位都是agv存储区的才有效
	 * 一次只能移动一个库位的库存，否则抛异常
	 *
	 * @param sourceStock 移动的原库存
	 * @param targetLocId 目标库位
	 * @return true:表示触发了agv的移动任务，false：表示不符合创建agv移动任务
	 */
	public boolean moveStockToSchedule(List<Stock> sourceStock, Long targetLocId) {
		AssertUtil.notEmpty(sourceStock, "AGV库内移动任务下发失败,原库存为空无法移动");
		AssertUtil.notNull(targetLocId, "AGV库内移动任务下发失败,目标库位为空");

		List<Long> sourceIds = sourceStock.stream()
				.map(Stock::getLocId)
				.distinct()
				.collect(Collectors.toList());
		if (sourceIds.size() > 1) {
			throw new ServiceException("AGV库内移动任务下发失败，不支持同时移动多个库位的库存");
		}

		if (!locationBiz.isAgvZone(sourceIds.get(0))
				|| !locationBiz.isAgvZone(targetLocId)) {
			log.warn("AGV库内移动任务下发失败，AGV只能移动自动区的库存");
			return false;
		}
		
		WmsTask moveTask = wmsTaskFactory.createMoveTask(sourceStock, targetLocId);
		if (sendToSchedule(Collections.singletonList(moveTask))) {
			moveTask.setTaskState(WmsTaskStateEnum.ISSUED);
		}
		locationBiz.freezeLocByTask(targetLocId, moveTask.getTaskId().toString());
		stockBiz.freezeStockByTask(sourceStock, false, moveTask.getTaskId());
		wmsTaskDao.save(moveTask);
		return true;
	}

	/**
	 * 拣货任务到agv，按库位下发
	 *
	 * @param locId    库位id
	 * @param so       出库单信息
	 * @param soDetail 出库单明细信息
	 */
	public void pickToSchedule(Long locId, SoHeader so, SoDetail soDetail) {
		AssertUtil.notNull(locId, "AGV拣货任务下发失败,locId为空");
		AssertUtil.notNull(so, "AGV拣货任务下发失败,so为空");
		AssertUtil.notNull(soDetail, "AGV拣货任务下发失败,soDetail为空");

		List<Stock> sourceStock = stockQueryBiz.findStockByLocation(locId);
		WmsTask pickTask = wmsTaskFactory.createPickTask(sourceStock, so, soDetail);
		if (sendToSchedule(Collections.singletonList(pickTask))) {
			pickTask.setTaskState(WmsTaskStateEnum.ISSUED);
		}
		stockBiz.freezeStockByTask(sourceStock, false, pickTask.getTaskId());
		wmsTaskDao.save(pickTask);
	}
}