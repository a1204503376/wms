package org.nodes.wms.biz.putway.strategy;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.biz.basics.dictionary.DictionaryBiz;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.systemParam.SystemParamBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.log.exception.ServiceException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 天宜定制：适用于天宜的上架策略
 *
 * @author nodesc
 */
@Component
@RequiredArgsConstructor
public class TianYiPutwayStrategy {

	private final LpnTypeBiz lpnTypeBiz;
	private final LocationBiz locationBiz;
	private final StockBiz stockBiz;
	private final SystemParamBiz systemParamBiz;
	private final DictionaryBiz dictionaryBiz;
	private final StockQueryBiz stockQueryBiz;

	public Location run(List<Stock> stocks) {
		// 获取要上架库存的箱型
		LpnType lpnType = lpnTypeBiz.findLpnTypeByBoxCode(stocks.get(0).getBoxCode());
		// 根据箱型查询所有可以上架的空库位(自动存储区的)，要按照上架顺序排序,
		List<Location> locationList = locationBiz.findEnableAgvLocation(lpnType,
			dictionaryBiz.findZoneTypeOfAutoStore().getDictKey().toString());
		// 获取列的最大载重
		BigDecimal maxLoadWeight = systemParamBiz.findMaxLoadWeightOfColumn();
		// 计算当前库存的载重
		BigDecimal currentWeight = staticsLoadWeightByStock(stocks, lpnType);
		// 根据顺序刷选并返回合格的库位，刷选条件：判断该列的重量是否超过了最大限重
		for (Location location : locationList) {
			// 计算当前货架上的载重
			BigDecimal loadWeight = staticsLoadWeightByColumn(location);
			if (BigDecimalUtil.le(loadWeight.add(currentWeight), maxLoadWeight)) {
				if (stockBiz.judgeEnableOnLocation(location)) {
					return location;
				}
			}
		}
		// 否则返回空的库位
		return null;
	}

	/**
	 * 判断库存上架到目标库位之后是否超过最大载重
	 *
	 * @param stockList 待上库存
	 * @param targetLoc 目标库位
	 * @return true：未超重
	 */
	public boolean isNotOverweight(List<Stock> stockList, Location targetLoc){
		AssertUtil.notEmpty(stockList, "载重校验失败，待上库存为空");
		AssertUtil.notNull(targetLoc, "载重校验失败，待上库位为空");

		LpnType lpnType = lpnTypeBiz.findLpnTypeByBoxCode(stockList.get(0).getBoxCode());
		BigDecimal currentStockWeight = staticsLoadWeightByStock(stockList, lpnType);
		BigDecimal weightOfColumn = staticsLoadWeightByColumn(targetLoc);
		BigDecimal maxLoadWeight = systemParamBiz.findMaxLoadWeightOfColumn();
		return BigDecimalUtil.le(currentStockWeight.add(weightOfColumn), maxLoadWeight);
	}

	/**
	 * 根据库存计算重量
	 * 注意：是根据箱的个数计算重量，不是库存的数量
	 *
	 * @param stockList 库存
	 * @param lpnType   容器类型
	 * @return 当前库存的载重
	 */
	private BigDecimal staticsLoadWeightByStock(List<Stock> stockList, LpnType lpnType) {
		List<String> boxCodes = stockList.stream()
			.map(Stock::getBoxCode)
			.distinct()
			.collect(Collectors.toList());
		return lpnType.getWeight().multiply(BigDecimal.valueOf(boxCodes.size()));
	}

	/**
	 * 计算库位一列的载重, 虚拟库位不参与计算
	 *
	 * @return
	 */
	private BigDecimal staticsLoadWeightByColumn(Location location) {
		// 获取同列的所有库位
		List<Location> locationList = locationBiz.getLocationByColumn(location);
		// 虚拟库位不参与计算
		if (locationBiz.isVirtualLocation(locationList)) {
			throw new ServiceException("虚拟库位不存在载重计算");
		}
		// 获取该列的所有库存
		List<Stock> stockList = stockQueryBiz.findStockByLocation(locationList);
		// 根据箱型计算总重, 需要注意同一个库位上可能存在多个stock，算重量时只需根据库位计算
		List<Stock> stocks = stockList
			.stream()
			.collect(Collectors.collectingAndThen(Collectors.toCollection(()
					-> new TreeSet<>(Comparator.comparing(o -> o.getLocId())))
				, ArrayList::new));
		// 库位1：stock1 stock2 库位2：stock3  重量：stock1.箱型重量 + stock3.箱型重量
		BigDecimal sumWeight = BigDecimal.ZERO;
		for (Stock stock : stocks) {
			LpnType lpnType = lpnTypeBiz.findLpnTypeByBoxCode(stock.getLpnCode());
			sumWeight = sumWeight.add(lpnType.getWeight());
		}
		return sumWeight;
	}
}
