package org.nodes.wms.core.outstock.sales.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.outstock.sales.entity.SalesDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;

/**
 * 发货单明细
视图实体类
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SalesDetailVO对象", description = "发货单明细 ")
public class SalesDetailVO extends SalesDetail {
	private static final long serialVersionUID = 1L;

	/**
	 * 单据状态名称
	 */
	private String detailStatusName;

	/**
	 * 包装明细ID
	 */
	@ApiModelProperty("包装明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspdId;

	/**
	 * 包装名称
	 */
	private String wspName;
	/**
	 * 包装层级描述
	 */
	private String skuLevelName;
	/**
	 * 是否序列号管理
	 */
	private String isSn;

	/**
	 * 计划数量
	 */
	@ApiModelProperty("计划数量")
	private String planQtyName;

	/**
	 * 实际数量
	 */
	@ApiModelProperty("实际数量")
	private String scanQtyName;

	/**
	 * 剩余数量
	 */
	@ApiModelProperty("剩余数量")
	private String surplusQtyName;

	/**
	 * 物品类型
	 */
	@ApiModelProperty("物品类型")
	private String skuType;
}
