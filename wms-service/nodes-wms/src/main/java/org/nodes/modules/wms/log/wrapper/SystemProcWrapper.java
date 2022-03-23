package org.nodes.modules.wms.log.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.modules.wms.log.vo.SystemProcVO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;

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
