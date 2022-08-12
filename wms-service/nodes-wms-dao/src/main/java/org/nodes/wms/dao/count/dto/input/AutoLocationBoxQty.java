package org.nodes.wms.dao.count.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * PDA显示箱号和箱子内物品总数量的返回对象
 *
 * @author nodes
 */
@Data
public class AutoLocationBoxQty implements Serializable {
	private static final long serialVersionUID = 7499778727385443412L;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 箱内物品总数量
	 */
	private BigDecimal totalQty;
	/**
	 * 传输给前端做是否无误的判断
	 */
	private Boolean isValid = false;

	/**
	 * 传输给前端按钮默认显示
	 */
	private Boolean isButton = true;
}
