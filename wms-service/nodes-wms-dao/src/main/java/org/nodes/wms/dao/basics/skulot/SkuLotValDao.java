package org.nodes.wms.dao.basics.skulot;

import org.nodes.wms.dao.basics.skulot.entities.SkuLotVal;
import org.springblade.core.mp.base.BaseService;

/**
 * 批属性验证Dao
 */
public interface SkuLotValDao extends BaseService<SkuLotVal> {
	/**
	 * 根据货主id和库房id获取批属性验证
	 *
	 * @param whid 库房id
	 * @param woId 货主id
	 * @return
	 */
	SkuLotVal getByWhIdAndWoId(Long whid, Long woId);
}
