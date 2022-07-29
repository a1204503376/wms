package org.nodes.wms.dao.stock.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 判断库存是否是序列号管理
 */
@Data
public class FindAllSerialNumberManageRequest implements Serializable {
	private static final long serialVersionUID = 1855271549168247733L;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
}
