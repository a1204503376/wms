package org.nodes.wms.dao.putway.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 按箱上架策略请求对象
 **/
@Data
public class AddByBoxShelfRequest implements Serializable {
	private static final long serialVersionUID = 6938605109923273805L;
	/**
	 * 库存ID
	 */
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
	private Long whId;
	/**
	 * 总数
	 */
	private BigDecimal qty;

	/**
	 * 托盘号
	 */
	private String lpnCode;
}
