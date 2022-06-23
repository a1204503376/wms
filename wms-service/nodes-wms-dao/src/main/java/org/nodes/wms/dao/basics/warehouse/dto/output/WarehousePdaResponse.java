package org.nodes.wms.dao.basics.warehouse.dto.output;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;


/**
 * 库房 下拉框 返回对象
 */
@Data
public class WarehousePdaResponse {
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 库房编码
	 */
	private String whCode;
	/**
	 * 库房名称
	 */
	private String whName;
}
