package org.nodes.wms.dao.putway.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.putway.dto.output.BoxDto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 呼叫Agv接收前端参数dto
 */
@Data
public class CallAgvRequest implements Serializable {
	private static final long serialVersionUID = 2699200295053404180L;
	/**
	 * 库位id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 库房id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 托盘号
	 */
	private String lpnCode;
	/**
	 * 数量
	 */
	private BigDecimal qty;
	/**
	 * 箱型
	 */
	private String lpnType;
	/**
	 * 箱码集合
	 */
	List<BoxDto> boxList;
}
