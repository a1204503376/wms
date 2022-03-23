package org.nodes.wms.core.basedata.dto;

import lombok.Data;
import org.nodes.wms.core.basedata.enums.SkuLogTypeEnum;

import javax.validation.constraints.NotNull;

/**
 * @author pengwei
 * @program WmsCore
 * @description 物品日志DTO
 * @since 2020-12-09
 */
@Data
public class SkuLogDTO {

	/**
	 * 物品ID
	 */
	@NotNull(message = "参数 skuId 不能为空")
	private Long skuId;
	/**
	 * 日志类型枚举
	 */
	@NotNull(message = "参数 skuLogTypeEnum 不能为空")
	private SkuLogTypeEnum skuLogTypeEnum;
}
