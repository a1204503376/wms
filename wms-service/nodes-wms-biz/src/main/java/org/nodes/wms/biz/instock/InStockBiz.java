package org.nodes.wms.biz.instock;

import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailLpnPdaResponse;

/**
 * 收货相关业务
 */
public interface InStockBiz {

	/**
	 * 按箱收货,支持无单收货
	 * @param request
	 */
	void receiveByBoxCode(ReceiveDetailLpnPdaResponse request);
}
