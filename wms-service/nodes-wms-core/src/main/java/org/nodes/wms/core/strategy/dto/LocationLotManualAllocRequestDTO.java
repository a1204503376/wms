package org.nodes.wms.core.strategy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wuhongjie
 * @date 2022/2/16 15:04
 */
@Data
public class LocationLotManualAllocRequestDTO extends ManualAllocRequestDTO{
	private String lotNumbers;
	private String locCodes;
}
