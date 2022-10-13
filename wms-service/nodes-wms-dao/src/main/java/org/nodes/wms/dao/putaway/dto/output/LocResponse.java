package org.nodes.wms.dao.putaway.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

/**
 * 呼叫AGV返回前端库位信息dto
 */
@Data
public class LocResponse {
	/**
	 * 库位id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 前端展示的库位编码
	 */
	private String locCodeView;

	/**
	 * 库位是否为空
	 */
	private Boolean isEmpty;

	/**
	 * C1 C2标识字段
	 * isCBifurcate = 1 为C1库位
	 * isCBifurcate = 2 为C2库位
	 */
	private Integer isCBifurcate;
}
