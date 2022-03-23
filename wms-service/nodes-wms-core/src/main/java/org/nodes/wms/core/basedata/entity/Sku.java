
package org.nodes.wms.core.basedata.entity;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.Length;
import org.nodes.wms.core.common.entity.AttributeBase;

import javax.validation.constraints.NotNull;

/**
 * 物品实体类
 *
 * @author pengwei
 * @since 2019-12-09
 */
@Data
@TableName("wms_sku")
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Sku对象", description = "物品")
public class Sku extends AttributeBase {

	private static final long serialVersionUID = 1L;

	/**
	 * 物品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "sku_id", type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "物品ID")
	private Long skuId;
	/**
	 * 货主ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "货主ID", required = true)
	@NotNull
	private Long woId;
	/**
	 * 物品分类ID（默认其它）
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "物品分类ID（默认其它）", required = true)
	@NotNull
	private Long skuTypeId;
	/**
	 * 包装ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "包装ID", required = true)
	@NotNull
	private Long wspId;
	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;
	/**
	 * 物品批属性设置ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "物品批属性设置ID", required = true)
	private Long wslId;
	/**
	 * 物品批属性验证ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "物品批属性验证ID", required = true)
	@NotNull
	private Long wslvId;
	/**
	 * 物品编码
	 */
	@ApiModelProperty(value = "物品编码")
	@Length(max = 50, message = "字符长度不能超过50")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ApiModelProperty(value = "物品名称")
	@Length(max = 200, message = "字符长度不能超过200")
	private String skuName;
	/**
	 * 物品名称简称
	 */
	@ApiModelProperty(value = "物品名称简称")
	@Length(max = 100, message = "字符长度不能超过100")
	private String skuNameS;
	/**
	 * 物品说明
	 */
	@ApiModelProperty(value = "物品说明")
	@Length(max = 200, message = "字符长度不能超过200")
	private String skuRemark;
	/**
	 * 物品条码清单
	 */
	@ApiModelProperty(value = "物品条码清单")
	@Length(max = 200, message = "字符长度不能超过200")
	private String skuBarcodeList;
	/**
	 * 物品名称拼音
	 */
	@ApiModelProperty(value = "物品名称拼音")
	@Length(max = 100, message = "字符长度不能超过100")
	private String skuNamePy;
	/**
	 * 存货类型
	 */
	@ApiModelProperty(value = "存货类型")
//	@Length(max = 50, message = "字符长度不能超过50")
	private Integer skuStorageType;
	/**
	 * ABC分类
	 */
	@ApiModelProperty(value = "ABC分类")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer abc;
	/**
	 * 体积
	 */
	@ApiModelProperty(value = "体积")
	private BigDecimal skuVolume;
	/**
	 * 净重
	 */
	@ApiModelProperty(value = "净重")
	private BigDecimal skuNetWeight;
	/**
	 * 皮重
	 */
	@ApiModelProperty(value = "皮重")
	private BigDecimal skuTareWeight;
	/**
	 * 毛重
	 */
	@ApiModelProperty(value = "毛重")
	private BigDecimal skuGrossWeight;
	/**
	 * 温度上限
	 */
	@ApiModelProperty(value = "温度上限")
	private BigDecimal skuTempUpperLimit;
	/**
	 * 温度下限
	 */
	@ApiModelProperty(value = "温度下限")
	private BigDecimal skuTempLowerLimit;
	/**
	 * 保质期有无
	 */
	@ApiModelProperty(value = "保质期有无")
	@Length(max = 50, message = "字符长度不能超过50")
	private String qualityType;
	/**
	 * 几天内截至
	 */
	@ApiModelProperty(value = "几天内截至")
	private Integer qualityBy;
	/**
	 * 几天内交货
	 */
	@ApiModelProperty(value = "几天内交货")
	private Integer qualityTransport;
	/**
	 * 几天内最佳
	 */
	@ApiModelProperty(value = "几天内最佳")
	private Integer qualityBest;
	/**
	 * 保质期日期类型
	 */
	@ApiModelProperty(value = "保质期日期类型")
//	@Length(max = 50, message = "字符长度不能超过50")
	private Integer qualityDateType;
	/**
	 * 保质期小时数
	 */
	@ApiModelProperty(value = "保质期小时数")
	private Integer qualityHours;
	/**
	 * 批次号生成掩码
	 */
	@ApiModelProperty(value = "批次号生成掩码")
	@Length(max = 200, message = "字符长度不能超过200")
	private String skuLotMask;
	/**
	 * 子级物品
	 */
	@ApiModelProperty(value = "子级物品")
	@Length(max = 1, message = "字符长度不能超过1")
	private String isSublevel;
	/**
	 * 序列号管理（1：序列号管理  0：非序列号管理）
	 */
	@ApiModelProperty(value = "序列号管理（1：序列号管理  0：非序列号管理）")
	private Integer isSn;
}
