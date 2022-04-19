package org.nodes.wms.biz.application;

import org.nodes.wms.dao.application.dto.ReceiveForAsnRequest;

/**
 * 收货管理
 */
public interface ReceiveBiz {

	/**
	 * 手持收货信息采集
	 *
	 * @param receiveForAsnRequest 请求对象
	 */
	void receiveForAsn(ReceiveForAsnRequest receiveForAsnRequest);
}
