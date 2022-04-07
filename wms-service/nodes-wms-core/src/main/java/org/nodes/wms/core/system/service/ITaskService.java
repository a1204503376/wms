
package org.nodes.wms.core.system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.nodes.wms.core.system.dto.TaskDTO;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.vo.TaskVO;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 服务类
 *
 * @author pengwei
 * @since 2019-12-10
 */
public interface ITaskService extends BaseService<Task> {

	/**
	 * 变更任务执行人员
	 *
	 * @param ids    任务主键ID字符串
	 * @param userId 用户主键ID
	 * @return 是否成功
	 */
	boolean changeUser(String ids, Long userId);

	/**
	 * 关闭任务
	 *
	 * @param ids 多个任务主键ID拼接的字符串
	 * @return 是否成功
	 */
	boolean close(String ids);

	/**
	 * 创建任务
	 *
	 * @param taskDTO 任务信息
	 * @return 创建的任务
	 */
	Task create(TaskDTO taskDTO);


	/**
	 * 获取所有任务根据类型
	 *
	 * @param task 任务主表
	 * @return 任务
	 */
	List<Task> getTaskListForType(Task task);

	/**
	 * 删除指定任务
	 *
	 * @param billId   订单ID
	 * @param taskType 任务类型
	 * @return 是否成功
	 */
	boolean delete(Long billId, TaskTypeEnum taskType);

	/**
	 * 更新任务开始时间
	 *
	 * @param billNo       订单编码
	 * @param taskTypeEnum 任务类型
	 */
	boolean updateBeginTime(String billNo, TaskTypeEnum taskTypeEnum);

	/**
	 * 更新任务开始执行时间
	 *
	 * @param taskId 任务ID
	 * @return 是否成功
	 */
	boolean updateBeginTime(Long taskId);

	/**
	 * 根据单据id关闭任务
	 *
	 * @param billId       单据ID
	 * @param taskTypeEnum 任务类型
	 * @return
	 */
	boolean closeTask(Long billId, TaskTypeEnum taskTypeEnum);

	/**
	 * 自动分配任务
	 *
	 * @param userId 用户ID，当需要指定分配给某个人的时候传递
	 * @return 是否成功
	 */
	boolean autoTask(Long userId);
	/**
	 *  根据订单编号模糊搜索拣货任务
	 * @param billNo
	 * @return
	 */
	List<Task> getTakesBybillNo(String billNo);
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
