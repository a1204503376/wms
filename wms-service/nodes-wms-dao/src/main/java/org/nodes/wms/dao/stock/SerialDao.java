package org.nodes.wms.dao.stock;

import org.nodes.wms.dao.stock.entities.Serial;
import org.nodes.wms.dao.stock.enums.SerialStateEnum;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 库存序列号Dao接口
 **/
public interface SerialDao extends BaseService<Serial> {

	/**
	 * 根据序列号编码获取在库的序列号信息
	 *
	 * @param serialNoList 序列号编码
	 * @return 在库的序列号
	 */
	List<Serial> getSerialBySerialNo(List<String> serialNoList);

	/**
	 * 查询已经出库的序列号
	 *
	 * @param serialNoList
	 * @return
	 */
	List<Serial> getOutBoundSerialBySerialNo(List<String> serialNoList);

	/**
	 * 根据stockid获取在库的序列号列表
	 *
	 * @param stockId
	 * @return
	 */
	List<Serial> getSerialByStockId(Long stockId);

	/**
	 * 根据stockid获取在库的序列号列表
	 *
	 * @param stockId
	 * @return 序列号集合
	 */
	List<String> getSerialNoByStockId(Long stockId);

	/**
	 * 更新序列号状态
	 *
	 * @param serialNoList
	 * @param state
	 * @param stockId      可为空，如果为空则不需要更新stockid字段
	 */
	void updateSerialState(List<String> serialNoList, SerialStateEnum state, Long stockId);

	/**
	 * 根据库存id获取序列号数量
	 *
	 * @param stockId 库存id
	 * @return 系列号数量
	 */
	int getSerialCountByStockId(Long stockId);
}
