package org.nodes.wms.core.outstock.loading.wrapper;

import org.nodes.wms.core.outstock.so.entity.SoRegister;
import org.nodes.wms.core.outstock.so.vo.SoRegisterVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.ObjectUtil;

public class SoRegisterWrapper extends BaseEntityWrapper<SoRegister, SoRegisterVO> {
	public static SoRegisterWrapper build() {
		return new SoRegisterWrapper();
	}


	@Override
	public SoRegisterVO entityVO(SoRegister entity) {
		SoRegisterVO soRegisterVO = BeanUtil.copy(entity, SoRegisterVO.class);
		Warehouse warehouse = WarehouseCache.getById(entity.getWhId());
		if (ObjectUtil.isNotEmpty(warehouse)) {
			soRegisterVO.setWhName(warehouse.getWhName());
		}
		return soRegisterVO;
	}
}
