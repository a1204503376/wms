package org.nodes.wms.core.strategy.dto;

import lombok.Data;
import org.nodes.wms.core.outstock.so.dto.CreatePickPlanByWellenDTO;
import org.nodes.wms.core.stock.core.entity.Stock;

import java.util.List;

/**
 * @author wuhongjie
 * @date 2022/2/16 15:01
 */
@Data
public class ManualAllocRequestDTO {
	private List<Stock> stocks;
	private CreatePickPlanByWellenDTO dto;
}
