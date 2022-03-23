
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
 * 物品入库设置实体类
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Data
@TableName("wms_sku_instock")
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "SkuInstock对象", description = "物品入库设置")
public class SkuInstock extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 物品入库设置ID
	 */
	@ApiModelProperty(value = "物品入库设置ID")
	@TableId(value = "wsi_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wsiId;
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
	 * 上架策略ID
	 */
	@ApiModelProperty(value = "上架策略ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssiId;
	/**
	 * 上架策略执行类型
	 */
	@ApiModelProperty(value = "上架策略执行类型")
	private Integer ssiProcType;
	/**
	 * 上架库区ID
	 */
	@ApiModelProperty(value = "上架库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long zoneId;
	/**
	 * 上架库位ID
	 */
	@ApiModelProperty(value = "上架库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 射频默认收货包装ID
	 */
	@ApiModelProperty(value = "射频默认收货包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 射频默认收货层级
	 */
	@ApiModelProperty(value = "射频默认收货层级")
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
	 * 退货库区ID
	 */
	@ApiModelProperty(value = "退货库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long returnZoneId;
	/**
	 * 退货库位ID
	 */
	@ApiModelProperty(value = "退货库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long returnLocId;
	/**
	 * 拣货库区ID
	 */
	@ApiModelProperty(value = "拣货库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long pickZoneId;
	/**
	 * 拣货库位ID
	 */
	@ApiModelProperty(value = "拣货库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long pickLocId;
	/**
	 * 库房低储
	 */
	@ApiModelProperty(value = "库房低储")
	private Integer lowStorage;
	/**
	 * 库房高储
	 */
	@ApiModelProperty(value = "库房高储")
	private Integer highStorage;

	/**
	 * 是否为系统级数据(0否，1是）
	 */
	@ApiModelProperty("是否为系统more(0否，1是）")
	private Integer isDefault;
}
