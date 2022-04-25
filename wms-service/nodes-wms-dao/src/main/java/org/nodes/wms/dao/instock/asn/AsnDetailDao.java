package org.nodes.wms.dao.instock.asn;

import java.util.List;

/**
 * ASN单明细 DAO接口
 **/
public interface AsnDetailDao {

	/**
	 * 根据Asn单id查询Asn单明细id集合
	 *
	 * @param asnBillIdList:
	 * @return java.util.List<java.lang.Long>
	 */
	List<Long> selectAsnDetailIdListByAsnBillId(List<Long> asnBillIdList);
}
