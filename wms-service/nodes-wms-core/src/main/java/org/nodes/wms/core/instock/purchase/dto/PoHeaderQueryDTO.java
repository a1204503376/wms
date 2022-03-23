package org.nodes.wms.core.instock.purchase.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.instock.purchase.entity.PoHeader;

/**
 * author: pengwei
 * date: 2021/6/1 13:07
 * description: PoHeaderQueryDTO
 */
@Data
@ApiModel(value = "采购订单查询条件", description = "采购订单查询条件")
public class PoHeaderQueryDTO extends PoHeader {

	/**
	 * 预计到货时间范围
	 */
	@ApiModelProperty("预计到货时间范围")
	private String scheduledArrivalRange;

	/**
	 * 实际到货时间范围
	 */
	@ApiModelProperty("实际到货时间范围")
	private String actualArrivalRange;

	/**
	 * 单据完成时间范围
	 */
	@ApiModelProperty("单据完成时间范围")
	private String finishRange;
}
