package org.nodes.wms.dao.basics.skulot;

import org.nodes.wms.dao.basics.skulot.entities.SkuLot;
import org.springblade.core.mp.base.BaseService;

/**
 * 物品批属性Dao
 */
public interface SkuLotDao extends BaseService<SkuLot> {
	SkuLot getSkuLotByWoId(Long woId);
}
