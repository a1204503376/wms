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
	 * @return true:删除成功, false:删除失败
	 */
	Boolean deleteAsnDetailByAsnBillId(List<Long> asnBillIdList);

	/**
	 * 根据Asn单id查询Asn单明细id集合
	 *
	 * @param asnBillIdList:
	 * @return List<Long>
	 */
	List<Long> selectAsnDetailIdListByAsnBillId(List<Long> asnBillIdList);

	/**
	 * 新增Asn明细信息
	 *
	 * @param asnDetailList: Asn单明细集合
	 * @return true:新增成功, false:新增失败
	 */
	public Boolean addAsnDetail(List<AsnDetail> asnDetailList);

	/**
	 * 根据ASN单id 查询ASN单明细信息集合
	 *
	 * @param asnBillId: Asn单id
	 * @return List<AsnDetailEditResponse>
	 */
	public List<AsnDetail> getAsnDetailByAsnBillId(Long asnBillId);
}
