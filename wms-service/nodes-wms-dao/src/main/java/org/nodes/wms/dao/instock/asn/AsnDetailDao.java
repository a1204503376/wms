package org.nodes.wms.dao.instock.asn;

import org.nodes.wms.dao.instock.asn.entities.AsnDetail;

import java.util.List;

/**
 * ASN单明细 DAO接口
 **/
public interface AsnDetailDao {

	/**
	 * 根据Asn单id 删除ASN单详细信息
	 *
	 * @param asnBillIdList: Asn单id集合
	 * @return boolean
	 */
	Boolean deleteAsnDetailByAsnBillId(List<Long> asnBillIdList);

	/**
	 * 根据Asn单id查询Asn单明细id集合
	 *
	 * @param asnBillIdList:
	 * @return java.util.List<java.lang.Long>
	 */
	List<Long> selectAsnDetailIdListByAsnBillId(List<Long> asnBillIdList);

	/**
	 * 新增Asn明细信息
	 *
	 * @param asnDetail: Asn单明细对象
	 * @return java.lang.Boolean
	 */
	public Boolean addAsnDetail(AsnDetail asnDetail);
}
