package org.nodes.wms.dao.instock.asn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnBillExportResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnBillViewResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;

import java.util.List;

/**
 * ASN单据 DAO接口
 */
@SuppressWarnings("AlibabaClassMustHaveAuthor")
public interface AsnHeaderDao {

	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param pageParamsQuery 分页请求参数
	 * @return Page<PageResponse>
	 */
	Page<PageResponse> selectPageAsnBill(IPage<?> page, PageParamsQuery pageParamsQuery);

	/**
	 * 新增/修改 ASN单头表信息和明细
	 *
	 * @param asnHeader ASN单头表实体
	 * @return true:新增成功 , false:新增失败
	 */
	boolean saveOrUpdateAsnHeader(AsnHeader asnHeader);

	/**
	 * 获取ASN单详细信息
	 *
	 * @param id: Asn单id
	 * @return AsnBillViewResponse
	 */
	AsnBillViewResponse getAsnBillViewDetailById(Long id);

	/**
	 * 根据Asn单id 删除ASN单头表信息
	 *
	 * @param idList: Asn单id集合
	 * @return true:删除成功 ,false:删除失败
	 */
	boolean deleteAsnHeaderById(List<Long> idList);

	/**
	 * Excel 导出(导出当前查询条件)
	 *
	 * @param pageParamsQuery: 查询条件
	 * @return List<AsnExportResponse>
	 */
    List<AsnBillExportResponse> listByParamsQuery(PageParamsQuery pageParamsQuery);

	/**
	 * 根据ASN单id查询ASN单头表信息
	 *
	 * @param asnBillId: ASN单id
	 * @return AsnDetailByEditResponse
	 */
	AsnHeader getAsnHeaderByAsnBillId(Long asnBillId);
}
