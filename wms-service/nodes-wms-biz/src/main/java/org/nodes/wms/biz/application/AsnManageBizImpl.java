package org.nodes.wms.biz.application;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.instock.asn.AsnBiz;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 彭永程
 * @date 2022-04-25 10:20
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
		Boolean delAsnBillFlag = asnBiz.removeAsnBillById(asnBillIdList);
		// 2.删除ASN明细表
		Boolean delAsnDetailFlag = asnBiz.removeAsnDetailById(asnBillIdList);
		// 3.删除收货单头表 (传asnBillIdList)

		// 4.根据asnBillIdList查询到asn明细单传asnDetailIdList
		List<Long> asnDetailIdList = asnBiz.getAsnDetailIdList(asnBillIdList);
		// 5.删除收货单明细表(传asnDetailIdList)
		return true;
	}
}
