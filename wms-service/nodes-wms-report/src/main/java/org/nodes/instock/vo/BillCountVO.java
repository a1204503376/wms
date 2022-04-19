package org.nodes.instock.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author pengwei
 * @program nodes-wms-report
 * @description 单量VO类
 * @create 20200402
 */
@Data
public class
BillCountVO {
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
