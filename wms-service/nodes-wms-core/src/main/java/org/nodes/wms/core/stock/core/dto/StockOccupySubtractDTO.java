package org.nodes.wms.core.stock.core.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;

/**
 * @author pengwei
 * @program WmsCore
 * @description 库存占用扣减参数类
 * @create 20200224
 */
@Data
public class StockOccupySubtractDTO {

	/**
	 * 事务ID(通常为：波次ID，入库单表头ID，盘点单表头ID等)
	 */
	@ApiModelProperty("事务ID")
	private Long transId;

	/**
	 * 占用类型
	 *
	 * @see org.nodes.wms.core.stock.core.enums.StockOccupyTypeEnum
	 */
	@ApiModelProperty("占用类型")
	private Integer occupyType;

	/**
	 * 发货单ID
	 */
	@ApiModelProperty("发货单ID")
	private Long soBillId;

	/**
	 * 出库单明细ID
	 */
	@ApiModelProperty("出库单明细ID")
	private Long soDetailId;

	/**
	 * 拣货计划ID
	 */
	@ApiModelProperty("拣货计划ID")
	private Long pickPlanId;

	/**
	 * 盘点单记录ID
	 */
	@ApiModelProperty("盘点单记录ID")
	private Long wcrId;

	/**
	 * 扣减库存占用数量
	 * <li>小于等于零 或者 为null 的情况下表示全部扣减</li>
	 * <li>大于零则扣减指定数量</li>
	 */
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal qty;
}
