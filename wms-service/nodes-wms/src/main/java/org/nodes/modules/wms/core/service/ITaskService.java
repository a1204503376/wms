package org.nodes.modules.wms.core.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.vo.TaskVO;

import java.util.List;

public interface ITaskService extends org.nodes.wms.core.system.service.ITaskService {

	/**
	 * 查询任务
	 *
	 * @param queryWrapper
	 * @return
	 */
	List<TaskVO> list(QueryWrapper<Task> queryWrapper);
	/**
	 * 获取任务详细信息
	 *
	 * @param taskId 任务ID
	 * @return 任务
	 */
	TaskVO getDetail(Long taskId);
}
