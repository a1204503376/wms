
package org.nodes.wms.core.strategy.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 补货策略明细实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@TableName("st_relenishment_detail")
@ApiModel(value = "RelenishmentDetail对象", description = "RelenishmentDetail对象")
public class RelenishmentDetail extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 补货策略明细ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "补货策略明细ID")
	@TableId(value = "ssid_id", type = IdType.ASSIGN_ID)
	private Long ssidId;
	/**
	 * 补货策略ID
	 */
	@ApiModelProperty(value = "补货策略ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long ssiId;
	/**
	 * 补货层级
	 */
	@ApiModelProperty("补货层级")
	private Integer skuLevel;
	/**
	 * 从库区ID
	 */
	@ApiModelProperty(value = "从库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long fromZoneId;

	/**
	 * 至库区ID
	 */
	@ApiModelProperty(value = "至库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private Long toZoneId;


	/**
	 * 处理顺序
	 */
	@ApiModelProperty(value = "处理顺序")
	private Integer ssidProcOrder;

}
