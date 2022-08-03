package org.nodes.wms.biz.task;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.systemParam.SystemParamBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.putway.PutwayStrategyActuator;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.task.factory.PublishJobFactory;
import org.nodes.wms.biz.task.factory.WmsTaskFactory;
import org.nodes.wms.biz.task.util.SendToScheduleUtil;
import org.nodes.wms.dao.application.dto.scheduling.SchedulingGlobalResponse;
import org.nodes.wms.dao.application.dto.scheduling.SchedulingResponse;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.task.WmsTaskDao;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * 天宜定制：agv调度任务
 *
 * @author caiyun
 */
@Service
@RequiredArgsConstructor
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

		WmsTask putwayTask = wmsTaskFactory.create(stocks.get(0));
		wmsTaskDao.save(putwayTask);
		// 调用上架策略生成目标库位，并把目标库位保存到任务表中
		Location targetLoc = putwayStrategyActuator.run(BigDecimal.ZERO, stocks);
		if (!targetLoc.getLocId().equals(locationBiz.getUnknowLocation(stocks.get(0).getWhId()).getLocId())) {
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
	 * @param putwayTask
	 * @return true:发送成功
	 */
	public boolean sendToSchedule(List<WmsTask> putwayTask) {
		String url = systemParamBiz.findScheduleUrl().concat(POST_JOB_API);

		SchedulingGlobalResponse schedulingGlobalResponse = sendToScheduleUtil.sendPost(url, publishJobFactory.createPublishJobRequestList(putwayTask));
		SchedulingResponse schedulingResponse = schedulingGlobalResponse.getSchedulingResponse();
		return schedulingResponse.hasFailed();
	}

	public void moveStockToSchedule() {

	}

	public void pickToSchedule() {

	}
}
