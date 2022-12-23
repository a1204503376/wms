package org.nodes.wms.dao.task.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.task.dto.input.TaskPageQuery;
import org.nodes.wms.dao.task.dto.output.TaskExcelResponse;
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

	/**
	 * 根据query条件获取工作任务
	 *
	 * @param wrapper 查询条件
	 * @return 工作任务
	 */
    List<TaskExcelResponse> listForExport(@Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
