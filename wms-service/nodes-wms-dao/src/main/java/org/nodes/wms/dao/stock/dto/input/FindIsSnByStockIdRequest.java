package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;

@Data
public class FindIsSnByStockIdRequest implements Serializable {
	private static final long serialVersionUID = 8426662604328035856L;
	/**
	 * 库存ID
	 */
	private Long stockId;
}
