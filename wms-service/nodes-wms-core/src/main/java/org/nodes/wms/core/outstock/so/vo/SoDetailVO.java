
package org.nodes.wms.core.outstock.so.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.outstock.so.entity.SoDetail;

/**
 * 视图实体类
 *
 * @author NodeX
 * @since 2020-02-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "DetailVO对象", description = "DetailVO对象")
public class SoDetailVO extends SoDetail  {
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
}
