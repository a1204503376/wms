package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.strategy.dto.ManualAllocRequestDTO;
import org.nodes.wms.core.strategy.service.IManualAllocStrategy;
import org.nodes.wms.core.warehouse.service.IZoneService;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.context.annotation.Primary;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 先进先出具体执行
 * @author wuhongjie
 * @date 2022/2/16 14:13
 */
@Primary
public class FIFOManualAllocStrategy implements IManualAllocStrategy {
	IZoneService zoneService;
	public FIFOManualAllocStrategy(){
		this.zoneService = SpringUtil.getBean(IZoneService.class);
	}
	@Override
	public List<Stock> execute(ManualAllocRequestDTO manualAllocRequestDTO) {
		List<Zone> zoneList = this.zoneService.list();
		Comparator<Stock> stockComparator = Comparator.comparing(Stock::getSkuId)
			.thenComparing(Stock::getLotNumber);
		stockComparator = stockComparator.thenComparing((o1, o2) -> {
			Zone zone1 = zoneList.stream().filter(zone -> zone.getZoneId().equals(o1.getZoneId()))
				.findFirst().orElse(null);
			Zone zone2 = zoneList.stream().filter(zone -> zone.getZoneId().equals(o2.getZoneId()))
				.findFirst().orElse(null);
			if (Func.isEmpty(zone1) || Func.isEmpty(zone2)) {
				return -1;
			}
			return zone1.getZoneCode().compareTo(zone2.getZoneCode());
		}).thenComparing(Stock::getSkuLot1);
		return manualAllocRequestDTO.getStocks().stream()
			.sorted(stockComparator)
			.collect(Collectors.toList());
	}
}
