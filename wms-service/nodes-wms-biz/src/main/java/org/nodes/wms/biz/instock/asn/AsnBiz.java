package org.nodes.wms.biz.instock.asn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.asn.dto.input.AddOrEditAsnBillRequest;
import org.nodes.wms.dao.instock.asn.dto.input.AsnBillIdRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnBillByEditResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;

import javax.servlet.http.HttpServletResponse;
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
	 * @return AsnDetailResponse
	 */
	AsnDetailResponse getAsnContactDetail(AsnBillIdRequest asnBillIdRequest);

	/**
	 * 根据Asn单id删除Asn单头表信息
	 *
	 * @param asnBillIdList: Asn单id
	 * @return true:删除成功, false:删除失败
	 */
	boolean removeAsnBillById(List<Long> asnBillIdList);

	/**
	 * 根据Asn单id删除Asn单明细信息
	 *
	 * @param asnBillIdList: Asn单id集合
	 * @return true:删除成功, false:删除失败
	 */
	boolean removeAsnDetailByAsnBillId(List<Long> asnBillIdList);

	/**
	 * 新增Asn单
	 *
	 * @param addOrEditAsnBillRequest: Asn单创建对象
	 * @return AsnHeader
	 */
    AsnHeader add(AddOrEditAsnBillRequest addOrEditAsnBillRequest);

	/**
	 * 编辑-根据ASN单id获取Asn单头表和Asn明细信息
	 *
	 * @param asnBillId: ASN单id
	 * @return AsnDetailByEditResponse
	 */
	AsnBillByEditResponse findAsnHeaderAndAsnDetail(Long asnBillId);

	/**
	 * 编辑ASN单及明细
	 *
	 * @param addOrEditAsnBillRequest: 编辑参数对象
	 * @return AsnHeader
	 */
	AsnHeader edit(AddOrEditAsnBillRequest addOrEditAsnBillRequest);

	/**
	 * Excel 导出(导出当前查询条件)
	 *
	 * @param pageParamsQuery: 条件参数
	 * @param response:
	 */
	void exportAsnBill(PageParamsQuery pageParamsQuery, HttpServletResponse response);
}
