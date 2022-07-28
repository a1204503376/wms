package org.nodes.wms.dao.stock.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * PC库存移动数据请求对象
 **/
@Data
public class StockPcMoveDetailRequest implements Serializable {

	private static final long serialVersionUID = 632861266851607319L;

	/**
	 * 数量
	 */
	private BigDecimal qty;

	/**
	 * 目标库位id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long targetLocId;

	/**
	 * 序列号
	 */
	private List<String> serialNumberList;
}
