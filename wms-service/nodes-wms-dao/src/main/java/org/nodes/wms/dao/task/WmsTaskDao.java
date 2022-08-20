package org.nodes.wms.dao.task;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.task.dto.input.TaskPageQuery;
import org.nodes.wms.dao.task.dto.output.TaskPageResponse;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskProcTypeEnum;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.springblade.core.mp.base.BaseService;

import java.math.BigDecimal;
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

	/**
	 * 根据状态获取任务
	 *
	 * @param taskState 查询参数
	 * @return 分页对象
	 */
	List<WmsTask> getTaskByState(int taskState);

	/**
	 * 根据箱码获取任务
	 *
	 * @param boxCode          箱码
	 * @param taskProcTypeEnum 任务执行方式
	 * @return 任务
	 */
	WmsTask findTaskByBoxCode(String boxCode, WmsTaskProcTypeEnum taskProcTypeEnum);

	/**
	 * 根据任务ID修改任务状态，以及实际量
	 *
	 * @param taskId        任务ID
	 * @param taskStateEnum 任务状态
	 * @param scanQty       实际量
	 */
	void updateWmsTaskStateByTaskId(Long taskId, WmsTaskStateEnum taskStateEnum, BigDecimal scanQty);

	/**
	 * 根据发货单发货单详情查询可用的任务
	 *
	 * @param soBillId   发货单ID
	 * @param soDetailId 发货单详情ID
	 * @return 可用的任务
	 */
	WmsTask getEnableTaskBySoBillId(Long soBillId, Long soDetailId);

}
