package org.nodes.wms.dao.putway.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 按箱上架策略请求对象
 **/
@Data
public class PutwayByBoxRequest implements Serializable {
	private static final long serialVersionUID = 6938605109923273805L;
	/**
	 * 库存ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 库房id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 总数
	 */
	private BigDecimal qty;

	/**
	 * 是否整箱收货
	 */
	private Boolean isAllLpnPutaway;
}
