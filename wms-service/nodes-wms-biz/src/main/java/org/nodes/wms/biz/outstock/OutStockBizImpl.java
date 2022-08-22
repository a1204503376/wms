package org.nodes.wms.biz.outstock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.DictKVConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.biz.outstock.logSoPick.modular.LogSoPickFactory;
import org.nodes.wms.biz.outstock.plan.SoPickPlanBiz;
import org.nodes.wms.biz.outstock.so.SoBillBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.biz.stockManage.StockManageBiz;
import org.nodes.wms.biz.task.WmsTaskBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.outstock.SoPickPlanDao;
import org.nodes.wms.dao.outstock.logSoPick.LogSoPickDao;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.*;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindPickingBySoBillIdResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.OutboundAccessAreaLocationQueryResponse;
import org.nodes.wms.dao.outstock.logSoPick.entities.LogSoPick;
import org.nodes.wms.dao.outstock.so.dto.input.PickByPcRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillDistributedRequest;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.dao.outstock.so.enums.SoDetailStateEnum;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.SoPickPlanForDistributionResponse;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.dto.output.PickByPcStockDto;
import org.nodes.wms.dao.stock.dto.output.StockSoPickPlanResponse;
import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskProcTypeEnum;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void pickByPcsOnPc(PickByPcRequest request) {
		SoHeader soHeader = soBillBiz.getSoHeaderById(request.getSoBillId());
		SoDetail soDetail = soBillBiz.getSoDetailById(request.getPickByPcStockDtoList().get(0).getSoDetailId());
		// 1 业务判断：
		// 1.1 如果单据有拣货计划则不能使用PC拣货
		if (soPickPlanBiz.hasEnablePickPlan(request.getSoBillId())) {
			throw new ServiceException("拣货失败,收货单已存在拣货计划");
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
				soDetail.getSoLineNo());
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
			throw new ServiceException("拣货失败,收货单状态为" + soHeader.getSoBillState() + "不能进行拣货");
		}
		if (soDetail.getBillDetailState().equals(SoDetailStateEnum.DELETED)
			|| soDetail.getBillDetailState().equals(SoDetailStateEnum.ALL_OUT_STOCK)) {
			throw new ServiceException("拣货失败,收货单明细状态为" + soDetail.getBillDetailState() + "不能进行拣货");
		}
		if (BigDecimalUtil.gt(pickQty, soDetail.getSurplusQty())) {
			throw new ServiceException("拣货失败,收货数量大于剩余数量");
		}
	}

	private void canPick(List<SoDetail> soDetailList, List<Stock> stockList) {
		BigDecimal surplusQty = BigDecimal.ZERO;
		BigDecimal pickQty = BigDecimal.ZERO;
		for (SoDetail soDetail : soDetailList) {
			if (soDetail.getBillDetailState().equals(SoDetailStateEnum.DELETED)
				|| soDetail.getBillDetailState().equals(SoDetailStateEnum.ALL_OUT_STOCK)) {
				throw new ServiceException("拣货失败,收货单明细状态为" + soDetail.getBillDetailState() + "不能进行拣货");
			}
			surplusQty = surplusQty.add(soDetail.getSurplusQty());
		}
		for (Stock stock : stockList) {
			pickQty = pickQty.add(stock.getStockBalance());
		}
		if (BigDecimalUtil.gt(pickQty, surplusQty)) {
			throw new ServiceException("拣货失败,收货数量大于剩余数量");
		}
	}

	private void canPick(SoHeader soHeader) {
		if (soHeader.getSoBillState().equals(SoBillStateEnum.COMPLETED)
			|| soHeader.getSoBillState().equals(SoBillStateEnum.ALL_OUT_STOCK)
			|| soHeader.getSoBillState().equals(SoBillStateEnum.CANCELED)) {
			throw new ServiceException("拣货失败,收货单状态为" + soHeader.getSoBillState() + "不能进行拣货");
		}
	}

	private void canPick(SoDetail soDetail, BigDecimal pickQty) {
		if (soDetail.getBillDetailState().equals(SoDetailStateEnum.DELETED)
			|| soDetail.getBillDetailState().equals(SoDetailStateEnum.ALL_OUT_STOCK)) {
			throw new ServiceException("拣货失败,收货单明细状态为" + soDetail.getBillDetailState() + "不能进行拣货");
		}
		if (BigDecimalUtil.gt(pickQty, soDetail.getSurplusQty())) {
			throw new ServiceException("拣货失败,收货数量大于剩余数量");
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
		request.setSoBillState(soBillStateEnums);
		return soBillBiz.getAllPickingByNo(Condition.getPage(query), request);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void pickByPcs(PickByPcsRequest request) {
		SoHeader soHeader = soBillBiz.getSoHeaderById(request.getSoBillId());
		SoDetail soDetail = soBillBiz.getSoDetailById(request.getSoDetailId());
		WmsTask task = wmsTaskBiz.findEnableTaskBySoBillId(request.getSoBillId(), request.getSoDetailId());
		if (Func.isNotEmpty(task)) {
			if (!Func.equals(task.getTaskProcType(), WmsTaskProcTypeEnum.BY_PCS)) {
				throw new ServiceException("PDA按件拣货失败，存在任务，但不是按件拣货的任务");
			}

			//执行按照任务拣货
			PickByBoxCodeRequest pickByBoxCodeRequest = new PickByBoxCodeRequest();
			pickByBoxCodeRequest.setBoxCode(task.getBoxCode());
			pickByBox(pickByBoxCodeRequest, WmsTaskProcTypeEnum.BY_PCS);
			return;
		}

		Location sourceLocation = locationBiz.findLocationByLocCode(request.getWhId(), request.getLocCode());
		// 1 业务判断：
		// 1.1 单据和单据明细行的状态如果为终结状态，则不能进行拣货
		// 1.1 拣货数量是否超过剩余数量
		canPick(soHeader, soDetail, request.getQty());

		// 2 生成拣货记录，需要注意序列号（log_so_pick)
		List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(request.getBoxCode());
		AssertUtil.notNull(stockList, "PDA拣货失败，根据箱码获取库存失败");
		Stock stock = stockList.stream()
			.filter(stockParam -> Func.equals(stockParam.getSkuCode(), request.getSkuCode())
				&& Func.equals(stockParam.getSkuLot1(), request.getSkuLot1())
				&& Func.equals(stockParam.getLocCode(), request.getLocCode()))
			.findFirst().orElse(null);
		AssertUtil.notNull(stockList, "PDA拣货失败，根据您输入的条件找不到对应的库存");
		AssertUtil.notNull(stock, "PDA拣货失败，根据您输入的条件找不到对应的库存");
		LogSoPick logSoPick = logSoPickFactory.createLogSoPick(request, soHeader, soDetail, stock, sourceLocation);
		logSoPickDao.saveLogSoPick(logSoPick);

		// 3 调用拣货计划中相应的函数
		// 3 移动库存到出库集货区
		Location location = locationBiz
			.getLocationByZoneType(request.getWhId(), DictKVConstant.ZONE_TYPE_PICK_TO).get(0);
		stockBiz.moveStock(stock, request.getSerailList(), request.getQty(),
			location, StockLogTypeEnum.OUTSTOCK_BY_PC_PDA, soHeader.getSoBillId(), soHeader.getSoBillNo(),
			soDetail.getSoLineNo());
		// 4 更新出库单明细中的状态和数量
		soBillBiz.updateSoDetailStatus(soDetail, request.getQty());
		// 5 更新发货单状态
		soBillBiz.updateSoBillState(soHeader);
		// 6 记录业务日志
		logBiz.auditLog(AuditLogType.OUTSTOCK, "PDA按件拣货");
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
		//1、根据箱码查询任务
		WmsTask task = wmsTaskBiz.findEnableTaskByBoxCode(request.getBoxCode(), taskProcTypeEnum);
		SoHeader soHeader = soBillBiz.getSoHeaderById(task.getBillId());
		List<Stock> stockList = stockQueryBiz.findEnableStockByBoxCode(request.getBoxCode());
		//2、参数校验 头表
		canPick(soHeader);
		//3、根据拣货计划生成拣货记录,根据任务id从拣货计划中查找
		List<SoPickPlan> soPickPlanList = soPickPlanBiz.findPickByTaskId(task.getTaskId());
		List<SoDetail> soDetailList = new ArrayList<>();
		for (SoPickPlan soPickPlan : soPickPlanList) {
			SoDetail soDetail = soBillBiz.getSoDetailById(soPickPlan.getSoDetailId());
			soDetailList.add(soDetail);
		}
		canPick(soDetailList, stockList);
		//4、根据拣货计划下发库存和释放占用 会进行如下操作(1. 释放库存占用 2.移动库存到出库暂存区 3.更新拣货计划 4.生产并保存拣货记录)
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
			LogSoPick logSoPick = soPickPlanBiz.pickByPlan(soDetail, soPickPlan, soPickPlan.getPickPlanQty(), serialNumberList);
			soBillBiz.updateSoDetailStatus(soDetail, soDetail.getPlanQty());
		}

		//5、更新出库单信息
		soBillBiz.updateSoBillState(soHeader);

		//6、更新任务
		wmsTaskBiz.updateWmsTaskStateByTaskId(task.getTaskId(), WmsTaskStateEnum.COMPLETED, task.getTaskQty());

		//7、记录业务日志
		logBiz.auditLog(AuditLogType.OUTSTOCK, soHeader.getSoBillId(), soHeader.getSoBillNo(), spliceLog(taskProcTypeEnum, stockList));
	}

	@Override
	public IPage<OutboundAccessAreaLocationQueryResponse> findLocOfAgvPickTo(
		FindLocOfAgvPickToRequest request, Query query) {
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public Boolean pickOnAgvPickTo(MoveOnAgvPickToRequest request) {
		//1、判断库位是否有库存
		boolean emptyLocation = stockQueryBiz.isEmptyLocation(request.getLocId());
		if (!emptyLocation) {
			throw new ServiceException("查询不到当前库位的库存");
		}

		//2、根据库位查询库存
		List<Stock> stockList = stockQueryBiz.findStockByLocation(request.getLocId());
		AssertUtil.notNull(stockList, "接驳区拣货失败，根据库位查询不到对应库存");
		List<String> boxCodeList = stockList.stream()
			.map(Stock::getBoxCode)
			.distinct()
			.collect(Collectors.toList());

		//3、判断是不是超拣---直接走按箱的流程
		//4、如果没有超拣则执行按箱的流---直接走按箱的流程
		WmsTask task = wmsTaskBiz.findEnableTaskByBoxCode(stockList.get(0).getBoxCode(), WmsTaskProcTypeEnum.BY_LOC);
		SoHeader soHeader = soBillBiz.getSoHeaderById(task.getBillId());
		SoDetail soDetail = soBillBiz.getSoDetailById(task.getBillDetailId());
		//2、参数校验
		if (BigDecimalUtil.gt(task.getTaskQty().subtract(task.getScanQty()), soDetail.getSurplusQty())) {
			return false;
		}

		//5、如果超了则返回
		for (String boxCode : boxCodeList) {
			PickByBoxCodeRequest boxCodeRequest = new PickByBoxCodeRequest();
			boxCodeRequest.setBoxCode(boxCode);
			pickByBox(boxCodeRequest, WmsTaskProcTypeEnum.BY_LOC);
		}
		return true;
	}

	@Override
	public void moveOnAgvPickTo(MoveOnAgvPickToRequest request) {
		//// 执行按箱移动
		//// 记录日志

		//C1 判断目标库位是不是人工拣货区，如果不是则报异常 (PS:只能移动到人工拣货区)
		//1、按库位移动

		//2、更新拣货计划
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
		String result = soPickPlanBiz.runByPickStrategy(soHeader, soDetails, pickPlans);
		// 更新发货单状态
		if (SoBillStateEnum.CREATE.equals(soHeader.getSoBillState())) {
			soBillBiz.updateState(soBillId, SoBillStateEnum.EXECUTING);
		}
		// 记录日志
		logBiz.auditLog(AuditLogType.DISTRIBUTE_STRATEGY, soBillId, "执行自动分配");

		return result;
	}

	@Override
	public boolean cancelDistribute(Long soBillId) {
		return false;
	}

	@Override
	public boolean issued(Long soBillId) {
		return false;
	}

	@Override
	public List<StockSoPickPlanResponse> getStockByDistributeAdjust(Long skuId, String skuLot1, String skuLot4) {
		SkuLotBaseEntity sku = new SkuLotBaseEntity();
		sku.setSkuLot1(skuLot1);
		sku.setSkuLot4(skuLot4);
		// 获取可分配的库存
		List<Stock> stockList = stockQueryBiz.findEnableStockBySkuAndSkuLot(skuId, sku);
		if (Func.isEmpty(stockList)) {
			throw ExceptionUtil.mpe("该物品没有可分配的库存");
		}
		// 总库存（有箱码的库存加无箱码的库存）
		List<Stock> allStock = new ArrayList<>();

		// 查找出有箱码的库存，追加到总的库存中
		List<String> boxCodeList = stockList.stream()
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
		List<Stock> stockOfNonBoxCode = stockList.stream()
			.filter(item -> Func.isEmpty(item.getBoxCode()))
			.collect(Collectors.toList());
		if (Func.isNotEmpty(stockOfNonBoxCode)) {
			allStock.addAll(stockOfNonBoxCode);
		}

		List<Long> stockIdList = allStock.stream()
			.map(Stock::getStockId)
			.collect(Collectors.toList());

		List<SoPickPlan> soPickPlanList = soPickPlanBiz.findByStockIds(stockIdList);

		List<StockSoPickPlanResponse> stockSoPickPlanList = Func.copy(allStock, StockSoPickPlanResponse.class);

		if (Func.isNotEmpty(soPickPlanList)) {
			// 拣货计划中根据stockId分组 统计每个stock对应的所有拣货计划拣货量总数
			Map<Long, List<SoPickPlan>> planListMap = soPickPlanList.stream()
				.collect(Collectors.groupingBy(SoPickPlan::getStockId));
			planListMap.forEach((stockId, planList) -> {

				BigDecimal pickRealQty = planList.stream()
					.map(SoPickPlan::getPickRealQty)
					.reduce(BigDecimal.ZERO, BigDecimal::add);

				for (StockSoPickPlanResponse stockSoPickPlan : stockSoPickPlanList) {
					if (stockSoPickPlan.getStockId().equals(stockId)) {
						stockSoPickPlan.setPickRealQty(pickRealQty);
						break;
					}
				}
			});
		}
		return stockSoPickPlanList;
	}

	@Override
	public boolean manualDistribute(SoBillDistributedRequest soBillDistributedRequest) {
		return false;
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void cancelOutStock(List<Long> logSoPickIdList) {
		// 根据拣货记录id查找所有的拣货记录
		List<LogSoPick> logSoPickList = logSoPickDao.getByIds(logSoPickIdList);
		logSoPickList.forEach(logSoPick -> {
			// 判断收货记录中是否存在撤销数为负数的，有就抛异常
			if (BigDecimalUtil.lt(logSoPick.getPickRealQty(), BigDecimal.ZERO)) {
				throw new ServiceException("撤销失败，选择的记录中不允许有已撤销的记录");
			}
			// 根据拣货记录下架库存
			stockBiz.moveStockByCancelPick(StockLogTypeEnum.INSTOCK_BY_CANCEL_PICK, logSoPick);
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
			logBiz.auditLog(AuditLogType.OUTSTOCK, soHeader.getSoBillId(), soHeader.getSoBillNo(), "撤销收货");
		});
	}

	private String spliceLog(WmsTaskProcTypeEnum taskProcTypeEnum, List<Stock> stockList) {
		StringBuilder logString = new StringBuilder();
		for (Stock stock : stockList) {
			logString.append(String.format(" SKU[%s]批次[%s]数量[%s] ", stock.getSkuCode(), stock.getSkuLot1(), stock.getStockBalance().intValue()));
		}
		return String.format("PDA%s 箱码:[%s]%s", taskProcTypeEnum.getDesc(), stockList.get(0).getBoxCode(), logString);
	}
}
