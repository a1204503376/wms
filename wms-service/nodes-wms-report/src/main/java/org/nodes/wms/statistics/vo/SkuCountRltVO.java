package org.nodes.wms.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @program: WmsCore
 * @description: 物品缺货量
 * @author: pengwei
 * @create: 2020-04-15 10:39
 **/
@Data
public class SkuCountRltVO {

	/**
	 * 缺货物品数量
	 */
	@ApiModelProperty("缺货物品数量")
	private BigDecimal count = BigDecimal.ZERO;

	/**
	 * 物品缺货总数
	 */
	@ApiModelProperty("物品缺货总数")
	private BigDecimal qty = BigDecimal.ZERO;
}
