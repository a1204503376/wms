package org.nodes.wms.dao.basics.zone.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 库区下拉选择 返回对象
 */
@Data
public class ZoneSelectResponse implements Serializable {

	private static final long serialVersionUID = -8977626550408849729L;

	/**
	 * 库区主键ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long zoneId;

	/**
	 * 库区编码
	 */
	private String zoneCode;

	/**
	 * 库区名称
	 */
	private String zoneName;
}
