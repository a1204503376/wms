package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.strategy.dto.ManualAllocRequestDTO;
import org.nodes.wms.core.strategy.service.IManualAllocStrategy;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.context.annotation.Primary;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 批次范围具体执行
 * @author wuhongjie
 * @date 2022/2/16 14:13
 */
@Primary
public class LotRangeManualAllocStrategy implements IManualAllocStrategy {
	@Override
	public List<Stock> execute(ManualAllocRequestDTO manualAllocRequestDTO) {
		List<Stock> stockList = new ArrayList<>();
		List<Stock> list = manualAllocRequestDTO.getStocks().stream().filter(u -> {
			if (Func.isEmpty(u.getSkuLot1())) {
				return false;
			}
			LocalDateTime date = DateUtil.fromDate(DateUtil.parse(u.getSkuLot1(), DateUtil.PATTERN_DATETIME));
			ChronoLocalDate current = ChronoLocalDate.from(date);
			LocalDate localDate1 = manualAllocRequestDTO.getDto().getLotRange()[0];
			LocalDate localDate2 = manualAllocRequestDTO.getDto().getLotRange()[1];
			return (localDate1.isBefore(current) || localDate1.isEqual(current))
				&& (localDate2.isAfter(current) || localDate2.isEqual(current));
		}).collect(Collectors.toList());
		stockList.addAll(list);
		stockList.addAll(manualAllocRequestDTO.getStocks().stream().filter(u -> {
			return list.stream().filter(stock -> stock.getStockId().equals(u.getStockId()))
				.count() == 0L;
		}).collect(Collectors.toList()));
		return stockList;
	}
}
