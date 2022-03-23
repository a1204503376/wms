package org.nodes.wms.core.stock.transfer.service;

import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.stock.transfer.entity.TransferDetail;
import org.nodes.wms.core.stock.transfer.entity.TransferHeader;
import org.springblade.core.mp.base.BaseService;

import java.math.BigDecimal;

/**
 * 库内移动明细 服务类
 *
 * @author pengwei
 * @since 2020-08-03
 */
public interface ITransferDetailService extends BaseService<TransferDetail> {
	/**
	 * 保存或者更新(在原有的记录上更新数量)库内移动明细
	 *
	 * @param transferHeader 库内移动表头
	 * @param stock          库存信息
	 * @param qty            数量
	 * @param systemProcId   系统日志ID
	 * @return 是否成功
	 */
	boolean saveOrUpdate(TransferHeader transferHeader, Stock stock, SoDetail soDetail, BigDecimal qty, Long systemProcId);
}
