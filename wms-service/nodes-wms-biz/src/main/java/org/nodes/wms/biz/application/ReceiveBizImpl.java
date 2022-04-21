package org.nodes.wms.biz.application;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.instock.asn.AsnBiz;
import org.nodes.wms.dao.application.dto.input.ReceiveForAsnRequest;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
@RequiredArgsConstructor
public class ReceiveBizImpl implements ReceiveBiz {

	private final AsnBiz asnBiz;
//	private final OutBiz outBiz;

	@Override
	public void receiveForAsn(ReceiveForAsnRequest receiveForAsnRequest) {
		// 1.
		// 2.
		// 3.
	}
}
