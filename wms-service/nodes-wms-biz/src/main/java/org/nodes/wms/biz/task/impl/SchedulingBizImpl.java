package org.nodes.wms.biz.task.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.outstock.plan.SoPickPlanBiz;
import org.nodes.wms.biz.putaway.PutawayStrategyActuator;
import org.nodes.wms.biz.putaway.modular.PutawayFactory;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.task.SchedulingBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeCodeEnum;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.common.log.dto.input.NoticeMessageRequest;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.putaway.PutawayLogDao;
import org.nodes.wms.dao.putaway.entities.PutawayLog;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.nodes.wms.dao.task.WmsTaskDao;
import org.nodes.wms.dao.task.dto.QueryAndFrozenEnableOutboundRequest;
import org.nodes.wms.dao.task.dto.SchedulingBroadcastNotificationRequest;
import org.nodes.wms.dao.task.dto.SyncTaskStateRequest;
import org.nodes.wms.dao.task.dto.input.NewLocationOnDoubleWarehousingRequest;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
	/**
	 * 已取消 （人工取消）
	 */
	private final Integer AGV_TASK_STATE_CANCEL_BY_AGV = 0;
	/**
	 * 调度系统发送到agv成功
	 */
	private final Integer SEND_AGV_RETURN_SUCCESS = 4;

	private final LocationBiz locationBiz;
	private final ZoneBiz zoneBiz;
	private final LogBiz logBiz;
	private final StockQueryBiz stockQueryBiz;
	private final StockBiz stockBiz;
	private final WmsTaskDao wmsTaskDao;
	private final SoPickPlanBiz soPickPlanBiz;
	private final LpnTypeBiz lpnTypeBiz;
	private final PutawayStrategyActuator putawayStrategyActuator;
	private final PutawayFactory putawayFactory;
	private final PutawayLogDao putawayLogDao;

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public String selectAndFrozenEnableOutbound(QueryAndFrozenEnableOutboundRequest request) {
		WmsTask wmsTask = wmsTaskDao.getById(request.getTaskDetailId());
		AssertUtil.notNull(wmsTask, "任务在WMS系统中不存在");
		if (Func.isNotEmpty(wmsTask.getToLocCode())) {
			throw ExceptionUtil.mpe("任务已经存在目标库位[{}]", wmsTask.getToLocCode());
		}

		// 根据箱型（ABC）获取出库接驳区的库位/D箱人工拣货区库位
		String zoneCode = WmsAppConstant.ZONE_CODE_AGV_SHIPMENT_CONNECTION_AREA;
		if (Func.equals(request.getLpnTypeCode(), WmsAppConstant.BOX_TYPE_D)) {
			zoneCode = WmsAppConstant.ZONE_CODE_D_PICK_AREA;
		}
		Zone zone = zoneBiz.findByCode(zoneCode);
		List<Location> locationList = locationBiz.findLocationByZoneId(zone.getZoneId());

		for (Location location : locationList) {
			LpnType locLpnType = lpnTypeBiz.findLpnTypeById(location.getLpnTypeId());
			LpnTypeCodeEnum requestParseBoxCode = lpnTypeBiz.parseBoxCode(request.getLpnTypeCode());
			// 判断库位是否有库存
			if (location.enableStock() && stockQueryBiz.isEmptyLocation(location.getLocId())
				&& locLpnType.getCode().equals(requestParseBoxCode.getCode())) {
				// 如果没有库存则冻结库位
				locationBiz.freezeLocByTask(location.getLocId(), request.getTaskDetailId().toString());
				// 更新任务信息
				wmsTask.setToLocId(location.getLocId());
				wmsTask.setToLocCode(location.getLocCode());
				wmsTask.setRemark("更新并冻结目标库位");
				wmsTaskDao.updateById(wmsTask);
				log.info("调度系统,AGV拣货任务{}成功冻结目标库位{}", request.getTaskDetailId(), location.getLocCode());
				return location.getLocCode();
			}
		}

		log.warn("调度系统,AGV拣货任务{}未查询到可用的目标库位", request.getTaskDetailId());
		throw ExceptionUtil.mpe("未查询到可用的目标库位");
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void broadcastNotificationActivity(List<SchedulingBroadcastNotificationRequest> request) {
		for (SchedulingBroadcastNotificationRequest notificationRequest : request) {
			NoticeMessageRequest message = new NoticeMessageRequest();
			message.setLog(String.format("任务[%s]：[%s]",
				notificationRequest.getTaskDetailId(), notificationRequest.getMsg()));
			logBiz.noticeMesssage(message);

			WmsTask wmsTask = wmsTaskDao.getById(notificationRequest.getTaskDetailId());
			if (Func.notNull(wmsTask)) {
				wmsTaskDao.updateRemark(wmsTask.getTaskId(), notificationRequest.getMsg());
			}
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
			onException(wmsTask, request.getMsg());
		} else if (AGV_TASK_STATE_CANCEL_BY_AGV.equals(request.getState())) {
			onCancelByAgv(wmsTask, request.getMsg());
		} else if (SEND_AGV_RETURN_SUCCESS.equals(request.getState())) {
			onSuccessForSendAgv(wmsTask);
		} else {
			onOtherHandle(wmsTask, request);
		}

		log.info("接收调度系统任务状态变更通知,状态:{},任务:{}", request.getState(), request.getTaskDetailId());
	}

	private void onSuccessForSendAgv(WmsTask wmsTask) {
		if (Func.isNotEmpty(wmsTask.getConfirmDate())){
			return;
		}

		wmsTaskDao.updateState(wmsTask.getTaskId(), WmsTaskStateEnum.AGV_RECEIVED, "AGV系统收到任务");
		log.info("接收调度系统任务状态变更通知, agv系统接收到任务[{}]", wmsTask.getTaskId());
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public String newLocationOnDoubleWarehousing(NewLocationOnDoubleWarehousingRequest request) {
		WmsTask wmsTask = wmsTaskDao.getById(request.getTaskId());
		AssertUtil.notNull(wmsTask, "推荐新的库位失败,查不到对应的任务");

		if (wmsTask.getTaskState().equals(WmsTaskStateEnum.AGV_COMPLETED)
			|| wmsTask.getTaskState().equals(WmsTaskStateEnum.COMPLETED)
			|| wmsTask.getTaskState().equals(WmsTaskStateEnum.CANCELED)) {
			throw new ServiceException("推荐新的库位失败,任务状态已完结不支持推荐新的库位");
		}

		// 1. 原来的目标库位使用状态有系统业务冻结改为冻结，并清空loc_flag_desc
		Long oldLocId = wmsTask.getToLocId();
		String oldLocCode = wmsTask.getToLocCode();
		locationBiz.unfreezeLocByTask(request.getTaskId());
		locationBiz.freezeLoc(oldLocId);

		// 2. 生成新的目标库位，并冻结目标库位
		List<Stock> stocks = stockQueryBiz.findStockByDropId(wmsTask.getTaskId());
		AssertUtil.notEmpty(stocks, "推荐新的库位失败,没有查询到该任务的库存");
		Location newLocation = putawayStrategyActuator.run(null, stocks);
		AssertUtil.notNull(newLocation, "推荐新的库位失败,没有可推荐的新库位");
		locationBiz.freezeLocByTask(newLocation.getLocId(), wmsTask.getTaskId().toString());

		// 3. 更新任务中的目标库位和消息
		wmsTask.setToLocCode(newLocation.getLocCode());
		wmsTask.setToLocId(newLocation.getLocId());
		wmsTask.setRemark(String.format("双重入库,目标库位由[%s]变为[%s]", oldLocCode, wmsTask.getToLocCode()));
		wmsTaskDao.updateById(wmsTask);

		// 4. 发送通知消息
		NoticeMessageRequest message = new NoticeMessageRequest();
		message.setLog(String.format("任务[%s]执行了双重入库,新的库位[%s],请检查原库位[%s]的状态，如没问题请在库位编辑中修改使用状态为正常",
			wmsTask.getTaskId(), wmsTask.getToLocCode(), oldLocCode));
		logBiz.noticeMesssage(message);

		return newLocation.getLocCode();
	}

	private void onOtherHandle(WmsTask wmsTask, SyncTaskStateRequest request) {
		wmsTaskDao.updateRemark(wmsTask.getTaskId(), request.getMsg());
	}

	/**
	 * AGV开始执行任务，WMS系统的操作：
	 * 修改任务状态
	 * 将原库位库存转移到中间库位，中间库位的库存仍是冻结状态
	 *
	 * @param wmsTask 任务
	 */
	private void onStart(WmsTask wmsTask) {
		boolean checkTaskState = WmsTaskStateEnum.ISSUED.equals(wmsTask.getTaskState())
			|| WmsTaskStateEnum.ABNORMAL.equals(wmsTask.getTaskState())
			|| WmsTaskStateEnum.AGV_RECEIVED.equals(wmsTask.getTaskState());
		if (!checkTaskState) {
			throw new ServiceException("状态更新失败,只有已下发的任务才可以执行");
		}
		if (Func.isEmpty(wmsTask.getToLocCode()) || Func.isEmpty(wmsTask.getToLocId())) {
			throw new ServiceException("状态更新失败,只有以及获取目标库位的任务才可以执行");
		}
		// 修改任务状态
		wmsTaskDao.updateState(wmsTask.getTaskId(), WmsTaskStateEnum.START_EXECUTION, "开始执行");
		// 将原库位库存移动到中间库位
		List<Stock> stockList = stockQueryBiz.findStockByDropId(wmsTask.getTaskId());
		for (Stock stock : stockList) {
			Stock middleStock = stockBiz.moveToInTransitByDropId(stock, wmsTask.getTaskId().toString(),
				StockLogTypeEnum.STOCK_AGV_MOVE, null);
			if (WmsTaskTypeEnum.AGV_PICKING.equals(wmsTask.getTaskTypeCd())) {
				List<SoPickPlan> soPickPlanList = soPickPlanBiz.findPickByTaskId(wmsTask.getTaskId(), stock.getStockId());
				Location location = locationBiz.findByLocId(middleStock.getLocId());
				Zone zone = zoneBiz.findById(location.getZoneId());
				for (SoPickPlan soPickPlan : soPickPlanList) {
					soPickPlanBiz.updatePickByPartParam(soPickPlan.getPickPlanId(), middleStock.getStockId(), location, zone, null, null);
				}
			}

		}

		log.info("agv任务开始[{}]-[{}]", wmsTask.getTaskId(), wmsTask);
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
		boolean checkTaskState = WmsTaskStateEnum.START_EXECUTION.equals(wmsTask.getTaskState())
			|| WmsTaskStateEnum.ABNORMAL.equals(wmsTask.getTaskState());
		if (!checkTaskState) {
			throw new ServiceException(String.format(
				"任务执行完毕状态更新失败,任务[%d]当前状态[%s]不是开始执行状态",
				wmsTask.getTaskId(), wmsTask.getTaskState().getDesc()));
		}
		// 修改任务状态
		wmsTaskDao.updateState(wmsTask.getTaskId(), WmsTaskStateEnum.AGV_COMPLETED, "执行完毕");
		locationBiz.unfreezeLocByTask(wmsTask.getTaskId().toString());
		// 将中间库位的库存移动到目标库位
		Location targetLoc = locationBiz.findByLocId(wmsTask.getToLocId());
		List<Stock> stockList = stockQueryBiz.findStockByDropId(wmsTask.getTaskId());
		List<Stock> targetStockList = new ArrayList<>();
		for (Stock stock : stockList) {
			Stock targetStock = stockBiz.moveAllStockFromDropId(stock, targetLoc, wmsTask.getTaskId().toString(),
				StockLogTypeEnum.STOCK_AGV_MOVE, null);
			targetStockList.add(targetStock);
			if (WmsTaskTypeEnum.AGV_PICKING.equals(wmsTask.getTaskTypeCd())) {
				Location location = locationBiz.findByLocId(targetStock.getLocId());
				Zone zone = zoneBiz.findById(location.getZoneId());
				List<SoPickPlan> soPickPlanList = soPickPlanBiz.findPickByTaskId(wmsTask.getTaskId(), stock.getStockId());
				for (SoPickPlan soPickPlan : soPickPlanList) {
					soPickPlanBiz.updatePickByPartParam(soPickPlan.getPickPlanId(), targetStock.getStockId(), location, zone, null, null);
				}
			} else if (WmsTaskTypeEnum.AGV_PUTAWAY.equals(wmsTask.getTaskTypeCd())) {
				// 生成上架记录
				PutawayLog putawayLog = putawayFactory.create(stock, targetLoc);
				putawayLogDao.save(putawayLog);
			}
		}

		stockBiz.unfreezeStockByDropId(targetStockList, wmsTask.getTaskId());

		log.info("agv任务结束[{}]-[{}]", wmsTask.getTaskId(), wmsTask);
	}

	private void onException(WmsTask wmsTask, String msg) {
		boolean checkTaskState = WmsTaskStateEnum.COMPLETED.equals(wmsTask.getTaskState())
			|| WmsTaskStateEnum.CANCELED.equals(wmsTask.getTaskState());
		if (checkTaskState) {
			throw new ServiceException("状态更新失败,任务已经完成");
		}
		// 修改任务状态
		wmsTaskDao.updateState(wmsTask.getTaskId(), WmsTaskStateEnum.ABNORMAL, msg);

		log.info("agv任务异常[{}]-[{}]", wmsTask.getTaskId(), wmsTask);
	}

	private void onCancelByAgv(WmsTask wmsTask, String msg) {
		boolean checkTaskState = WmsTaskStateEnum.COMPLETED.equals(wmsTask.getTaskState())
			|| WmsTaskStateEnum.AGV_COMPLETED.equals(wmsTask.getTaskState());
		if (checkTaskState) {
			throw new ServiceException("状态更新失败,任务已经完成");
		}

		//中间库位库存
		List<Stock> stockList = stockQueryBiz.findStockByDropId(wmsTask.getTaskId());
		//解冻中间库位库存
		stockBiz.unfreezeAndReduceOccupy(stockList, wmsTask.getTaskId());

		//如果目标库位不为空则把目标库位进行解冻
		if (Func.isNotEmpty(wmsTask.getToLocId())) {
			locationBiz.unfreezeLocByTask(wmsTask.getTaskId().toString());
		}

		// 修改任务状态
		wmsTaskDao.updateState(wmsTask.getTaskId(), WmsTaskStateEnum.CANCELED, msg);
		log.info("agv任务异常[{}]-[{}]", wmsTask.getTaskId(), wmsTask);
	}
}
