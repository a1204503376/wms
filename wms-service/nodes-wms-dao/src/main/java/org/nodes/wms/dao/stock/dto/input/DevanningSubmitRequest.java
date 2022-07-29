package org.nodes.wms.dao.stock.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.stock.dto.output.DevanningStockResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 判断库存是否可移动请求对象
 */
@Data
public class DevanningSubmitRequest implements Serializable {
	private static final long serialVersionUID = 1855271549168247733L;
	/**
	 * 是否序列号管理 true为是 false为否
	 */
	private Boolean isSn;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 是否新箱码
	 */
	private Boolean newBoxCode;
	/**
	 * 序列号集合
	 */
	private List<String> serialNumberList;
	/**
	 * 目标库位编码
	 */
	private String locCode;
	/**
	 * 拆箱库存响应对象/请求过来
	 */
	private List<DevanningStockResponse> stockList;
}
