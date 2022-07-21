package org.nodes.wms.biz.instock.asn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.common.log.dto.output.LogDetailPageResponse;
import org.nodes.wms.dao.instock.asn.dto.input.AddOrEditAsnBillRequest;
import org.nodes.wms.dao.instock.asn.dto.input.AsnBillPageQuery;
import org.nodes.wms.dao.instock.asn.dto.output.*;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderResponse;

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
	 * @param asnBillPageQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	Page<PageResponse> getPageAsnBill(IPage<?> page, AsnBillPageQuery asnBillPageQuery);

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
	 * @param asnBillPageQuery: 条件参数
	 * @param response:
	 */
	void exportAsnBill(AsnBillPageQuery asnBillPageQuery, HttpServletResponse response);

	/**
	 * 查看明细-根据ASN单id查询ASN单头表信息
	 *
	 * @param asnBillId: ASN单id
	 * @return AsnHeaderForDetailResponse ASN单头表信息
	 */
	AsnHeaderForDetailResponse findAsnHeaderForDetailByAsnBillId(Long asnBillId);

	/**
	 * 查看明细-根据ASN单id分页查询ASN单明细信息
	 *
	 * @param asnBillId: ASN单id
	 * @param page:      分页参数
	 * @return AsnHeaderForDetailResponse ASN单头表信息
	 */
	Page<AsnDetailForDetailResponse> findAsnDetailForDetailByAsnBillId(IPage<?> page, Long asnBillId);

	/**
	 * 查看明细-根据ASN单id分页查询发货单头表信息
	 *
	 * @param asnBillId: ASN单id
	 * @param page:      分页参数
	 * @return Page<ReceiveHeaderResponse> 发货单头表信息
	 */
	Page<ReceiveHeaderResponse> findReceiveHeaderForDetailByAsnBillId(IPage<?> page, Long asnBillId);

	/**
	 * 查看明细-根据ASN单id分页查询ASN单审计日志信息
	 *
	 * @param page:      分页参数
	 * @param asnBillId: ASN单id
	 * @return Page<AsnLogForDetailResponse> 审计日志信息
	 */
	Page<LogDetailPageResponse> findAsnLogForDetailByAsnBillId(IPage<?> page, Long asnBillId);

	/**
	 * 根据ASN单id删除ASN单
	 *
	 * @param asnBillIdList ASN单id
	 * @return true：删除成功，false：删除失败
	 */
	boolean remove(List<Long> asnBillIdList);

}
