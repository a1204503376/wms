
package org.nodes.wms.dao.basics.sku.entities;

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
import java.math.BigDecimal;

/**
 * 物品替代实体类
 */
@Data
@TableName("wms_sku_replace")
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "SkuReplace对象", description = "物品替代")
public class SkuReplace extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 物品替代ID
	 */
	@TableId(value = "wsrep_id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wsrepId;
	/**
	 * 物品ID
	 */
	@NotNull()
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	/**
	 * 包装ID
	 */
	@NotNull
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 计量单位ID
	 */
	@NotNull
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wsuId;
	/**
	 * 物品数量
	 */
	@NotNull
	private BigDecimal qty;
	/**
	 * 替代物品ID
	 */
	@NotNull
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wsrepSkuId;
	/**
	 * 替代包装ID
	 */
	@NotNull
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wsrepWspId;
	/**
	 * 替代计量单位ID
	 */
	@NotNull
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wsrepWsuId;
	/**
	 * 替代数量
	 */
	@NotNull
	private BigDecimal wsrepQty;

	/**
	 * 顺序
	 */
	private Integer wsrepOrder;
}
