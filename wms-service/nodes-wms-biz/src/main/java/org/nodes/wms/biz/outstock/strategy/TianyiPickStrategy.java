package org.nodes.wms.biz.outstock.strategy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.constant.DictKVConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.outstock.plan.modular.SoPickPlanFactory;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeCodeEnum;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 天宜定制出库策略
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TianyiPickStrategy {

	private final StockQueryBiz stockQueryBiz;
	private final SoPickPlanFactory soPickPlanFactory;
	private final LpnTypeBiz lpnTypeBiz;

	/**
	 * 自动化拣货区,自动化备货区,自动化存储区
	 */
	private static final List<String> AGV_ZONE_TYPE_LIST = Arrays.asList(
		DictKVConstant.ZONE_TYPE_AGV_STORAGE.toString(),
		DictKVConstant.ZONE_TYPE_AGV_CHOICE.toString(),
		DictKVConstant.ZONE_TYPE_AGV_PICK.toString());
	/**
	 * 人工拣货区：存储区,拣货区,备货区
	 */
	private static final List<String> MANUAL_ZONE_TYPE_LIST = Arrays.asList(
		DictKVConstant.ZONE_TYPE_CHOICE.toString(),
		DictKVConstant.ZONE_TYPE_PICK.toString(),
		DictKVConstant.ZONE_TYPE_STORAGE.toString());

	/**
	 * 策略分配，天宜的策略分配是基于箱分配，如果自动区一个箱中有多个物品时，如果不是全部出库的不应该分配该库存
	 * 优先分配自动区的，如果自动区要拆箱则再从人工区中出,尽量不拆箱
	 *
	 * @param soHeader           soHeader
	 * @param soDetail           soDetail
	 * @param soDetailList       soDetailList
	 * @param pickPlanOfSoDetail 当前明细之前分配的拣货计划
	 * @return 本次分配计划
	 */
	public List<SoPickPlan> run(SoHeader soHeader, SoDetail soDetail,
								List<SoDetail> soDetailList, List<SoPickPlan> pickPlanOfSoDetail) {
		// 根据发货单明细中的批属性分别查询人工区和自动区的库存，并分别进行按入库日期进行排序
		SkuLotBaseEntity skuLot = new SkuLotBaseEntity();
		SkuLotUtil.setAllSkuLot(soDetail, skuLot);

		List<Stock> agvStockList = stockQueryBiz.findEnableStockByZoneTypeAndSkuLot(soHeader.getWhId(),
			soDetail.getSkuId(), StockStatusEnum.NORMAL, AGV_ZONE_TYPE_LIST, skuLot);
		agvStockList = sort(agvStockList);

		List<Stock> manualStockList = stockQueryBiz.findEnableStockByZoneTypeAndSkuLot(soHeader.getWhId(),
			soDetail.getSkuId(), StockStatusEnum.NORMAL, MANUAL_ZONE_TYPE_LIST, skuLot);
		manualStockList = sort(manualStockList);
		if (Func.isEmpty(agvStockList) && Func.isEmpty(manualStockList)) {
			return null;
		}


		LpnTypeCodeEnum lpnTypeCodeEnum =lpnTypeBiz.tryParseBoxCode(Func.isNotEmpty(agvStockList) ?
			agvStockList.get(0).getBoxCode() : manualStockList.get(0).getBoxCode());
		if (LpnTypeCodeEnum.D.equals(lpnTypeCodeEnum)){
			return plan(soHeader, soDetail, soDetailList, pickPlanOfSoDetail, manualStockList, agvStockList);
		} else {
			return plan(soHeader, soDetail, soDetailList, pickPlanOfSoDetail, agvStockList, manualStockList);
		}
	}

	private List<SoPickPlan> plan(SoHeader soHeader, SoDetail soDetail,
					  List<SoDetail> soDetailList, List<SoPickPlan> pickPlanOfSoDetail,
					  List<Stock> firstStockList, List<Stock> secondStockList){
		List<SoPickPlan> newPickPlanList = new ArrayList<>();
		// 注意如果自动区的箱中存在不需要出的库存则不应该分配该箱
		// 如果是自动区的则需要根据箱码中生成其它物品的拣货计划
		BigDecimal surplusQty = getSurplusQty(soDetail, pickPlanOfSoDetail);
		List<Long> skuIdsOfSoDetail = getAllSkuIdFromSoDetail(soDetailList);
		boolean needUnpackStock = false;
		for (Stock stock : firstStockList) {
			if (stock.isnotEnable()){
				continue;
			}

			List<Stock> stockOfLoc = stockQueryBiz.findStockByLocation(stock.getLocId());
			if (Func.isEmpty(stockOfLoc) || isNotCondition(stockOfLoc, skuIdsOfSoDetail)) {
				log.warn("[自动分配]单据:{}明细:{}分配的自动区库位:{}存在不出库的库存",
					soHeader.getSoBillNo(), soDetail.getSoLineNo(), stock.getLocCode());
				continue;
			}

			if (BigDecimalUtil.ge(surplusQty, stock.getStockEnable())) {
				// 分配该库存
				surplusQty = surplusQty.subtract(stock.getStockEnable());
				List<SoPickPlan> pickPlans = soPickPlanFactory.createOnAgvArea(soDetail, soDetailList, stock, stockOfLoc);
				newPickPlanList.addAll(pickPlans);
			} else {
				// 需要拆箱的库存，如果人工区没有库存或库存数量不足，就只能进行分配需要拆箱的库存，否则从人工区分配
				if (canPlanFromManualZone(secondStockList, surplusQty)) {
					needUnpackStock = true;
				} else {
					surplusQty = surplusQty.subtract(stock.getStockEnable());
					List<SoPickPlan> pickPlans = soPickPlanFactory.createOnAgvArea(soDetail, soDetailList, stock, stockOfLoc);
					newPickPlanList.addAll(pickPlans);
				}

				break;
			}
		}

		if (needUnpackStock || BigDecimalUtil.gt(surplusQty, BigDecimal.ZERO)) {
			for (Stock stock : secondStockList) {
				if (BigDecimalUtil.le(surplusQty, BigDecimal.ZERO)) {
					break;
				}
				if (stock.isnotEnable()){
					continue;
				}

				BigDecimal planQty = BigDecimalUtil.ge(surplusQty, stock.getStockEnable()) ? stock.getStockEnable() : surplusQty;
				SoPickPlan soPickPlan = soPickPlanFactory.create(soDetail.getSoBillId(), soDetail, stock, planQty);
				newPickPlanList.add(soPickPlan);
				surplusQty = surplusQty.subtract(planQty);
			}
		}

		return newPickPlanList;
	}

	private boolean canPlanFromManualZone(List<Stock> manualStocks, BigDecimal surplusQty) {
		if (Func.isEmpty(manualStocks)) {
			return false;
		}

		BigDecimal stockEnable = manualStocks.stream()
			.map(Stock::getStockEnable)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
		return BigDecimalUtil.ge(stockEnable, surplusQty);
	}

	private BigDecimal getSurplusQty(SoDetail soDetail, List<SoPickPlan> pickPlanOfSoDetail) {
		BigDecimal occupyQty = BigDecimal.ZERO;
		if (Func.isNotEmpty(pickPlanOfSoDetail)) {
			occupyQty = pickPlanOfSoDetail.stream()
				.map(item -> item.getPickPlanQty().subtract(item.getPickRealQty()))
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		}

		return soDetail.getSurplusQty().subtract(occupyQty);
	}

	private boolean isNotCondition(List<Stock> stockList, List<Long> skuIdsOfSoDetail) {
		// 判断自动区的中物品的库存是否全部在对应的出库明细中
		List<Long> skuIdOfStock = stockList.stream()
			.map(Stock::getSkuId)
			.collect(Collectors.toList());

		return !skuIdsOfSoDetail.containsAll(skuIdOfStock);
	}

	private List<Long> getAllSkuIdFromSoDetail(List<SoDetail> soDetailList) {
		AssertUtil.notEmpty(soDetailList, "运行策略失败,发货单明细参数为空");
		return soDetailList.stream()
			.map(SoDetail::getSkuId)
			.collect(Collectors.toList());
	}

	private List<Stock> sort(List<Stock> stockList) {
		if (Func.isEmpty(stockList)) {
			return new ArrayList<>();
		}

		return stockList.stream()
			.sorted(Comparator.comparing(Stock::getSkuLot3).thenComparing(Stock::getLocCode))
			.collect(Collectors.toList());
	}

}
