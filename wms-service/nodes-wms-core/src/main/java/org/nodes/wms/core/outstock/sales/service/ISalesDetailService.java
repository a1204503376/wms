package org.nodes.wms.core.outstock.sales.service;

import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.sales.dto.SalesDetailDTO;
import org.nodes.wms.core.outstock.sales.entity.SalesDetail;
import org.springblade.core.mp.base.BaseService;

/**
 * 发货单明细
 * 服务类
 *
 * @author NodeX
 * @since 2021-05-31
 */
public interface ISalesDetailService extends BaseService<SalesDetail> {
	/**
	 * 保存或更新销售订单明细
	 *
	 * @param salesDetail 销售订单明细
	 * @return 是否成功
	 */
	boolean saveOrUpdate(SalesDetailDTO salesDetail);

	/**
	 * 更新PO明细数量
	 *
	 * @param soDetail 出库单明细
	 * @return 是否成功
	 */
	boolean updateQty(SoDetail soDetail);
}
