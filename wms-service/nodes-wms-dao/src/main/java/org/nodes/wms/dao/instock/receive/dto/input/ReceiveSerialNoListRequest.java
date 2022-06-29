package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 收货单序列号接收
 **/
@Data
public class ReceiveSerialNoListRequest implements Serializable {
	private static final long serialVersionUID = -6414754798971462196L;
	/**
	 * 序列号管理
	 */
	private List<String> serialNumberList;
}
