package org.nodes.wms.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pengwei
 * @program WmsCore
 * @description 库存信息
 * @create 20200408
 */
@Data
public class StockSkuCountRltVO {

	/**
	 * 当前月物品总数
	 */
	@ApiModelProperty("当前月物品总数")
	private Integer currentSkuCount;
	/**
	 * 上个月物品总数
	 */
	@ApiModelProperty("上个月物品总数")
	private Integer lastSkuCount;
	/**
	 * 比率
	 */
	@ApiModelProperty("比率")
	private Integer rate;
}
