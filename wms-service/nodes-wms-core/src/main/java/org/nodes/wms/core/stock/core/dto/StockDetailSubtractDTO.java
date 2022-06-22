package org.nodes.wms.core.stock.core.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.tool.utils.Func;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * author: pengwei
 * date: 2021/11/24 16:44
 * description: 库存明细扣减DTO
 */
@Data
public class StockDetailSubtractDTO {
	/**
	 * 库存id
	 */
	@NotNull
	@ApiModelProperty("库存id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 库存对象
	 */
	private Stock stock;
	/**
	 * 容器编码
	 */
	@ApiModelProperty("容器编码")
	private String lpnCode = StringPool.EMPTY;
	/**
	 * 箱号
	 */
	@ApiModelProperty("箱号")
	private String boxCode = StringPool.EMPTY;
	/**
	 * 下架数量
	 */
	@NotNull
	@Min(value = 1)
	@ApiModelProperty("下架数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal pickQty;
	/**
	 * 订单明细id
	 */
	@ApiModelProperty("订单明细id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long billDetailId;
	/**
	 * 订单id
	 */
	@ApiModelProperty("订单id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 波次id
	 */
	@ApiModelProperty("波次id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wellenId;
	/**
	 * 序列号集合
	 */
	@ApiModelProperty("序列号集合")
	private List<String> serialList;
	/**
	 * 系统日志id
	 */
	@ApiModelProperty("系统日志id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long systemProcId;

	public String getBoxCode() {
		return Func.isNull(boxCode) ? StringPool.EMPTY : boxCode;
	}

	public String getLpnCode() {
		return Func.isNull(lpnCode) ? StringPool.EMPTY : lpnCode;
	}
}
