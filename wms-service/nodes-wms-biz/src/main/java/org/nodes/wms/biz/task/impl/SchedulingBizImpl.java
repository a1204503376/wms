package org.nodes.wms.biz.task.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.task.SchedulingBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeCodeEnum;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.common.log.dto.input.NoticeMessageRequest;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.nodes.wms.dao.task.WmsTaskDao;
import org.nodes.wms.dao.task.dto.QueryAndFrozenEnableOutboundRequest;
import org.nodes.wms.dao.task.dto.SchedulingBroadcastNotificationRequest;
import org.nodes.wms.dao.task.dto.SyncTaskStateRequest;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 天宜定制 调度系统
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulingBizImpl implements SchedulingBiz {

	/**
	 * AGV开始执行
	 */
	private final Integer AGV_TASK_STATE_BEGIN = 5;
	/**
	 * AGV执行结束
	 */
	private final Integer AGV_TASK_STATE_END = 6;
	/**
	 * AGV异常中断
	 */
	private final Integer AGV_TASK_STATE_EXCEPTION = 51;

	private final LocationBiz locationBiz;
	private final ZoneBiz zoneBiz;
	private final LogBiz logBiz;
	private final StockQueryBiz stockQueryBiz;
	private final StockBiz stockBiz;
	private final WmsTaskDao wmsTaskDao;

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public String selectAndFrozenEnableOutbound(QueryAndFrozenEnableOutboundRequest request) {
		// 根据箱型（ABC）获取出库接驳区的库位/D箱人工拣货区库位
		String area = WmsAppConstant.ZONE_CODE_AGV_SHIPMENT_CONNECTION_AREA;
		if (Func.equals(request.getLpnTypeCode(), LpnTypeCodeEnum.D)) {
			area = WmsAppConstant.ZONE_CODE_D_PICK_AREA;
		}
		Zone zone = zoneBiz.findByCode(area);
		List<Location> locationList = locationBiz.findLocationByZoneId(zone.getZoneId());

		for (Location location : locationList) {
			// 判断库位是否有库存
			if (location.enableStock() && stockQueryBiz.isEmptyLocation(location.getLocId())) {
				// 如果没有库存则冻结库位
				locationBiz.freezeLocByTask(location.getLocId(), request.getTaskDetailId().toString());
				// 更新任务信息
				WmsTask wmsTask = wmsTaskDao.getById(request.getTaskDetailId());
				wmsTask.setToLocId(location.getLocId());
				wmsTask.setToLocCode(location.getLocCode());
				wmsTaskDao.updateById(wmsTask);
				return location.getLocCode();
			}
		}
		return "";
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void broadcastNotificationActivity(List<SchedulingBroadcastNotificationRequest> request) {
		for (SchedulingBroadcastNotificationRequest notificationRequest : request) {
			NoticeMessageRequest message = new NoticeMessageRequest();
			message.setLog(String.format("任务[%s]：[%s]",
					notificationRequest.getTaskDetailId(), notificationRequest.getMsg()));
			logBiz.noticeMesssage(message);
		}
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void synchronizeTaskStatus(SyncTaskStateRequest request) {
		WmsTask wmsTask = wmsTaskDao.getById(request.getTaskDetailId());
		AssertUtil.notNull(wmsTask, "任务状态变更通知失败,查不到对应的任务");

		if (AGV_TASK_STATE_BEGIN.equals(request.getState())) {
			onStart(wmsTask);
		} else if (AGV_TASK_STATE_END.equals(request.getState())) {
			onSuccess(wmsTask);
		} else if (AGV_TASK_STATE_EXCEPTION.equals(request.getState())) {
			onException(wmsTask);
		}

		log.info("接收调度系统任务状态变更通知,状态:%d,任务:%d",
				request.getState(), request.getTaskDetailId());
	}

	/**
	 * AGV开始执行任务，WMS系统的操作：
	 * 修改任务状态
	 * 将原库位库存转移到中间库位，中间库位的库存仍是冻结状态
	 *
	 * @param wmsTask 任务
	 */
	private void onStart(WmsTask wmsTask) {
		// 修改任务状态
		wmsTaskDao.updateState(wmsTask.getTaskId(), WmsTaskStateEnum.START_EXECUTION);
		// 将原库位库存移动到中间库位
		List<Stock> stockList = stockQueryBiz.findStockByDropId(wmsTask.getTaskId());
		for (Stock stock : stockList) {
			stockBiz.moveAllStockToDropId(stock, wmsTask.getTaskId().toString(), StockLogTypeEnum.STOCK_AGV_MOVE);
		}

		log.info("agv任务开始[%d]-[%s]", wmsTask.getTaskId(), wmsTask);
	}

	/**
	 * AGV执行完毕，WMS的操作有：
	 * 修改任务状态
	 * 移动库存到目标库位
	 * 解冻目标库存
	 * 解冻目标库位
	 *
	 * @param wmsTask 任务
	 */
	private void onSuccess(WmsTask wmsTask) {
		if (WmsTaskStateEnum.ISSUED.equals(wmsTask.getTaskState())) {
			throw new ServiceException(String.format(
					"任务执行完毕状态更新失败,任务[%d]当前状态[%s]不是开始执行状态",
					wmsTask.getTaskId(), wmsTask.getTaskState().getDesc()));
		}
		// 修改任务状态
		wmsTaskDao.updateState(wmsTask.getTaskId(), WmsTaskStateEnum.COMPLETED);
		locationBiz.unfreezeLocByTask(wmsTask.getTaskId().toString());
		// 将中间库位的库存移动到目标库位
		Location targetLoc = locationBiz.findByLocId(wmsTask.getToLocId());
		List<Stock> stockList = stockQueryBiz.findStockByDropId(wmsTask.getTaskId());
		List<Stock> targetStockList = new ArrayList<Stock>();
		for (Stock stock : stockList) {
			Stock targetStock = stockBiz.moveAllStockFromDropId(stock, targetLoc, wmsTask.getTaskId().toString(),
					StockLogTypeEnum.STOCK_AGV_MOVE);
			targetStockList.add(targetStock);
		}

		stockBiz.unfreezeStockByDropId(targetStockList, wmsTask.getTaskId());

		log.info("agv任务结束[%d]-[%s]", wmsTask.getTaskId(), wmsTask);
	}

	private void onException(WmsTask wmsTask) {
		if (WmsTaskStateEnum.COMPLETED.equals(wmsTask.getTaskState())){
			throw new ServiceException("状态更新失败,任务已经完成");
		}
		// 修改任务状态
		wmsTaskDao.updateState(wmsTask.getTaskId(), WmsTaskStateEnum.ABNORMAL);

		log.info("agv任务异常[%d]-[%s]", wmsTask.getTaskId(), wmsTask);
	}
}
