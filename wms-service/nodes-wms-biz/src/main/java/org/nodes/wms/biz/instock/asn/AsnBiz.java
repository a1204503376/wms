package org.nodes.wms.biz.instock.asn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.asn.dto.input.AddAsnBillRequest;
import org.nodes.wms.dao.instock.asn.dto.input.AsnBillIdRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailByEditResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;

import java.util.List;

/**
 * ASN单据 业务类
 */
public interface AsnBiz {

	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param pageParamsQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	Page<PageResponse> getPageAsnBill(IPage<?> page,
									  PageParamsQuery pageParamsQuery);

	/**
	 * 获取ASN头表、ASN明细信息
	 *
	 * @param asnBillIdRequest:
	 * @return org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse
	 */
	AsnDetailResponse getAsnContactDetail(AsnBillIdRequest asnBillIdRequest);

	/**
	 * 根据Asn单id删除Asn单头表信息
	 *
	 * @param asnBillIdList: Asn单id
	 * @return java.lang.Boolean
	 */
	Boolean removeAsnBillById(List<Long> asnBillIdList);

	/**
	 * 根据Asn单id删除Asn单明细信息
	 *
	 * @param asnBillIdList: Asn单id集合
	 * @return java.lang.Boolean
	 */
	Boolean removeAsnDetailByAsnBillId(List<Long> asnBillIdList);

	/**
	 * 根据Asn单id查询Asn单明细id
	 *
	 * @param asnBillIdList: Asn单id
	 * @return java.util.List<java.lang.Long>
	 */
	List<Long> getAsnDetailIdList(List<Long> asnBillIdList);

	/**
	 * 新增Asn单
	 *
	 * @param addAsnBillRequest: Asn单创建对象
	 * @return java.lang.Boolean
	 */
    Boolean add(AddAsnBillRequest addAsnBillRequest);

	/**
	 * 编辑-获取Asn单头表和Asn明细信息
	 *
	 * @param asnBillIdRequest: Asn单id请求对象
	 * @return void
	 */
	AsnDetailByEditResponse getAsnHeaderAndAsnDetail(AsnBillIdRequest asnBillIdRequest);
}
