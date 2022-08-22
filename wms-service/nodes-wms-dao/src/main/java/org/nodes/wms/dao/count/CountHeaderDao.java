
package org.nodes.wms.dao.count;

import org.nodes.wms.dao.count.entity.CountHeader;
import org.nodes.wms.dao.count.enums.StockCountStateEnum;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 盘点单头 DAO接口
 */
public interface CountHeaderDao extends BaseService<CountHeader> {

	/**
	 * 根据盘点单号查询盘点单集合
	 */
    List<CountHeader> selectByCountBillNo(String countBillNo);
	/**
	 * 根据盘点单id查询盘点单集合
	 */
	List<CountHeader> selectByCountBillId(Long countBillId);

	void updateCountHeaderStateByCountBillId(Long countBillId, StockCountStateEnum stockCountStateEnum);
}
