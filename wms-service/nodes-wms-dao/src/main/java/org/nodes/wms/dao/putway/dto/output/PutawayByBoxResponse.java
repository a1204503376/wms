package org.nodes.wms.dao.putway.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 按箱上架请求对象
 **/
@Data
public class PutawayByBoxResponse implements Serializable {
	private static final long serialVersionUID = 6229009210855257224L;
	/**
	 * 库存ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private List<Long> stockId;
	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 总数
	 */
	private BigDecimal qty;

	/**
	 * 托盘号
	 */
	private String lpnCode;
}
