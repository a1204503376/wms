package org.nodes.modules.wms.log.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.modules.wms.log.vo.SystemProcVO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.ObjectUtil;

public class SystemProcWrapper extends BaseEntityWrapper<SystemProc, SystemProcVO> {
	public static SystemProcWrapper build() {
		return new SystemProcWrapper();
	}

	@Override
	public SystemProcVO entityVO(SystemProc entity) {
		SystemProcVO systemProcVO = BeanUtil.copy(entity, SystemProcVO.class);
		Warehouse warehouse = WarehouseCache.getById(systemProcVO.getWhId());
		if (ObjectUtil.isNotEmpty(warehouse)) {
			systemProcVO.setWhName(warehouse.getWhName());
		}
		systemProcVO.setDataItemDesc(DictCache.getValue("data_item", entity.getDataItem()));
		return systemProcVO;
	}
}
