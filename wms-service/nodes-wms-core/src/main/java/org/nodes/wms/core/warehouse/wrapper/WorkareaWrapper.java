package org.nodes.wms.core.warehouse.wrapper;

import org.nodes.wms.core.outstock.so.dto.SavePickPlanDetailDTO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.entity.WorkArea;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.nodes.wms.core.warehouse.vo.WorkAreaVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

public class WorkareaWrapper extends BaseEntityWrapper<WorkArea, WorkAreaVO> {

	public static WorkareaWrapper build() {
		return new WorkareaWrapper();
	}

	@Override
	public WorkAreaVO entityVO(WorkArea workArea) {
		WorkAreaVO workAreaVO = BeanUtil.copy(workArea, WorkAreaVO.class);
		if (Func.isNotEmpty(workAreaVO)) {
			Warehouse warehouse = WarehouseCache.getById(workArea.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				workAreaVO.setWhName(warehouse.getWhName());
			}
			if (Func.isNotEmpty(workAreaVO.getStatus())) {
				if (workAreaVO.getStatus().equals(0)) {
					workAreaVO.setStatusDesc("启用");
				} else {
					workAreaVO.setStatusDesc("停用");
				}
			}
		}

		return workAreaVO;
	}
}
