package org.nodes.wms.dao.crontab;

import org.nodes.wms.dao.crontab.entity.CrontabTask;

/**
 * 定时任务表 dao接口
 */
public interface CrontabTaskDao {
	/**
	 * 根据id修改启用状态
	 * @param taskId 任务id
	 * @param num 启用状态值
	 */
	void editEnabledById(Long taskId, int num);

	/**
	 * 根据任务名称获取任务实体
	 * @param taskName 任务名称
	 */
	CrontabTask getCrontabTaskByName(String taskName);
}
