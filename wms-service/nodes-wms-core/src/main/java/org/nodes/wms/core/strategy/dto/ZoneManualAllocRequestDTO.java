package org.nodes.wms.core.strategy.dto;

import lombok.Data;

/**
 * @author wuhongjie
 * @date 2022/2/16 15:04
 */
@Data
public class ZoneManualAllocRequestDTO extends ManualAllocRequestDTO{
	private Long[] zoneIds;
}
