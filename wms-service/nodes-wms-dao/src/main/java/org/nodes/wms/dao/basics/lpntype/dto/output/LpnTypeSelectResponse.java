package org.nodes.wms.dao.basics.lpntype.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 容器类型下拉组件返回对象
 **/
@Data
public class LpnTypeSelectResponse implements Serializable {

	private static final long serialVersionUID = 840644742979285262L;

	/**
	 * 容器类型id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 容器类型编码
	 */
	private String code;
}
