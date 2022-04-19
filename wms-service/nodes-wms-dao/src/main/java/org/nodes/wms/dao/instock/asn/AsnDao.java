package org.nodes.wms.dao.instock.asn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.nodes.wms.dao.instock.asn.entitits.AsnDetail;
import org.nodes.wms.dao.instock.asn.entitits.AsnHeader;

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
	Page<PageResponse> getAsnPageForWrapper(IPage<?> page, PageParamsQuery pageParamsQuery);

	/**
	 * 保存ASN单头表和明细
	 *
	 * @param asnHeader ASN单头表实体
	 * @param asnDetail ASN单明细实体
	 */
	void addAsnHeaderAndAsnDetail(AsnHeader asnHeader, AsnDetail asnDetail);

	/**
	 * 分页查询
	 *
	 * @param page            分页对象
	 * @param pageParamsQuery 分页请求参数
	 * @return IPage<PageResponse>
	 */
	Page<PageResponse> getAsnPage(IPage<?> page, PageParamsQuery pageParamsQuery);
}
