package org.nodes.wms.biz.outstock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.DictKVConstant;
import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.outstock.logSoPick.modular.LogSoPickFactory;
import org.nodes.wms.biz.outstock.plan.SoPickPlanBiz;
import org.nodes.wms.biz.outstock.plan.modular.SoPickPlanFactory;
import org.nodes.wms.biz.outstock.so.SoBillBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.stockManage.StockManageBiz;
import org.nodes.wms.biz.task.AgvTask;
import org.nodes.wms.biz.task.WmsTaskBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeCodeEnum;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.outstock.SoPickPlanDao;
import org.nodes.wms.dao.outstock.logSoPick.LogSoPickDao;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.*;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindPickToSoPickPlanResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindPickingBySoBillIdResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.OutboundAccessAreaLocationQueryResponse;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.so.dto.input.PickByPcRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillDistributedRequest;
import org.nodes.wms.dao.outstock.so.dto.output.StockIdAndSoPickPlanQtyRequest;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.dao.outstock.so.enums.SoDetailStateEnum;
import org.nodes.wms.dao.outstock.soPickPlan.dto.input.FindPickPlanBySoBillIdAndBoxCodeRequest;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.FindPickPlanBySoBillIdAndBoxCodeResponse;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.dto.output.PickByPcStockDto;
import org.nodes.wms.dao.stock.dto.output.StockSoPickPlanResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskProcTypeEnum;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 出库业务实现类
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class OutStockBizImpl implements OutStockBiz {

	private final SoPickPlanDao soPickPlanDao;
	private final LogSoPickDao logSoPickDao;
	private final SoBillBiz soBillBiz;
	private final SoPickPlanBiz soPickPlanBiz;
	private final LogSoPickFactory logSoPickFactory;
	private final StockBiz stockBiz;
	private final StockQueryBiz stockQueryBiz;
	private final LocationBiz locationBiz;
	private final LogBiz logBiz;
	private final WmsTaskBiz wmsTaskBiz;
	private final StockManageBiz stockManageBiz;
	private final ZoneBiz zoneBiz;
	private final AgvTask agvTask;
	private final SoPickPlanFactory soPickPlanFactory;
	private final LpnTypeBiz lpnTypeBiz;

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void pickByPcsOnPc(PickByPcRequest request) {
		SoHeader soHeader = soBillBiz.getSoHeaderById(request.getSoBillId());
		SoDetail soDetail = soBillBiz.getSoDetailById(request.getPickByPcStockDtoList().get(0).getSoDetailId());
		// 1 业务判断：
		// 1.1 如果单据有拣货计划则不能使用PC拣货
		if (soPickPlanBiz.hasEnablePickPlan(request.getSoBillId())) {
			throw new ServiceException("拣货失败,发货单已存在拣货计划");
		}
		// 1.2 单据和单据明细行的状态如果为终结状态，则不能进行拣货
		// 1.3 拣货数量是否超过剩余数量
		BigDecimal pickQty = request.getPickByPcStockDtoList().stream()
			.map(PickByPcStockDto::getOutStockQty)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		canPick(soHeader, soDetail, pickQty);

		List<PickByPcStockDto> pickByPcStockDtoList = request.getPickByPcStockDtoList();
		// 2 生成拣货记录，需要注意序列号（log_so_pick)
		for (PickByPcStockDto pickByPcStockDto : pickByPcStockDtoList) {
			Stock stock = stockQueryBiz.findStockById(pickByPcStockDto.getStockId());
			LogSoPick logSoPick = logSoPickFactory.createLogSoPick(pickByPcStockDto, soHeader, soDetail, stock);
			logSoPickDao.saveLogSoPick(logSoPick);
			Location location = locationBiz
				.getLocationByZoneType(stock.getWhId(), DictKVConstant.ZONE_TYPE_PICK_TO).get(0);
			// 3 移动库存到出库集货区
			stockBiz.moveStock(stock, pickByPcStockDto.getSerailList(), pickByPcStockDto.getOutStockQty(),
				location, StockLogTypeEnum.OUTSTOCK_BY_PC, soHeader.getSoBillId(), soHeader.getSoBillNo(),
				soDetail.getSoLineNo(), null);
		}
		// 4 更新出库单明细中的状态和数量
		soBillBiz.updateSoDetailStatus(soDetail, pickQty);
		// 5 更新发货单状态
		soBillBiz.updateSoBillState(soHeader);
		// 6 记录业务日志
		logBiz.auditLog(AuditLogType.OUTSTOCK, "PC拣货");
	}

	private void canPick(SoHeader soHeader, SoDetail soDetail, BigDecimal pickQty) {
		if (soHeader.getSoBillState().equals(SoBillStateEnum.COMPLETED)
			|| soHeader.getSoBillState().equals(SoBillStateEnum.ALL_OUT_STOCK)
			|| soHeader.getSoBillState().equals(SoBillStateEnum.CANCELED)) {
			throw new ServiceException("拣货失败,发货单状态为" + soHeader.getSoBillState() + "不能进行拣货");
		}
		if (soDetail.getBillDetailState().equals(SoDetailStateEnum.DELETED)
			|| soDetail.getBillDetailState().equals(SoDetailStateEnum.ALL_OUT_STOCK)) {
			throw new ServiceException("拣货失败,发货单明细状态为" + soDetail.getBillDetailState() + "不能进行拣货");
		}
		if (BigDecimalUtil.gt(pickQty, soDetail.getSurplusQty())) {
			throw new ServiceException("拣货失败,拣货数量大于剩余数量");
		}
	}

	private void canPick(SoHeader soHeader) {
		AssertUtil.notNull(soHeader.getSoBillState(), "拣货失败,发货单状态为空");
		if (soHeader.getSoBillState().equals(SoBillStateEnum.COMPLETED)
			|| soHeader.getSoBillState().equals(SoBillStateEnum.ALL_OUT_STOCK)
			|| soHeader.getSoBillState().equals(SoBillStateEnum.CANCELED)) {
			throw new ServiceException("拣货失败,发货单状态为" + soHeader.getSoBillState() + "不能进行拣货");
		}
	}

	private void canPick(SoDetail soDetail, BigDecimal pickQty) {
		if (soDetail.getBillDetailState().equals(SoDetailStateEnum.DELETED)
			|| soDetail.getBillDetailState().equals(SoDetailStateEnum.ALL_OUT_STOCK)) {
			throw new ServiceException("拣货失败,发货单明细状态为" + soDetail.getBillDetailState() + "不能进行拣货");
		}
		if (BigDecimalUtil.gt(pickQty, soDetail.getSurplusQty())) {
			throw new ServiceException("拣货失败,发货数量大于剩余数量");
		}
	}

	@Override
	public List<SoPickPlanForDistributionResponse> getSoPickPlanBySoBillIdAndSoDetailId(Long soBillId,
																						Long soDetailId) {
		AssertUtil.notNull(soBillId, "查询拣货计划失败，发货单id为空");
		return soPickPlanDao.getBySoBillIdAndSoDetailId(soBillId, soDetailId);
	}

	@Override
	public IPage<FindAllPickingResponse> findSoHeaderByNo(findSoHeaderByNoRequest request, Query query) {
		List<SoBillStateEnum> soBillStateEnums = new ArrayList<>();
		soBillStateEnums.add(SoBillStateEnum.EXECUTING);
		soBillStateEnums.add(SoBillStateEnum.PART);
		soBillStateEnums.add(SoBillStateEnum.CREATE);
		request.setSoBillState(soBillStateEnums);
		return soBillBiz.getAllPickingByNo(Condition.getPage(query), request);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Boolean pickByPcs(PickByPcsRequest request) {
		SoHeader soHeader = soBillBiz.getSoHeaderById(request.getSoBillId());
		SoDetail soDetail;
		if (Func.isNotEmpty(request.getSoDetailId())) {
			soDetail = soBillBiz.getSoDetailById(request.getSoDetailId());
		} else {
			soDetail = soBillBiz.findSoDetailByHeaderIdAndSkuCode(request.getSoBillId(), request.getSkuCode());
		}
		List<Stock> stockLists = stockQueryBiz.findEnableStockByBoxCode(request.getBoxCode());
		if (stockLists.size() == 0) {
			throw new ServiceException("PDA拣货失败，根据箱码获取库存失败");
		}
		WmsTask task = wmsTaskBiz.findPickTaskByBoxCode(request.getBoxCode(), WmsTaskProcTypeEnum.BY_PCS, null);
		if (Func.isNotEmpty(task)) {
			if (!Func.equals(task.getTaskProcType(), WmsTaskProcTypeEnum.BY_PCS)) {
				throw new ServiceException("PDA按件拣货失败，存在任务，但不是按件拣货的任务");
			}
			// 按照任务执行方式进行执行
			pickByPcsByTask(task, stockLists, soHeader, request, request.getQty());
			return soHeaderSoBillState(request.getSoBillId());
		}
		// 按照库存的方法执行
		pickByPcsByStock(request, soHeader, soDetail, stockLists);
		return soHeaderSoBillState(request.getSoBillId());
	}

	private Boolean soHeaderSoBillState(Long soBillId) {
		SoHeader soHeader = soBillBiz.getSoHeaderById(soBillId);
		return Objects.equals(soHeader.getSoBillState(), SoBillStateEnum.ALL_OUT_STOCK);
	}

	@Override
	public IPage<FindPickingBySoBillIdResponse> findOpenSoDetail(FindOpenSoDetailRequest request,
																 Query query) {
		IPage<SoDetail> page = soBillBiz.getPickingBySoBillId(request.getSoBillId(), query);
		AssertUtil.notNull(page, "查询结果为空");
		return page.convert(result -> BeanUtil.copy(result, FindPickingBySoBillIdResponse.class));
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void pickByBox(PickByBoxCodeRequest request, WmsTaskProcTypeEnum taskProcTypeEnum) {
		// 1、根据箱码查询任务
		WmsTask task = wmsTaskBiz.findPickTaskByBoxCode(request.getBoxCode(), taskProcTypeEnum, null);
		SoHeader soHeader = soBillBiz.getSoHeaderById(task.getBillId());
		AssertUtil.notNull(soHeader, "根据任务存在的发货单头表信息查询发货单失败");
		List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(request.getBoxCode());
		if (stockList.size() == 0) {
			throw new ServiceException("按箱拣货失败，根据箱码查询不到对应库存");
		}
		// 2、参数校验 头表
		canPick(soHeader);
		boxCodeCanPick(request, task, soHeader);
		// 3、根据拣货计划生成拣货记录,根据任务id从拣货计划中查找
		List<SoPickPlan> soPickPlanList = soPickPlanBiz.findPickByTaskId(task.getTaskId());
		canPick(soPickPlanList, stockList);

		// 拣货、更新拣货计划
		updateSoPickPlan(soPickPlanList);

		// 5、更新出库单信息
		soBillBiz.updateSoBillState(soHeader);

		// 6、更新任务
		wmsTaskBiz.updateWmsTaskStateByTaskId(task.getTaskId(), WmsTaskStateEnum.COMPLETED, task.getTaskQty());

		// 7、记录业务日志
		logBiz.auditLog(AuditLogType.OUTSTOCK, soHeader.getSoBillId(), soHeader.getSoBillNo(),
			spliceLog(taskProcTypeEnum, stockList));
	}

	void boxCodeCanPick(PickByBoxCodeRequest request, WmsTask task, SoHeader soHeader) {
		LpnTypeCodeEnum lpnTypeCodeEnum = lpnTypeBiz.parseBoxCode(request.getBoxCode());
		//如果是D箱进行拦截判断，如果数量有问题则抛异常让用户去拆箱
		if (LpnTypeCodeEnum.D.equals(lpnTypeCodeEnum)) {
			//判断他是不是D箱人工拣货区，如果是则判断数量
			Zone zone = zoneBiz.findByCode(WmsAppConstant.ZONE_CODE_D_PICK_AREA);
			Location location = null;
			if (Func.isNotEmpty(task.getToLocCode())) {
				location = locationBiz.findLocationByLocCode(request.getWhId(), task.getToLocCode());
			} else {
				location = locationBiz.findLocationByLocCode(request.getWhId(), task.getFromLocCode());
				location.setZoneId(-1L);
			}
			if (zone.getZoneId().equals(location.getZoneId())) {
				BigDecimal soDetailSurplusQty = BigDecimal.ZERO;
				BigDecimal soPickPlanSurplusQty = BigDecimal.ZERO;
				List<SoDetail> soDetailList = soBillBiz.getEnableSoDetailBySoHeaderId(soHeader.getSoBillId());
				for (SoDetail soDetail : soDetailList) {
					soDetailSurplusQty = soDetailSurplusQty.add(soDetail.getSurplusQty());
				}
				List<SoPickPlan> soPickPlans = soPickPlanBiz.findBySoHeaderId(soHeader.getSoBillId());
				for (SoPickPlan soPickPlan : soPickPlans) {
					soPickPlanSurplusQty = soPickPlanSurplusQty.add(soPickPlan.getSurplusQty());
				}
				if (BigDecimalUtil.gt(soPickPlanSurplusQty, soDetailSurplusQty)) {
					throw new ServiceException("D箱拣货失败，请先拆箱");
				}
			}
		}
	}

	@Override
	public List<OutboundAccessAreaLocationQueryResponse> findLocOfAgvPickTo(FindLocOfAgvPickToRequest request) {
		Zone zone = zoneBiz.findByCode(WmsAppConstant.ZONE_CODE_AGV_SHIPMENT_CONNECTION_AREA);
		List<Location> locationList = locationBiz.findLocationByZoneId(zone.getZoneId());
		return BeanUtil.copy(locationList, OutboundAccessAreaLocationQueryResponse.class);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Boolean pickOnAgvPickTo(OnAgvPickToRequest request) {
		// 1、判断库位是否有库存
		boolean emptyLocation = stockQueryBiz.isEmptyLocation(request.getLocId());
		if (emptyLocation) {
			throw new ServiceException(String.format("查询不到%s的库存", request.getLocCodeView()));
		}

		// 2、根据库位查询库存
		List<Stock> stockList = stockQueryBiz.findStockByLocation(request.getLocId());
		AssertUtil.notNull(stockList, "接驳区拣货失败，根据库位查询不到对应库存");
		List<String> boxCodeList = stockList.stream()
			.map(Stock::getBoxCode)
			.distinct()
			.collect(Collectors.toList());

		// 3、判断是不是超拣---直接走按箱的流程
		// 4、如果没有超拣则执行按箱的流---直接走按箱的流程
		WmsTask task = wmsTaskBiz.findPickTaskByBoxCode(stockList.get(0).getBoxCode(), WmsTaskProcTypeEnum.BY_BOX, null);
		// 3、根据拣货计划生成拣货记录,根据任务id从拣货计划中查找
		List<SoPickPlan> soPickPlanList = soPickPlanBiz.findPickByTaskId(task.getTaskId());
		AssertUtil.notNull(soPickPlanList, "接驳区拣货失败，根据任务查询不到对应的拣货计划");
		SoDetail soDetail = soBillBiz.getSoDetailById(soPickPlanList.get(0).getSoDetailId());
		AssertUtil.notNull(soDetail, "接驳区拣货失败，根据拣货计划查询不到对应的发货单详情");
		// 2、参数校验
		if (BigDecimalUtil.gt(task.getTaskQty().subtract(task.getScanQty()), soDetail.getSurplusQty())) {
			return false;
		}

		// 5、如果超了则返回
		for (String boxCode : boxCodeList) {
			PickByBoxCodeRequest boxCodeRequest = new PickByBoxCodeRequest();
			boxCodeRequest.setBoxCode(boxCode);
			pickByBox(boxCodeRequest, WmsTaskProcTypeEnum.BY_BOX);
		}
		return true;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void moveOnAgvPickTo(MoveOnAgvPickToRequest request) {
		Location targetLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getTargetLocCode());
		// 判断目标库位是不是人工拣货区，如果不是则报异常 (PS:只能移动到人工拣货区)
		if (!locationBiz.isPickLocation(targetLocation)) {
			throw new ServiceException("接驳区移动失败，目标库位不是人工拣货区,请输入或扫描人工拣货区库位后重试");
		}
		Zone zone = zoneBiz.findById(targetLocation.getZoneId());
		Location sourceLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getSourceLocCode());

		// 校验目标库位箱型
		stockManageBiz.canMoveToBoxType(targetLocation, sourceLocation);

		List<Stock> stockList = stockQueryBiz.findStockByLocation(sourceLocation.getLocId());
		AssertUtil.notNull(stockList, "接驳区移动失败，根据原库位查询不到相应库存");
		if (stockList.size() == 0) {
			throw new ServiceException("接驳区移动失败，根据原库位查询不到相应库存");
		}
		WmsTask task = wmsTaskBiz.findPickTaskByBoxCode(stockList.get(0).getBoxCode(), WmsTaskProcTypeEnum.BY_BOX, null);
		SoHeader soHeader = soBillBiz.getSoHeaderById(task.getBillId());
		List<SoPickPlan> soPickPlanList = soPickPlanBiz.findPickByTaskId(task.getTaskId());
		SoDetail soDetail = soBillBiz.getSoDetailById(soPickPlanList.get(0).getSoDetailId());
		stockManageBiz.canMove(sourceLocation, targetLocation, stockList, stockList.get(0).getBoxCode(), false);

		List<Stock> stocks = stockBiz.moveStockByBoxCodeOfOccupy(stockList.get(0).getBoxCode(), stockList.get(0).getBoxCode(),
			stockList.get(0).getLpnCode(), targetLocation, StockLogTypeEnum.STOCK_MOVE_BY_BOX_PDA,
			soDetail.getSoBillId(), soDetail.getSoBillNo(), soDetail.getSoLineNo(), null);
		// 任务id和stockID更新拣货计划中的stockID和库位信息
		for (SoPickPlan pickPlan : soPickPlanList) {
			for (Stock stock : stockList) {
				for (Stock targetStock : stocks) {
					if (Func.equals(pickPlan.getStockId(), stock.getStockId())
						&& targetStock.getBoxCode().equals(stock.getBoxCode())
						&& targetStock.getLpnCode().equals(stock.getLpnCode())
						&& targetStock.getSkuLot1().equals(stock.getSkuLot1())
						&& targetStock.getStockBalance().equals(stock.getStockBalance())
					) {
						soPickPlanBiz.updatePickByTaskIdAndStockId(pickPlan.getTaskId(), stock.getStockId(),
							targetStock.getStockId(), targetLocation, zone);
					}
				}
			}
		}

		// 6、更新任务
		wmsTaskBiz.updateWmsTaskByPartParam(task.getTaskId(), WmsTaskProcTypeEnum.BY_BOX, targetLocation, null);

		// 7、记录业务日志
		logBiz.auditLog(AuditLogType.MOVE_STOCK, soHeader.getSoBillId(),
			soHeader.getSoBillNo(), spliceLog(WmsTaskProcTypeEnum.BY_LPN, stockList));
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public String autoDistribute(Long soBillId) {
		// 参数校验：已经完成的订单不能分配；有未完成的拣货计划的不能再次分配
		SoHeader soHeader = soBillBiz.getSoHeaderById(soBillId);
		if (soBillBiz.isFinish(soHeader)) {
			throw ExceptionUtil.mpe("已经完成的单据不能再次分配");
		}
		List<SoPickPlan> pickPlans = soPickPlanBiz.findBySoHeaderId(soBillId);
		if (Func.isNotEmpty(pickPlans)) {
			throw ExceptionUtil.mpe("整单分配失败,当前单据中存在未完成的拣货计划,请撤销或完成之后再分配");
		}
		// 自动生成拣货计划,即使有的明细因为库存不足导致无法全部分配，也可以部分分配成功
		List<SoDetail> soDetails = soBillBiz.getEnableSoDetailBySoHeaderId(soBillId);
		String result = soPickPlanBiz.runPickStrategy(soHeader, soDetails, pickPlans);
		// 更新发货单状态
		if (SoBillStateEnum.CREATE.equals(soHeader.getSoBillState())) {
			soBillBiz.updateState(soBillId, SoBillStateEnum.EXECUTING);
		}
		// 记录日志
		logBiz.auditLog(AuditLogType.DISTRIBUTE_STRATEGY, soBillId, soHeader.getSoBillNo(), "执行自动分配:" + result);

		if (Func.isEmpty(result)) {
			result = "分配成功";
		}
		return result;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void cancelDistribute(Long soBillId) {
		List<SoPickPlan> soPickPlanList = soPickPlanBiz.findBySoHeaderId(soBillId);
		if (Func.isEmpty(soPickPlanList)) {
			throw ExceptionUtil.mpe("取消分配失败,当前单据尚未执行分配");
		}
		if (Func.isNotEmpty(soPickPlanList.get(0).getTaskId())) {
			throw ExceptionUtil.mpe("取消分配失败,已经下发的不能取消分配");
		}

		SoHeader soHeader = soBillBiz.getSoHeaderById(soBillId);
		soPickPlanBiz.cancelPickPlan(soPickPlanList, soHeader);

		logBiz.auditLog(AuditLogType.DISTRIBUTE_STRATEGY, soBillId, soHeader.getSoBillNo(), "全部取消分配");
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void issued(Long soBillId) {
		List<SoPickPlan> soPickPlanList = soPickPlanBiz.findBySoHeaderId(soBillId);
		if (Func.isEmpty(soPickPlanList)) {
			throw ExceptionUtil.mpe("取消分配失败,当前单据尚未执行分配");
		}

		// 已经下发过的不用重复下发，自动区的按库位下发任务到agv，人工区的按照实际情况分为按箱和按件任务
		SoHeader soHeader = soBillBiz.getSoHeaderById(soBillId);
		Map<Long, List<SoPickPlan>> locId2SoPickPlan = soPickPlanList.stream()
			.collect(Collectors.groupingBy(SoPickPlan::getLocId, Collectors.toList()));
		for (Map.Entry<Long, List<SoPickPlan>> entry : locId2SoPickPlan.entrySet()) {
			Long locId = entry.getKey();
			List<SoPickPlan> soPickPlanOfLoc = entry.getValue();
			Zone zone = zoneBiz.findById(soPickPlanOfLoc.get(0).getZoneId());
			AssertUtil.notNull(zone, "下发任务失败,库区{}不存在", soPickPlanOfLoc.get(0).getZoneId());
			if (WmsAppConstant.ZONE_CODE_AGV.equals(zone.getZoneCode())) {
				issuedAgvTask(soPickPlanOfLoc, soHeader);
			} else {
				issuedManualTask(soPickPlanOfLoc, soHeader);
			}
		}

		logBiz.auditLog(AuditLogType.DISTRIBUTE_STRATEGY,
			soBillId, soHeader.getSoBillNo(), "执行拣货任务下发成功");
	}

	private void issuedAgvTask(List<SoPickPlan> soPickPlanOfLoc, SoHeader soHeader) {
		if (Func.isEmpty(soPickPlanOfLoc.get(0).getTaskId())) {
			WmsTask task = agvTask.pickToSchedule(soPickPlanOfLoc, soHeader, null);
			soPickPlanDao.updateTask(soPickPlanOfLoc, task.getTaskId());
		} else {
			WmsTask task = wmsTaskBiz.findByTaskId(soPickPlanOfLoc.get(0).getTaskId());
			if (WmsTaskStateEnum.NOT_ISSUED.equals(task.getTaskState())) {
				agvTask.sendPickToSchedule(task, soHeader);
				wmsTaskBiz.updateTaskStateToIssued(task.getTaskId());
			} else {
				throw new ServiceException("任务下发失败，该任务已经下发完成");
			}
		}
	}

	private void issuedManualTask(List<SoPickPlan> soPickPlanOfLoc, SoHeader soHeader) {
		List<String> issuedBoxCode = new ArrayList<>();
		for (SoPickPlan soPickPlan : soPickPlanOfLoc) {
			if (Func.isNotEmpty(soPickPlan.getTaskId())) {
				continue;
			}
			if (Func.isNotEmpty(soPickPlan.getBoxCode()) && issuedBoxCode.contains(soPickPlan.getBoxCode())) {
				continue;
			}

			WmsTask wmsTask;
			if (isFullBoxOutStock(soPickPlan, soPickPlanOfLoc)) {
				List<SoPickPlan> soPickPlanOfBox = soPickPlanOfLoc.stream()
					.filter(v -> soPickPlan.getBoxCode().equals(v.getBoxCode()))
					.collect(Collectors.toList());
				wmsTask = wmsTaskBiz.create(WmsTaskTypeEnum.PICKING, WmsTaskProcTypeEnum.BY_BOX,
					soPickPlanOfBox, soHeader);
				issuedBoxCode.add(soPickPlan.getBoxCode());
				soPickPlanDao.updateTask(soPickPlanOfBox, wmsTask.getTaskId());
			} else {
				wmsTask = wmsTaskBiz.create(WmsTaskTypeEnum.PICKING, WmsTaskProcTypeEnum.BY_PCS,
					Collections.singletonList(soPickPlan), soHeader);
				soPickPlanDao.updateTask(Collections.singletonList(soPickPlan), wmsTask.getTaskId());
			}
		}
	}

	private boolean isFullBoxOutStock(SoPickPlan currentPlan, List<SoPickPlan> soPickPlanList) {
		// 判断是否整箱拣货，如果返回true表示是整箱拣货
		if (Func.isEmpty(currentPlan.getBoxCode())) {
			return false;
		}

		String boxCode = currentPlan.getBoxCode();
		List<Stock> stockOfBox = stockQueryBiz.findEnableStockByBoxCode(boxCode);
		if (Func.isEmpty(stockOfBox)) {
			return false;
		}

		for (Stock stock : stockOfBox) {
			BigDecimal planQty = soPickPlanList.stream()
				.filter(v -> v.getStockId().equals(stock.getStockId()))
				.map(v -> v.getPickPlanQty().subtract(v.getPickRealQty()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			if (BigDecimalUtil.ne(planQty, stock.getStockBalance())) {
				return false;
			}
		}

		return true;
	}

	@Override
	public List<StockSoPickPlanResponse> getStockByDistributeAdjust(
		Long skuId, String skuLot1, String skuLot4, Long soBillId) {
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		skuLot.setSkuLot1(skuLot1);
		skuLot.setSkuLot4(skuLot4);
		// 获取可分配的库存
		List<Stock> stockList = stockQueryBiz.findEnableStockBySkuAndSkuLot(skuId, skuLot);
		// 只要库存状态为正常的库存
		List<Stock> normalStockList = stockList.stream()
			.filter(stock -> stock.getStockStatus().equals(StockStatusEnum.NORMAL))
			.collect(Collectors.toList());
		if (Func.isEmpty(normalStockList)) {
			throw ExceptionUtil.mpe("该物品没有可分配的库存");
		}
		// 总库存（有箱码的库存加无箱码的库存）
		List<Stock> allStock = new ArrayList<>();

		// 查找出有箱码的库存，追加到总的库存中
		List<String> boxCodeList = normalStockList.stream()
			.map(Stock::getBoxCode)
			.filter(Func::isNotEmpty)
			.collect(Collectors.toList());
		if (Func.isNotEmpty(boxCodeList)) {
			List<Stock> stockOfBoxCodes = stockQueryBiz.findEnableStockByBoxCode(boxCodeList);
			if (Func.isNotEmpty(stockOfBoxCodes)) {
				allStock.addAll(stockOfBoxCodes);
			}
		}
		// 查找出箱码为空的库存，追加到总的库存中
		List<Stock> stockOfNonBoxCode = normalStockList.stream()
			.filter(item -> Func.isEmpty(item.getBoxCode()))
			.collect(Collectors.toList());
		if (Func.isNotEmpty(stockOfNonBoxCode)) {
			allStock.addAll(stockOfNonBoxCode);
		}

		List<Long> stockIdList = allStock.stream()
			.map(Stock::getStockId)
			.collect(Collectors.toList());

		List<SoPickPlan> soPickPlanList = soPickPlanBiz.findByStockIdsAndSoBillId(stockIdList, soBillId);

		List<StockSoPickPlanResponse> stockSoPickPlanList = Func.copy(allStock, StockSoPickPlanResponse.class);

		if (Func.isNotEmpty(soPickPlanList)) {
			// 拣货计划中根据stockId分组 统计每个stock对应的所有拣货计划分配量总数
			Map<Long, List<SoPickPlan>> planListMap = soPickPlanList.stream()
				.collect(Collectors.groupingBy(SoPickPlan::getStockId));
			planListMap.forEach((stockId, planList) -> {

				// 分配量求和(计划分配量减去实际分配量)
				BigDecimal pickQty = planList.stream()
					.reduce(BigDecimal.ZERO, (temp, plan) -> plan.getPickPlanQty().subtract(plan.getPickRealQty()),
						BigDecimal::add);

				// 获取拣货计划id
				List<Long> planIdList = planList.stream().map(SoPickPlan::getPickPlanId).collect(Collectors.toList());

				for (StockSoPickPlanResponse stockSoPickPlan : stockSoPickPlanList) {
					if (stockSoPickPlan.getStockId().equals(stockId)) {
						stockSoPickPlan.setPickQty(pickQty);
						stockSoPickPlan.setSoPickPlanList(planIdList);
						// 赋值 可用量( 表里的数据 + 分配量 )
						stockSoPickPlan.setStockEnable(stockSoPickPlan.getStockEnable().add(pickQty));
						break;
					}
				}
			});
		} else {
			for (StockSoPickPlanResponse stockPlan : stockSoPickPlanList) {
				stockPlan.setPickQty(BigDecimal.ZERO);
			}
		}
		return stockSoPickPlanList;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void manualDistribute(SoBillDistributedRequest request) {
		AssertUtil.notNull(request, "调整分配失败，请求参数为空");
		SoHeader soHeader = soBillBiz.getSoHeaderById(request.getSoBillId());
		AssertUtil.notNull(request, "调整分配失败，发货单已经删除无法执行分配调整");
		// 删除原有的分配记录和释放库存占用
		if (Func.isNotEmpty(request.getOldSoPickPlanList())) {
			soPickPlanBiz.cancelPickPlan(soHeader, request.getOldSoPickPlanList());
		}
		// 按照用户输入信息重新形成分配记录
		if (Func.isNotEmpty(request.getStockIdAndSoPickPlanQtyList())) {
			List<SoPickPlan> soPickPlanList = new ArrayList<>();
			SoDetail soDetail = soBillBiz.getSoDetailById(request.getSoDetailId());
			for (StockIdAndSoPickPlanQtyRequest item : request.getStockIdAndSoPickPlanQtyList()) {
				if (BigDecimalUtil.le(item.getSoPickPlanQty(), BigDecimal.ZERO)) {
					continue;
				}

				Stock stock = stockQueryBiz.findStockById(item.getStockId());
				SoPickPlan soPickPlan = soPickPlanFactory.create(request.getSoBillId(), soDetail,
					stock, item.getSoPickPlanQty());
				soPickPlanList.add(soPickPlan);
			}
			soPickPlanBiz.occupyStockAndSavePlan(soPickPlanList);
		}
		// 更新发货单状态
		if (SoBillStateEnum.CREATE.equals(soHeader.getSoBillState())) {
			soBillBiz.updateState(request.getSoBillId(), SoBillStateEnum.EXECUTING);
		}
		logBiz.auditLog(AuditLogType.DISTRIBUTE_STRATEGY, request.getSoBillId(),
			soHeader.getSoBillNo(), "执行调整分配");
	}

	@Override
	public IPage<FindPickingBySoBillIdResponse> findPickPlanBySoBillId(FindOpenSoDetailRequest request, Query query) {
		IPage<SoDetail> page = soBillBiz.getPickingBySoBillId(request.getSoBillId(), query);
		AssertUtil.notNull(page, "查询结果为空");
		return page.convert(soDetail -> {
			List<SoPickPlan> soPickPlanList = soPickPlanBiz.findPickBySoDetailId(soDetail.getSoDetailId());
			FindPickingBySoBillIdResponse detail = BeanUtil.copy(soDetail, FindPickingBySoBillIdResponse.class);
			List<FindPickToSoPickPlanResponse> pickPlanResponseList = BeanUtil.copy(soPickPlanList, FindPickToSoPickPlanResponse.class);
			detail.setSoPickPlanList(pickPlanResponseList);
			return detail;
		});
	}

	@Override
	public List<FindPickPlanBySoBillIdAndBoxCodeResponse> getPickPlanBySoBillIdAndBoxCode(FindPickPlanBySoBillIdAndBoxCodeRequest request) {
		List<FindPickPlanBySoBillIdAndBoxCodeResponse> response = BeanUtil.copy(soPickPlanBiz.findSoPickPlanByBoxCode(request.getSoBillId(), request.getBoxCode()), FindPickPlanBySoBillIdAndBoxCodeResponse.class);
		return response;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Boolean bulkPick(BulkPickRequest request) {
		SoHeader soHeader = soBillBiz.getSoHeaderById(request.getSoBillId());
		List<Stock> stockLists = stockQueryBiz.findEnableStockByBoxCode(request.getBoxCode());
		if (stockLists.size() == 0) {
			throw new ServiceException("PDA拣货失败，根据箱码获取库存失败");
		}
		if (Func.isNotEmpty(request.getPickPlanId())) {
			SoPickPlan soPickPlan = soPickPlanBiz.findSoPickPlanById(request.getPickPlanId());
			WmsTask task = wmsTaskBiz.findPickTaskByBoxCode(request.getBoxCode(), null, null);

//			if (!Func.equals(task.getTaskProcType(), WmsTaskProcTypeEnum.BY_PCS)) {
//				throw new ServiceException("PDA按件拣货失败，存在任务，但不是按件拣货的任务");
//			}
			// 按照任务执行方式进行执行
			bulkPickByTask(task, soHeader, request, soPickPlan, request.getQty());
			return soHeaderSoBillState(request.getSoBillId());
		} else {
			SoDetail soDetail = soBillBiz.findSoDetailByHeaderIdAndSkuCode(request.getSoBillId(), request.getSkuCode());
			// 按照库存的方法执行
			bulkPickByStock(request, soHeader, soDetail, stockLists);
		}
		return soHeaderSoBillState(request.getSoBillId());
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void cancelOutStock(List<Long> logSoPickIdList) {
		// 根据拣货记录id查找所有的拣货记录
		List<LogSoPick> logSoPickList = logSoPickDao.getByIds(logSoPickIdList);
		// 判断发货单状态是否为已关闭，关闭的发货单不允许撤销拣货
		List<Long> soPickIdList = logSoPickList.stream()
			.map(LogSoPick::getSoBillId).
			distinct().collect(Collectors.toList());
		soPickIdList.forEach(id -> {
			SoHeader soHeader = soBillBiz.getSoHeaderById(id);
			if (SoBillStateEnum.COMPLETED.equals(soHeader.getSoBillState())) {
				throw ExceptionUtil.mpe("撤销拣货失败，发货单[编码：{}]已关闭", soHeader.getSoBillNo());
			}
		});

		logSoPickList.forEach(logSoPick -> {
			// 判断该拣货记录是否已经撤销过了
			if (Func.isNotBlank(logSoPick.getCancelLogId())) {
				throw ExceptionUtil.mpe("撤销拣货失败，发货记录[id:{}]已撤销", logSoPick.getLsopId());
			}
			// 判断发货记录中是否存在撤销数量为负数的，有就抛异常
			if (BigDecimalUtil.lt(logSoPick.getPickRealQty(), BigDecimal.ZERO)) {
				throw new ServiceException("撤销拣货失败，选择的记录中不允许有撤销的记录");
			}
			// 根据拣货记录下架库存
			stockBiz.moveStockByCancelPick(StockLogTypeEnum.INSTOCK_BY_CANCEL_PICK, logSoPick, null);
			// 将该拣货记录标记为已撤销
			logSoPickDao.setCancelPick(logSoPick.getLsopId());
			// 生成一笔反向的拣货记录
			logSoPick.setLsopId(null);
			logSoPick.setPickRealQty(logSoPick.getPickRealQty().negate());
			logSoPickDao.saveLogSoPick(logSoPick);
			// 更新发货单明细状态与实收数量
			SoDetail soDetail = soBillBiz.getSoDetailById(logSoPick.getSoDetailId());
			soBillBiz.updateSoDetailStatus(soDetail, logSoPick.getPickRealQty());
			// 更新发货单头表状态
			SoHeader soHeader = soBillBiz.getSoHeaderById(logSoPick.getSoBillId());
			soBillBiz.updateSoBillState(soHeader);
			// 记录业务日志
			logBiz.auditLog(AuditLogType.OUTSTOCK, soHeader.getSoBillId(), soHeader.getSoBillNo(), "撤销发货");
		});
	}

	private String spliceLog(WmsTaskProcTypeEnum taskProcTypeEnum, List<Stock> stockList) {
		StringBuilder logString = new StringBuilder();
		for (Stock stock : stockList) {
			logString.append(String.format(" SKU[%s]批次[%s]数量[%s] ", stock.getSkuCode(), stock.getSkuLot1(),
				stock.getStockBalance().intValue()));
		}
		return String.format("PDA%s 箱码:[%s]%s", taskProcTypeEnum.getDesc(), stockList.get(0).getBoxCode(), logString);
	}

	private void canPick(List<SoPickPlan> soPickPlanList, List<Stock> stockList) {
		List<SoDetail> soDetailList = new ArrayList<>();
		BigDecimal surplusQty = BigDecimal.ZERO;
		for (SoPickPlan soPickPlan : soPickPlanList) {
			SoDetail soDetail = soBillBiz.getSoDetailById(soPickPlan.getSoDetailId());
			soDetailList.add(soDetail);
		}
		List<SoDetail> soDetails = soDetailList.stream()
			.distinct()
			.collect(Collectors.toList());
		BigDecimal pickQty = BigDecimal.ZERO;

		for (SoDetail soDetail : soDetails) {
			if (soDetail.getBillDetailState().equals(SoDetailStateEnum.DELETED)
				|| soDetail.getBillDetailState().equals(SoDetailStateEnum.ALL_OUT_STOCK)) {
				throw new ServiceException("拣货失败,发货单明细状态为" + soDetail.getBillDetailState() + "不能进行拣货");
			}
			surplusQty = surplusQty.add(soDetail.getSurplusQty());
		}
		for (Stock stock : stockList) {
			pickQty = pickQty.add(stock.getStockBalance());
		}
		if (BigDecimalUtil.gt(pickQty, surplusQty)) {
			throw new ServiceException("拣货失败,发货数量大于剩余数量,请拆箱之后再进行拣货");
		}
	}

	private void canPick(BigDecimal surplusQty, List<SoPickPlan> soPickPlanList, List<Stock> stockList) {
		List<SoDetail> soDetailList = new ArrayList<>();
		for (SoPickPlan soPickPlan : soPickPlanList) {
			SoDetail soDetail = soBillBiz.getSoDetailById(soPickPlan.getSoDetailId());
			soDetailList.add(soDetail);
		}
		BigDecimal pickQty = BigDecimal.ZERO;

		for (Stock stock : stockList) {
			pickQty = pickQty.add(stock.getStockBalance());
		}
		if (BigDecimalUtil.gt(surplusQty, pickQty)) {
			throw new ServiceException("拣货失败,发货数量大于剩余数量");
		}
	}

	/**
	 * 1)拣货计划拣货、1. 释放库存占用 2.移动库存到出库暂存区 3.更新拣货计划 4.生产并保存拣货记录
	 * 2)更新发货单明细
	 *
	 * @param soPickPlanList 拣货计划
	 */
	private void updateSoPickPlan(List<SoPickPlan> soPickPlanList) {
		for (SoPickPlan soPickPlan : soPickPlanList) {
			List<Serial> serialList = stockQueryBiz.findSerialByStock(soPickPlan.getStockId());
			List<String> serialNumberList = null;
			if (Func.isNotEmpty(serialList)) {
				serialNumberList = serialList
					.stream()
					.map(Serial::getSerialNumber)
					.collect(Collectors.toList());
			}
			SoDetail soDetail = soBillBiz.getSoDetailById(soPickPlan.getSoDetailId());
			soPickPlanBiz.pickByPlan(soDetail, soPickPlan, soPickPlan.getSurplusQty(), serialNumberList);
			soBillBiz.updateSoDetailStatus(soDetail, soPickPlan.getSurplusQty());
		}
	}

	void pickByPcsByTask(WmsTask task, List<Stock> stockList, SoHeader soHeader, PickByPcsRequest request, BigDecimal qty) {
		// 根据任务ID拣货计划
		List<SoPickPlan> soPickPlanList = soPickPlanBiz.findPickByTaskId(task.getTaskId());

		// 校验是否超发
		canPick(qty, soPickPlanList, stockList);

		for (Stock stock : stockList) {
			canPickLocation(stock.getLocId());
		}

		// 拣货、更新拣货计划
		SoDetail soDetail = soBillBiz.getSoDetailById(soPickPlanList.get(0).getSoDetailId());
		soPickPlanBiz.pickByPlan(soDetail, soPickPlanList.get(0), soPickPlanList.get(0).getSurplusQty(), request.getSerailList());
		soBillBiz.updateSoDetailStatus(soDetail, soPickPlanList.get(0).getSurplusQty());

		// 5、更新出库单信息
		soBillBiz.updateSoBillState(soHeader);

		// 6、更新任务
		wmsTaskBiz.updateWmsTaskStateByTaskId(task.getTaskId(), WmsTaskStateEnum.COMPLETED, task.getTaskQty());

		// 7、记录业务日志
		logBiz.auditLog(AuditLogType.OUTSTOCK, soHeader.getSoBillId(), soHeader.getSoBillNo(),
			spliceLog(WmsTaskProcTypeEnum.BY_PCS, stockList));
	}

	void bulkPickByTask(WmsTask task, SoHeader soHeader, BulkPickRequest request, SoPickPlan soPickPlan, BigDecimal qty) {
		List<SoPickPlan> soPickPlanList = new ArrayList<>();
		soPickPlanList.add(soPickPlan);
		List<Stock> stockList = new ArrayList<>();
		Stock stock = stockQueryBiz.findStockById(soPickPlan.getStockId());
		stockList.add(stock);
		// 校验是否超发
		canPick(qty, soPickPlanList, stockList);

		for (Stock stocks : stockList) {
			canPickLocation(stocks.getLocId());
		}

		// 拣货、更新拣货计划
		SoDetail soDetail = soBillBiz.getSoDetailById(soPickPlan.getSoDetailId());
		soPickPlanBiz.pickByPlan(soDetail, soPickPlan, qty, request.getSerailList());
		soBillBiz.updateSoDetailStatus(soDetail, qty);

		// 5、更新出库单信息
		soBillBiz.updateSoBillState(soHeader);

		task.setScanQty(task.getScanQty().add(qty));
		WmsTaskStateEnum taskStateEnum = WmsTaskStateEnum.START_EXECUTION;
		if (BigDecimalUtil.eq(task.getTaskQty(), task.getScanQty())) {
			taskStateEnum = WmsTaskStateEnum.COMPLETED;
		}
		// 6、更新任务
		wmsTaskBiz.updateWmsTaskStateByTaskId(task.getTaskId(), taskStateEnum, task.getScanQty());

		// 7、记录业务日志
		logBiz.auditLog(AuditLogType.OUTSTOCK, soHeader.getSoBillId(), soHeader.getSoBillNo(),
			spliceLog(WmsTaskProcTypeEnum.BY_PCS, stockList));
	}

	void pickByPcsByStock(PickByPcsRequest request, SoHeader soHeader, SoDetail soDetail, List<Stock> stockList) {
		AssertUtil.notNull(soHeader, "PDA拣货失败，发货单参数为空");
		AssertUtil.notNull(soDetail, "PDA拣货失败，发货单明细参数为空");
		AssertUtil.notNull(stockList, "PDA拣货失败，库存参数为空");

		Location sourceLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		// 1 业务判断：
		// 1.1 单据和单据明细行的状态如果为终结状态，则不能进行拣货
		// 1.1 拣货数量是否超过剩余数量
		canPick(soHeader, soDetail, request.getQty());

		// 2 生成拣货记录，需要注意序列号（log_so_pick)
		Stock stock = stockList.stream()
			.filter(stockParam -> Func.equals(stockParam.getSkuCode(), request.getSkuCode())
				&& Func.equals(stockParam.getSkuLot1(), request.getSkuLot1())
				&& Func.equals(stockParam.getLocCode(), request.getLocCode()))
			.findFirst().orElse(null);
		AssertUtil.notNull(stockList, "PDA拣货失败，根据您输入的条件找不到对应的库存");
		AssertUtil.notNull(stock, "PDA拣货失败，根据您输入的条件找不到对应的库存");
		canPickLocation(stock.getLocId());

		LogSoPick logSoPick = logSoPickFactory.createLogSoPick(request, soHeader, soDetail, stock, sourceLocation);
		logSoPickDao.saveLogSoPick(logSoPick);

		// 3 调用拣货计划中相应的函数
		// 3 移动库存到出库集货区
		Location location = locationBiz
			.getLocationByZoneType(request.getWhId(), DictKVConstant.ZONE_TYPE_PICK_TO).get(0);
		stockBiz.moveStock(stock, request.getSerailList(), request.getQty(),
			location, StockLogTypeEnum.OUTSTOCK_BY_PC_PDA, soHeader.getSoBillId(), soHeader.getSoBillNo(),
			soDetail.getSoLineNo(), null);
		// 4 更新出库单明细中的状态和数量
		soBillBiz.updateSoDetailStatus(soDetail, request.getQty());
		// 5 更新发货单状态
		soBillBiz.updateSoBillState(soHeader);
		// 6 记录业务日志
		logBiz.auditLog(AuditLogType.OUTSTOCK, soHeader.getSoBillId(), soHeader.getSoBillNo(),
			String.format("PDA%s 箱码:[%s] SKU[%s]批次[%s]数量[%s] ", WmsTaskProcTypeEnum.BY_PCS.getDesc(), stockList.get(0).getBoxCode(), request.getSkuCode(), request.getSkuLot1(),
				request.getQty()));
	}

	void bulkPickByStock(BulkPickRequest request, SoHeader soHeader, SoDetail soDetail, List<Stock> stockList) {
		AssertUtil.notNull(soHeader, "PDA拣货失败，发货单参数为空");
		AssertUtil.notNull(soDetail, "PDA拣货失败，发货单明细参数为空");
		AssertUtil.notNull(stockList, "PDA拣货失败，库存参数为空");

		Location sourceLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		// 1 业务判断：
		// 1.1 单据和单据明细行的状态如果为终结状态，则不能进行拣货
		// 1.1 拣货数量是否超过剩余数量
		canPick(soHeader, soDetail, request.getQty());

		// 2 生成拣货记录，需要注意序列号（log_so_pick)
		Stock stock = stockList.stream()
			.filter(stockParam -> Func.equals(stockParam.getSkuCode(), request.getSkuCode())
				&& Func.equals(stockParam.getSkuLot1(), request.getSkuLot1())
				&& Func.equals(stockParam.getLocCode(), request.getLocCode()))
			.findFirst().orElse(null);
		AssertUtil.notNull(stockList, "PDA拣货失败，根据您输入的条件找不到对应的库存");
		AssertUtil.notNull(stock, "PDA拣货失败，根据您输入的条件找不到对应的库存");
		canPickLocation(stock.getLocId());

		LogSoPick logSoPick = logSoPickFactory.createLogSoPick(request, soHeader, soDetail, stock, sourceLocation);
		logSoPickDao.saveLogSoPick(logSoPick);

		// 3 调用拣货计划中相应的函数
		// 3 移动库存到出库集货区
		Location location = locationBiz
			.getLocationByZoneType(request.getWhId(), DictKVConstant.ZONE_TYPE_PICK_TO).get(0);
		stockBiz.moveStock(stock, request.getSerailList(), request.getQty(),
			location, StockLogTypeEnum.OUTSTOCK_BY_PC_PDA, soHeader.getSoBillId(), soHeader.getSoBillNo(),
			soDetail.getSoLineNo(), null);
		// 4 更新出库单明细中的状态和数量
		soBillBiz.updateSoDetailStatus(soDetail, request.getQty());
		// 5 更新发货单状态
		soBillBiz.updateSoBillState(soHeader);
		// 6 记录业务日志
		logBiz.auditLog(AuditLogType.OUTSTOCK, soHeader.getSoBillId(), soHeader.getSoBillNo(),
			String.format("PDA%s 箱码:[%s] SKU[%s]批次[%s]数量[%s] ", WmsTaskProcTypeEnum.BY_PCS.getDesc(), stockList.get(0).getBoxCode(), request.getSkuCode(), request.getSkuLot1(),
				request.getQty()));
	}

	/**
	 * 判断库位是否是 出库暂存区，收货接驳区，发货接驳区库位
	 * 是则抛异常
	 *
	 * @param locId locId
	 */
	void canPickLocation(Long locId) {
		Location originalLocation = locationBiz.findByLocId(locId);
		if (locationBiz.isPickToLocation(originalLocation)) {
			throw new ServiceException("拣货失败，原库位不能是出库暂存区库位");
		}
		if (locationBiz.isAgvTemporaryLocation(originalLocation)) {
			throw new ServiceException("拣货失败，原库位不能是收货接驳区库位/发货接驳区库位");
		}
	}

	@Override
	public IPage<FindAllPickingResponse> outStockCheckoutFindSoBill(findSoHeaderByNoRequest request, Query query) {
		List<SoBillStateEnum> soBillStateEnums = new ArrayList<>();
		soBillStateEnums.add(SoBillStateEnum.EXECUTING);
		soBillStateEnums.add(SoBillStateEnum.PART);
		soBillStateEnums.add(SoBillStateEnum.ALL_OUT_STOCK);
		request.setSoBillState(soBillStateEnums);
		return soBillBiz.getAllPickingByNo(Condition.getPage(query), request);
	}

	@Override
	public void outStockCheckout(Long soBillId, String boxCode, List<String> boxCodeList) {
		// 获取明细，排除未收货的
		List<SoDetail> soDetailList = soBillBiz.findSoDetailExcludeNormal(soBillId);
		// 获取扫码箱子中的库存
		List<Stock> currentBoxStockList = stockQueryBiz.findStockOnPickTo(boxCode);
		// 获取已复核箱子中的库存
		List<Stock> checkedStockList = new ArrayList<>();
		if (Func.isNotEmpty(boxCodeList)) {
			boxCodeList.forEach(item -> {
				List<Stock> checkedStock = stockQueryBiz.findStockOnPickTo(item);
				checkedStockList.addAll(checkedStock);
			});
		}
		// 标记 该箱存储的物品、批次、专用客户和物品明细中的是否一致
		boolean flag = false;
		for (SoDetail soDetail : soDetailList) {
			if (flag) {
				continue;
			}
			for (Stock currentStock : currentBoxStockList) {
				// 在已复核过的箱中找出和明细相同的物品
				List<Stock> filterCheckedStockList = checkedStockList.stream()
					.filter(x -> (x.getSkuLot2().equals(currentStock.getSkuLot2())
						&& x.getSkuId().equals(currentStock.getSkuId())))
					.collect(Collectors.toList());
				// skuLot1 为空，可以是任意批次的，不为空，则只能是指定批次的
				if (Func.isNotEmpty(soDetail.getSkuLot1())) {
					if (soDetail.getSkuLot1().equals(currentStock.getSkuLot1())) {
						filterCheckedStockList.stream()
							.filter(x -> x.getSkuLot1().equals(currentStock.getSkuLot1()));
					} else {
						continue;
					}
				}
				// skuLot4 为空，可以是任意客户的
				if (Func.isNotEmpty(soDetail.getSkuLot4())) {
					if (soDetail.getSkuLot4().equals(currentStock.getSkuLot4())) {
						filterCheckedStockList.stream()
							.filter(x -> x.getSkuLot4().equals(currentStock.getSkuLot4()));
					} else {
						continue;
					}
				}
				if (soDetail.getSkuLot2().equals(currentStock.getSkuLot2())
					&& soDetail.getSkuId().equals(currentStock.getSkuId())) {
					BigDecimal checkedQty = filterCheckedStockList.stream()
						.map(Stock::getStockEnable)
						.reduce(BigDecimal.ZERO, BigDecimal::add);

					if (BigDecimalUtil.gt(checkedQty, BigDecimal.ZERO)) {
						if (BigDecimalUtil.gt((checkedQty.add(currentStock.getStockEnable())), soDetail.getScanQty())) {
							throw ExceptionUtil.mpe("复核失败，原因：" +
									"物品[{}]在当前箱中数量为{}，在已核过数量为{}，实际发货数量为{}",
								currentStock.getSkuCode(), currentStock.getStockEnable(), checkedQty, soDetail.getScanQty());
						}
					}
					flag = true;
				}
			}
		}
		if (!flag) {
			throw ExceptionUtil.mpe("复核失败，原因：该箱中不存在发货单[{}]明细中的物品", soDetailList.get(0).getSoBillNo());
		}
	}
}
