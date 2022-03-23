package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 按箱移动验证返回用VO
 * @Author zx
 * @Date 2020/7/31
 **/
@Data
public class StockMoveByBoxVerifyVO {

	/**
	 * 库存ID
	 */
	@ApiModelProperty("库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	public StockMoveByBoxVerifyVO(Long stockId) {
		this.stockId = stockId;
	}
}
