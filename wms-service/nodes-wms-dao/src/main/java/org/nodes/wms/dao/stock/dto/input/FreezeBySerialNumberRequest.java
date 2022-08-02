package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 库存冻结请求对象
 */
@Data
public class FreezeBySerialNumberRequest implements Serializable {
	private static final long serialVersionUID = -7524460869718941418L;
	/**
     * 序列号
	 */
	private List<String> serialNumber;
}
