package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.core.outstock.so.enums.CreatePickPlanByWellenTypeEnum;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.strategy.dto.ManualAllocRequestDTO;
import org.nodes.wms.core.strategy.service.IManualAllocStrategy;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * @author wuhongjie
 * @date 2022/2/16 15:41
 */
@Primary
public class ManualAllocStrategyUtil {
	public static List<Stock> executeStrategy(ManualAllocRequestDTO manualAllocRequestDTO){
		IManualAllocStrategy manualAllocStrategy = null;
		if (CreatePickPlanByWellenTypeEnum.FIFO.getIndex().equals(manualAllocRequestDTO.getDto().getType())){
			manualAllocStrategy = new FIFOManualAllocStrategy();
		}else if(CreatePickPlanByWellenTypeEnum.LIFO.getIndex().equals(manualAllocRequestDTO.getDto().getType())){
			manualAllocStrategy = new LIFOManualAllocStrategy();
		}else if(CreatePickPlanByWellenTypeEnum.LOT_RANGE.getIndex().equals(manualAllocRequestDTO.getDto().getType())){
			manualAllocStrategy = new LotRangeManualAllocStrategy();
		}else if(CreatePickPlanByWellenTypeEnum.ZONE_PRIORITY.getIndex().equals(manualAllocRequestDTO.getDto().getType())){
			manualAllocStrategy = new ZonePriorityManualAllocStrategy();
		}else if(CreatePickPlanByWellenTypeEnum.LOT.getIndex().equals(manualAllocRequestDTO.getDto().getType())){
			manualAllocStrategy = new LotManualAllocStrategy();
		}else if(CreatePickPlanByWellenTypeEnum.LOCATION.getIndex().equals(manualAllocRequestDTO.getDto().getType())){
			manualAllocStrategy = new LocationManualAllocStrategy();
		}else if(CreatePickPlanByWellenTypeEnum.LOCATION_LOT.getIndex().equals(manualAllocRequestDTO.getDto().getType())){
			manualAllocStrategy = new LocationLotManualAllocStrategy();
		}
		return manualAllocStrategy.execute(manualAllocRequestDTO);
	}
}
