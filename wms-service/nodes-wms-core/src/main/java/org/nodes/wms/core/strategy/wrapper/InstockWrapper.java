package org.nodes.wms.core.strategy.wrapper;

import org.nodes.wms.core.strategy.entity.Instock;
import org.nodes.wms.core.strategy.vo.InstockVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.ObjectUtil;

public class InstockWrapper extends BaseEntityWrapper<Instock, InstockVO> {

	public static InstockWrapper build() {
		return new InstockWrapper();
	}

	@Override
	public InstockVO entityVO(Instock entity) {
		InstockVO instockVO = BeanUtil.copy(entity, InstockVO.class);
		if (ObjectUtil.isNotEmpty(instockVO)) {
			Warehouse warehouse = WarehouseCache.getById(entity.getWhId());
			if (ObjectUtil.isNotEmpty(warehouse)) {
				instockVO.setWhName(warehouse.getWhName());
			}
		}
		return instockVO;
	}
}
