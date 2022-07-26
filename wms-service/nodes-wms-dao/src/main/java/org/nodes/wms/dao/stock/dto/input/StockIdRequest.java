package org.nodes.wms.dao.stock.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 库存id请求类
 **/
@Data
public class StockIdRequest implements Serializable {

	private static final long serialVersionUID = -2158102074376966654L;

	/**
	 * 库存id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
}
