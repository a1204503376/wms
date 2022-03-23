
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
 * 分配策略货源库区实体类
 *
 * @author pengwei
 * @since 2020-02-12
 */
@Data
@TableName("st_outstock_zone_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "OutstockZoneDetail对象", description = "分配策略货源库区")
public class OutstockZoneDetail extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 分配策略货源库区ID
	 */
	@ApiModelProperty(value = "分配策略货源库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "stozd_id", type= IdType.ASSIGN_ID)
	private Long stozdId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 分配策略明细ID
	 */
	@ApiModelProperty(value = "分配策略明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssodId;
	/**
	 * 库区分类
	 */
	@ApiModelProperty("库区分类")
	private Integer zoneType;
	/**
	 * 货源库区
	 */
	@ApiModelProperty(value = "货源库区")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long zoneId;
	/**
	 * 层级
	 */
	@ApiModelProperty(value = "层级")
	private Integer skuLevel;


}
