package org.nodes.wms.core.instock.purchase.service;

import org.nodes.wms.core.instock.asn.entity.AsnDetail;
import org.nodes.wms.core.instock.purchase.dto.PoDetailDTO;
import org.nodes.wms.core.instock.purchase.entity.PoDetail;
import org.springblade.core.mp.base.BaseService;

/**
 * 收货单明细表 服务类
 *
 * @author NodeX
 * @since 2021-05-31
 */
public interface IPoDetailService extends BaseService<PoDetail> {

	/**
	 * 保存或更新采购订单明细
	 *
	 * @param poDetail 采购订单明细
	 * @return 是否成功
	 */
	boolean saveOrUpdate(PoDetailDTO poDetail);

	/**
	 * 更新PO明细数量
	 *
	 * @param asnDetail 入库单明细
	 * @return 是否成功
	 */
	boolean updateQty(AsnDetail asnDetail);
}
