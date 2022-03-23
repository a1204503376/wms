
package org.nodes.wms.core.stock.core.service;

import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.entity.StockLog;
import org.nodes.wms.core.stock.core.enums.StockProcTypeEnum;
import org.springblade.core.mp.base.BaseService;

/**
 * 库存日志 服务类
 *
 * @author pengwei
 * @since 2020-02-14
 */
public interface IStockLogService extends BaseService<StockLog> {

	/**
	 * 记录库存日志
	 *
	 * @param stock         库存信息
	 * @param stockDetail   库存明细
	 * @param stockProcType 处理类型
	 * @param systemProcId  系统日志ID
	 * @return 库存日志
	 */
	StockLog create(Stock stock, StockDetail stockDetail, StockProcTypeEnum stockProcType, Long systemProcId);
}
