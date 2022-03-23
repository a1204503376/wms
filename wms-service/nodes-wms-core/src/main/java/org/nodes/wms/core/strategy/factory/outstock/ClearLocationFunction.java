package org.nodes.wms.core.strategy.factory.outstock;

import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.strategy.enums.OutstockFuncEnum;
import org.nodes.wms.core.strategy.factory.IFunctionCode;
import org.nodes.wms.core.strategy.factory.annotation.Code;
import org.springblade.core.tool.utils.Func;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @program: WmsCore
 * @description: 清库位执行代码
 * @author: pengwei
 * @create: 2020-12-16 13:59
 **/
@Code(OutstockFuncEnum.CLEAR_LOCATION)
public class ClearLocationFunction extends BaseFunction implements IFunctionCode {

	@Override
	public List<Stock> execute(List<Stock> stockListAll, BigDecimal planQty) {
		if (!new Integer(1).equals(super.getOutstockDetail().getAcrossLot())) {
			// 不允许混批：需要根据物品出库设置里周转方式进行分组; 根据批属性分组，找到库存可用量与明细剩余量最接近的
			BigDecimal surplusQty = planQty;
			Map<String, List<Stock>> map = stockListAll.stream().collect(Collectors.groupingBy(stock -> {
				return super.getTurnoverItemValue(stock);
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
			List<Stock> stockList = null;
			String firstKey = successMap.keySet().stream().sorted().findFirst().orElse(null);
			if (Func.isEmpty(firstKey)) {
				// 从库存不足的里面取
				firstKey = errorMap.keySet().stream().sorted().findFirst().orElse(null);
				if (Func.isEmpty(firstKey)) {
					stockList = new ArrayList<>();
				} else {
					stockList = errorMap.get(firstKey);
				}
			} else {
				// 选择成功的第一条
				stockList = successMap.get(firstKey);
			}
			return stockList;
		} else {
			// 允许混批：对库存先按出库设置的周转方式排序，再由小到大排序
			Comparator<Stock> comparator = super.sort(null, stockListAll);
			comparator.thenComparing((stock1, stock2) -> {
				BigDecimal stockQty1 = stock1.getStockQty().subtract(stock1.getPickQty())
					.subtract(stock1.getOccupyQty());
				BigDecimal stockQty2 = stock2.getStockQty().subtract(stock2.getPickQty())
					.subtract(stock2.getOccupyQty());
				return stockQty1.compareTo(stockQty2);
			});
			stockListAll.stream().sorted(comparator);
			return stockListAll;
		}
	}
}
