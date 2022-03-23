
package org.nodes.wms.core.basedata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.tenant.mp.TenantEntity;

import javax.validation.constraints.NotNull;

/**
 * 物品出库设置实体类
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Data
@TableName("wms_sku_outstock")
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "SkuOutstock对象", description = "物品出库设置")
public class SkuOutstock extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 物品出库设置ID
	 */
	@ApiModelProperty(value = "物品出库设置ID")
	@TableId(value = "wso_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wsoId;
	/**
	 * 物品ID
	 */
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull(message = "物品不能为空！")
	private Long skuId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull(message = "库房不能为空！")
	private Long whId;
	/**
	 * 货主ID
	 */
	@ApiModelProperty(value = "货主ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull(message = "货主不能为空！")
	private Long woId;
	/**
	 * 分配策略ID
	 */
	@ApiModelProperty(value = "分配策略ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssoId;
	/**
	 * 波次划分策略ID
	 */
	@ApiModelProperty(value = "波次划分策略ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long sspId;
	/**
	 * 周转方式1
	 */
	@ApiModelProperty(value = "周转方式1")
	private Integer turnoverType1;
	/**
	 * 周转方式信息1
	 */
	@ApiModelProperty(value = "周转方式信息1")
	private Integer turnoverItem1;
	/**
	 * 周转方式2
	 */
	@ApiModelProperty(value = "周转方式2")
	private Integer turnoverType2;
	/**
	 * 周转方式信息2
	 */
	@ApiModelProperty(value = "周转方式信息2")
	private Integer turnoverItem2;
	/**
	 * 周转方式3
	 */
	@ApiModelProperty(value = "周转方式3")
	private Integer turnoverType3;
	/**
	 * 周转方式信息3
	 */
	@ApiModelProperty(value = "周转方式信息3")
	private Integer turnoverItem3;
	/**
	 * 默认出库货位
	 */
	@ApiModelProperty(value = "默认出库货位")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 默认出库包装ID
	 */
	@ApiModelProperty(value = "默认出库包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 默认出库包装层级
	 */
	@ApiModelProperty(value = "默认出库包装层级")
	private Integer skuLevel;
	/**
	 * 质检库区ID
	 */
	@ApiModelProperty(value = "质检库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long qcZoneId;
	/**
	 * 质检库位ID
	 */
	@ApiModelProperty(value = "质检库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long qcLocId;
	/**
	 * 不合格品库区ID
	 */
	@ApiModelProperty(value = "不合格品库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long unqualiflyZoneId;
	/**
	 * 不合格品库位ID
	 */
	@ApiModelProperty(value = "不合格品库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long unqualiflyLocId;
	/**
	 * 优先出库库区ID
	 */
	@ApiModelProperty(value = "优先出库库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long firstSoZoneId;
	/**
	 * 是否为系统级数据(0否，1是）
	 */
	@ApiModelProperty("是否为系统more(0否，1是）")
	private Integer isDefault;
}
