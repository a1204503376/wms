package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 库存冻结请求对象
 */
@Data
public class FreezeByLotNumberRequest implements Serializable {
	private static final long serialVersionUID = -7524460869718941418L;
	/**
     * 批次号
	 */
	private String lotNumber;
}
