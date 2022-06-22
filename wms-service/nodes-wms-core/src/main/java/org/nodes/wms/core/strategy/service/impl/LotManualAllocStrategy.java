package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.strategy.dto.ManualAllocRequestDTO;
import org.nodes.wms.core.strategy.service.IManualAllocStrategy;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.context.annotation.Primary;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 指定批次具体执行
 * @author wuhongjie
 * @date 2022/2/16 14:13
 */
@Primary
public class LotManualAllocStrategy implements IManualAllocStrategy {
	@Override
	public List<Stock> execute(ManualAllocRequestDTO manualAllocRequestDTO) {
		manualAllocRequestDTO.getStocks()
			.removeIf(stock -> StringUtil.indexOfIgnoreCase(manualAllocRequestDTO.getDto().getLotNumbers(), stock.getLotNumber()) < 0);
		//按批次号先进先出排序
		Comparator<Stock> stockComparator = Comparator.comparing(Stock::getSkuId)
			.thenComparing(Stock::getLotNumber);
		return manualAllocRequestDTO.getStocks().stream()
			.sorted(stockComparator)
			.collect(Collectors.toList());
	}
}
