package org.nodes.wms.dao.count.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * PDA显示箱号和箱子内物品总数量的返回对象
 */
@Data
public class PdaBoxQtyResponse implements Serializable {

	private static final long serialVersionUID = 2040077459648458152L;
	/**
	 * 箱号
	 */
	private String boxCode;

	/**
	 * 箱内物品总数量
	 */
	private BigDecimal totalQty;
	/**
	 * 传输给前端做是否无误的判断
	 */
	private Boolean isValid = false;
}
