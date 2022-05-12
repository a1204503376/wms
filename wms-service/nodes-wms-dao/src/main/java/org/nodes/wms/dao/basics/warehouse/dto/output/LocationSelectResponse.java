package org.nodes.wms.dao.basics.warehouse.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 库位下拉选择 返回对象
 */
@Data
public class LocationSelectResponse {
	/**
	 * 库位主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;

	/**
	 * 库位编码
	 */
	private String locCode;

}
