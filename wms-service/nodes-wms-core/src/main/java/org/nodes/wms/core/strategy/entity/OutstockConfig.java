
package org.nodes.wms.core.strategy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 分配策略执行条件实体类
 *
 * @author pengwei
 * @since 2020-02-12
 */
@Data
@TableName("st_outstock_config")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OutstockConfig对象", description = "分配策略执行条件")
public class OutstockConfig extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 分配策略执行条件ID
	 */
	@ApiModelProperty(value = "分配策略执行条件ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "ssoc_id", type= IdType.ASSIGN_ID)
	private Long ssocId;
	/**
	 * 分配策略明细ID
	 */
	@ApiModelProperty(value = "分配策略明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssodId;
	/**
	 * 物品分类ID
	 */
	@ApiModelProperty(value = "物品分类ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuTypeId;
	/**
	 * 单据种类编码
	 */
	@ApiModelProperty(value = "单据种类编码")
	private String billTypeCd;


}
