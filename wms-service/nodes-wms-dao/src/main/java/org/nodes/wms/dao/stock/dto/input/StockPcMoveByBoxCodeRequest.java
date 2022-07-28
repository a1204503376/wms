package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * PC库存按箱移动请求对象
 */
@Data
public class StockPcMoveByBoxCodeRequest implements Serializable {

	private static final long serialVersionUID = 7230714379374859500L;

	/**
	 * 箱码
	 */
	private List<String> boxCodeList;

	/**
	 * 目标箱码
	 */
	private String targetBoxCode;

	/**
	 * 目标库位id
	 */
	private String targetLocId;

	/**
	 * 目标lpn编码
	 */
	private String targetLpnCode;
}
