package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 收货单头表 请求参数
 **/
@Data
public class ReceivePdaQuery implements Serializable {
	private static final long serialVersionUID = 7752254814035478515L;
	/**
	 * 收货单编码或者上游编码
	 */
	private String no;
	/**
	 * 库房id
	 */
	private Long whId;
}
