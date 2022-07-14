package org.nodes.wms.dao.task.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.task.dto.input.TaskDetailPageRequest;
import org.nodes.wms.dao.task.dto.output.TaskDetailExcelResponse;
import org.nodes.wms.dao.task.dto.output.TaskDetailPageResponse;
import org.nodes.wms.dao.task.entities.TaskDetail;

import java.util.HashMap;
import java.util.List;


/**
 * 任务明细 Mapper 接口
 */
public interface TaskDetailMapper extends BaseMapper<TaskDetail> {
	/**
	 * 分页查询
	 * @param page 底部分页参数
	 * @param request 查询条件
	 * @return 分页数据
	 */
	Page<TaskDetailPageResponse> getPage(IPage<?> page, @Param("query") TaskDetailPageRequest request);

	/**
	 * 根据条件查询对应的数据
	 * @param params 查询条件
	 * @return TaskDetailExcelResponse集合
	 */
	List<TaskDetailExcelResponse> getTaskList(@Param("query") HashMap<String, Object> params);
}
