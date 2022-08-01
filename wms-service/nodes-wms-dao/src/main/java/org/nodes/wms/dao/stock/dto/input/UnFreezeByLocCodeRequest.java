package org.nodes.wms.dao.stock.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 库存冻结请求对象
 */
@Data
public class UnFreezeByLocCodeRequest implements Serializable {
	private static final long serialVersionUID = 178752972775816389L;
	/**
     * 库位编码
	 */
	private String locCode;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
}
