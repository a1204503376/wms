package org.nodes.wms.biz.application;

import java.util.List;

/**
 * ASN单管理与收货单管理 业务接口
 **/
public interface AsnManageBiz {

	/**
	 * 删除ASN单及明细 和 收货单及明细
	 *
	 * @param asnBillIdList: ASN单id集合
	 * @return boolean
	 */
	boolean remove(List<Long> asnBillIdList);
}
