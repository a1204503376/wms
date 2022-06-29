package org.nodes.wms.core.outstock.so.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.allot.enums.AllotBillStateEnum;
import org.nodes.wms.core.allot.service.IAllotHeaderService;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageDetailCache;
import org.nodes.wms.core.basedata.dto.SkuLogDTO;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.ISkuLogService;
import org.nodes.wms.core.basedata.service.ISkuPackageDetailService;
import org.nodes.wms.core.basedata.service.ISkuReplaceService;
import org.nodes.wms.core.basedata.service.ISkuUmService;
import org.nodes.wms.core.basedata.vo.SkuReplaceVO;
import org.nodes.wms.core.basedata.wrapper.SkuLotWrapper;
import org.nodes.wms.core.instock.asn.vo.LpnItemVo;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.outstock.inventory.entity.SoInventory;
import org.nodes.wms.core.outstock.inventory.service.ISoInventoryService;
import org.nodes.wms.core.outstock.so.cache.SoCache;
import org.nodes.wms.core.outstock.so.dto.CreatePickPlanDTO;
import org.nodes.wms.core.outstock.so.dto.PickPlanDTO;
import org.nodes.wms.core.outstock.so.dto.SavePickPlanDTO;
import org.nodes.wms.core.outstock.so.dto.SavePickPlanDetailDTO;
import org.nodes.wms.core.outstock.so.entity.*;
import org.nodes.wms.core.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.core.outstock.so.enums.SoDetailStateEnum;
import org.nodes.wms.core.outstock.so.enums.WellenStateEnum;
import org.nodes.wms.core.outstock.so.mapper.PickPlanMapper;
import org.nodes.wms.core.outstock.so.mapper.SoDetailMapper;
import org.nodes.wms.core.outstock.so.mapper.WellenMapper;
import org.nodes.wms.core.outstock.so.service.*;
import org.nodes.wms.core.outstock.so.vo.*;
import org.nodes.wms.core.outstock.so.wrapper.PickPlanWrapper;
import org.nodes.wms.core.stock.core.dto.StockMoveDTO;
import org.nodes.wms.core.stock.core.dto.StockOccupyDTO;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.entity.StockOccupy;
import org.nodes.wms.core.stock.core.enums.EventTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockOccupyTypeEnum;
import org.nodes.wms.core.stock.core.enums.StockStatusEnum;
import org.nodes.wms.core.stock.core.mapper.SerialMapper;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.nodes.wms.core.stock.core.service.IStockOccupyService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.vo.SerialVO;
import org.nodes.wms.core.stock.core.wrapper.StockWrapper;
import org.nodes.wms.core.strategy.dto.OutstockExecuteDTO;
import org.nodes.wms.core.strategy.entity.OutstockLog;
import org.nodes.wms.core.strategy.service.IOutstockLogService;
import org.nodes.wms.core.strategy.service.IOutstockService;
import org.nodes.wms.core.system.dto.TaskDTO;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.enums.ZoneVirtualTypeEnum;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.sku.enums.SkuLevelEnum;
import org.nodes.wms.dao.basics.sku.enums.SkuLogTypeEnum;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 拣货计划表 服务实现类
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class PickPlanServiceImpl<M extends PickPlanMapper, T extends PickPlan>
	extends BaseServiceImpl<PickPlanMapper, PickPlan>
	implements IPickPlanService {

	@Autowired
	IWellenService wellenService;
	@Autowired
	IWellenDetailService wellenDetailService;
	@Autowired
	ISoHeaderService soHeaderService;
	@Autowired
	ISoDetailService soDetailService;
	@Autowired
	ISkuReplaceService skuReplaceService;
	@Autowired
	IStockService stockService;
	@Autowired
	IStockDetailService stockDetailService;
	@Autowired
	IStockOccupyService stockOccupyService;
	@Autowired
	IOutstockService outstockService;
	@Autowired
	IOutstockLogService outstockLogService;
	@Autowired
	ITaskService taskService;
	@Autowired
	ISoPickService soPickService;
	@Autowired
	ISkuUmService skuUmService;
	@Autowired
	ISkuLogService skuLogService;
	@Autowired
	SerialMapper serialMapper;
	@Autowired
	WellenMapper wellenMapper;
	@Autowired
	SoDetailMapper soDetailMapper;
	@Autowired
	ISystemProcService systemProcService;
	@Autowired
	ISoInventoryService soInventoryService;
	@Autowired
	IAllotHeaderService allotHeaderService;

	/**
	 * 存储操作人员正在生成拣货计划的订单
	 */
	private static List<CreatePickPlanDetailVO> TempPickPlanList = new ArrayList<>();

	/**
	 * 创建拣货计划明细
	 *
	 * @param result   创建拣货计划对象
	 * @param soHeader 出库单表头
	 * @param soDetail 出库单明细
	 * @param sku      物品对象
	 * @param repSku   被替代品对象
	 */
	public synchronized void createPickPlanDetail(CreatePickPlanVO result,
												  SoHeader soHeader,
												  SoDetail soDetail,
												  Sku sku,
												  Sku repSku,
												  CreatePickPlanDTO createPickPlanDTO) {
		// 替代物品的情况下，修改物品信息的包装为明细的包装；方便后面查询库存
		if (Func.isEmpty(repSku)) {
			sku.setWspId(soDetail.getWspId());
		}
		int allocMode = Func.toInt(ParamCache.getValue(ParamEnum.STRATEGY_ALLOC_MODE.getKey()));
		ISwitchAllocStrategyMode.AllocModeEnum allocModeByValue = ISwitchAllocStrategyMode.AllocModeEnum.
			getAllocModeByValue(allocMode);
		OutstockExecuteDTO outstockExecute = null;
		switch (allocModeByValue) {
			case MANUAL:
				outstockExecute = outstockService.manualExecute(
					soHeader,
					soDetail,
					sku,
					createPickPlanDTO.getIsWellen(), createPickPlanDTO.getDto());
				break;
			case AUTO:
				// 执行分配策略，获取可用库存
				outstockExecute = outstockService.execute(
					soHeader,
					soDetail,
					sku);
				break;
		}
		// 发生异常 或者 查询出的库存为空 的情况下，直接生成缺货信息
		if (Func.isNotEmpty(outstockExecute.getException()) || ObjectUtil.isEmpty(outstockExecute.getStockList())) {
			CreatePickPlanDetailVO detail = new CreatePickPlanDetailVO();
			BeanUtil.copyProperties(outstockExecute, detail);
			detail.setSoBillNo(soHeader.getSoBillNo());
			detail.setWhId(soHeader.getWhId());
			detail.setSkuCode(sku.getSkuCode());
			detail.setSkuName(sku.getSkuName());
			detail.setPlanQty(soDetail.getPlanQty());
			detail.setPickPlanQty(BigDecimal.ZERO);
			detail.setErrorQty(soDetail.getPlanQty());
			detail.setStockQty(BigDecimal.ZERO);
			detail.setWsuName(soDetail.getBaseUmName());
			if (ObjectUtil.isNotEmpty(repSku)) {
				outstockExecute.setException("替代物品：" + repSku.getSkuName() + " 库存不足！");
			} else {
				outstockExecute.setException("物品：" + sku.getSkuName() + " 库存不足！");
			}
			detail.setException(outstockExecute.getException());
			result.getPickPlanDetailErrorList().add(detail);
			// 查询该物品所有可用库存
			List<Stock> stockList = stockService.list(Condition.getQueryWrapper(new Stock())
				.lambda()
				.eq(Stock::getWhId, soHeader.getWhId())
				.eq(Stock::getSkuId, soDetail.getSkuId())
				.eq(Stock::getWspId, soDetail.getWspId())
				.apply("stock_qty - pick_qty > 0"));
			result.getOtherStockList().addAll(StockWrapper.build().listVO(stockList));
			return;
		}
		// 保存执行分配策略日志
		outstockLogService.save(outstockExecute);
		/**
		 * 2020.12.17-彭伟：支持多次生成拣货计划
		 * 在这里需要查询该物品已分配的拣货计划
		 */
		// 获取计算数量
		BigDecimal occupyQty = BigDecimal.ZERO;
		// 获取已分配的数量
		List<StockOccupy> stockOccupyList = stockOccupyService.list(
			Condition.getQueryWrapper(new StockOccupy())
				.lambda()
				.eq(StockOccupy::getOccupyType, StockOccupyTypeEnum.PickPlan.getIndex())
				.eq(StockOccupy::getWhId, soHeader.getWhId())
				.eq(StockOccupy::getSkuId, sku.getSkuId())
				.eq(StockOccupy::getSoBillId, soHeader.getSoBillId())
				.eq(StockOccupy::getSoDetailId, soDetail.getSoDetailId()));
		if (Func.isNotEmpty(stockOccupyList)) {
			occupyQty = stockOccupyList.stream().map(StockOccupy::getOccupyQty)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		}
		// 已分配数量
		List<BigDecimal> pickPlanQtyList = new ArrayList<>();
		// 待分配数量
		BigDecimal planQty = soDetail.getPlanQty().subtract(soDetail.getScanQty()).subtract(occupyQty);
		if (BigDecimalUtil.le(planQty, BigDecimal.ZERO)) {
			throw new ServiceException("已经全部分配无需再分配！");
		}
		List<Stock> stockList = outstockExecute.getStockList();
		// 循环库存列表
		for (Stock stock : stockList) {
			// 获取已分配总数量
			BigDecimal totalPickPlanQty = pickPlanQtyList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
			if (BigDecimalUtil.ge(totalPickPlanQty, planQty)) {
				// 分配足够了退出
				break;
			}
			// 创建拣货计划明细
			CreatePickPlanDetailVO pickPlan = new CreatePickPlanDetailVO();
			BeanUtil.copyProperties(outstockExecute, pickPlan);
			pickPlan.setUserId(AuthUtil.getUserId());
			pickPlan.setSoBillId(soHeader.getSoBillId());
			pickPlan.setSoBillNo(soHeader.getSoBillNo());
			pickPlan.setWhId(soHeader.getWhId());
			pickPlan.setSoDetailId(soDetail.getSoDetailId());
			pickPlan.setLocId(stock.getLocId());
			Location location = LocationCache.getById(stock.getLocId());
			if (ObjectUtil.isEmpty(location)) {
				pickPlan.setException(String.format(
					"基础信息异常：库存指向的库位ID：%s 在库位表中不存在！", stock.getLocId()));
				break;
			}
			pickPlan.setLocCode(location.getLocCode());
			pickPlan.setSkuId(sku.getSkuId());
			pickPlan.setSkuCode(sku.getSkuCode());
			pickPlan.setSkuName(sku.getSkuName());
			if (ObjectUtil.isNotEmpty(repSku)) {
				pickPlan.setRepSkuId(repSku.getSkuId());
				pickPlan.setRepSkuCode(repSku.getSkuCode());
				pickPlan.setRepSkuName(repSku.getSkuName());
			}
			pickPlan.setLotNumber(stock.getLotNumber());
			pickPlan.setWspId(stock.getWspId());
			pickPlan.setSkuLevel(stock.getSkuLevel());
			pickPlan.setPlanQty(soDetail.getPlanQty().subtract(occupyQty));
			pickPlan.setPickPlanQty(BigDecimal.ZERO);
			pickPlan.setPickRealQty(BigDecimal.ZERO);
			pickPlan.setWsuName(soDetail.getBaseUmName());
			//批属性
			boolean isOutStockSkuLot = false;//创建出库单未指定批属性
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				if (Func.isNotEmpty(soDetail.skuLotGet(i))) {
					isOutStockSkuLot = true;
					pickPlan.skuLotSet(i, soDetail.skuLotGet(i));
				}
			}
			if (!isOutStockSkuLot) {
				for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
					if (Func.isNotEmpty(stock.skuLotGet(i))) {
						pickPlan.skuLotSet(i, stock.skuLotGet(i));
					}
				}
			}

			// 计算当前库存可分配数量
			BigDecimal qty = stock.getStockQty().subtract(stock.getOccupyQty())
				.subtract(stock.getPickQty());
			// 扣除该库存已生成的拣货计划量
			BigDecimal acc_qty = TempPickPlanList.stream().filter(u -> {
				return u.getStockId().equals(stock.getStockId());
			}).map(u -> u.getPickPlanQty()).reduce(BigDecimal.ZERO, BigDecimal::add);
			qty = qty.subtract(acc_qty);
			if (BigDecimalUtil.le(qty, BigDecimal.ZERO)) {
				continue;
			}
			pickPlan.setStockId(stock.getStockId());
			pickPlan.setStockQty(qty);
			// 分配拣货数量
			if (qty.compareTo(planQty.subtract(totalPickPlanQty)) >= 0) {
				pickPlan.setPickPlanQty(planQty.subtract(totalPickPlanQty));
			} else {
				pickPlan.setPickPlanQty(qty);
			}
			if (pickPlan.getPickPlanQty().equals(BigDecimal.ZERO)) {
				// 分配数量 = 0 的情况下跳过
				continue;
			}
			pickPlanQtyList.add(pickPlan.getPickPlanQty());
			totalPickPlanQty = totalPickPlanQty.add(pickPlan.getPickPlanQty());

			result.getPickPlanDetailList().add(pickPlan);
			TempPickPlanList.add(pickPlan);
		}

		// 分配数量小于计划量时分配替代物品
		BigDecimal totalPickPlanQty = pickPlanQtyList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		if (BigDecimalUtil.lt(totalPickPlanQty, planQty)) {
			// 当库存不足时，查询当前物品是否有替代物品
			List<SkuReplaceVO> skuReplaceList = skuReplaceService.listBySkuId(sku.getSkuId());
			if (ObjectUtil.isEmpty(skuReplaceList)) {
				String exception = StringPool.EMPTY;
				if (ObjectUtil.isNotEmpty(repSku)) {
					exception = "物品：" + repSku.getSkuName() + " 库存不足！";
				} else {
					exception = "物品：" + sku.getSkuName() + " 库存不足！";
				}
				// 生成缺货明细
				CreatePickPlanDetailVO detail = new CreatePickPlanDetailVO();
				BeanUtil.copyProperties(outstockExecute, detail);
				detail.setSoBillNo(soHeader.getSoBillNo());
				detail.setWhId(soHeader.getWhId());
				detail.setSkuCode(sku.getSkuCode());
				detail.setSkuName(sku.getSkuName());
				detail.setPickPlanQty(totalPickPlanQty);
				detail.setErrorQty(planQty.subtract(totalPickPlanQty));
				detail.setPlanQty(soDetail.getPlanQty());
				detail.setStockQty(BigDecimal.ZERO);
				detail.setWsuName(soDetail.getBaseUmName());
				detail.setException(exception);
				result.getPickPlanDetailErrorList().add(detail);
				// 查询该物品所有可用库存
				List<Stock> otherStockList = stockService.list(Condition.getQueryWrapper(new Stock())
					.lambda()
					.eq(Stock::getWhId, soHeader.getWhId())
					.eq(Stock::getSkuId, soDetail.getSkuId())
					.eq(Stock::getWspId, soDetail.getWspId())
					.func(sql -> {
						if (Func.isNotEmpty(stockList)) {
							List<Long> stockIdList = NodesUtil.toList(stockList, Stock::getStockId);
							sql.notIn(Stock::getStockId, stockIdList);
						}
					})
					.apply("stock_qty - pick_qty > 0"));
				result.getOtherStockList().addAll(StockWrapper.build().listVO(otherStockList));
			} else {
				for (SkuReplaceVO skuReplaceVO : skuReplaceList) {
					Sku replaceSku = SkuCache.getById(skuReplaceVO.getWsrepSkuId());
					if (ObjectUtil.isEmpty(replaceSku) || !skuReplaceVO.getWspId().equals(sku.getWspId())) {
						continue;
					}
					// 计算剩余分配数量
					BigDecimal diffQty = planQty.subtract(totalPickPlanQty);
//					// 计算替代物品需要替代的数量
//					if (diffQty % skuReplaceVO.getQty() != 0) {
//						// 剩余数量不能被整除的情况下跳过该替代品
//						continue;
//					}
//					// 替代数量 = 剩余未分配数量 / 物品数量 * 物品替代数量
//					diffQty = diffQty / skuReplaceVO.getQty() * skuReplaceVO.getWsrepQty();

					soDetail.setPlanQty(diffQty);
					// 替代物品的情况下，修改下包装为替代包装；方便后面查询
					replaceSku.setWspId(skuReplaceVO.getWsrepWspId());
					// 生成替代品的拣货计划
					this.createPickPlanDetail(result, soHeader, soDetail, replaceSku, sku, null);
				}
			}
		}
	}

	@Override
	public CreatePickPlanVO createPickPlan(CreatePickPlanDTO createPickPlanDTO) {
		CreatePickPlanVO result = new CreatePickPlanVO();
		try {
			// 获取所有订单表头
			List<SoHeader> soHeaderListAll = soHeaderService.listByIds(createPickPlanDTO.getSoBillIdList());
			// 获取所有订单明细
			List<SoDetail> soDetailListAll = soDetailService.list(Condition.getQueryWrapper(new SoDetail())
				.lambda()
				.in(SoDetail::getSoBillId, createPickPlanDTO.getSoBillIdList())
				.ne(SoDetail::getSurplusQty, BigDecimal.ZERO)
				.func(sql -> {
					if (Func.isNotEmpty(createPickPlanDTO.getSoDetailIdList())) {
						sql.in(SoDetail::getSoDetailId, createPickPlanDTO.getSoDetailIdList());
					}
				}));
			for (Long billId : createPickPlanDTO.getSoBillIdList()) {

				SoHeader soHeader = soHeaderListAll.stream().filter(u -> {
					return u.getSoBillId().equals(billId);
				}).findFirst().orElse(null);
				if (ObjectUtil.isEmpty(soHeader)) {
					throw new ServiceException("订单ID：" + billId + " 不存在！");
				}

				// 判断当前订单状态
				if (!SoBillStateEnum.CREATE.getIndex().equals(soHeader.getSoBillState()) &&
					!SoBillStateEnum.EXECUTING.getIndex().equals(soHeader.getSoBillState())) {
					throw new ServiceException("订单编码: " + soHeader.getSoBillNo() + " 当前状态不允许生成拣货计划！");
				}

				// 记录系统日志
				SystemProcDTO systemProcParam = new SystemProcDTO();
				systemProcParam.setProcType(SystemProcTypeEnum.PICK_PLAN);
				systemProcParam.setDataType(DataTypeEnum.SO_BILL_NO);
				systemProcParam.setAction(ActionEnum.PLAN_BILL);
				systemProcParam.setBillNo(soHeader.getSoBillNo());
				systemProcParam.setWhId(soHeader.getWhId());
				SystemProc systemProc = systemProcService.create(systemProcParam);

				// 获取订单明细
				List<SoDetail> soDetailList = soDetailListAll.stream().filter(u -> {
					return u.getSoBillId().equals(billId);
				}).collect(Collectors.toList());
				for (SoDetail soDetail : soDetailList) {
					// 获取物品详细信息
					Sku sku = SkuCache.getById(soDetail.getSkuId());
					if (ObjectUtil.isEmpty(sku)) {
						throw new ServiceException("物品ID：" + soDetail.getSkuId() + "在基础数据(物品表)中不存在！");
					}

					this.createPickPlanDetail(result, soHeader, soDetail, sku, null,
						createPickPlanDTO);
				}
			}
			String promptStr = ParamCache.getValue(ParamEnum.OUTSTOCK_PICKPLAN_PROMPT.getKey());
			if (StringPool.ZERO.equals(promptStr) && Func.isNotEmpty(result.getPickPlanDetailErrorList())) {
				result.setPrompt(true);
			} else if (StringPool.ONE.equals(promptStr)) {
				result.setPrompt(true);
			} else {
				SavePickPlanDTO savePickPlanDTO = new SavePickPlanDTO();
				savePickPlanDTO.setIsWellen(createPickPlanDTO.getIsWellen());
				List<SavePickPlanDetailDTO> pickPlanDetailList = new ArrayList<>();
				List<CreatePickPlanDetailVO> createPickPlanDetailList = result.getPickPlanDetailList().stream()
					.filter(u -> {
						return createPickPlanDTO.getSoBillIdList().contains(u.getSoBillId());
					}).collect(Collectors.toList());
				for (CreatePickPlanDetailVO item : createPickPlanDetailList) {
					pickPlanDetailList.add(BeanUtil.copy(item, SavePickPlanDetailDTO.class));
				}
				savePickPlanDTO.setDetailList(pickPlanDetailList);
				this.savePickPlan(savePickPlanDTO);
			}
		} catch (Exception ex) {
			throw new ServiceException(ex.getMessage());
		} finally {
			TempPickPlanList.clear();
		}
		return result;
	}

	@Override
	public synchronized boolean savePickPlan(SavePickPlanDTO savePickPlanDTO) {
		if (Func.isEmpty(savePickPlanDTO.getDetailList())) {
			throw new ServiceException("保存的分配明细不能为空！");
		}
		// 忽略已经保存的明细行
		List<Long> soDetailIdList = NodesUtil.toList(savePickPlanDTO.getDetailList(), SavePickPlanDetailDTO::getSoDetailId);
		List<StockOccupy> stockOccupyList = stockOccupyService.list(Condition.getQueryWrapper(new StockOccupy())
			.lambda()
			.in(StockOccupy::getSoDetailId, soDetailIdList));
		if (Func.isNotEmpty(stockOccupyList)) {
			List<SoDetail> soDetailList = soDetailService.listByIds(soDetailIdList);
			// 删除已经保存的拣货计划明细（避免同时点保存）
			savePickPlanDTO.getDetailList().removeIf(new Predicate<SavePickPlanDetailDTO>() {
				@Override
				public boolean test(SavePickPlanDetailDTO savePickPlanDetailDTO) {
					SoDetail soDetail = soDetailList.stream().filter(u -> {
						return u.getSoDetailId().equals(savePickPlanDetailDTO.getSoDetailId());
					}).findFirst().orElse(null);
					if (Func.isEmpty(soDetail)) {
						return false;
					}
					BigDecimal pickPlanQty = savePickPlanDetailDTO.getPickPlanQty();
					BigDecimal occupyQty = stockOccupyList.stream().filter(u -> {
						return u.getSoDetailId().equals(savePickPlanDetailDTO.getSoDetailId());
					}).map(StockOccupy::getOccupyQty).reduce(BigDecimal.ZERO, BigDecimal::add);
					return BigDecimalUtil.gt(
						pickPlanQty.add(occupyQty), soDetail.getPlanQty().subtract(soDetail.getScanQty()));
				}
			});
		}
		// 客户端传过来的分配明细可能顺序是乱的，先按订单ID排序
		savePickPlanDTO.getDetailList()
			.sort(Comparator.comparing(SavePickPlanDetailDTO::getSoBillId));
		// 记录包含的订单
		List<SoHeader> soHeaderList = new ArrayList<>();
		for (SavePickPlanDetailDTO savePickPlanDetail : savePickPlanDTO.getDetailList()) {
			Long existSoHeader = soHeaderList.stream().filter(u -> {
				return u.getSoBillId().equals(savePickPlanDetail.getSoBillId());
			}).count();
			if (existSoHeader > 0) {
				continue;
			}
			// 更新出库订单状态
			SoHeader soHeader = soHeaderService.updateBillState(
				savePickPlanDetail.getSoBillId(), SoBillStateEnum.EXECUTING, true);
			soHeaderList.add(soHeader);
		}
		// 创建波次
		List<WellenVO> wellenList = null;
		ActionEnum actionEnum = null;
		if (savePickPlanDTO.getIsWellen()) {
			// 波次分配
			actionEnum = ActionEnum.PLAN_BILL;
			wellenList = wellenService.create(soHeaderList, soDetailIdList);
		} else {
			// 按单分配
			actionEnum = ActionEnum.PLAN_WELLEN;
			wellenList = new ArrayList<>();
			for (SoHeader soHeader : soHeaderList) {
				wellenList.add(wellenService.create(soHeader, soDetailIdList));
			}
		}
		// 先获取库存，避免在生成到保存中间库存有变动
		List<Long> stockIdList = NodesUtil.toList(savePickPlanDTO.getDetailList(), SavePickPlanDetailDTO::getStockId);
		List<Stock> stockList = null;
		if (Func.isEmpty(stockIdList)) {
			stockList = new ArrayList<>();
		} else {
			stockList = stockService.list(Condition.getQueryWrapper(new Stock())
				.lambda()
				.in(Stock::getStockId, stockIdList));
		}

		// 根据生成的波次创建系统日志和分配
		for (WellenVO wellen : wellenList) {
			// 记录系统日志
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.PICK_PLAN);
			systemProcParam.setDataType(DataTypeEnum.SO_BILL_NO);
			systemProcParam.setAction(actionEnum);
			systemProcParam.setBillNo(String.valueOf(wellen.getWellenNo()));
			systemProcParam.setWhId(wellen.getWhId());
			SystemProc systemProc = systemProcService.create(systemProcParam);
			// 创建任务
			TaskDTO taskDTO = new TaskDTO();
			taskDTO.setTaskType(TaskTypeEnum.Pick.getIndex());
			taskDTO.setWhId(wellen.getWhId());
			taskDTO.setBillTypeCd("901");
			taskDTO.setBillId(wellen.getWellenId());
			taskDTO.setBillNo(Func.toStr(wellen.getWellenNo()));
			taskDTO.setTaskQty(wellen.getDetailList().size());
			Task task = taskService.create(taskDTO);
			AtomicReference<Integer> taskQty = new AtomicReference<>(0);

			List<Stock> finalStockList = stockList;
			wellen.getDetailList().stream().collect(Collectors.groupingBy(WellenDetail::getSoBillId))
				.forEach((soBillId, wellenDetailList) -> {
					// 创建出库清单
					SoHeader soHeader = soHeaderList.stream().filter(u -> {
						return u.getSoBillId().equals(soBillId);
					}).findFirst().orElse(null);
					SoInventory soInventory = BeanUtil.copy(soHeader, SoInventory.class);
					soInventory.setSoBillNo(SoCache.getInventoryNo());
					soInventory.setOrderNo(soHeader.getSoBillNo());
					soInventory.setBillKey(soHeader.getSoBillNo());
					soInventoryService.save(soInventory);

					for (WellenDetailVO wellenDetailVO : wellenDetailList) {
						wellenDetailVO.setInventoryId(soInventory.getId());
						wellenDetailService.updateById(wellenDetailVO);
						// 更新订单明细状态
						soDetailService.updateState(
							wellenDetailVO.getSoDetail().getSoDetailId(),
							SoDetailStateEnum.AllocWellen);
						// 更新分配日志的波次编码
						UpdateWrapper<OutstockLog> outstockLogUpdateWrapper = new UpdateWrapper<>();
						outstockLogUpdateWrapper.lambda()
							.set(OutstockLog::getWellenNo, wellenDetailVO.getWellenNo())
							.eq(OutstockLog::getSoBillNo, wellenDetailVO.getSoBillNo())
							.isNull(OutstockLog::getWellenNo);
						outstockLogService.update(outstockLogUpdateWrapper);
						// 开始-保存拣货计划
						// 通过订单ID查找
						List<SavePickPlanDetailDTO> detailList = savePickPlanDTO.getDetailList().stream().filter(u -> {
							return u.getSoBillId().equals(wellenDetailVO.getSoBillId())
								&& u.getSoDetailId().equals(wellenDetailVO.getSoDetailId());
						}).collect(Collectors.toList());
						if (Func.isEmpty(detailList)) {
							// 移除未分配拣货计划的波次明细
							wellenDetailService.removeById(wellenDetailVO);
						} else {
							for (SavePickPlanDetailDTO detail : detailList) {
								// 保存拣货计划前，验证库存是否变动导致库存不足
								Stock stock = finalStockList.stream().filter(u -> {
									return u.getStockId().equals(detail.getStockId());
								}).findFirst().orElse(null);
								if (Func.isEmpty(stock)) {
									throw new ServiceException("指定库存不存在(ID:" + detail.getStockId() + ")");
								}
								// 计算可用库存
								BigDecimal qty = stock.getStockQty().subtract(stock.getPickQty())
									.subtract(stock.getOccupyQty());
								if (BigDecimalUtil.lt(qty, detail.getPickPlanQty())) {
									throw new ServiceException("库存不足，请重新生成！");
								}
								if (ObjectUtil.isNotEmpty(task)) {
									detail.setTaskId(task.getTaskId());
								}
								detail.setWellenId(wellen.getWellenId());
								// 如果是波次分配的情况下，需要合并拣货计划
								if (savePickPlanDTO.getIsWellen()) {
									PickPlan pickPlan = super.list(Condition.getQueryWrapper(new PickPlan())
										.lambda()
										.eq(PickPlan::getWellenId, wellen.getWellenId())
										.eq(PickPlan::getLocId, stock.getLocId())
										.eq(PickPlan::getSkuId, stock.getSkuId())
										.eq(PickPlan::getLotNumber, stock.getLotNumber())
										.eq(PickPlan::getWspId, stock.getWspId())
										.eq(PickPlan::getSkuLevel, stock.getSkuLevel())
										.eq(PickPlan::getWhId, stock.getWhId())).stream().findFirst().orElse(null);
									if (Func.isNotEmpty(pickPlan)) {
										pickPlan.setPickPlanQty(pickPlan.getPickPlanQty().add(detail.getPickPlanQty()));
									} else {
										pickPlan = detail;
										taskQty.getAndSet(taskQty.get() + 1);
									}
//									pickPlan.setCreateDept(soHeader.getCreateDept());
									super.saveOrUpdate(pickPlan);
									detail.setPickPlanId(pickPlan.getPickPlanId());
								} else {
									detail.setSoBillNo(soHeader.getSoBillNo());
//									detail.setCreateDept(soHeader.getCreateDept());
									super.save(detail);
									taskQty.getAndSet(taskQty.get() + 1);
								}
								// 保存成功后将已保存的从列表中移除
								savePickPlanDTO.getDetailList().remove(detail);
								// 创建库存占用
								StockOccupyDTO stockOccupyDTO = new StockOccupyDTO();
								stockOccupyDTO.setTransId(wellenDetailVO.getWellenId());
								stockOccupyDTO.setOccupyType(StockOccupyTypeEnum.PickPlan.getIndex());
								stockOccupyDTO.setWhId(wellen.getWhId());
								stockOccupyDTO.setStockId(stock.getStockId());
								stockOccupyDTO.setSkuId(detail.getSkuId());
								stockOccupyDTO.setSkuCode(detail.getSkuCode());
								stockOccupyDTO.setSkuName(detail.getSkuName());
								stockOccupyDTO.setSystemProcId(systemProc.getSystemProcId());
								stockOccupyDTO.setOccupyTime(LocalDateTime.now());
								stockOccupyDTO.setOccupyQty(detail.getPickPlanQty());
								stockOccupyDTO.setSoBillId(wellenDetailVO.getSoBillId());
								stockOccupyDTO.setSoBillNo(wellenDetailVO.getSoBillNo());
								stockOccupyDTO.setSoDetailId(wellenDetailVO.getSoDetail().getSoDetailId());
								stockOccupyDTO.setPickPlanId(detail.getPickPlanId());
								stockOccupyService.add(stockOccupyDTO);
							}
						}
					}
				});
			// 更新任务数量
			task.setTaskQty(taskQty.get());
			taskService.updateById(task);
			// 修怍波次主表状态
			wellenService.updateState(wellen.getWellenId(), WellenStateEnum.AllocComplated);
		}

		return true;
	}

	/**
	 * 按单拣货
	 *
	 * @param pickTaskSubmitVO
	 * @return
	 */
	@Override
	public PickTaskVO submitPickInfoWithOrder(PickTaskSubmitVO pickTaskSubmitVO) {
		Wellen wellen = wellenService.getOne(Wrappers.lambdaQuery(Wellen.class)
			.eq(Wellen::getWellenNo, pickTaskSubmitVO.getWellenNo()));
		SoHeader soHeader = soHeaderService.getOne(Wrappers.lambdaQuery(SoHeader.class)
			.eq(SoHeader::getSoBillNo, pickTaskSubmitVO.getSoBillNo()));
		if (Func.isEmpty(soHeader)) {
			throw new ServiceException("订单不存在！");
		}
		if (Func.isEmpty(wellen)) {
			throw new ServiceException("波次信息不存在！");
		}
		if (Func.isEmpty(pickTaskSubmitVO.getPickPlans())) {
			throw new ServiceException("拣货计划不存在！");
		}
		List<SoDetail> soDetails = soDetailService.list(Wrappers.lambdaQuery(SoDetail.class).eq(SoDetail::getSoBillNo, pickTaskSubmitVO.getSoBillNo()));
		PickPlanMapper planMapper = SpringUtil.getBean(PickPlanMapper.class);
		List<PickPlan> pickPlanList = planMapper.selectPickPlanListForPickByWellenId(wellen.getWellenId(), null, pickTaskSubmitVO.getSoBillNo());
		for (PickPlan pickPlan : pickTaskSubmitVO.getPickPlans()) {
			PickPlan pickPlan2 = pickPlanList.stream().filter(pickPlan1 -> pickPlan1.getPickPlanId().equals(pickPlan.getPickPlanId())).findFirst().orElse(null);
			if (Func.isNotEmpty(pickPlan2)) {
				throw new ServiceException("拣货计划不存在！");
			}
			if (BigDecimalUtil.gt(pickPlan.getUnPlanQty(), pickPlan.getPickPlanQty())) {
				throw new ServiceException("拣货量超过计划量！");
			}

			PickPlanDTO pickPlanDTO = PickPlanWrapper.pickSubmitVO2PickPlanDTO(pickPlan, pickTaskSubmitVO);
			pickPlanDTO.setPickQty(pickPlan.getUnPlanQty());
			pickPlanDTO.setTargetLpnCode("");
			pickPlanDTO.setSoBillId(soHeader.getSoBillId());
			pickPlanDTO.setSourceLpnCode("");
			pickPlanDTO.setSourceLocCode(pickPlanDTO.getLocCode());
			pickPlanDTO.setTargetLocCode(pickTaskSubmitVO.getTargetLocCode());
			//保存系统日志
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.PICK);
			systemProcParam.setDataType(DataTypeEnum.WellenNo);

			if (Func.isNotEmpty(pickPlanDTO.getWellenNo())) { // 波次编码不为空
				systemProcParam.setBillNo(pickPlanDTO.getWellenNo());
			} else if (Func.isNotEmpty(pickPlanDTO.getTaskId())) { // 任务id不为空
				systemProcParam.setBillNo(pickPlanDTO.getTaskId().toString());
			}
			systemProcParam.setAction(ActionEnum.PICK_BASE);
			systemProcParam.setLpnCode("");
			systemProcParam.setOperationQty(pickPlan.getUnPlanQty());
//			systemProcParam.setOperationUnit(SkuPackageDetailCache.getById(pickPlanDTO.getWspdId()).getWsuName());
			systemProcParam.setWhId(pickPlanDTO.getWhId());
			SystemProc systemProc = systemProcService.create(systemProcParam);

			pickPlanDTO.setSystemProcId(systemProc.getSystemProcId());
			//库存移动
			this.moveStockSku(pickPlanDTO);
			//更新拣货计划数量
			pickPlan.setPickRealQty((pickPlan.getPickRealQty().add(pickPlanDTO.getPickQty())));
			baseMapper.updateById(pickPlan);
			this.saveLogSoPick(pickPlanDTO);

			List<SoDetail> soDetailList = soDetails.stream().filter(soDetail -> soDetail.getSkuId().equals(pickPlanDTO.getSkuId())).collect(Collectors.toList());
			List<SoDetail> unUpdateSoDetails = new ArrayList<>();
			for (SoDetail soDetail : soDetailList) {
				if (BigDecimalUtil.eq(soDetail.getSurplusQty(), pickPlan.getUnPlanQty())) {
					soDetail.setSurplusQty(BigDecimal.ZERO);
					soDetail.setScanQty(soDetail.getScanQty().add(pickPlan.getUnPlanQty()));
					unUpdateSoDetails.add(soDetail);
					break;
				}
				if (BigDecimalUtil.lt(soDetail.getSurplusQty(), pickPlan.getUnPlanQty())) {
					soDetail.setSurplusQty(BigDecimal.ZERO);
					soDetail.setScanQty(soDetail.getScanQty().add(soDetail.getSurplusQty()));
					unUpdateSoDetails.add(soDetail);
					continue;
				}
				if (BigDecimalUtil.gt(soDetail.getSurplusQty(), pickPlan.getUnPlanQty())) {
					soDetail.setSurplusQty(soDetail.getSurplusQty().subtract(pickPlan.getUnPlanQty()));
					soDetail.setScanQty(soDetail.getScanQty().add(pickPlan.getUnPlanQty()));
					unUpdateSoDetails.add(soDetail);
					continue;
				}
			}
			soDetailService.updateBatchById(unUpdateSoDetails);

			this.closeTack(pickPlanDTO);
			// 保存物品操作记录
			SkuLogDTO skuLogDTO = new SkuLogDTO();
			skuLogDTO.setSkuId(pickPlan.getSkuId());
			skuLogDTO.setSkuLogTypeEnum(SkuLogTypeEnum.OUTSTOCK);
			skuLogService.saveOrUpdate(skuLogDTO);
		}

		return null;
	}

	@Override
	public void cancelPick(String ids) {
		ISoPickService soPickService = SpringUtil.getBean(ISoPickService.class);
		IPickPlanService pickPlanService = SpringUtil.getBean(IPickPlanService.class);
		List<SoPick> soPickList = soPickService.list(Wrappers.lambdaQuery(SoPick.class)
			.in(SoPick::getLsopId, Func.toLongList(ids)));
		if (Func.isEmpty(soPickList)) {
			throw new ServiceException("拣货记录不存在！");
		}
		//更新拣货记录状态
		soPickList = soPickList.stream().map(soPick -> {
			soPick.setStatus(1);
			return soPick;
		}).collect(Collectors.toList());
		soPickService.updateBatchById(soPickList);
		//更新或者删除拣货计划
		Map<Long, List<SoPick>> listMap = soPickList.stream().collect(Collectors.groupingBy(SoPick::getPickPlanId));
		List<PickPlan> pickPlanList = pickPlanService.list(Wrappers.lambdaQuery(PickPlan.class)
			.in(PickPlan::getPickPlanId, NodesUtil.toList(soPickList, SoPick::getPickPlanId)));
		//拣货计划对应的出库单明细
		List<SoDetail> soDetailList = soDetailService.list(Wrappers.lambdaQuery(SoDetail.class)
			.in(SoDetail::getSoBillNo, NodesUtil.toList(pickPlanList, PickPlan::getSoBillNo)));
		//出库暂存区的库存明细
		List<StockDetail> stockDetails = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.in(StockDetail::getSoBillId, NodesUtil.toList(soDetailList, SoDetail::getSoBillId)));
		//出库暂存区的库存
		List<Stock> stockList11 = stockService.list(Wrappers.lambdaQuery(Stock.class)
			.in(Stock::getStockId, NodesUtil.toList(stockDetails, StockDetail::getStockId)));
		//拣货计划对应的原库存
		List<Stock> stockList = stockService.list(Wrappers.lambdaQuery(Stock.class)
			.in(Stock::getStockId, NodesUtil.toList(pickPlanList, PickPlan::getStockId)));
		//原库存明细
		List<StockDetail> stockDetails11 = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.in(StockDetail::getStockId, NodesUtil.toList(stockList, Stock::getStockId)));
//		stockDetails.addAll(stockDetails11);

		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
		List<Location> locationList = locationService.list(Wrappers.lambdaQuery(Location.class)
			.in(Location::getLocCode, NodesUtil.toList(soPickList, SoPick::getLocCode)));
		List<PickPlan> deleteList = new ArrayList<>();
		List<PickPlan> updateList = new ArrayList<>();
		List<SoDetail> updateSoDetailList = new ArrayList<>();
		List<StockDetail> deleteStockDetailList = new ArrayList<>();
		List<Stock> deleteStockList = new ArrayList<>();
		List<Stock> updateStockList = new ArrayList<>();
		List<StockDetail> updateStockDetailList = new ArrayList<>();
		listMap.forEach((pickPlanId, soPicks) -> {
			PickPlan pickPlan1 = pickPlanList.stream()
				.filter(pickPlan -> pickPlan.getPickPlanId().equals(pickPlanId))
				.findFirst().orElse(null);
			if (Func.isEmpty(pickPlan1)) {
				throw new ServiceException("拣货计划不存在!");
			}
			//处理拣货计划
			BigDecimal pickRealQty = soPicks.stream()
				.map(soPick -> soPick.getPickRealQty())
				.reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal subtract = pickPlan1.getPickRealQty().subtract(pickRealQty);
			pickPlan1.setPickRealQty(subtract);
			if (BigDecimalUtil.le(subtract, BigDecimal.ZERO)) {
				deleteList.add(pickPlan1);
			} else {
				updateList.add(pickPlan1);
			}
			//该拣货计划对应的出库单明细
			List<SoDetail> soDetails = soDetailList.stream()
				.filter(soDetail -> soDetail.getSoBillNo().equals(pickPlan1.getSoBillNo()))
				.collect(Collectors.toList());
			//该拣货计划对应的出库单出库暂存区的库存明细
			List<StockDetail> stockDetails1 = stockDetails.stream()
				.filter(stockDetail -> stockDetail.getSoBillId().equals(soDetails.get(0).getSoBillId()))
				.collect(Collectors.toList());

			//对应的出库暂存库存
			List<Stock> stocks1 = stockList11.stream()
				.filter(stock -> NodesUtil.toList(stockDetails1, StockDetail::getStockId).contains(stock.getStockId()))
				.collect(Collectors.toList());

			stockDetails1.forEach(stockDetail -> {
				SoDetail soDetail1 = soDetails.stream()
					.filter(soDetail -> soDetail.getSoDetailId().equals(stockDetail.getBillDetailId()))
					.findFirst().orElse(null);
				soDetail1.setScanQty(soDetail1.getScanQty().subtract(stockDetail.getStockQty()));
				soDetail1.setSurplusQty(soDetail1.getSurplusQty().add(stockDetail.getStockQty()));
				if (BigDecimalUtil.eq(soDetail1.getScanQty(), BigDecimal.ZERO)
					&& Func.isNotEmpty(deleteList)) {
					soDetail1.setBillDetailState(SoDetailStateEnum.UnAlloc.getIndex());
				}
				updateSoDetailList.add(soDetail1);
			});

			BigDecimal reduce = stockDetails1.stream()
				.map(stockDetail -> stockDetail.getStockQty())
				.reduce(BigDecimal.ZERO, BigDecimal::add);

			//原库存
			Stock stock1 = stockList.stream().
				filter(stock -> stock.getStockId().equals(pickPlan1.getStockId()))
				.findFirst().orElse(null);

			//为了获取原库存库区信息
			Location location1 = locationList.stream()
				.filter(location -> location.getLocCode().equals(soPicks.get(0).getLocCode()))
				.findFirst().orElse(null);
			if (Func.isEmpty(stock1)) {
				//创建原库存 和 库存明细
				stockDetails1.forEach(stockDetail -> {
					StockDetail stockDetail1 = BeanUtil.copy(stockDetail, StockDetail.class);
					stockDetail1.setZoneId(location1.getZoneId());
					stockDetail1.setLocId(soPicks.get(0).getLocId());
					stockDetail1.setPickQty(BigDecimal.ZERO);
					stockDetail1.setId(null);
					stockDetail1.setBillDetailId(null);
					stockDetail1.setSoBillId(null);
					stockDetail1.setStockId(pickPlan1.getStockId());
					updateStockDetailList.add(stockDetail1);
				});
				stocks1.forEach(stock -> {
					Stock stock2 = BeanUtil.copy(stock, Stock.class);
					stock2.setZoneId(location1.getZoneId());
					stock2.setLocId(soPicks.get(0).getLocId());
					stock2.setLocCode(soPicks.get(0).getLocCode());
					stock2.setPickQty(BigDecimal.ZERO);
					stock2.setStockId(pickPlan1.getStockId());
					updateStockList.add(stock2);
				});
			} else {
				//全存在 就更新  部分存在就新增和更新
				stock1.setPickQty(stock1.getPickQty().subtract(reduce));
				stock1.setOccupyQty(stock1.getOccupyQty().add(reduce));
				if (Func.isNotEmpty(deleteList)) {
					stock1.setOccupyQty(BigDecimal.ZERO);
				}
				updateStockList.add(stock1);
				List<StockDetail> stockDetails2 = stockDetails11.stream()
					.filter(stockDetail -> stockDetail.getStockId().equals(stock1.getStockId()))
					.collect(Collectors.toList());
				stockDetails1.forEach(stockDetail -> {

					StockDetail stockDetail2 = stockDetails2.stream()
						.filter(stockDetail1 -> stockDetail1.getBoxCode().equals(stockDetail.getBoxCode())
							&& stockDetail1.getLpnCode().equals(stockDetail.getLpnCode()))
						.findFirst().orElse(null);
					if (Func.isEmpty(stockDetail2)) {
						StockDetail stockDetail1 = BeanUtil.copy(stockDetail, StockDetail.class);
						stockDetail1.setZoneId(location1.getZoneId());
						stockDetail1.setLocId(soPicks.get(0).getLocId());
						stockDetail1.setPickQty(BigDecimal.ZERO);
						stockDetail1.setStockId(stock1.getStockId());
						stockDetail1.setBillDetailId(null);
						stockDetail1.setSoBillId(null);
						stockDetail1.setId(null);
						updateStockDetailList.add(stockDetail1);
					} else {
						stockDetail2.setPickQty(stockDetail2.getPickQty().subtract(stockDetail.getStockQty()));
						updateStockDetailList.add(stockDetail2);
					}
				});

			}
			deleteStockDetailList.addAll(stockDetails1);
			deleteStockList.addAll(stocks1);
		});
		if (Func.isNotEmpty(updateList)) {
			pickPlanService.updateBatchById(updateList);
		}
		if (Func.isNotEmpty(deleteList)) {
			pickPlanService.removeByIds(NodesUtil.toList(deleteList, PickPlan::getPickPlanId));
		}
		if (Func.isNotEmpty(updateSoDetailList)) {
			soDetailService.updateBatchById(updateSoDetailList);
		}
		if (Func.isNotEmpty(updateStockDetailList)) {
			stockDetailService.saveOrUpdateBatch(updateStockDetailList);
		}
		if (Func.isNotEmpty(deleteStockDetailList)) {
			stockDetailService.removeByIds(NodesUtil.toList(deleteStockDetailList, StockDetail::getId));
		}
		if (Func.isNotEmpty(updateStockList)) {
			stockService.saveOrUpdateBatch(updateStockList);
		}
		if (Func.isNotEmpty(deleteStockList)) {
			stockService.removeByIds(NodesUtil.toList(deleteStockList, Stock::getStockId));
		}
	}

	@Override
	public LpnItemVo getInfoByLpnCode(String lpnCode, String pickPlanId, BigDecimal count) {
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		List<Zone> zoneList = zoneService.list(Wrappers.lambdaQuery(Zone.class)
			.eq(Zone::getZoneType, ZoneVirtualTypeEnum.Pick.getIndex()));

		IPickPlanService pickPlanService = SpringUtil.getBean(IPickPlanService.class);
		PickPlan pickPlan = pickPlanService.getById(pickPlanId);
		if (Func.isEmpty(pickPlan)) {
			throw new ServiceException("没有查询到拣货计划！");
		}
		List<PickPlan> pickPlans = pickPlanService.list(Wrappers.lambdaQuery(PickPlan.class).eq(PickPlan::getSoBillNo, pickPlan.getSoBillNo())
			.apply("pick_plan_qty>pick_real_qty"));
		if (Func.isEmpty(pickPlans)) {
			throw new ServiceException("没有查询到拣货计划！");
		}
		List<StockDetail> stockDetails = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.notIn(StockDetail::getZoneId, NodesUtil.toList(zoneList, Zone::getZoneId))
			.in(StockDetail::getStockId, NodesUtil.toList(pickPlans, PickPlan::getStockId))
			.apply("stock_qty<>pick_qty"));
		if (Func.isEmpty(stockDetails)) {
			throw new ServiceException("请扫描拣货计划指定的托盘！");
		}
		pickPlan = pickPlans.stream().filter(pickPlan11 -> {
			List<StockDetail> stockDetails1 = stockDetails.stream().filter(stockDetail -> stockDetail.getLocId().equals(pickPlan11.getLocId())).collect(Collectors.toList());
			return pickPlan11.getStockId().equals(stockDetails1.get(0).getStockId());
		}).findFirst().orElse(null);
		BigDecimal pickPlanQty = pickPlans.stream().map(PickPlan::getPickPlanQty).reduce(BigDecimal::add).get();
		BigDecimal pickRealQty = pickPlans.stream().map(PickPlan::getPickRealQty).reduce(BigDecimal::add).get();
		List<StockDetail> stockDetails1 = stockDetails.stream().filter(stockDetail -> stockDetail.getLpnCode().equals(lpnCode)).collect(Collectors.toList());
		if (Func.isEmpty(stockDetails1)) {
			throw new ServiceException("请扫描拣货计划指定的托盘！");
		}
		BigDecimal qty = stockDetails1.stream().map(StockDetail::getStockQty).reduce(BigDecimal::add).get();
		BigDecimal pickQty = stockDetails1.stream().map(StockDetail::getPickQty).reduce(BigDecimal::add).get();
//		List<Stock> stocks = stockService.list(Wrappers.lambdaQuery(Stock.class)
//			.in(Stock::getStockId, NodesUtil.toList(stockDetails, StockDetail::getStockId)));
//		BigDecimal occupyQty = stocks.stream().map(Stock::getOccupyQty).reduce(BigDecimal::add).get();
		qty = qty.subtract(pickQty);
//			.subtract(occupyQty).add(pickPlan.getPickPlanQty().subtract(pickPlan.getPickRealQty()));

		SkuPackageDetail packageDetail = SkuPackageDetailCache.getOne(pickPlan.getWspId(), SkuLevelEnum.Base.getIndex());
		LpnItemVo lpnItemVo = new LpnItemVo();
		lpnItemVo.setLpnCode(lpnCode);
		BigDecimal subtract = pickPlanQty.subtract(pickRealQty).subtract(count);
		if (BigDecimalUtil.gt(qty, subtract)) {
			lpnItemVo.setQty(subtract);
		} else {
			lpnItemVo.setQty(qty);
		}
		if (Func.isNotEmpty(packageDetail)) {
			lpnItemVo.setUm(packageDetail.getWsuName());
		}
		if (BigDecimalUtil.le(lpnItemVo.getQty(), BigDecimal.ZERO)) {
			throw new ServiceException("超过拣货计划数量！");
		}
		return lpnItemVo;
	}

	/**
	 * 按托拣货
	 * @param detail
	 * @return
	 */
	private String fetchGroupKey(SoDetail detail) {
		return detail.getSkuId().toString()
			+ "_"
			+ detail.getWspId().toString();
	}

	@Override
	public PickTaskVO submitTrayPickInfo(PickTaskSubmitVO pickTaskSubmitVO) {

		String taskId = pickTaskSubmitVO.getTaskId();
		String wellenNo = pickTaskSubmitVO.getWellenNo();
		if (Func.isEmpty(wellenNo) && Func.isEmpty(taskId)) {
			throw new ServiceException("波次号或任务必须存在一个！");
		}
		if (Func.isEmpty(wellenNo) && Func.isNotEmpty(taskId)) {
			Task task = taskService.getById(taskId);
			wellenNo = task.getBillNo();
		}
		Wellen wellen = wellenService.getOne(Wrappers.lambdaQuery(Wellen.class)
			.eq(Wellen::getWellenNo, wellenNo));
		if (Func.isEmpty(wellen)) {
			throw new ServiceException("波次信息不存在！");
		}
		List<WellenDetail> wellenDetails = wellenDetailService.listByWellenId(wellen.getWellenId());
		if (Func.isEmpty(wellenDetails)) {
			throw new ServiceException("波次明细不存在！");
		}
		List<SoDetail> soDetails = soDetailService.list(Wrappers.lambdaQuery(SoDetail.class)
			.in(SoDetail::getSoBillNo, NodesUtil.toList(wellenDetails, WellenDetail::getSoBillNo)));
		if (Func.isEmpty(soDetails)) {
			throw new ServiceException("出库单明细不存在！");
		}
		Map<String, List<SoDetail>> soDetailMap = soDetails.stream()
			.collect(Collectors.groupingBy(d -> fetchGroupKey(d)));


		IPickPlanService pickPlanService = SpringUtil.getBean(IPickPlanService.class);
		List<PickPlan> pickPlanList = pickPlanService.list(Condition.getQueryWrapper(new PickPlan()).lambda()
			.eq(PickPlan::getWellenId, wellen.getWellenId()));
		PickPlan pickPlan = pickPlanList.stream().filter(u -> {
			return u.getPickPlanId().equals(pickTaskSubmitVO.getPickPlanId());
		}).findFirst().orElse(null);
		if (Func.isEmpty(pickPlan)) {
			throw new ServiceException("拣货计划不存在！");
		}
		List<PickPlan> pickPlans1 = pickPlanService.list(Wrappers.lambdaQuery(PickPlan.class).eq(PickPlan::getSoBillNo, pickPlan.getSoBillNo())
			.apply("pick_plan_qty>pick_real_qty"));
		if (Func.isEmpty(pickPlan)) {
			throw new ServiceException("没有查询到拣货计划！");
		}
		IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
		List<Zone> zoneList = zoneService.list(Wrappers.lambdaQuery(Zone.class)
			.eq(Zone::getZoneType, ZoneVirtualTypeEnum.Pick.getIndex()));
		List<StockDetail> stockDetails = stockDetailService.list(Wrappers.lambdaQuery(StockDetail.class)
			.notIn(StockDetail::getZoneId, NodesUtil.toList(zoneList, Zone::getZoneId))
			.in(StockDetail::getStockId, NodesUtil.toList(pickPlans1, PickPlan::getStockId))
			.apply("stock_qty - pick_qty > 0"));
		if (Func.isEmpty(stockDetails)) {
			throw new ServiceException("系统没有查询到要拣货的物品！");
		}
		BigDecimal pickPlanQty = pickPlans1.stream().map(PickPlan::getPickPlanQty).reduce(BigDecimal::add).get();
		BigDecimal pickRealQty = pickPlans1.stream().map(PickPlan::getPickRealQty).reduce(BigDecimal::add).get();
		BigDecimal subtract = pickPlanQty.subtract(pickRealQty);
		BigDecimal qtys = pickTaskSubmitVO.getTargetLpnCodes().stream().map(LpnItemVo::getQty).reduce(BigDecimal::add).get();
		if (BigDecimalUtil.gt(qtys, subtract)) {
			throw new ServiceException("拣货数量超过计划数量！");
		}

		pickPlan = pickPlans1.stream().filter(pickPlan11 -> {
			List<StockDetail> stockDetails1 = stockDetails.stream().filter(stockDetail -> stockDetail.getLocId().equals(pickPlan11.getLocId())).collect(Collectors.toList());
			return pickPlan11.getStockId().equals(stockDetails1.get(0).getStockId());
		}).findFirst().orElse(null);
//		for (StockDetail stockDetail : stockDetails) {
//			if (pickPlan.getStockId().equals(stockDetail)) {
//				throw new ServiceException("请按拣货计划指定的库位进行拣货!");
//			}
//		}

		PickPlan finalPickPlan = pickPlan;
		List<StockDetail> detailList = stockDetails.stream()
			.filter(stockDetail -> stockDetail.getStockId().equals(finalPickPlan.getStockId()))
			.collect(Collectors.toList());
		if (Func.isEmpty(detailList)) {
			throw new ServiceException("请按拣货计划指定的库位进行拣货!");
		}
		ISkuPackageDetailService packageDetailService = SpringUtil.getBean(ISkuPackageDetailService.class);
		SkuPackageDetail packageDetail = SkuPackageDetailCache.getOne(pickPlan.getWspId(), SkuLevelEnum.Plate.getIndex());
		if (Func.isEmpty(packageDetail)) {
			throw new ServiceException("包装明细不存在！");
		}
		PickPlanDTO pickPlanDTO = new PickPlanDTO();
		for (LpnItemVo lpnItemVo : pickTaskSubmitVO.getTargetLpnCodes()) {
			if (!NodesUtil.toList(stockDetails, StockDetail::getLpnCode).contains(lpnItemVo.getLpnCode())) {
				throw new ServiceException(String.format("没有查询到[%s]托盘的库存信息！", lpnItemVo.getLpnCode()));
			}
			List<StockDetail> stockDetailList = stockDetails.stream()
				.filter(stockDetail -> stockDetail.getLpnCode().equals(lpnItemVo.getLpnCode())).collect(Collectors.toList());

			for (StockDetail stockDetail : stockDetailList) {
				pickPlan = pickPlanList.stream().filter(item -> {
					return item.getStockId().equals(stockDetail.getStockId());
				}).findFirst().orElse(null);
				if (Func.isEmpty(pickPlan)) continue;
				pickPlanDTO = PickPlanWrapper.pickSubmitVO2PickPlanDTO(pickPlan, pickTaskSubmitVO);
				if (BigDecimalUtil.le(lpnItemVo.getQty(), BigDecimal.ZERO)) {
					break;
				}
				if (BigDecimalUtil.le(stockDetail.getStockQty(), lpnItemVo.getQty())) {
					pickPlanDTO.setPickQty(stockDetail.getStockQty());
					lpnItemVo.setQty(lpnItemVo.getQty().subtract(stockDetail.getStockQty()));
				} else {
					pickPlanDTO.setPickQty(lpnItemVo.getQty());
					lpnItemVo.setQty(BigDecimal.ZERO);
				}
				pickPlanDTO.setTargetLpnCode(lpnItemVo.getLpnCode());
				pickPlanDTO.setSoBillId(stockDetail.getSoBillId());
				pickPlanDTO.setSourceLpnCode(lpnItemVo.getLpnCode());
				pickPlanDTO.setLotNumber(stockDetail.getLotNumber());
				Location location = LocationCache.getById(stockDetail.getLocId());
				if (Func.isNotEmpty(location)) {
					pickPlanDTO.setSourceLocCode(location.getLocCode());
				}
				pickPlanDTO.setTargetLocCode(pickTaskSubmitVO.getTargetLocCode());
				//保存系统日志
				SystemProcDTO systemProcParam = new SystemProcDTO();
				systemProcParam.setProcType(SystemProcTypeEnum.PICK);
				systemProcParam.setDataType(DataTypeEnum.WellenNo);

				if (Func.isNotEmpty(pickPlanDTO.getWellenNo())) { // 波次编码不为空
					systemProcParam.setBillNo(pickPlanDTO.getWellenNo());
				} else if (Func.isNotEmpty(pickPlanDTO.getTaskId())) { // 任务id不为空
					systemProcParam.setBillNo(pickPlanDTO.getTaskId().toString());
				}
				systemProcParam.setAction(ActionEnum.PICK_BASE);
				systemProcParam.setLpnCode(pickPlanDTO.getTargetLpnCode());
				systemProcParam.setOperationQty(pickPlanDTO.getPickQty());
				systemProcParam.setWhId(pickPlanDTO.getWhId());
				SystemProc systemProc = systemProcService.create(systemProcParam);

				pickPlanDTO.setSystemProcId(systemProc.getSystemProcId());
				//库存移动
				this.moveStockSku(pickPlanDTO);
				//更新拣货计划数量
				pickPlan.setPickRealQty((pickPlan.getPickRealQty().add(pickPlanDTO.getPickQty())));
				baseMapper.updateById(pickPlan);
				this.saveLogSoPick(pickPlanDTO);
				List<SoDetail> soDetailList1 = soDetailMap.get(stockDetail.getSkuId() + "_" + stockDetail.getWspId());
				List<SoDetail> unUpdateSoDetails = new ArrayList<>();
				for (SoDetail soDetail : soDetailList1) {
					if (BigDecimalUtil.eq(soDetail.getSurplusQty(), pickPlanDTO.getPickQty())) {
						soDetail.setSurplusQty(BigDecimal.ZERO);
						soDetail.setScanQty(soDetail.getScanQty().add(pickPlanDTO.getPickQty()));
						unUpdateSoDetails.add(soDetail);
						break;
					}
					if (BigDecimalUtil.lt(soDetail.getSurplusQty(), pickPlanDTO.getPickQty())) {
						soDetail.setSurplusQty(BigDecimal.ZERO);
						soDetail.setScanQty(soDetail.getScanQty().add(soDetail.getSurplusQty()));
						unUpdateSoDetails.add(soDetail);
						continue;
					}
					if (BigDecimalUtil.gt(soDetail.getSurplusQty(), pickPlanDTO.getPickQty())) {
						soDetail.setSurplusQty(soDetail.getSurplusQty().subtract(pickPlanDTO.getPickQty()));
						soDetail.setScanQty(soDetail.getScanQty().add(pickPlanDTO.getPickQty()));
						unUpdateSoDetails.add(soDetail);
						continue;
					}
				}
				soDetailService.updateBatchById(unUpdateSoDetails);
			}
		}
		//更新任务开始时间
		taskService.updateBeginTime(pickTaskSubmitVO.getWellenNo(), TaskTypeEnum.Pick);
		//计划列表
		List<PickPlan> pickPlans = baseMapper.selectPickPlanListForPickByTaskId(pickPlan.getWellenId(), pickPlan.getTaskId());
		// 拣货完成
		if (pickPlans.size() <= 0) {
			Map<Long, List<SoDetail>> soHeaderMap = soDetails.stream().collect(Collectors.groupingBy(SoDetail::getSoBillId));
			for (List<SoDetail> value : soHeaderMap.values()) {
				PickPlanDTO finalPickPlanDTO = pickPlanDTO;
				for (SoDetail soDetail : value) {
					finalPickPlanDTO.setSoBillId(soDetail.getSoBillId());
					Task task = taskService.getById(finalPickPlanDTO.getTaskId());
					if (Func.isEmpty(task)) {
						continue;
					}
					this.closeTack(finalPickPlanDTO);
				}
			}
		}
		// 保存物品操作记录
		SkuLogDTO skuLogDTO = new SkuLogDTO();
		skuLogDTO.setSkuId(pickPlan.getSkuId());
		skuLogDTO.setSkuLogTypeEnum(SkuLogTypeEnum.OUTSTOCK);
		skuLogService.saveOrUpdate(skuLogDTO);
		return null;
	}

	/**
	 * 按件拣货提交
	 *
	 * @param pickTaskSubmitVO
	 * @return
	 */
	@Override
	public synchronized PickTaskVO submitPickInfo(PickTaskSubmitVO pickTaskSubmitVO) {

//		if (pickTaskSubmitVO.getTargetLpnCode().equals(pickTaskSubmitVO.getSourceLpnCode())) {
//			throw new ServiceException("拣货容器与源容器不能相同");
//		}
		if (Func.isEmpty(pickTaskSubmitVO.getPickPlanId())) {
			throw new ServiceException("拣货计划ID不能为空");
		}
		if (Func.isEmpty(pickTaskSubmitVO.getWspdId())) {
			throw new ServiceException("包装明细ID不能为空");
		}

		if (Func.isEmpty(pickTaskSubmitVO.getWellenNo()) && Func.isEmpty(pickTaskSubmitVO.getTaskId())) {
			throw new ServiceException("波次编码与任务ID至少有一个不为空:请前端调试");
		}

		Wellen one = wellenService.getOne(Condition.getQueryWrapper(new Wellen()).lambda().eq(Wellen::getWellenNo, pickTaskSubmitVO.getWellenNo()));
		Location byCode = LocationCache.getByCode(pickTaskSubmitVO.getSourceLocCode());
		List<StockDetail> stockDetailList = stockDetailService.list(Condition.getQueryWrapper(new StockDetail())
			.lambda()
			.eq(StockDetail::getLocId, byCode.getLocId())
			.func(sql -> {
					if (Func.isNotEmpty(pickTaskSubmitVO.getTargetLpnCode())) {
						sql.eq(StockDetail::getLpnCode, pickTaskSubmitVO.getTargetLpnCode());
					}
				}
			));
//		if (Func.isNotEmpty(stockDetailList)) {
//			throw new ServiceException("该容器被占用,不能使用");
//		}

		//原库位信息
		Location location = LocationCache.getValid(pickTaskSubmitVO.getSourceLocCode());

		//获得拣货计划
		PickPlan param = new PickPlan();
		param.setPickPlanId(pickTaskSubmitVO.getPickPlanId());
		param.setSkuCode(pickTaskSubmitVO.getSkuCode());
		PickPlan pickPlan = baseMapper.selectOne(Condition.getQueryWrapper(param));

		// 验证物品编码是否一致
		if (Func.isEmpty(pickPlan)) {
			throw new ServiceException(String.format("物品[%s]不在拣货计划内", pickTaskSubmitVO.getSkuCode()));
		}
		//明细包装
		SkuPackageDetail packageDetail = SkuPackageDetailCache.getById(pickTaskSubmitVO.getWspdId());
		BigDecimal pickQty = BigDecimal.ZERO;
		if (1 != SkuCache.getById(pickPlan.getSkuId()).getIsSn()) {//是序列号管理
			//拣货数量, 需要根据选择的层级进行换算
			pickQty = pickTaskSubmitVO.getPickQty().multiply(new BigDecimal(packageDetail.getConvertQty()));
		} else {
			pickQty = pickTaskSubmitVO.getPickQty();
		}
		pickTaskSubmitVO.setPickQty(pickQty);
		// 验证拣货数量不能超过计划量
		if (pickQty.compareTo((pickPlan.getPickPlanQty().subtract(pickPlan.getPickRealQty()))) > 0) {
			throw new ServiceException(String.format("拣货数量超过计划数量"));
		}

		// 验证批次号是否一致
		if (!pickTaskSubmitVO.getLotNumber().equals(pickPlan.getLotNumber())) {
			throw new ServiceException(String.format("物品[%s]的批次[%s]不匹配", pickTaskSubmitVO.getSkuCode(), pickTaskSubmitVO.getLotNumber()));
		}
		//赋值
		PickPlanDTO pickPlanDTO = PickPlanWrapper.pickSubmitVO2PickPlanDTO(pickPlan, pickTaskSubmitVO);
		//保存系统日志
		SystemProcDTO systemProcParam = new SystemProcDTO();
		systemProcParam.setProcType(SystemProcTypeEnum.PICK);
		systemProcParam.setDataType(DataTypeEnum.WellenNo);

		if (Func.isNotEmpty(pickPlanDTO.getWellenNo())) { // 波次编码不为空
			systemProcParam.setBillNo(pickPlanDTO.getWellenNo());
		} else if (Func.isNotEmpty(pickPlanDTO.getTaskId())) { // 任务id不为空
			systemProcParam.setBillNo(pickPlanDTO.getTaskId().toString());
		}
		systemProcParam.setAction(ActionEnum.PICK_BASE);
		systemProcParam.setLpnCode(pickPlanDTO.getTargetLpnCode());
		systemProcParam.setOperationQty(pickPlanDTO.getPickQty());
		systemProcParam.setOperationUnit(SkuPackageDetailCache.getById(pickPlanDTO.getWspdId()).getWsuName());
		systemProcParam.setWhId(pickPlanDTO.getWhId());
		SystemProc systemProc = systemProcService.create(systemProcParam);

		pickPlanDTO.setSystemProcId(systemProc.getSystemProcId());

		Stock paramStock = new Stock();
		paramStock.setSkuId(pickPlanDTO.getSkuId());
		paramStock.setLotNumber(pickPlanDTO.getLotNumber());
		//paramStock.setStockStatus(StockStatusEnum.NORMAL);
		Stock stock = stockService.list(Condition.getQueryWrapper(paramStock))
			.stream().findFirst().orElse(null);

		List<SoDetail> soDetails = soDetailMapper.selectSodetailsByPickPlanId(pickPlanDTO.getPickPlanId());
		//库存移动
		this.moveStockSku(pickPlanDTO);

		//更新拣货计划数量
		pickPlan.setPickRealQty((pickPlan.getPickRealQty().add(pickPlanDTO.getPickQty())));
		baseMapper.updateById(pickPlan);

		//拣货的总数
		BigDecimal count = pickPlanDTO.getPickQty();
		for (SoDetail soDetail : soDetails) {
			//更新出库单明细中的物品数量
			soDetailService.updateSoDetailQty(soDetail, count);
			if (count.compareTo(BigDecimal.ZERO) == 0) {
				break;
			}
		}

		//更新发货明细数量
		if (soDetails.size() > 0) {
			pickPlanDTO.setSoBillId(soDetails.get(0).getSoBillId());
			pickPlanDTO.setSoBillNo(soDetails.get(0).getSoBillNo());
			pickPlanDTO.setSoDetailId(soDetails.get(0).getSoDetailId());
		}
		if (Func.isNotEmpty(stock)) {
			pickPlanDTO.setSkuLot(new SkuLotBaseEntity());
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				pickPlanDTO.getSkuLot().skuLotSet(i, stock.skuLotGet(i));
			}
		}
		// 库存增加
//		this.addStockForPick(pickPlanDTO, systemProc.getSystemProcId());

		//更新波次
		wellenService.updateState(pickPlanDTO.getWellenId(), WellenStateEnum.Begin);
		//保存拣货日志
		this.saveLogSoPick(pickPlanDTO);

		Long wellenId = null;
		Long taskId = null;
		//判断是波次还是任务
		Map<String, String> map = new HashMap<>();
		if (Func.isNotEmpty(pickTaskSubmitVO.getWellenNo())) { // 波次编码不为空
			map.put("wellenNo", pickTaskSubmitVO.getWellenNo());
			wellenId = wellenMapper.selectWellenListByNo(pickTaskSubmitVO.getWellenNo()).getWellenId();
		} else if (Func.isNotEmpty(pickTaskSubmitVO.getTaskId())) { // 任务id不为空
			map.put("taskId", pickTaskSubmitVO.getTaskId());
			taskId = Long.valueOf(pickTaskSubmitVO.getTaskId());
		}
		/**
		 * description	取消订单验证
		 * author		pengwei
		 * date			2020-07-02
		 */
		// 验证取消订单 - 开始
		Map<Long, List<WellenDetail>> wellenDetailMap = wellenDetailService.listByWellenId(pickPlanDTO.getWellenId())
			.stream().collect(Collectors.groupingBy(WellenDetail::getSoBillId));
		if (Func.isNotEmpty(wellenDetailMap) && wellenDetailMap.size() == 1) {
			SoHeader soHeader = soHeaderService.getById(String.valueOf(wellenDetailMap.entrySet().toArray()[0]));
			if (Func.isEmpty(soHeader)) {
				throw new ServiceException("数据异常：指定出库单不存在！");
			}
			if (soHeader.getSoBillState().equals(SoBillStateEnum.CANCEL.getIndex())) {
				throw new ServiceException("取消订单不允许再执行拣货操作；请将已拣物品放到暂存区后返回任务列表刷新任务！");
			}
		}


		//更新任务开始时间
		taskService.updateBeginTime(pickPlanDTO.getWellenNo(), TaskTypeEnum.Pick);
		//计划列表
		List<PickPlan> pickPlans = baseMapper.selectPickPlanListForPickByTaskId(wellenId, taskId);
		// 拣货完成
		if (pickPlans.size() <= 0) {
			this.closeTack(pickPlanDTO);
		}
		// 保存物品操作记录
		SkuLogDTO skuLogDTO = new SkuLogDTO();
		skuLogDTO.setSkuId(pickPlanDTO.getSkuId());
		skuLogDTO.setSkuLogTypeEnum(SkuLogTypeEnum.OUTSTOCK);
		skuLogService.saveOrUpdate(skuLogDTO);

		return this.getByTaskId(map);
	}

	/**
	 * 按件拣货 库存移动
	 *
	 * @param pickPlanDTO
	 * @return
	 */
	@Override
	public boolean moveStockSku(PickPlanDTO pickPlanDTO) {
		Warehouse warehouse = WarehouseCache.getById(pickPlanDTO.getWhId());
		if (Func.isEmpty(warehouse)) {
			throw new ServiceException("指定库房不存在！");
		}
		StockMoveDTO stockMoveDTO = new StockMoveDTO();
		stockMoveDTO.setSerialList(pickPlanDTO.getSerialList()); //序列号列表
		stockMoveDTO.setTargetLpnCode(pickPlanDTO.getTargetLpnCode()); //目标容器
		stockMoveDTO.setSourceLpnCode(pickPlanDTO.getSourceLpnCode()); //原容器
		//原库位
		Location sourceLoc = LocationCache.getValid(pickPlanDTO.getSourceLocCode());
		stockMoveDTO.setSourceLocId(sourceLoc.getLocId()); //原库位
		stockMoveDTO.setTargetBoxCode(pickPlanDTO.getBoxCode());
		String locCode;
		if (ZoneVirtualTypeEnum.isVirtual(pickPlanDTO.getTargetLocCode())) {
			locCode = Func.firstCharToUpper(pickPlanDTO.getTargetLocCode().toLowerCase()) + warehouse.getWhCode();
		} else {
			locCode = pickPlanDTO.getTargetLocCode();
		}
		//目标库位
		Location targetLoc = LocationCache.getValid(locCode);

		if (sourceLoc.getLocId() == targetLoc.getLocId()) {
			throw new ServiceException(String.format(
				"目标库位[%s]与原库位[%s]相同请更换库位", targetLoc.getLocCode(), sourceLoc.getLocCode()));
		}
		stockMoveDTO.setTargetLocId(targetLoc.getLocId()); //目标库位
		stockMoveDTO.setSkuId(pickPlanDTO.getSkuId()); //物品id
		stockMoveDTO.setMoveQty(pickPlanDTO.getPickQty()); //数量
		stockMoveDTO.setEventType(EventTypeEnum.Move);
		stockMoveDTO.setSystemProcId(pickPlanDTO.getSystemProcId());
		stockMoveDTO.setLotNumber(pickPlanDTO.getLotNumber());
		stockMoveDTO.setDataId(pickPlanDTO.getPickPlanId());
		stockService.stockMove(stockMoveDTO);
		return true;
	}

	/**
	 * 保存拣货日志
	 *
	 * @param pickPlanDTO
	 * @return
	 */
	@Override
	public boolean saveLogSoPick(PickPlanDTO pickPlanDTO) {
		SoPick soPick = new SoPick();
		soPick.setProcTime(LocalDateTime.now()); //业务发生时间
		soPick.setPickPlanId(pickPlanDTO.getPickPlanId());
		soPick.setLocId(pickPlanDTO.getLocId());
		soPick.setLocCode(pickPlanDTO.getLocCode());
		soPick.setLpnCode(pickPlanDTO.getTargetLpnCode());
		soPick.setTaskId(pickPlanDTO.getTaskId());
		soPick.setSkuId(pickPlanDTO.getSkuId());
		soPick.setSkuCode(pickPlanDTO.getSkuCode());
		soPick.setSkuName(pickPlanDTO.getSkuName());
		soPick.setLotNumber(pickPlanDTO.getLotNumber());
		if (Func.isNotEmpty(pickPlanDTO.getWellenNo())) {
			soPick.setWellenNo(Long.valueOf(pickPlanDTO.getWellenNo()));
		}
		soPick.setPickRealQty(pickPlanDTO.getPickQty());
		soPick.setWspId(pickPlanDTO.getWspId()); //通过拣货计划表获得
		soPick.setSkuLevel(pickPlanDTO.getSkuLevel());
		List<SkuPackageDetail> packageDetails = SkuPackageDetailCache.listByWspId(pickPlanDTO.getWspId())
			.stream()
			.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
			.collect(Collectors.toList());
		for (SkuPackageDetail packageDetail : packageDetails) {
			if (packageDetail.getSkuLevel() == pickPlanDTO.getSkuLevel()) {
				soPick.setWsuName(packageDetail.getWsuName());
				soPick.setConvertQty(packageDetail.getConvertQty());
			}
		}
//		soPick.setLpnCode(pickPlanDTO.getLpnCode());
		soPick.setTargetLpnCode(pickPlanDTO.getTargetLpnCode());
		soPick.setSoBillId(pickPlanDTO.getSoBillId());
		soPick.setSoBillNo(pickPlanDTO.getSoBillNo());
		soPick.setSoDetailId(pickPlanDTO.getSoDetailId());

//		soPick.setUserId(pickPlanDTO.getUserId());
//		soPick.setUserName(pickPlanDTO.getUserName());

		soPick.setSoDetailCode(Func.join(pickPlanDTO.getSerialList()));
		soPick.setWhId(pickPlanDTO.getWhId());

		if (Func.isNotEmpty(pickPlanDTO.getSkuLot())) {
			for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
				if (!pickPlanDTO.getSkuLot().skuLotChk(i)) {
					continue;
				}
				soPick.skuLotSet(i, pickPlanDTO.getSkuLot().skuLotGet(i));
			}
		}
		// 找到对应的波次明细
		WellenDetail wellenDetail = wellenDetailService.getOne(Condition.getQueryWrapper(new WellenDetail())
			.lambda()
			.eq(WellenDetail::getWellenId, pickPlanDTO.getWellenId())
			.eq(WellenDetail::getSoDetailId, pickPlanDTO.getSoDetailId())
			.last("limit 1"));
		if (Func.isNotEmpty(wellenDetail)) {
			soPick.setInventoryId(wellenDetail.getInventoryId());
		}
		return soPickService.save(soPick);
	}

	/**
	 * 关闭任务
	 *
	 * @param pickPlanDTO
	 * @return
	 */
	@Override
	public boolean closeTack(PickPlanDTO pickPlanDTO) {
		// 查询单据明细，是否都拣完了；如果还有未拣货完成的明细，则不需要修改订单状态
		boolean finished = soDetailService.count(Condition.getQueryWrapper(new SoDetail()).lambda()
			.eq(SoDetail::getSoBillId, pickPlanDTO.getSoBillId())
			.gt(SoDetail::getSurplusQty, BigDecimal.ZERO)) == 0;
		if (finished) {
			soHeaderService.updateBillState(pickPlanDTO.getSoBillId(), SoBillStateEnum.COMPLETED, true);
		}
		//关闭波次
		wellenService.updateState(pickPlanDTO.getWellenId(), WellenStateEnum.Close);
		//关闭任务
		if (Func.isNotEmpty(pickPlanDTO.getTaskId())) {
			taskService.close(pickPlanDTO.getTaskId().toString());
		}
		return true;
	}

	@Override
	public PickTaskVO getByTaskId(Map<String, String> param) {
		PickTaskVO pickTaskVO = new PickTaskVO();
		String wellenNo = param.get("wellenNo");
		String taskId = param.get("taskId");
		Long wellenId = null;
		if (Func.isEmpty(wellenNo) && Func.isEmpty(taskId)) {
			throw new ServiceException("波次编码与任务ID至少有一个不为空:请前端调试");
		}
		Task detail = taskService.getOne(Wrappers.lambdaQuery(Task.class).eq(Task::getBillNo, wellenNo));
		if (Func.isNotEmpty(detail)) {
			List<Long> ids = new ArrayList<>();
			ids.add(detail.getBillId());
			List<WellenSoHeaderVo> wellenSoHeaderVos = wellenDetailService.getSoHeaderByWellenId(ids);
			if (Func.isNotEmpty(wellenSoHeaderVos)) {
				pickTaskVO.setOrderNo(wellenSoHeaderVos.get(0).getSoHeader().getOrderNo());
				pickTaskVO.setSobillNo(wellenSoHeaderVos.get(0).getSoHeader().getSoBillNo());
				pickTaskVO.setCName(wellenSoHeaderVos.get(0).getSoHeader().getCName());
				pickTaskVO.setTransportDate(wellenSoHeaderVos.get(0).getSoHeader().getTransportDate());
			}
		}
		//登录用户信息
		BladeUser user = AuthUtil.getUser();
		//总数量
		PickPlan pickPlanParam = new PickPlan();
		if (Func.isNotEmpty(wellenNo)) { // 波次编码不为空
			if (Func.isEmpty(wellenMapper.selectWellenListByNo(wellenNo))) {
				return pickTaskVO;
			}
			wellenId = wellenMapper.selectWellenListByNo(wellenNo).getWellenId();
			pickPlanParam.setWellenId(wellenId);
		}
		if (Func.isNotEmpty(taskId)) { // 任务id不为空
			pickPlanParam.setTaskId(Long.parseLong(taskId));
		}
		// 待办拣货计划
		List<PickPlan> list = baseMapper.selectPickPlanListForPickByTaskId(wellenId, pickPlanParam.getTaskId());

		int planCount = super.count(Condition.getQueryWrapper(pickPlanParam).lambda()
			.orderByDesc(PickPlan::getLocCode));
		//没有待办拣货计划
		if (list.size() <= 0) {
			return pickTaskVO;
		}
		Map<Long, BigDecimal> collect = list.stream().collect(Collectors.groupingBy(PickPlan::getSkuId))
			.entrySet()
			.stream().collect(Collectors.toMap(Map.Entry::getKey, m -> m.getValue()
				.stream()
				.filter(d -> Objects.nonNull(d.getPickPlanQty()))
				.map(PickPlan::getPickPlanQty).reduce(BigDecimal.ZERO, BigDecimal::add)));
		Map<Long, BigDecimal> collect1 = list.stream().collect(Collectors.groupingBy(PickPlan::getSkuId))
			.entrySet()
			.stream().collect(Collectors.toMap(Map.Entry::getKey, m -> m.getValue()
				.stream()
				.filter(d -> Objects.nonNull(d.getPickRealQty()))
				.map(PickPlan::getPickRealQty).reduce(BigDecimal.ZERO, BigDecimal::add)));
		pickTaskVO.setCount(planCount); //总进度
		pickTaskVO.setFinish(planCount - list.size()); //完成进度

		if (Func.isEmpty(wellenNo)) { // 波次编码为空 则显示任务id
			pickTaskVO.setTitleCode(taskId); //任务id
		}
		if (Func.isEmpty(taskId)) { // 任务id为空 则显示波次编码
			pickTaskVO.setTitleCode(wellenNo); //波次编码
		}

		List<PickSkuVO> pickSkuVOs = new ArrayList<>();
		for (PickPlan pickPlan : list) {
			PickSkuVO pickSkuVO = new PickSkuVO();
			PickPlanWrapper.build().pickPlan2PickSkuVO(pickPlan, pickSkuVO);
			pickSkuVO.setTotalPlanQty(collect.get(pickPlan.getSkuId()));
			pickSkuVO.setTotalScanQty(collect1.get(pickPlan.getSkuId()));
			pickSkuVO.setIsSn(SkuCache.getById(pickPlan.getSkuId()).getIsSn()); //是否序列号
			pickSkuVO.setUserName(user.getNickName());// 拣货人
			pickSkuVO.setSkuId(pickPlan.getSkuId()); //物品ID
			pickSkuVO.setLotNumber(pickPlan.getLotNumber());
			//通过包装id查询包装明细列表
			List<SkuPackageDetail> skuPackageDetails = SkuPackageDetailCache.listByWspId(pickPlan.getWspId())
				.stream()
				.sorted(Comparator.comparingInt(SkuPackageDetail::getSkuLevel))
				.collect(Collectors.toList());
			//包装列表返回
			List<SkuPackageDetailViewVO> packageDetailResultList = new ArrayList<>();
			for (SkuPackageDetail skuPackageDetail : skuPackageDetails) {
				SkuPackageDetailViewVO packageViewVO = new SkuPackageDetailViewVO(); //默认包装明细
				packageViewVO.setWspdId(skuPackageDetail.getWspdId()); // 包装明细id
				packageViewVO.setWsuCode(skuPackageDetail.getWsuCode()); //计量单位编码
				packageViewVO.setWsuName(skuPackageDetail.getWsuName()); //计量单位名称
				packageViewVO.setConvertQty(skuPackageDetail.getConvertQty()); // 换算倍率
				packageViewVO.setSkuLevel(skuPackageDetail.getSkuLevel()); //层级

				if (skuPackageDetail.getSkuLevel() == pickPlan.getSkuLevel()) {
					pickSkuVO.setDefaultPackageDetail(packageViewVO);//set默认包装明细
					pickSkuVO.setSkuSpec("1-" + skuPackageDetail.getConvertQty()); //set规格

					pickSkuVO.setPlanQtyName(skuUmService.convert(pickPlan.getWspId(),
						pickPlan.getSkuLevel(), pickPlan.getPickPlanQty()));
					pickSkuVO.setRealQtyName(skuUmService.convert(pickPlan.getWspId(),
						pickPlan.getSkuLevel(), pickPlan.getPickRealQty()));

				}
				//添加包装列表信息
				packageDetailResultList.add(packageViewVO);
			}
			//set包装明细列表
			pickSkuVO.setPackageDetails(packageDetailResultList);

			try {
				List<Stock> stocks = stockService.list(Condition.getQueryWrapper(new Stock()).lambda()
					.eq(Stock::getLotNumber, pickPlan.getLotNumber())
					.eq(Stock::getSkuId, pickPlan.getSkuId()));

				if (stocks.size() > 0) {
					Map<String, Object> skuLotMap = SkuLotWrapper.skuLotToMapWithStock(stocks.get(0));
					SkuLotBaseEntity skuLotDTO = new SkuLotBaseEntity();
					if (Func.isNotEmpty(skuLotMap)) {
						for (String s : skuLotMap.keySet()) {
							String t = s.substring(s.lastIndexOf("t") + 1, s.length());
							skuLotDTO.skuLotSet(Integer.parseInt(t), skuLotMap.get(s).toString());
						}
					}
					pickSkuVO.setSkuLot(skuLotDTO);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			//通过拣货计划id查询拣货日志获得序列号
			QueryWrapper<SoPick> soPickQueryWrapper = new QueryWrapper<>();
			soPickQueryWrapper.lambda().eq(SoPick::getPickPlanId, pickPlan.getPickPlanId());
			List<String> serialList = new ArrayList<>();
			List<SoPick> soPickList = soPickService.list(soPickQueryWrapper);
			if (Func.isNotEmpty(soPickList)) {
				if (soPickList.size() > 0) {
					for (SoPick soPick : soPickList) {
						if (Func.isNotEmpty(soPick.getSoDetailCode())) {
							String[] soDetailCodes = soPick.getSoDetailCode().split(",");
							for (int i = 0; i < soDetailCodes.length; i++) {
								String soDetailCode = soDetailCodes[i];
								serialList.add(soDetailCode);
							}
						}
					}
				}
			}
			pickSkuVO.setSerialList(serialList);
			pickSkuVOs.add(pickSkuVO);
		}
		pickSkuVOs.sort(new Comparator<PickSkuVO>() {
			@Override
			public int compare(PickSkuVO o1, PickSkuVO o2) {
				ILocationService locationService = SpringUtil.getBean(ILocationService.class);
				//Location location1 = LocationCache.getByCode(o1.getSourceLocCode());
				//Location location2 = LocationCache.getByCode(o2.getSourceLocCode());
				Location location1 = locationService.list(Condition.getQueryWrapper(new Location())
					.lambda()
					.eq(Location::getLocCode, o1.getSourceLocCode())
				).stream().findFirst().orElse(null);

				Location location2 = locationService.list(Condition.getQueryWrapper(new Location())
					.lambda()
					.eq(Location::getLocCode, o2.getSourceLocCode())
				).stream().findFirst().orElse(null);

				if (Func.isEmpty(location1) || Func.isEmpty(location2)) {
					return 0;
				}
				if (Func.isEmpty(location1.getLogicAllocation()) || Func.isEmpty(location2.getLogicAllocation())) {
					return 0;
				}
				return location1.getLogicAllocation().compareTo(location2.getLogicAllocation());
			}
		});
		pickTaskVO.setPickPlans(pickSkuVOs);

		return pickTaskVO;
	}

	@Override
	public List<CreateDetailPickPlanVO> getDetailPickPlan(List<Long> billIdList) {
		// 获取所有订单表头
		List<SoHeader> soHeaderListAll = soHeaderService.listByIds(billIdList);
		// 获取所有还可以分配的明细
		List<SoDetail> soDetailListAll = soDetailService.list(Condition.getQueryWrapper(new SoDetail()).lambda()
			.in(SoDetail::getSoBillId, billIdList)
			.eq(SoDetail::getBillDetailState, SoDetailStateEnum.UnAlloc.getIndex())
			.ne(SoDetail::getSurplusQty, BigDecimal.ZERO));
		List<CreateDetailPickPlanVO> resultList = new ArrayList<>();

		for (SoHeader soHeader : soHeaderListAll) {
			// 判断当前订单状态
			if (!SoBillStateEnum.CREATE.getIndex().equals(soHeader.getSoBillState()) &&
				!SoBillStateEnum.EXECUTING.getIndex().equals(soHeader.getSoBillState())) {
				throw new ServiceException("订单编码: " + soHeader.getSoBillNo() + " 当前状态不允许生成拣货计划！");
			}
			// 记录系统日志
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.PICK_PLAN);
			systemProcParam.setDataType(DataTypeEnum.SO_BILL_NO);
			systemProcParam.setAction(ActionEnum.PLAN_DETAIL);
			systemProcParam.setBillNo(soHeader.getSoBillNo());
			systemProcParam.setWhId(soHeader.getWhId());
			SystemProc systemProc = systemProcService.create(systemProcParam);
			// 获取订单明细
			List<SoDetail> soDetailList = soDetailListAll.stream().filter(u -> {
				return u.getSoBillId().equals(soHeader.getSoBillId());
			}).collect(Collectors.toList());
			if (Func.isEmpty(soDetailList)) {
				throw new ServiceException(String.format("订单[%s] 未找到可分配的订单明细！", soHeader.getSoBillNo()));
			}
			for (SoDetail soDetail : soDetailList) {
				// 获取物品详细信息
				Long skuId = soDetail.getSkuId();
				Sku sku = SkuCache.getById(skuId);
				if (ObjectUtil.isEmpty(sku)) {
					throw new ServiceException("物品ID：" + skuId + "在基础数据(物品表)中不存在！");
				}
				CreateDetailPickPlanVO createDetailPickPlan = BeanUtil.copy(soDetail, CreateDetailPickPlanVO.class);
				resultList.add(createDetailPickPlan);
			}
		}
		return resultList;
	}

	/**
	 * 拣货序列号验证
	 *
	 * @param serialVO
	 * @return
	 */
	@Override
	public boolean lpnHasSerial(SerialVO serialVO) {
		if (Func.isEmpty(serialVO.getSkuId())) {
			throw new ServiceException("物品ID不能为空");
		}
		if (Func.isEmpty(serialVO.getLpnCode())) {
			throw new ServiceException("容器编码不能为空");
		}
		if (Func.isEmpty(serialVO.getSerialNumber())) {
			throw new ServiceException("序列号不能为空");
		}
		List<SerialVO> serialVOList = serialMapper.selectSerialFromStock(serialVO.getLpnCode()
			, serialVO.getSerialNumber(), serialVO.getSkuId());
		if (serialVOList.size() == 0) {
			throw new ServiceException("容器上没有该序列号");
		}
		return true;
	}

	@Override
	public boolean rollback(List<Long> billIdList) {
		// 获取所有订单
		List<SoHeader> soHeaderList = soHeaderService.listByIds(billIdList);
		for (SoHeader soHeader : soHeaderList) {
			// 记录系统日志
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.PICK_PLAN);
			systemProcParam.setDataType(DataTypeEnum.SO_BILL_NO);
			systemProcParam.setAction(ActionEnum.PLAN_ROLLBACK);
			systemProcParam.setBillNo(soHeader.getSoBillNo());
			systemProcParam.setWhId(soHeader.getWhId());
			systemProcService.create(systemProcParam);

			// 订单状态=创建，不允许执行取消分配操作
			if (!soHeader.getSoBillState().equals(SoBillStateEnum.EXECUTING.getIndex())) {
				throw new ServiceException(
					String.format(
						"不允许执行取消分配操作（订单编码：%s 当前状态：%s）",
						soHeader.getSoBillNo(), SoBillStateEnum.CREATE.getName()));
			}
			// 查询订单是否存在拣货记录
			int soPickCount = soPickService.count(Condition.getQueryWrapper(new SoPick()).lambda()
				.eq(SoPick::getSoBillId, soHeader.getSoBillId()));
			if (soPickCount > 0) {
				throw new ServiceException("订单[" + soHeader.getSoBillNo() + "] 已经开始拣货，不允许执行该操作！");
			}
			// 更新订单状态 = 单据创建
			soHeaderService.updateBillState(soHeader, SoBillStateEnum.CREATE, true);
			//更新调拨单状态 = 待出库
			allotHeaderService.updateBillState(soHeader.getOrderNo(), AllotBillStateEnum.UN_OUTSTOCK);
			// 根据订单ID回滚波次信息
			List<StockOccupy> stockOccupyList = wellenService.rollback(soHeader.getSoBillId());
			// 删除拣货计划
			if (ObjectUtil.isNotEmpty(stockOccupyList)) {
				for (StockOccupy stockOccupy : stockOccupyList) {
					super.removeById(stockOccupy.getPickPlanId());
				}
			}
		}
		return true;
	}

	@Override
	public IPage<PickPlan> page(IPage<PickPlan> page, PickPlanDTO pickPlan) {

		if (ObjectUtil.isNotEmpty(pickPlan.getWellenNo())) {
			Wellen wellenQuery = new Wellen();
			Long wellenNo = Long.valueOf(pickPlan.getWellenNo());
			wellenQuery.setWellenNo(wellenNo);
			Wellen wellen = wellenMapper.selectOne(Condition.getQueryWrapper(wellenQuery));
			if (ObjectUtil.isNotEmpty(wellen)) {
				pickPlan.setWellenId(wellen.getWellenId());
			} else {
				pickPlan.setWellenId(-1L);
			}
		}
		QueryWrapper pickPlanQueryWrapper = Condition.getQueryWrapper(pickPlan)
			.orderByDesc("pick_plan_id");
		return super.page(page, pickPlanQueryWrapper);
	}
}
