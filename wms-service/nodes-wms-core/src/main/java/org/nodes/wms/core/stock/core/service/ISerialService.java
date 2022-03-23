package org.nodes.wms.core.stock.core.service;

import org.nodes.wms.core.stock.core.entity.Serial;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.springblade.core.mp.base.BaseService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 序列号 服务类
 *
 * @author zx
 * @since 2020-02-21
 */
public interface ISerialService extends BaseService<Serial> {

	/**
	 * 存储序列号
	 *
	 * @param serialNumberList 序列号
	 * @param stock            库存信息
	 * @param stockDetail      库存明细
	 * @param systemProcId     系统日志ID
	 * @return 序列号信息
	 */
	List<Serial> saveBatch(List<String> serialNumberList, Stock stock, StockDetail stockDetail,
						   Long systemProcId);

	/**
	 * 删除序列号
	 *
	 * @param serialNumberList 序列号
	 * @param stock            库存信息
	 * @param stockDetail      库存明细
	 * @param systemProcId     系统日志ID
	 * @return 是否成功
	 */
	Boolean removeBatch(List<String> serialNumberList, Stock stock, StockDetail stockDetail,
						Long systemProcId);

	/**
	 * 根据库存id 获取序列号集合
	 *
	 * @param stockId 库存id
	 * @return 序列号集合
	 */
	List<Serial> listByStockId(Long stockId);

	/**
	 * 根据库存明细id 获取序列号集合
	 *
	 * @param stockDetailId 库存明细id
	 * @return 序列号集合
	 */
	List<Serial> listByStockDetailId(Long stockDetailId);
}
