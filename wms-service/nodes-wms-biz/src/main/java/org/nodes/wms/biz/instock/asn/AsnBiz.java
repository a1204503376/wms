package org.nodes.wms.biz.instock.asn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.asn.dto.input.AddAsnBillRequest;
import org.nodes.wms.dao.instock.asn.dto.input.AsnBillIdRequest;
import org.nodes.wms.dao.instock.asn.dto.input.EditAsnBillRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailByEditResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;

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
	Boolean removeAsnBillById(List<Long> asnBillIdList);

	/**
	 * 根据Asn单id删除Asn单明细信息
	 *
	 * @param asnBillIdList: Asn单id集合
	 * @return true:删除成功, false:删除失败
	 */
	Boolean removeAsnDetailByAsnBillId(List<Long> asnBillIdList);

	/**
	 * 根据Asn单id查询Asn单明细id
	 *
	 * @param asnBillIdList: Asn单id
	 * @return List<Long>
	 */
	List<Long> getAsnDetailIdList(List<Long> asnBillIdList);

	/**
	 * 新增Asn单
	 *
	 * @param addAsnBillRequest: Asn单创建对象
	 * @return true:新增成功, false:新增失败
	 */
    Boolean add(AddAsnBillRequest addAsnBillRequest);

	/**
	 * 编辑-获取Asn单头表和Asn明细信息
	 *
	 * @param asnBillIdRequest: Asn单id请求对象
	 * @return AsnDetailByEditResponse
	 */
	AsnDetailByEditResponse getAsnHeaderAndAsnDetail(AsnBillIdRequest asnBillIdRequest);

	/**
	 * 编辑ASN单及明细
	 *
	 * @param editAsnBillRequest: 编辑参数对象
	 * @return true:修改成功，false:修改失败
	 */
	boolean edit(EditAsnBillRequest editAsnBillRequest);

	/**
	 * Excel 导出(导出当前查询条件)
	 *
	 * @param pageParamsQuery: 条件参数
	 * @param response:
	 * @return void
	 */
	void exportAsnBill(PageParamsQuery pageParamsQuery, HttpServletResponse response);
}
