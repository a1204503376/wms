package org.nodes.wms.dao.task;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.task.dto.input.TaskPageQuery;
import org.nodes.wms.dao.task.dto.output.TaskPageResponse;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * @author nodesc
 */
public interface WmsTaskDao extends BaseService<WmsTask> {

	/**
	 * 修改任务状态
	 *
	 * @param taskId 任务id
	 * @param state  状态
	 */
	void updateState(Long taskId, WmsTaskStateEnum state);

	/**
	 * 获取工作任务分页
	 *
	 * @param page          分页参数
	 * @param taskPageQuery 查询参数
	 * @return 分页对象
	 */
	Page<TaskPageResponse> getPage(IPage<Object> page, TaskPageQuery taskPageQuery);

	/**
	 * 根据任务id查询任务
	 *
	 * @param taskIdList 任务id
	 * @return 任务
	 */
	List<WmsTask> getTaskByIds(List<Long> taskIdList);
}
