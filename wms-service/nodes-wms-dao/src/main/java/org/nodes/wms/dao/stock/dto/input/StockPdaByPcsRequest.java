package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 根据箱码查询库存
 **/
@Data
public class StockPdaByPcsRequest implements Serializable {
	private static final long serialVersionUID = 6944206036296901150L;
	/**
	 * 箱号
	 */
	private String boxCode;
}
