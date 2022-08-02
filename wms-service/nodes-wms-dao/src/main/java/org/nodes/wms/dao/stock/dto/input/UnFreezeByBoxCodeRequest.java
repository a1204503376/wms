package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 库存冻结请求对象
 */
@Data
public class UnFreezeByBoxCodeRequest implements Serializable {
	private static final long serialVersionUID = 3397719500514891160L;
	/**
	 * 箱码
	 */
	private String boxCode;
}
