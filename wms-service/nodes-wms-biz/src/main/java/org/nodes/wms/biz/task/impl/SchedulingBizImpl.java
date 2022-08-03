package org.nodes.wms.biz.task.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.task.SchedulingBiz;
import org.nodes.wms.biz.task.TaskBiz;
import org.nodes.wms.biz.task.modular.TaskDetailFactory;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeCodeEnum;
import org.nodes.wms.dao.basics.zone.constant.ZoneConstant;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.common.log.dto.input.NoticeMessageRequest;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.task.WmsTaskDao;
import org.nodes.wms.dao.task.dto.QueryAndFrozenEnableOutboundRequest;
import org.nodes.wms.dao.task.dto.SchedulingBroadcastNotificationRequest;
import org.nodes.wms.dao.task.dto.SyncTaskStateRequest;
import org.nodes.wms.dao.task.entities.TaskDetail;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulingBizImpl implements SchedulingBiz {
	private final LocationBiz locationBiz;
	private final ZoneBiz zoneBiz;
	private final LogBiz logBiz;
	private final TaskDetailFactory taskDetailFactory;
	private final TaskBiz taskBiz;
	private final StockQueryBiz stockQueryBiz;
	private final StockBiz stockBiz;
	private final WmsTaskDao wmsTaskDao;

	@Override
	public String selectAndFrozenEnableOutbound(QueryAndFrozenEnableOutboundRequest request) {
		// 根据箱型（ABC）获取出库接驳区的库位/D箱人工拣货区库位
		String area = ZoneConstant.ZONE_CODE_AGV_SHIPMENT_CONNECTION_AREA;
		if (Func.equals(request.getLpnTypeCode(), LpnTypeCodeEnum.D)) {
			area = ZoneConstant.ZONE_CODE_D_PICK_AREA;
		}
		Zone zone = zoneBiz.findByCode(area);
		List<Location> locationList = locationBiz.findLocationByZoneId(zone.getZoneId());
		String locCode = "";
		for (Location location : locationList) {
			// 判断库位是否有库存
			if (location.enableStock() && stockQueryBiz.isEmptyLocation(location.getLocId())) {
				// 如果没有库存则冻结
				locationBiz.freezeLocByTask(location.getLocId(), request.getTaskDetailId().toString());
				locCode = location.getLocCode();
				break;
			}
		}
		return locCode;
	}

	@Override
	public void broadcastNotificationActivity(List<SchedulingBroadcastNotificationRequest> request) {
		for (SchedulingBroadcastNotificationRequest notificationRequest : request) {
			NoticeMessageRequest message = new NoticeMessageRequest();
			message.setLog(String.format("任务[%s]：[%s]",
					notificationRequest.getTaskDetailId(), notificationRequest.getMsg()));
			logBiz.noticeMesssage(message);
		}
	}

	@Override
	public void synchronizeTaskStatus(SyncTaskStateRequest request) {
		WmsTask wmsTask = wmsTaskDao.getById(request.getTaskDetailId());

		if (Func.equals(request.getState(), 1)) {
			onStart(wmsTask);
		}
	}

	private void onStart(WmsTask wmsTask) {
		// 修改任务状态
		wmsTaskDao.updateState(wmsTask.getTaskId(), WmsTaskStateEnum.START_EXECUTION);
		// 将原库位库存移动到中间库位
		List<Stock> stockList = stockQueryBiz.findStockByTaskId(wmsTask.getTaskId());
		Location tempLoc = locationBiz.getInTransitLocation(wmsTask.getWhId());
		for (Stock stock : stockList) {
			// stockBiz.moveAllStock(stock,); TODO
		}
	}

	public void onSuccess(Long taskDetailId) {
		// 修改任务状态
		// 将中间库位的库存移动到目标库位
		// 解冻目标库位
	}

	private void onException(Long taskDetailId) {

		// 修改任务状态

	}
}
