package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.strategy.dto.ManualAllocRequestDTO;
import org.nodes.wms.core.strategy.dto.ZoneManualAllocRequestDTO;
import org.nodes.wms.core.strategy.service.IManualAllocStrategy;
import org.springblade.core.tool.utils.Func;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 库区优先具体执行
 *
 * @author wuhongjie
 * @date 2022/2/16 14:13
 */
@Primary
public class ZonePriorityManualAllocStrategy implements IManualAllocStrategy {
	@Override
	public List<Stock> execute(ManualAllocRequestDTO manualAllocRequestDTO) {
		List<Stock> stockList = new ArrayList<>();
		List<Stock> list = manualAllocRequestDTO.getStocks().stream().filter(u ->
			Func.isNotEmpty(manualAllocRequestDTO.getDto().getZoneIds()) && Arrays.asList(manualAllocRequestDTO.getDto().getZoneIds())
			.contains(u.getZoneId())).collect(Collectors.toList());
		;
		stockList.addAll(list);
		stockList.addAll(manualAllocRequestDTO.getStocks().stream().filter(u -> list.stream()
			.filter(stock -> stock.getStockId().equals(u.getStockId()))
			.count() == 0L).collect(Collectors.toList()));
		return stockList;
	}
}
