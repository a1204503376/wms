
package org.nodes.wms.core.allot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.nodes.wms.core.common.entity.AttributeBase;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 调拨明细表实体类
 *
 * @author Wangjw
 * @since 2020-02-10
 */
@Data
@TableName("wms_allot_detail")
@ApiModel(value = "AllotDetail对象", description = "调拨明细表")
public class AllotDetail extends AttributeBase implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 调拨单明细ID
	 */
	@TableId(value = "allot_detail_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "调拨单明细ID")
	private Long allotDetailId;
	/**
	 * 调拨单ID
	 */
	@NotNull(message = "调拨单ID不能为空")
	@Length(min = 1, max = 20, message = "最小长度为1位，最大长度为20位")
	@ApiModelProperty(value = "调拨单ID")
	private Long allotBillId;
	/**
	 * 调拨单行号
	 */
	@NotNull(message = "调拨单行号不能为空")
	@Length(min = 1, max = 60, message = "最小长度为1位，最大长度为60位")
	@ApiModelProperty(value = "调拨单行号")
	private String allotLineNo;
	/**
	 * 物品ID
	 */
	@NotNull(message = "物品ID不能为空")
	@Length(min = 1, max = 20, message = "最小长度为1位，最大长度为20位")
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "物品ID")
	private Long skuId;
	/**
	 * 包装ID
	 */
	@NotNull(message = "包装ID不能为空")
	@Length(min = 1, max = 20, message = "最小长度为1位，最大长度为20位")
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "包装ID")
	private Long wspId;
	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级")
	private Integer skuLevel;
	/**
	 * 物品编码
	 */
	@NotNull(message = "物品编码不能为空")
	@Length(min = 1, max = 50, message = "最小长度为1位，最大长度为50位")
	@ApiModelProperty(value = "物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@NotNull(message = "物品名称不能为空")
	@Length(min = 1, max = 200, message = "最小长度为1位，最大长度为200位")
	@ApiModelProperty(value = "物品名称")
	private String skuName;
	/**
	 * 规格
	 */
	@Length(max = 50, message = "最大长度为50位")
	@ApiModelProperty(value = "规格")
	private String skuSpec;
	/**
	 * 换算倍率
	 */
	@NotNull(message = "换算倍率不能为空")
	@ApiModelProperty(value = "换算倍率")
	private Integer convertQty;
	/**
	 * 计量单位编码
	 */
	@NotNull(message = "计量单位编码不能为空")
	@Length(min = 1, max = 30, message = "最小长度为1位，最大长度为30位")
	@ApiModelProperty(value = "计量单位编码")
	private String umCode;
	/**
	 * 计量单位名称
	 */
	@NotNull(message = "计量单位名称不能为空")
	@Length(min = 1, max = 50, message = "最小长度为1位，最大长度为50位")
	@ApiModelProperty(value = "计量单位名称")
	private String umName;
	/**
	 * 基础计量单位编码
	 */
	@NotNull(message = "基础计量单位编码不能为空")
	@Length(min = 1, max = 30, message = "最小长度为1位，最大长度为30位")
	@ApiModelProperty(value = "基础计量单位编码")
	private String baseUmCode;
	/**
	 * 基础计量单位名称
	 */
	@NotNull(message = "基础计量单位名称不能为空")
	@Length(min = 1, max = 50, message = "最小长度为1位，最大长度为50位")
	@ApiModelProperty(value = "基础计量单位名称")
	private String baseUmName;
	/**
	 * 计划调拨数量
	 */
	@NotNull(message = "计划调拨数量不能为空")
	@ApiModelProperty(value = "计划调拨数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal allotPlanQty;
	/**
	 * 实际调出数量
	 */
	@ApiModelProperty(value = "实际调出数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal allotOutQty;
	/**
	 * 实际调入数量
	 */
	@ApiModelProperty(value = "实际调入数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal allotInQty;
	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;
}
