package org.nodes.wms.dao.putaway.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 按箱上架请求对象
 **/
@Data
public class FindPutawayDataByBoxCodeRequest implements Serializable {

	private static final long serialVersionUID = 5278548007801352620L;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 库房id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
}
