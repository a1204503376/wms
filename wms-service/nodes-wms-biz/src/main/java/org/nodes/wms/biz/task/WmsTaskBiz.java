package org.nodes.wms.biz.task;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.soPickPlan.entities.SoPickPlan;
import org.nodes.wms.dao.task.dto.input.StopTaskRequest;
import org.nodes.wms.dao.task.dto.input.TaskPageQuery;
import org.nodes.wms.dao.task.dto.output.TaskDetailExcelResponse;
import org.nodes.wms.dao.task.dto.output.TaskPageResponse;
import org.nodes.wms.dao.task.entities.TaskDetail;
import org.nodes.wms.dao.task.entities.WmsTask;
import org.nodes.wms.dao.task.enums.WmsTaskProcTypeEnum;
import org.nodes.wms.dao.task.enums.WmsTaskStateEnum;
import org.nodes.wms.dao.task.enums.WmsTaskTypeEnum;
import org.springblade.core.mp.support.Query;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * wms工作任务BIZ
 *
 * @author nodesc
 */
public interface WmsTaskBiz {
	/**
	 * 工作任务分页
	 *
	 * @param taskPageQuery 查询参数
	 * @param query         分页参数
	 * @return 分页对象
	 */
	Page<TaskPageResponse> page(TaskPageQuery taskPageQuery, Query query);

	/**
	 * 任务详情
	 *
	 * @param params 任务详情请求参数
	 * @return 按照条件查询出来的任务详情
	 */
	List<TaskDetailExcelResponse> selectTaskDetailList(HashMap<String, Object> params);

	/**
	 * 停止任务的动作
	 *
	 * @param request 停止任务的参数
	 */
	void stop(StopTaskRequest request);

	/**
	 * 取消任务的动作
	 *
	 * @param taskIdList 任务id
	 */
	void cancel(List<Long> taskIdList);

	/**
	 * 继续执行任务的动作
	 *
	 * @param taskIdList 任务id
	 */
	void continueTask(List<Long> taskIdList);

	/**
	 * 修改任务状态
	 *
	 * @param taskDetail 包含任务明细ID，任务状态
	 * @return true:表示成功
	 */
	Boolean updateTaskState(TaskDetail taskDetail);

	/**
	 * 导出工作任务列表
	 *
	 * @param taskPageQuery 查询参数
	 * @param response      响应对象
	 */
	void export(TaskPageQuery taskPageQuery, HttpServletResponse response);

	/**
	 * 业务日志
	 *
	 * @param wmsTask
	 */
	void log(WmsTask wmsTask);

	/**
	 * 根据箱码查询可用的任务
	 *
	 * @param boxCode          箱码
	 * @param taskProcTypeEnum 任务执行方式
	 * @return 任务
	 */
	WmsTask findPickTaskByBoxCode(String boxCode, WmsTaskProcTypeEnum taskProcTypeEnum);

	/**
	 * 根据任务ID修改任务状态，以及实际量
	 *
	 * @param taskId        任务ID
	 * @param taskStateEnum 任务状态
	 * @param scanQty       实际数量
	 */
	void updateWmsTaskStateByTaskId(Long taskId, WmsTaskStateEnum taskStateEnum, BigDecimal scanQty);

	/**
	 * 根据发货单发货单详情查询可用的任务
	 *
	 * @param soBillId   发货单ID
	 * @param soDetailId 发货单详情ID
	 * @return 可用的任务
	 */
	WmsTask findEnableTaskBySoBillId(Long soBillId, Long soDetailId);

	/**
	 * 创建任务
	 *
	 * @param taskType       任务类型
	 * @param procType       任务执行方式
	 * @param soPickPlanList 拣货记录明细
	 * @param soHeader       发货单
	 * @return WmsTask
	 */
	WmsTask create(WmsTaskTypeEnum taskType, WmsTaskProcTypeEnum procType,
				   List<SoPickPlan> soPickPlanList, SoHeader soHeader);

	/**
	 * 根据任务ID和其他参数修改任务
	 *
	 * @param taskId           任务ID
	 * @param taskProcTypeEnum 任务状态
	 * @param toLocation       目标库位
	 */
	void updateWmsTaskByPartParam(Long taskId, WmsTaskProcTypeEnum taskProcTypeEnum, Location toLocation, String boxCode);

	/**
	 * 根据任务ID获取任务
	 *
	 * @param taskId 任务ID
	 * @return 任务
	 */
	WmsTask findByTaskId(Long taskId);

	/**
	 * 更改任务状态为未下发状态
	 *
	 * @param taskId 任务ID
	 */
	void updateTaskStateToIssued(Long taskId);
}
