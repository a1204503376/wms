package org.nodes.wms.core.strategy.factory.outstock;

import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.strategy.factory.IFunctionCode;
import org.springblade.core.tool.utils.Func;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @program: WmsCore
 * @description: 足量清库位执行代码
 * @author: pengwei
 * @create: 2020-12-16 14:01
 **/
public class FullDoseClearLocFunction extends BaseFunction implements IFunctionCode {
	@Override
	public List<Stock> execute(List<Stock> stockListAll, BigDecimal planQty) {
		// 处理足量清库位的逻辑
		// 先按库存量由大到小排序
		stockListAll.stream().sorted(new Comparator<Stock>() {
			@Override
			public int compare(Stock stock1, Stock stock2) {
				BigDecimal qty1 = stock1.getStockQty().subtract(stock1.getPickQty())
					.subtract(stock1.getOccupyQty());
				BigDecimal qty2 = stock2.getStockQty().subtract(stock2.getPickQty())
					.subtract(stock2.getOccupyQty());
				return qty2.compareTo(qty1);
			}
		});
		List<List<Stock>> groupList = new ArrayList<>();
		BigDecimal surplusQty = planQty;
		// 用于记录大于需求量的库存(用于找不到合适库存的时候使用)
		List<Stock> successStockList = new ArrayList<>();

		if (!new Integer(1).equals(super.getOutstockDetail().getAcrossLot())) {
			// 不允许混批：
			List<Stock> stockList = null;
			Map<String, List<Stock>> map = stockListAll.stream().collect(Collectors.groupingBy(Stock::getLotNumber));
			for (String key : map.keySet()) {
				stockList = this.getStockList(map.get(key), groupList, successStockList, surplusQty);
				if (Func.isNotEmpty(stockList)) {
					break;
				}
			}
			return stockList;
		} else {
			// 允许混批：
			return this.getStockList(stockListAll, groupList, successStockList, surplusQty);
		}
	}

	private List<Stock> getStockList(List<Stock> stockListAll, List<List<Stock>> groupList, List<Stock> successStockList, BigDecimal surplusQty){
		for (int i = 0; i < stockListAll.size(); i++) {
			Stock stock = stockListAll.get(i);
			// 计算库存可用量
			BigDecimal qty = stock.getStockQty().subtract(stock.getPickQty())
				.subtract(stock.getOccupyQty());
			if (BigDecimalUtil.gt(qty, surplusQty)) {
				// 库存可用量 > 计划量, 记录当前库存并直接跳过
				successStockList.add(stock);
				continue;
			} else if (BigDecimalUtil.eq(qty, surplusQty)) {
				groupList.add(new ArrayList() {{
					add(stock);
				}});
				// 只需要找到满足需求量的库存就够了，所以可以直接跳出
				break;
			} else {
				// 这种情况下因为：库存量 < 需求量，所以需要对所有库存进行计算，找出最优的拣货方式
				List<Stock> list = new ArrayList() {{
					add(stock);
				}};
				this.addRecursion(stockListAll, list, i, surplusQty.subtract(qty));
				// 计算完成后，将 list 存放到 groupList 中，方便后面统计
				groupList.add(list);
			}
		}
		// 执行完以上算法后，从stockList中，找到size最小的list
		List<Stock> newStockList = groupList.stream().min(Comparator.comparing(List::size))
			.orElse(null);
		if (Func.isEmpty(newStockList)) {
			newStockList = successStockList;
		}
		return newStockList;
	}

	private void addRecursion(List<Stock> stockList, List<Stock> list, Integer index, BigDecimal diffQty) {
		for (int i = index + 1; i < stockList.size(); i++) {
			Stock stock = stockList.get(i);
			BigDecimal stockQty = stock.getStockQty().subtract(stock.getPickQty())
				.subtract(stock.getOccupyQty());
			if (BigDecimalUtil.eq(stockQty, diffQty)) {
				// 可用数量 = 差异数量
				list.add(stock);
				break;
			} else if (BigDecimalUtil.lt(stockQty, diffQty)) {
				list.add(stock);
				diffQty.subtract(stockQty);
				this.addRecursion(stockList, list, i, diffQty);
			}
		}
	}
}
