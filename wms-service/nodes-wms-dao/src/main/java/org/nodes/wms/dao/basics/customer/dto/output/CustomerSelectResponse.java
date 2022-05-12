package org.nodes.wms.dao.basics.customer.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 客户下拉选择 返回对象
 */
@Data
public class CustomerSelectResponse {
	/**
	 * 客户主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 客户编码
	 */
	private String code;

	/**
	 * 客户名称
	 */
	private String name;
}
