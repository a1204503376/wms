package org.nodes.wms.biz.application;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.instock.asn.AsnBiz;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Asn单管理和收货单管理 业务接口实现
 *
 **/
@Service
@RequiredArgsConstructor
public class AsnManageBizImpl implements AsnManageBiz {

	private final AsnBiz asnBiz;
	private final ReceiveBiz receiveBiz;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public boolean remove(List<Long> asnBillIdList) {
		// 1.删除ASN头表
		boolean delAsnBillFlag = asnBiz.removeAsnBillById(asnBillIdList);
		// 2.删除ASN明细表
		boolean delAsnDetailFlag = asnBiz.removeAsnDetailByAsnBillId(asnBillIdList);

		//3+4+5 = 在收货管理那边 删除收货头表+明细表
		// 3.删除收货单头表 (传asnBillIdList)

		return true;
	}
}
