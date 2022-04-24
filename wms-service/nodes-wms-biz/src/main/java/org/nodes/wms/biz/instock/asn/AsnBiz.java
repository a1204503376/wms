package org.nodes.wms.biz.instock.asn;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.instock.asn.dto.input.AsnRequest;
import org.nodes.wms.dao.instock.asn.dto.input.DeleteRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;

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

	// 新增 修改 删除 状态变更 。。。

	void save(AsnRequest asnRequest);

	/**
	 * 获取ASN头表、ASN明细
	 *
	 * @param deleteRequest:
	 * @return org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse
	 */
	AsnDetailResponse getAsnDetail(DeleteRequest deleteRequest);

	/**
	 * 删除Asn单及明细 和 对应的收货单及明细
	 *
	 * @param deleteRequest:
	 * @return boolean
	 */
    boolean removeAsnBillAndReceiveBill(DeleteRequest deleteRequest);
}
