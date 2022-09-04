
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
 * 上架策略批属性设定实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@TableName("st_instock_config_lot")
@ApiModel(value = "InstockConfigLot对象", description = "InstockConfigLot对象")
public class StInstockConfigLot extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 上架策略批属性设定ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "上架策略批属性设定ID")
	@TableId(value = "ssicl_id", type = IdType.ASSIGN_ID)

	private Long ssiclId;
	/**
	 * 上架策略明细ID
	 */
	@ApiModelProperty(value = "上架策略明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssidId;
	/**
	 * 批属性索引 1~30
	 */
	@ApiModelProperty(value = "批属性索引 1~30")
	private Integer skuLotNumber;
	/**
	 * 批属性操作符 0(=)，1(>)，2(<)，3(≥)，4(≤)，5(like), 6(不等于空)
	 */
	@ApiModelProperty(value = "批属性操作符 0(=)，1(>)，2(<)，3(≥)，4(≤)，5(like), 6(不等于空)")
	private int skuLotOperation;
	/**
	 * 批属性设定值 批属性操作符=1~5时，至少一个不为空
	 */
	@ApiModelProperty(value = "批属性设定值 批属性操作符=1~5时，至少一个不为空")
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
