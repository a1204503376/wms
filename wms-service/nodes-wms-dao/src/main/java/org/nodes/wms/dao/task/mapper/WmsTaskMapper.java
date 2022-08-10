package org.nodes.wms.dao.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.task.dto.input.TaskPageQuery;
import org.nodes.wms.dao.task.dto.output.TaskPageResponse;
import org.nodes.wms.dao.task.entities.WmsTask;

import java.util.List;

/**
 * @author nodesc
 */
public interface WmsTaskMapper extends BaseMapper<WmsTask> {
	/**
	 * 获取工作任务分页
	 *
	 * @param page          分页条件
	 * @param taskPageQuery 查询参数
	 * @return 分页对象
	 */
	Page<TaskPageResponse> getPage(IPage<Object> page, @Param("query") TaskPageQuery taskPageQuery);

	/**
	 * 根据状态获取任务
	 *
	 * @param taskState 查询参数
	 * @return 分页对象
	 */
	List<WmsTask> getTaskByState(int taskState);
}
