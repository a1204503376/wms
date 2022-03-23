package org.nodes.wms.statistics.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pengwei
 * @program WmsCore
 * @description 统计：出库单量
 * @create 20200402
 */
@Data
public class BillCountRltVO {

	/**
	 * 当日已完成订单总量
	 */
	@ApiModelProperty("当日已完成订单总量")
	private Integer billCountToday;

	/**
	 * 昨日已完成订单总量
	 */
	@ApiModelProperty("昨日已完成订单总量")
	private Integer billCountYesterday;

	/**
	 * 比率（正数为增长率，负数为下降率）
	 */
	@ApiModelProperty("比率（正数为增长率，负数为下降率）")
	private Integer rate;

	/**
	 * 当日订单包括的物品总数
	 */
	@ApiModelProperty("当日订单包括的物品总数")
	private Integer skuCountToday;
}
