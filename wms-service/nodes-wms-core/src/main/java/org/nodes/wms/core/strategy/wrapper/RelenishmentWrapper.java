package org.nodes.wms.core.strategy.wrapper;

import org.nodes.wms.core.strategy.entity.Relenishment;
import org.nodes.wms.core.strategy.vo.RelenishmentVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

public class RelenishmentWrapper extends BaseEntityWrapper<Relenishment, RelenishmentVO> {

	public static RelenishmentWrapper build() {
		return new RelenishmentWrapper();
	}

	@Override
	public RelenishmentVO entityVO(Relenishment entity) {
		RelenishmentVO RelenishmentVO = BeanUtil.copy(entity, RelenishmentVO.class);
		if (ObjectUtil.isNotEmpty(RelenishmentVO)) {
			Warehouse warehouse = WarehouseCache.getById(entity.getWhId());
			if (ObjectUtil.isNotEmpty(warehouse)) {
				RelenishmentVO.setWhName(warehouse.getWhName());
			}
		}
		return RelenishmentVO;
	}
}
