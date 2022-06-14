package org.nodes.wms.core.system.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.wms.core.basedata.cache.BillTypeCache;
import org.nodes.wms.core.system.entity.TaskHistory;
import org.nodes.wms.core.system.vo.TaskHistoryVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.cache.WorkAreaCache;
import org.nodes.wms.core.warehouse.entity.WorkArea;
import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;


/**
 * 任务履历包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2020-06-10
 */
public class TaskHistoryWrapper extends BaseEntityWrapper<TaskHistory, TaskHistoryVO> {

	public static TaskHistoryWrapper build() {
		return new TaskHistoryWrapper();
	}

	@Override
	public TaskHistoryVO entityVO(TaskHistory taskHistory) {
		TaskHistoryVO taskHistoryVO = BeanUtil.copy(taskHistory, TaskHistoryVO.class);
		//库房
		Warehouse warehouse = WarehouseCache.getById(taskHistoryVO.getWhId());
		if (Func.isNotEmpty(warehouse) && Func.isNotEmpty(warehouse.getWhName())) {
			taskHistoryVO.setWhName(warehouse.getWhName());
		}
		//任务类型
		taskHistoryVO.setTaskTypeName(DictCache.getValue("task_type", taskHistoryVO.getTaskTypeCd()));
		//任务执行方式
		taskHistoryVO.setTaskProcTypeName(DictCache.getValue("task_proc_type", taskHistoryVO.getTaskProcType()));
		//单据类型名称
		if (Func.isNotEmpty(taskHistoryVO.getBillTypeCd())) {
			BillType billType = BillTypeCache.getByCode(taskHistoryVO.getBillTypeCd());
			if (Func.isNotEmpty(billType)) {
				if (Func.isNotEmpty(billType.getBillTypeName()))
					taskHistoryVO.setBillTypeName(billType.getBillTypeName());
			}
		}
		//工作区
		if (Func.isNotEmpty(taskHistory.getWwaId())) {
			WorkArea workArea = WorkAreaCache.getById(taskHistoryVO.getWwaId());
			if (Func.isNotEmpty(workArea) && Func.isNotEmpty(workArea.getWwaName())) {
				taskHistoryVO.setWwaName(workArea.getWwaName());
			}
		}

		return taskHistoryVO;
	}
}
