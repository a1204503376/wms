package org.nodes.wms.core.basedata.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springblade.core.mp.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.tenant.mp.TenantEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 物料清单实体类
 *
 * @author NodeX
 * @since 2021-05-19
 */
@Data
@TableName("wms_sku_bom")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SkuBom对象", description = "物料清单")
public class SkuBom extends TenantEntity {

	private static final long serialVersionUID = 1L;
	/**
	 * 物品清单ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "ID")
	private Long id;
	/**
	 * 货主ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "货主ID")
	@NotNull
	private Long woId;
	/**
	 * 货主编码
	 */
	@ApiModelProperty(value = "货主编码")
	private String ownerCode;
	/**
	 * 货主名称
	 */
	@ApiModelProperty(value = "货主名称")
	private String ownerName;
	/**
	 * 物品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "物品ID")
	@NotNull
	private Long skuId;
	/**
	 * 物品编码
	 */
	@ApiModelProperty(value = "物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ApiModelProperty(value = "物品名称")
	private String skuName;
	/**
	 * 主单位编码
	 */
	@ApiModelProperty(value = "主单位编码")
	private String wsuCode;
	/**
	 * 主单位名称
	 */
	@ApiModelProperty(value = "主单位名称")
	private String wsuName;
	/**
	 * 组合物品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "组合物品ID")
	@NotNull
	private Long joinSkuId;
	/**
	 * 组合物品编码
	 */
	@ApiModelProperty(value = "组合物品编码")
	private String joinSkuCode;
	/**
	 * 组合物品名称
	 */
	@ApiModelProperty(value = "组合物品名称")
	private String joinSkuName;
	/**
	 * 组合数量
	 */
	@ApiModelProperty(value = "组合数量")
	private Integer qty;
	/**
	 * 组合单位编码
	 */
	@ApiModelProperty(value = "组合单位编码")
	private String joinWsuCode;
	/**
	 * 组合单位名称
	 */
	@ApiModelProperty(value = "组合单位名称")
	private String joinWsuName;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;


}
