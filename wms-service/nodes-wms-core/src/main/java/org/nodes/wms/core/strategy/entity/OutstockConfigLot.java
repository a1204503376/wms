
package org.nodes.wms.core.strategy.entity;

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
 * 分配策略批属性设定 实体类
 *
 * @author zhongls
 * @since 2019-12-10
 */
@Data
@TableName("st_outstock_config_lot")
@ApiModel(value = "OutstockConfigLot对象", description = "OutstockConfigLot对象")
public class OutstockConfigLot extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 分配策略批属性设定ID
	 */
	@ApiModelProperty(value = "分配策略批属性设定ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "ssocl_id", type = IdType.ASSIGN_ID)
	private Long ssoclId;
	/**
	 * 分配策略明细ID
	 */
	@ApiModelProperty(value = "分配策略明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssodId;
	/**
	 * 批属性索引
	 */
	@ApiModelProperty(value = "批属性索引")
	private Integer skuLotNumber;
	/**
	 * 批属性操作符
	 */
	@ApiModelProperty(value = "批属性操作符")
	private Integer skuLotOperation;
	/**
	 * 批属性设定值
	 */
	@ApiModelProperty(value = "批属性设定值")
	private String skuLotVal1;
	/**
	 * 批属性设定值
	 */
	@ApiModelProperty(value = "批属性设定值")
	private String skuLotVal2;
	/**
	 * 批属性设定值
	 */
	@ApiModelProperty(value = "批属性设定值")
	private String skuLotVal3;

}
