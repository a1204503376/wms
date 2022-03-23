package org.nodes.wms.core.outstock.sales.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * author: pengwei
 * date: 2021/6/3 14:27
 * description: SalesHeaderQueryDTO
 */
@Data
@ApiModel(value = "销售单查询条件")
public class SalesHeaderQueryDTO {
	/**
	 * 单据编号
	 */
	@ApiModelProperty("单据编号")
	private String soBillNo;

	/**
	 * 单据类型
	 */
	@ApiModelProperty("单据类型")
	private String billTypeCd;

	/**
	 * 单据状态
	 */
	@ApiModelProperty("单据状态")
	private Integer soBillState;
	/**
	 * T100单号
	 */
	@ApiModelProperty("T100单号")
	private String orderNo;
	/**
	 * 发货方式
	 */
	@ApiModelProperty(value = "发货方式")
	private Integer outstockType;

	/**
	 * 物品关键词
	 */
	@ApiModelProperty("物品关键词")
	private String skuKeyword ;
	/**
	 * 货主ID
	 */
	@ApiModelProperty("货主ID")
	private String woId;

	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库房ID")
	private Long whId;

	/**
	 * 部门ID
	 */
	@ApiModelProperty(value = "部门ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long deptId;

	/**
	 * 客户关键词
	 */
	@ApiModelProperty("客户关键词")
	private String cKeyword;

	/**
	 * 会计科目编码
	 */
	@ApiModelProperty(value = "会计科目编码")
	private String saccCode;

	/**
	 * 会计科目名称
	 */
	@ApiModelProperty("会计科目名称")
	private String saccName;

	/**
	 * 客户名称
	 */
	@JsonProperty("cName")
	@ApiModelProperty(value = "客户名称")
	private String cName;

	/**
	 * 物流名称
	 */
	@ApiModelProperty(value = "物流名称")
	private String expressName;

	/**
	 * 单据下发时间范围
	 */
	@ApiModelProperty("单据下发时间范围")
	private String arriveRange;

	/**
	 * 单据下发开始时间
	 */
	@ApiModelProperty("单据下发开始时间")
	private String arriveStart;
	/**
	 * 单据下发结束时间
	 */
	@ApiModelProperty("单据下发结束时间")
	private String arriveEnd;

	/**
	 * 单据发货时间范围
	 */
	@ApiModelProperty("单据发货时间范围")
	private String outStockRange;

	/**
	 * 单据发货开始时间
	 */
	@ApiModelProperty("单据发货开始时间")
	private String outStockStart;
	/**
	 * 单据发货结束时间
	 */
	@ApiModelProperty("单据发货结束时间")
	private String outStockEnd;

	/**
	 * 单据完成时间范围
	 */
	@ApiModelProperty("单据完成时间范围")
	private String finishRange;

	/**
	 * 单据完成开始时间
	 */
	@ApiModelProperty("单据完成开始时间")
	private String finishStart;
	/**
	 * 单据完成结束时间
	 */
	@ApiModelProperty("单据完成结束时间")
	private String finishEnd;

	/**
	 * 发货完成时间范围
	 */
	@ApiModelProperty("发货完成时间范围")
	private String transportRange;

	/**
	 * 发货完成开始时间
	 */
	@ApiModelProperty("发货完成开始时间")
	private String transportStart;
	/**
	 * 发货完成结束时间
	 */
	@ApiModelProperty("发货完成结束时间")
	private String transportEnd;

	/**
	 * 过账人员
	 */
	@ApiModelProperty("过账人员")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long postUser;
	/**
	 * 过账时间
	 */
	@ApiModelProperty("过账时间")
	private String postTime;
	/**
	 * 过账方式
	 */
	@ApiModelProperty("过账方式")
	private Integer postState;
	/**
	 * 同步状态
	 */
	@ApiModelProperty(value = "同步状态")
	private Integer syncState;
}
