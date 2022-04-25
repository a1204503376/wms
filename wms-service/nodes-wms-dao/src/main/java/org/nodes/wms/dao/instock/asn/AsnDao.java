package org.nodes.wms.dao.instock.asn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.asn.dto.input.DeleteRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;

import java.util.List;

/**
 * ASN单据 DAO接口
 */
@SuppressWarnings("AlibabaClassMustHaveAuthor")
public interface AsnDao {

	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param pageParamsQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	Page<PageResponse> selectPageAsnBill(IPage<?> page, PageParamsQuery pageParamsQuery);

	/**
	 * 保存ASN单头表和明细
	 *
	 * @param asnHeader ASN单头表实体
	 * @param asnDetail ASN单明细实体
	 */
	void addAsnHeaderAndAsnDetail(AsnHeader asnHeader, AsnDetail asnDetail);

	/**
	 * 获取ASN单详细信息
	 *
	 * @param deleteRequest: ASN单id
	 * @return org.nodes.wms.dao.instock.asn.dto.output.DetailResponse
	 */
	AsnDetailResponse selectAsnDetailByAsnBillId(DeleteRequest deleteRequest);

	/**
	 * 根据Asn单id 删除ASN单头表信息
	 *
	 * @param id: Asn单id
	 * @return java.lang.Integer
	 */
	Boolean deleteAsnHeaderById(List<Long> id);

	/**
	 * 根据Asn单id 删除ASN单详细信息
	 *
	 * @param id: Asn单id
	 * @return boolean
	 */
	Boolean deleteAsnDetailById(List<Long> id);

	/**
	 * 根据Asn单id 删除收货单头表信息
	 *
	 * @param id: Asn单id
	 * @return java.lang.Integer
	 */
	Boolean deleteReceiveHeaderById(List<Long> id);

	/**
	 * 根据Asn单id 删除收货单明细信息
	 *
	 * @param id: Asn单id
	 * @return java.lang.Integer
	 */
	Boolean deleteReceiveDetailById(List<Long> id);

	/**
	 * 查找收货单表中是否有Asn单id的记录
	 *
	 * @param asnDetailId:
	 * @return java.lang.Boolean
	 */
	Boolean hasAsnBillIdForReceiveBill(List<Long> asnDetailId);
}
