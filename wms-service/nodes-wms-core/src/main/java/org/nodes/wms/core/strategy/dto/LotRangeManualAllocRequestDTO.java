package org.nodes.wms.core.strategy.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @author wuhongjie
 * @date 2022/2/16 15:04
 */
@Data
public class LotRangeManualAllocRequestDTO extends ManualAllocRequestDTO{
	private LocalDate[] lotRange;
}
