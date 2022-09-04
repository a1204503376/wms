
package org.nodes.wms.dao.putaway.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 上架策略执行条件实体类
 *
 * @author ch
 * @since 2020-02-14
 */
@Data
@TableName("st_instock_config")
@ApiModel(value = "InstockConfig对象", description = "上架策略执行条件")
public class StInstockConfig extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 上架策略执行条件ID
	 */
	@ApiModelProperty(value = "上架策略执行条件ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "ssic_id", type = IdType.ASSIGN_ID)
	private Long ssicId;
	/**
	 * 上架策略明细ID
	 */
	@ApiModelProperty(value = "上架策略明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssidId;
	/**
	 * 物品分类ID
	 */
	@ApiModelProperty(value = "物品分类ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuTypeId;
	/**
	 * 单据种类编码101：采购入库单 201：销售出库单
	 */
	@ApiModelProperty(value = "单据种类编码101：采购入库单 201：销售出库单")
	private String billTypeCd;


}
