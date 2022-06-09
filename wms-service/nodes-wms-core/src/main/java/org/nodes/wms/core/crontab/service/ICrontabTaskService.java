package org.nodes.wms.core.crontab.service;

import org.nodes.wms.core.crontab.entity.CrontabTask;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 任务表 服务类
 *
 * @author NodeX
 * @since 2021-01-22
 */
public interface ICrontabTaskService extends BaseService<CrontabTask> {
   List<CrontabTask>  getCrontabTaskList();

	/**
	 * 根据id修改启用状态
	 * @param id
	 * @param num 启用状态修改值
	 */
	void EditEnabledById(Long id,int num);
}
