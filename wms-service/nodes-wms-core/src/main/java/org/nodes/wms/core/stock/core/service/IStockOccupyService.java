
package org.nodes.wms.core.stock.core.service;

import org.nodes.wms.core.stock.core.dto.StockOccupyDTO;
import org.nodes.wms.core.stock.core.dto.StockOccupySubtractDTO;
import org.nodes.wms.core.stock.core.entity.StockOccupy;
import org.springblade.core.mp.base.BaseService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 库存占用表 服务类
 *
 * @author pengwei
 * @since 2020-02-17
 */
public interface IStockOccupyService extends BaseService<StockOccupy> {

	/**
	 * 根据库存占用信息
	 *
	 * @param stockId    库存ID
	 * @param pickPlanId 拣货计划ID
	 * @param wcrId      盘点记录ID
	 * @return 库存占用集合
	 */
	List<StockOccupy> list(Long stockId, Long pickPlanId, Long wcrId);

	/**
	 * 更新占用数量(当库存占用量=0时删除)
	 * @param stockOccupy 库存占用信息
	 */
	void updateOccupyQty(StockOccupy stockOccupy);

	/**
	 * 库存占用
	 *
	 * @param stockOccupy 库存占用参数对象
	 */
	void add(@NotNull StockOccupyDTO stockOccupy);

	/**
	 * 库存占用解除
	 *
	 * @param stockOccupyReduce 库存占用扣减参数
	 * @return 占用的库存信息集合
	 */
	List<StockOccupy> subtract(@NotNull StockOccupySubtractDTO stockOccupyReduce);
}
