
package org.nodes.wms.core.strategy.service.impl;

import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.service.ISkuTypeService;
import org.nodes.wms.core.strategy.service.IOutstockConfigService;
import org.springblade.core.log.exception.ServiceException;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.core.basedata.cache.SkuTypeCache;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuOutstock;
import org.nodes.wms.dao.basics.sku.entities.SkuType;
import org.nodes.wms.core.common.entity.AttributeBase;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.strategy.cache.OutstockConfigCache;
import org.nodes.wms.core.strategy.cache.OutstockConfigUdfCache;
import org.nodes.wms.core.strategy.cache.OutstockDetailCache;
import org.nodes.wms.core.strategy.entity.Outstock;
import org.nodes.wms.core.strategy.entity.OutstockConfig;
import org.nodes.wms.core.strategy.entity.OutstockConfigUdf;
import org.nodes.wms.core.strategy.entity.OutstockDetail;
import org.nodes.wms.core.strategy.enums.OutstockFuncEnum;
import org.nodes.wms.core.strategy.mapper.OutstockDetailMapper;
import org.nodes.wms.core.strategy.service.IOutstockConfigUdfService;
import org.nodes.wms.core.strategy.service.IOutstockDetailService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.comparator.Comparators;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 分配策略明细 服务实现类
 *
 * @author zhongls
 * @since 2019-12-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class OutstockDetailServiceImpl<M extends OutstockDetailMapper, T extends OutstockDetail>
	extends BaseServiceImpl<OutstockDetailMapper, OutstockDetail>
	implements IOutstockDetailService {

	@Autowired
	IOutstockConfigUdfService outstockConfigUdfService;

	@Override
	public boolean removeById(Serializable id) {
		boolean result = super.removeById(id);
		if (result) {
			//OutstockDetailCache.remove((Long) id);
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
			//OutstockDetailCache.remove(idList);
		}
		return result;
	}

	@Override
	public boolean save(OutstockDetail entity) {
		boolean result = super.save(entity);
		if (result) {
			//OutstockDetailCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public boolean updateById(OutstockDetail entity) {
		boolean result = super.updateById(entity);
		if (result) {
			//OutstockDetailCache.saveOrUpdate(entity);
		}
		return result;
	}

	@Override
	public OutstockDetail find(Outstock outstock, AttributeBase attributeBase, String billTypeCd, Sku sku) {
		IOutstockConfigUdfService outstockConfigUdfService = SpringUtil.getBean(IOutstockConfigUdfService.class);
		OutstockDetail outstockDetail = null;
		// 获取物品的分配策略明细
		//List<OutstockDetail> outstockDetails = OutstockDetailCache.list(outstock.getSsoId());
		List<OutstockDetail> outstockDetails = super.list(Condition.getQueryWrapper(new OutstockDetail())
		.lambda()
		.eq(OutstockDetail::getSsoId,outstock.getSsoId())
		);
		if (ObjectUtil.isEmpty(outstockDetails)) {
			throw new ServiceException("分配策略：" + outstock.getSsoName() + " 明细为空！");
		}
		IOutstockConfigService outstockConfigService = SpringUtil.getBean(IOutstockConfigService.class);
		outstockDetails.sort(Comparator.comparing(OutstockDetail::getSsodProcOrder));
		// 遍历分配策略明细
		for (OutstockDetail item : outstockDetails) {
			// 获取所有执行条件
			//List<OutstockConfig> outstockConfigs = OutstockConfigCache.listBySsodId(item.getSsodId());
			List<OutstockConfig> outstockConfigs
				= outstockConfigService.list(Condition.getQueryWrapper(new OutstockConfig())
				.lambda()
				.eq(OutstockConfig::getSsodId,item.getSsodId())
			);
			// 执行条件为空的情况下，表示忽略（也就是满足）
			if (Func.isEmpty(outstockConfigs)) {
				outstockDetail = item;
				break;
			}
			// 获取满足执行条件的数量
			Long count = outstockConfigs.stream().filter(u -> {
				Boolean billTypeCdResult = true;
				Boolean skuTypeIdResult = true;
				if (ObjectUtil.isNotEmpty(u.getBillTypeCd())) {
					billTypeCdResult = u.getBillTypeCd().equals(billTypeCd);
				}
				if (ObjectUtil.isNotEmpty(u.getSkuTypeId())) {
					ISkuTypeService skuTypeService = SpringUtil.getBean(ISkuTypeService.class);
					SkuType skuType = skuTypeService.getById(sku.getSkuTypeId());
					skuTypeIdResult = ObjectUtil.isNotEmpty(skuType) && Func.isNotEmpty(skuType.getTypePath())
						&& skuType.getTypePath().contains(String.valueOf(u.getSkuTypeId()));
				}
				return billTypeCdResult && skuTypeIdResult;
			}).count();
			if (count == 0 /*< outstockConfigs.size()*/) {
				continue;
			}
			// 获取所有单据自定义属性
			//List<OutstockConfigUdf> outstockConfigUdfs = OutstockConfigUdfCache.listBySsodId(item.getSsodId());
			List<OutstockConfigUdf> outstockConfigUdfs = outstockConfigUdfService.list(Condition.getQueryWrapper(new OutstockConfigUdf())
			.lambda()
			.eq(OutstockConfigUdf::getSsodId,item.getSsodId())
			);
			List<Boolean> configUdfMatch = outstockConfigUdfService.matchBillUdf(
				outstockConfigUdfs, attributeBase);
			if (configUdfMatch.contains(false)) {
				continue;
			}
			if (count > 0) {
				outstockDetail = item;
				break;
			}
		}
		return outstockDetail;
	}

	@Override
	public List<Stock> execute(OutstockDetail outstockDetail, SkuOutstock skuOutstock, List<Stock> stockList,
							   BigDecimal planQty) {
		List<Integer> funcCodeList = Func.toIntList(outstockDetail.getOutstockFunction());
		if (Func.isEmpty(funcCodeList)) {
			return stockList;
		}
		List<Stock> resultStockList = new ArrayList(stockList);
		// 足量优先
		if (funcCodeList.contains(OutstockFuncEnum.FULL_DOSE.getKey())) {
			// 足量不考虑混批，只需要筛选出库存足够的库存就可以了
			resultStockList = stockList.stream().filter(u -> {
				BigDecimal qty = u.getStockQty().subtract(u.getPickQty())
					.subtract(u.getOccupyQty());
				return BigDecimalUtil.ge(qty, planQty);
			}).collect(Collectors.toList());
		}
		// 清库位优先
		if (funcCodeList.contains(OutstockFuncEnum.CLEAR_LOCATION.getKey())) {
			if (!new Integer(1).equals(outstockDetail.getAcrossLot())) {
				// 不允许混批：需要根据物品出库设置里周转方式进行分组; 根据批属性分组，找到库存可用量与明细剩余量最接近的
				BigDecimal surplusQty = planQty;
				Map<String, List<Stock>> map = stockList.stream().collect(Collectors.groupingBy(stock -> {
					return this.getTurnoverItemValue(skuOutstock, stock);
				}));
				// 筛选出库存足够的，库存不足够的批次库存
				Map<String, List<Stock>> successMap = new HashMap<>();
				Map<String, List<Stock>> errorMap = new HashMap<>();
				List<Stock> successList = new ArrayList<>();
				for (String key : map.keySet()) {
					List<Stock> list = map.get(key);
					// 计算库存可用量
					BigDecimal stockQty = list.stream().map(stock -> {
						return stock.getStockQty().subtract(stock.getPickQty())
							.subtract(stock.getOccupyQty());
					}).reduce(BigDecimal.ZERO, BigDecimal::add);
					// 库存不足的情况下跳过
					if (BigDecimalUtil.lt(stockQty, surplusQty)) {
						errorMap.put(key, list);
						continue;
					}
					successMap.put(key, list);
					successList.addAll(list);
				}
				// 从库存满足的里面，找到批次最早的
				String firstKey = successMap.keySet().stream().sorted().findFirst().orElse(null);
				if (Func.isEmpty(firstKey)) {
					// 从库存不足的里面取
					firstKey = errorMap.keySet().stream().sorted().findFirst().orElse(null);
					if (Func.isEmpty(firstKey)) {
						resultStockList = new ArrayList<>();
					} else {
						resultStockList = errorMap.get(firstKey);
					}
				} else {
					// 选择成功的第一条
					resultStockList = successMap.get(firstKey);
				}
				return resultStockList;
			} else {
				// 允许混批：对库存先按出库设置的周转方式排序，再由小到大排序
				Comparator<Stock> comparator = this.sort(null, skuOutstock, stockList);
//				comparator.thenComparing((stock1, stock2) -> {
//					BigDecimal stockQty1 = stock1.getStockQty().subtract(stock1.getPickQty())
//						.subtract(stock1.getOccupyQty());
//					BigDecimal stockQty2 = stock2.getStockQty().subtract(stock2.getPickQty())
//						.subtract(stock2.getOccupyQty());
//					return stockQty1.compareTo(stockQty2);
//				});

				stockList = stockList.stream().sorted(comparator).sorted((stock1, stock2) -> {
					BigDecimal stockQty1 = stock1.getStockQty().subtract(stock1.getPickQty())
						.subtract(stock1.getOccupyQty());
					BigDecimal stockQty2 = stock2.getStockQty().subtract(stock2.getPickQty())
						.subtract(stock2.getOccupyQty());
					return stockQty1.compareTo(stockQty2);
				}).collect(Collectors.toList());
				return stockList;
			}
		}

		return resultStockList;
	}
	protected String getTurnoverItemValue(SkuOutstock skuOutstock, Stock stock) {

		String fieldName1 = this.getTurnoverItemField(skuOutstock.getTurnoverItem1());
		String fieldName2 = this.getTurnoverItemField(skuOutstock.getTurnoverItem2());
		String fieldName3 = this.getTurnoverItemField(skuOutstock.getTurnoverItem3());

		Object obj1 = NodesUtil.getFieldValue(stock, fieldName1);
		Object obj2 = NodesUtil.getFieldValue(stock, fieldName2);
		Object obj3 = NodesUtil.getFieldValue(stock, fieldName3);

		return String.format("%s %s %s", Func.toStr(obj1), Func.toStr(obj2), Func.toStr(obj3));
	}

	protected String getTurnoverItemField(Integer turnoverItem) {
		if (Func.isEmpty(turnoverItem)) {
			return StringPool.EMPTY;
		}
		return turnoverItem == 0 ? "lotNumber" : ("skuLot" + turnoverItem);
	}

	/**
	 * 根据物品出库设置排序
	 *
	 * @param stockList 库存列表
	 */
	protected Comparator<Stock> sort(Comparator<Stock> comparator, SkuOutstock skuOutstock, List<Stock> stockList) {
		if (Func.isEmpty(comparator)) {
			comparator = Comparator.comparing(Stock::getStockId);
		}
		if (Func.isNotEmpty(skuOutstock.getTurnoverItem1())) {
			this.turnoverTypeSort(comparator, skuOutstock.getTurnoverType1(),
				skuOutstock.getTurnoverItem1());
		}
		if (Func.isNotEmpty(skuOutstock.getTurnoverItem2())) {
			this.turnoverTypeSort(comparator, skuOutstock.getTurnoverType2(),
				skuOutstock.getTurnoverItem2());
		}
		if (Func.isNotEmpty(skuOutstock.getTurnoverItem3())) {
			this.turnoverTypeSort(comparator, skuOutstock.getTurnoverType3(),
				skuOutstock.getTurnoverItem3());
		}
		return comparator;
	}

	protected String turnoverTypeSort(Comparator<Stock> comparator, int turnoverType, int turnoverItem) {
		switch (turnoverType) {
			case 1:
				return this.turnoverItemSort(comparator, turnoverItem, Comparator.naturalOrder());
			case 2:
				return this.turnoverItemSort(comparator, turnoverItem, Comparator.reverseOrder());
			default:
				return "";
		}
	}

	protected String turnoverItemSort(Comparator<Stock> comparator, int turnoverItem, Comparator order) {
		String orderStr = order.equals(Comparator.reverseOrder()) ? " desc" : "asc";
		if (turnoverItem == 0) {
			comparator.thenComparing(Stock::getLotNumber, order);
			return "lot_number " + orderStr;
		} else {
			switch (turnoverItem) {
				case 1:
					comparator.thenComparing(Stock::getSkuLot1, order);
					return "sku_lot1" + orderStr;
				case 2:
					comparator.thenComparing(Stock::getSkuLot2, order);
					return "sku_lot2" + orderStr;
				case 3:
					comparator.thenComparing(Stock::getSkuLot3, order);
					return "sku_lot3" + orderStr;
				case 4:
					comparator.thenComparing(Stock::getSkuLot4, order);
					return "sku_lot4" + orderStr;
				case 5:
					comparator.thenComparing(Stock::getSkuLot5, order);
					return "sku_lot5" + orderStr;
				case 6:
					comparator.thenComparing(Stock::getSkuLot6, order);
					return "sku_lot6" + orderStr;
				case 7:
					comparator.thenComparing(Stock::getSkuLot7, order);
					return "sku_lot7" + orderStr;
				case 8:
					comparator.thenComparing(Stock::getSkuLot8, order);
					return "sku_lot8" + orderStr;
				case 9:
					comparator.thenComparing(Stock::getSkuLot9, order);
					return "sku_lot9" + orderStr;
				case 10:
					comparator.thenComparing(Stock::getSkuLot10, order);
					return "sku_lot10" + orderStr;
				case 11:
					comparator.thenComparing(Stock::getSkuLot11, order);
					return "sku_lot11" + orderStr;
				case 12:
					comparator.thenComparing(Stock::getSkuLot12, order);
					return "sku_lot12" + orderStr;
				case 13:
					comparator.thenComparing(Stock::getSkuLot13, order);
					return "sku_lot13" + orderStr;
				case 14:
					comparator.thenComparing(Stock::getSkuLot14, order);
					return "sku_lot14" + orderStr;
				case 15:
					comparator.thenComparing(Stock::getSkuLot15, order);
					return "sku_lot15" + orderStr;
				case 16:
					comparator.thenComparing(Stock::getSkuLot16, order);
					return "sku_lot16" + orderStr;
				case 17:
					comparator.thenComparing(Stock::getSkuLot17, order);
					return "sku_lot17" + orderStr;
				case 18:
					comparator.thenComparing(Stock::getSkuLot18, order);
					return "sku_lot18" + orderStr;
				case 19:
					comparator.thenComparing(Stock::getSkuLot19, order);
					return "sku_lot19" + orderStr;
				case 20:
					comparator.thenComparing(Stock::getSkuLot20, order);
					return "sku_lot20" + orderStr;
				case 21:
					comparator.thenComparing(Stock::getSkuLot21, order);
					return "sku_lot21" + orderStr;
				case 22:
					comparator.thenComparing(Stock::getSkuLot22, order);
					return "sku_lot22" + orderStr;
				case 23:
					comparator.thenComparing(Stock::getSkuLot23, order);
					return "sku_lot23" + orderStr;
				case 24:
					comparator.thenComparing(Stock::getSkuLot24, order);
					return "sku_lot24" + orderStr;
				case 25:
					comparator.thenComparing(Stock::getSkuLot25, order);
					return "sku_lot25" + orderStr;
				case 26:
					comparator.thenComparing(Stock::getSkuLot26, order);
					return "sku_lot26" + orderStr;
				case 27:
					comparator.thenComparing(Stock::getSkuLot27, order);
					return "sku_lot27" + orderStr;
				case 28:
					comparator.thenComparing(Stock::getSkuLot28, order);
					return "sku_lot28" + orderStr;
				case 29:
					comparator.thenComparing(Stock::getSkuLot29, order);
					return "sku_lot29" + orderStr;
				case 30:
					comparator.thenComparing(Stock::getSkuLot30, order);
					return "sku_lot30" + orderStr;
				default:
					return "";
			}
		}
	}
}
