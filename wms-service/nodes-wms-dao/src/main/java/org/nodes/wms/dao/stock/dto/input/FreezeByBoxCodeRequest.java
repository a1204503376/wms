package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 库存冻结请求对象
 */
@Data
public class FreezeByBoxCodeRequest implements Serializable {
	private static final long serialVersionUID = 2925235302543061574L;
	/**
	 * 箱码
	 */
	private String boxCode;
}
