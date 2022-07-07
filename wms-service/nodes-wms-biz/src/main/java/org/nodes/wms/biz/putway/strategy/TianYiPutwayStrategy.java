package org.nodes.wms.biz.putway.strategy;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.basics.dictionary.DictionaryBiz;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.systemParam.SystemParamBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 天宜定制：适用于天宜的上架策略
 */
@Component
@RequiredArgsConstructor
public class TianYiPutwayStrategy {

	private final LpnTypeBiz lpnTypeBiz;
	private final LocationBiz locationBiz;
	private final StockBiz stockBiz;
	private final SystemParamBiz systemParamBiz;
	private final DictionaryBiz dictionaryBiz;

	public Location run(BigDecimal putwayQty, Stock sourceStock) {
		// 获取要上架库存的箱型
		LpnType lpnType = lpnTypeBiz.findLpnTypeByBoxCode(sourceStock.getBoxCode());
		// 根据箱型查询所有可以上架的空库位(自动存储区的)，要按照上架顺序排序,
		List<Location> locationList = locationBiz.findEnableAgvLocation(lpnType,
			dictionaryBiz.findZoneTypeOfAutoStore().getDictKey().toString());
		// 获取列的最大载重
		BigDecimal maxLoadWeight = systemParamBiz.findMaxLoadWeightOfColumn();
		// 根据顺序刷选并返回合格的库位，刷选条件：判断该列的重量是否超过了最大限重
		for (Location location : locationList) {
			BigDecimal loadWeight = staticsLoadWeightByColumn(location);
			if (BigDecimalUtil.le(loadWeight.add(lpnType.getWeight()), maxLoadWeight)) {
				if (stockBiz.judgeEnableOnLocation(location)) {
					return location;
				}
			}
		}
		// 否则返回空的库位
		return null;
	}

	/**
	 * 计算库位一列的载重
	 *
	 * @return
	 */
	private BigDecimal staticsLoadWeightByColumn(Location location) {
		// 获取同列的所有库位
		List<Location> locationList = locationBiz.getLocationByColumn(location);
		// 获取该列的所有库存
		List<Stock> stockList = stockBiz.findStockByLocation(locationList);
		// 根据箱型计算总重, 需要注意同一个库位上可能存在多个stock，算重量时只需根据库位计算
		List<Stock> stocks = stockList
			.stream()
			.collect(Collectors.collectingAndThen(Collectors.toCollection(()
					-> new TreeSet<>(Comparator.comparing(o -> o.getLocId())))
				, ArrayList::new));
		// 库位1：stock1 stock2 库位2：stock3  重量：stock1.箱型重量 + stock3.箱型重量
		BigDecimal sumWeight = BigDecimal.ZERO;
		for (Stock stock :
			stocks) {
			LpnType lpnType = lpnTypeBiz.findLpnTypeByBoxCode(stock.getLpnCode());
			sumWeight = sumWeight.add(lpnType.getWeight());
		}
		return sumWeight;
	}
}
