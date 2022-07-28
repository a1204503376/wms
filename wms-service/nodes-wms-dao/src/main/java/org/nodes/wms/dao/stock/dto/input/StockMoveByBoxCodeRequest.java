package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 库存按箱移动请求对象
 */
@Data
public class StockMoveByBoxCodeRequest implements Serializable {
	private static final long serialVersionUID = 6251527073571241801L;
	/**
	 * 目标库位编码
	 */
	private String locCode;
	/**
	 * lpn编码-托盘号
	 */
	private String lpnCode;
	/**
	 * 箱码
	 */
	private List<String> boxCodeList;
	/**
	 * 库房ID
	 */
	private Long whId;
}
