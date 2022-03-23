package org.nodes.wms.core.outstock.so.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.outstock.so.entity.PickPlan;

/**
 * @author pengwei
 * @program WmsCore
 * @description 保存拣货计划明细参数DTO实体类
 * @since 2020-11-05
 */
@Data
public class SavePickPlanDetailDTO extends PickPlan {

	/**
	 * 订单ID
	 */
	@ApiModelProperty("订单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

	/**
	 * 订单明细ID
	 */
	@ApiModelProperty("订单明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soDetailId;
}
