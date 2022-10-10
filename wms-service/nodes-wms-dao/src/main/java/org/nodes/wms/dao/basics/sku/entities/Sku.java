
package org.nodes.wms.dao.basics.sku.entities;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springblade.core.tenant.mp.TenantEntity;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 物品实体类
 */
@Data
@TableName("wms_sku")
public class Sku extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 物品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "sku_id", type = IdType.ASSIGN_ID)
	private Long skuId;
	/**
	 * 货主ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull
	private Long woId;
	/**
	 * 物品分类ID（默认其它）
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull
	private Long skuTypeId;
	/**
	 * 包装ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull
	private Long wspId;
	/**
	 * 包装名称
	 */
	private String wspName;
	/**
	 * 物品批属性设置ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wslId;
	/**
	 * 物品批属性验证ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull
	private Long wslvId;
	/**
	 * 物品编码
	 */
	@Length(max = 50, message = "字符长度不能超过50")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@Length(max = 200, message = "字符长度不能超过200")
	private String skuName;
	/**
	 * 物品名称简称
	 */
	@Length(max = 100, message = "字符长度不能超过100")
	private String skuNameS;
	/**
	 * 自定义字段1 (天宜：箱型)
	 */
	@Length(max = 255, message = "字符长度不能超过255")
	private String udf1;

	/**
	 * 自定义字段2
	 */
	@Length(max = 255, message = "字符长度不能超过255")
	private String udf2;

	/**
	 * 自定义字段3
	 */
	@Length(max = 255, message = "字符长度不能超过255")
	private String udf3;

	/**
	 * 自定义字段4
	 */
	@Length(max = 255, message = "字符长度不能超过255")
	private String udf4;

	/**
	 * 自定义字段5
	 */
	@Length(max = 255, message = "字符长度不能超过255")
	private String udf5;
	/**
	 * 物品规格型号
	 */
	private String skuSpec;
	/**
	 * 物品说明
	 */
	@Length(max = 200, message = "字符长度不能超过200")
	private String skuRemark;
	/**
	 * 物品条码清单
	 */
	@Length(max = 200, message = "字符长度不能超过200")
	private String skuBarcodeList;
	/**
	 * 物品名称拼音
	 */
	@Length(max = 100, message = "字符长度不能超过100")
	private String skuNamePy;
	/**
	 * 存货类型
	 */
//	@Length(max = 50, message = "字符长度不能超过50")
	private Integer skuStorageType;
	/**
	 * ABC分类
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Integer abc;
	/**
	 * 体积
	 */
	private BigDecimal skuVolume;
	/**
	 * 净重
	 */
	private BigDecimal skuNetWeight;
	/**
	 * 皮重
	 */
	private BigDecimal skuTareWeight;
	/**
	 * 毛重
	 */
	private BigDecimal skuGrossWeight;
	/**
	 * 温度上限
	 */
	private BigDecimal skuTempUpperLimit;
	/**
	 * 温度下限
	 */
	private BigDecimal skuTempLowerLimit;

	/**
	 * 物料标准价
	 */
	private BigDecimal skuPrice;
	/**
	 * 保质期有无
	 */
	@Length(max = 50, message = "字符长度不能超过50")
	private String qualityType;
	/**
	 * 几天内截至
	 */
	private Integer qualityBy;
	/**
	 * 几天内交货
	 */
	private Integer qualityTransport;
	/**
	 * 几天内最佳
	 */
	private Integer qualityBest;
	/**
	 * 保质期日期类型
	 */
//	@Length(max = 50, message = "字符长度不能超过50")
	private Integer qualityDateType;
	/**
	 * 保质期小时数
	 */
	private Integer qualityHours;
	/**
	 * 批次号生成掩码
	 */
	@Length(max = 200, message = "字符长度不能超过200")
	private String skuLotMask;
	/**
	 * 子级物品
	 */
	@Length(max = 1, message = "字符长度不能超过1")
	private String isSublevel;
	/**
	 * 序列号管理（1：序列号管理  0：非序列号管理）
	 */
	private Integer isSn;

	/**
	 * 是否含有bom
	 */
	private Integer hasBom;

	/**
	 * 存货类型
	 */
	private Integer inventoryType;

	/**
	 * 总货架寿命
	 */
	private String totalShelf;

	/**
	 * 附件
	 */
	private String appendix;

	/**
	 * 安全库存
	 */
	private BigDecimal safeStock;

	/**
	 * 最低储量
	 */
	private BigDecimal minimumReserves;
}
