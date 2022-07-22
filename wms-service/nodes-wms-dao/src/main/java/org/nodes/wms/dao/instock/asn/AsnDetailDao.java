package org.nodes.wms.dao.instock.asn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailForDetailResponse;
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
	boolean deleteAsnDetailByAsnBillId(List<Long> asnBillIdList);

	/**
	 * 新增Asn明细信息
	 *
	 * @param asnDetailList: Asn单明细集合
	 * @return true:新增成功, false:新增失败
	 */
	boolean saveOrUpdateAsnDetail(List<AsnDetail> asnDetailList);

	/**
	 * 根据ASN单id 查询ASN单明细信息集合
	 *
	 * @param asnBillId: Asn单id
	 * @return List<AsnDetailEditResponse>
	 */
	List<AsnDetail> getAsnDetailByAsnBillId(Long asnBillId);

	/**
	 * 根据id批量删除ASN单明细
	 *
	 * @param removeIdList: ASN单明细id
	 */
	void deleteByIds(List<Long> removeIdList);

	/**
	 * 查看明细-根据ASN单id分页查询ASN单明细
	 *
	 * @param asnBillId: ASN单id
	 * @param page: 分页参数
	 * @return Page<AsnDetailForDetailResponse> ASN单明细分页对象
	 */
	Page<AsnDetailForDetailResponse> getAsnDetailForDetailByAsnBillId(IPage<?> page, Long asnBillId);
}
