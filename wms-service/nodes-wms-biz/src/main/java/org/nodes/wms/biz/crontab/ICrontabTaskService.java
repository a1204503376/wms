package org.nodes.wms.biz.crontab;

import org.nodes.wms.dao.crontab.entity.CrontabTask;
import org.springblade.core.mp.base.BaseService;
import org.springframework.web.bind.annotation.RequestBody;

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
	 * 启动任务
	 * @param taskId 任务id
	 */
	void startCrontabTask(Long taskId);

	/**
	 * 停止任务
	 * @param taskId 任务id
	 */
	void stopCrontabTask(Long taskId);

	void logOfCrontabTask(String taskName, String log);

	/**
	 * 任务表新增
	 * @param crontabTask  任务实体
	 */
	boolean newCrontabTask(CrontabTask crontabTask);

	/**
	 * 任务表修改
	 * @param crontabTask 任务实体
	 */
	boolean editCrontabTask(CrontabTask crontabTask);



	/**
	 * 根据id集合批量删除
	 * @param ids id集合
	 */
	boolean deleteByIds(List<Long> ids);
}
