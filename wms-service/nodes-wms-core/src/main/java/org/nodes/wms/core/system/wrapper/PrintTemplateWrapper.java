package org.nodes.wms.core.system.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.system.entity.PrintTemplate;
import org.nodes.wms.core.system.vo.PrintTemplateVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

public class PrintTemplateWrapper extends BaseEntityWrapper<PrintTemplate, PrintTemplateVO> {
	public static PrintTemplateWrapper build() {
		return new PrintTemplateWrapper();
	}

	@Override
	public PrintTemplateVO entityVO(PrintTemplate entity) {
		PrintTemplateVO entityVO = BeanUtil.copy(entity, PrintTemplateVO.class);
		//货主
		if (Func.isNotEmpty(entityVO.getWoId())) {
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(entityVO.getWoId());
			if (ObjectUtil.isNotEmpty(owner)) {
				entityVO.setOwnerName(owner.getOwnerName());
			}
		}
		//库房
		Warehouse warehouse = WarehouseCache.getById(entityVO.getWhId());
		if (ObjectUtil.isNotEmpty(warehouse)) {
			entityVO.setWhName(warehouse.getWhName());
		}
		entityVO.setSptTypeDesc(DictCache.getValue("spt_type", entityVO.getSptType()));
		return entityVO;
	}
}
