package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.strategy.dto.LocationLotManualAllocRequestDTO;
import org.nodes.wms.core.strategy.dto.ManualAllocRequestDTO;
import org.nodes.wms.core.strategy.service.IManualAllocStrategy;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.service.ILocationService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.context.annotation.Primary;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 指定库位&批次具体执行
 *
 * @author wuhongjie
 * @date 2022/2/16 14:13
 */
@Primary
public class LocationLotManualAllocStrategy implements IManualAllocStrategy {
	ILocationService locationService;
	public LocationLotManualAllocStrategy(){
		this.locationService = SpringUtil.getBean(ILocationService.class);
	}
	@Override
	public List<Stock> execute(ManualAllocRequestDTO manualAllocRequestDTO) {
		manualAllocRequestDTO.getStocks().removeIf(stock -> {
			Location location = LocationCache.getById(stock.getLocId());
			if (Func.isEmpty(location)) {
				return true;
			}
			return StringUtil.indexOfIgnoreCase(manualAllocRequestDTO.getDto().getLotNumbers(), stock.getLotNumber()) < 0
				|| StringUtil.indexOfIgnoreCase(manualAllocRequestDTO.getDto().getLocCodes(), location.getLocCode()) < 0;
		});
		//按库位分组求和后再排序
		Map<Long, BigDecimal> map = new HashMap<>();
		manualAllocRequestDTO.getStocks().stream().collect(Collectors.groupingBy(Stock::getLocId)).forEach((locId, list) -> {
			BigDecimal stock_qty = list.stream().map(Stock::getStockQty).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal pick_qty = list.stream().map(Stock::getPickQty).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal occupy_qty = list.stream().map(Stock::getOccupyQty).reduce(BigDecimal.ZERO, BigDecimal::add);
			map.put(locId, stock_qty.subtract(pick_qty).subtract(occupy_qty));
		});

		//按批次号先进先出排序
		Comparator<Stock> stockComparator = Comparator.comparing(Stock::getSkuId)
			.thenComparing(Stock::getLotNumber);
		stockComparator = stockComparator.thenComparing(o -> map.getOrDefault(o.getLocId(), BigDecimal.ZERO));
		return manualAllocRequestDTO.getStocks().stream()
			.sorted(stockComparator)
			.collect(Collectors.toList());
	}
}
