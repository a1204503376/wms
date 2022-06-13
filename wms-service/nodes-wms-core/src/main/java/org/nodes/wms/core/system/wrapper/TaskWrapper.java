package org.nodes.wms.core.system.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.basedata.cache.BillTypeCache;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.vo.TaskVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.ObjectUtil;

/**
 * @author pengwei
 * @program WmsCore
 * @description 任务封装类
 * @create 20191210
 */
public class TaskWrapper extends BaseEntityWrapper<Task, TaskVO> {


	public static TaskWrapper build() {
		return new TaskWrapper();
	}

	@Override
	public TaskVO entityVO(Task entity) {
		TaskVO taskVO = BeanUtil.copy(entity, TaskVO.class);

		if (!ObjectUtil.isEmpty(taskVO)) {
			Warehouse warehouse = WarehouseCache.getById(taskVO.getWhId());
			if (ObjectUtil.isNotEmpty(warehouse)) {
				taskVO.setWhName(warehouse.getWhName());
			}
			BillType billType = BillTypeCache.getByCode(entity.getBillTypeCd());
			if (ObjectUtil.isNotEmpty(billType)) {
				taskVO.setBillTypeDesc(billType.getBillTypeName());
			}
			taskVO.setTaskProcTypeDesc(DictCache.getValue(DictConstant.TASK_PROC_TYPE, taskVO.getTaskProcType()));
			taskVO.setTaskTypeDesc(DictCache.getValue(DictConstant.TASK_TYPE, taskVO.getTaskTypeCd()));
			taskVO.setIsAllot(
				ObjectUtil.isEmpty(taskVO.getUserId()) ? StringPool.CHS_NO : StringPool.CHS_YES);
		}

		return taskVO;
	}
}
