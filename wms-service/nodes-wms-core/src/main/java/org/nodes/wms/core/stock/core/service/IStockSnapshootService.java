package org.nodes.wms.core.stock.core.service;

import org.nodes.wms.core.stock.core.entity.StockSnapshoot;
import org.springblade.core.mp.base.BaseService;

/**
 * 库存快照 服务类
 *
 * @author NodeX
 * @since 2021-05-28
 */
public interface IStockSnapshootService<T> extends BaseService<StockSnapshoot> {

	boolean stockSnapshoot();

	boolean initializeStockSnapshoot();
}
