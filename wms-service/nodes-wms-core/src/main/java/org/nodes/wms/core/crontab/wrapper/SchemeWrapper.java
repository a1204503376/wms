package org.nodes.wms.core.crontab.wrapper;

import org.nodes.core.enums.StatusEnum;
import org.nodes.wms.core.crontab.cache.CrontabTaskCache;
import org.nodes.wms.dao.crontab.entity.CrontabTask;
import org.nodes.wms.dao.crontab.entity.Scheme;
import org.nodes.wms.dao.crontab.vo.SchemeVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;


/**
 * 任务计划表包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-01-22
 */
public class SchemeWrapper extends BaseEntityWrapper<Scheme, SchemeVO>  {

	public static SchemeWrapper build() {
		return new SchemeWrapper();
 	}

	@Override
	public SchemeVO entityVO(Scheme scheme) {
		SchemeVO schemeVO = BeanUtil.copy(scheme, SchemeVO.class);
		if (Func.isNotEmpty(schemeVO)) {
			Warehouse warehouse = WarehouseCache.getById(scheme.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				schemeVO.setWhName(warehouse.getWhName());
			}
			CrontabTask crontabTask = CrontabTaskCache.getById(scheme.getCrontabTaskId());
			if (Func.isNotEmpty(crontabTask)) {
				schemeVO.setTaskName(crontabTask.getCrontabTaskName());
			}
			if (scheme.getStatus().equals(StatusEnum.ON.getIndex())) {
				schemeVO.setStatusDesc(StatusEnum.ON.getName());
			} else {
				schemeVO.setStatusDesc(StatusEnum.OFF.getName());
			}
		}

		return schemeVO;
	}
}
