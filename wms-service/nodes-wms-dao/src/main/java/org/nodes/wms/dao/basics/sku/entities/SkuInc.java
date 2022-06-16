
package org.nodes.wms.dao.basics.sku.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 物品供应商关联实体类
 */
@Data
@TableName("wms_sku_inc")
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SkuInc extends TenantEntity {

	private static final long serialVersionUID = -4428260816464273239L;

	/**
	 * 物品供应商关联ID
	 */
	@TableId(value = "wssup_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wssupId;
	/**
	 * 物品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	/**
	 * 供应商企业ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long peId;
	/**
	 * 包装ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 供应商优先顺序
	 */
	private Integer wssupOrder;

	/**
	 * 物品名称（针对当前企业的名称）
	 */
	private String skuName;

	/**
	 * 扩展字段1
	 */
	private String attribute1;

	/**
	 * 扩展字段2
	 */
	private String attribute2;

	/**
	 * 扩展字段3
	 */
	private String attribute3;

	/**
	 * 扩展字段4
	 */
	private String attribute4;

	/**
	 * 扩展字段5
	 */
	private String attribute5;

	/**
	 * 扩展字段6
	 */
	private String attribute6;

	/**
	 * 扩展字段7
	 */
	private String attribute7;

	/**
	 * 扩展字段8
	 */
	private String attribute8;

	/**
	 * 扩展字段9
	 */
	private String attribute9;

	/**
	 * 扩展字段10
	 */
	private String attribute10;

}
