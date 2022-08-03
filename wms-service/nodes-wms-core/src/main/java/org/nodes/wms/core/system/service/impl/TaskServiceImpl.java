
package org.nodes.wms.core.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.entity.User;
import org.nodes.core.base.enums.LoginStatusEnum;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.base.service.IUserService;
import org.nodes.core.constant.AppConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.count.entity.CountHeader;
import org.nodes.wms.core.count.service.ICountHeaderService;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.service.IWellenDetailService;
import org.nodes.wms.core.outstock.so.vo.WellenSoHeaderVo;
import org.nodes.wms.core.system.dto.TaskDTO;
import org.nodes.wms.core.system.entity.RoleTask;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.entity.TaskHistory;
import org.nodes.wms.core.system.entity.UserOnline;
import org.nodes.wms.core.system.enums.TaskProcTypeEnum;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.nodes.wms.core.system.mapper.TaskMapper;
import org.nodes.wms.core.system.service.IRoleTaskService;
import org.nodes.wms.core.system.service.ITaskHistoryService;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.system.service.IUserOnlineService;
import org.nodes.wms.core.system.vo.TaskVO;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author pengwei
 * @since 2019-12-10
 */
@Slf4j
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class TaskServiceImpl<M extends TaskMapper, T extends Task>
	extends BaseServiceImpl<TaskMapper, Task>
	implements ITaskService {

	@Autowired
	IUserOnlineService userOnlineService;
	@Autowired
	ITaskHistoryService taskHistoryService;
	@Autowired
	ISystemProcService systemProcService;
	@Autowired
	IRoleTaskService roleTaskService;

	/**
	 * 人员变更
	 *
	 * @param ids    任务主键ID字符串
	 * @param userId 用户主键ID
	 * @return 是否成功
	 */
	@Override
	public boolean changeUser(String ids, Long userId) {
		User user = UserCache.getById(userId);
		if (Func.isEmpty(user)) {
			throw new ServiceException("指定用户不存在！");
		}
		List<Long> list = Func.toLongList(ids);
		ICountHeaderService countHeaderService = SpringUtil.getBean(ICountHeaderService.class);
		IWellenDetailService wellenDetailService = SpringUtil.getBean(IWellenDetailService.class);
		for (Long id : list) {
			Task task = getById(id);
			if (Func.isEmpty(task)) {
				throw new ServiceException(String.format("指定任务不存在（任务ID：%s）！", id));
			}
			if(Func.isNotEmpty(task)&&"901".equals(task.getBillTypeCd())){
				List<WellenSoHeaderVo> soHeaderByWellenId = wellenDetailService.getSoHeaderByWellenId(Func.toLongList(task.getBillId().toString()));
				if(Func.isEmpty(soHeaderByWellenId)){
					throw new ServiceException(String.format("该任务拣货单不存在！"));
				}
				SoHeader soHeader = soHeaderByWellenId.get(0).getSoHeader();
				if(!soHeader.getDeptId().equals(Func.toLong(user.getDeptId()))){
					throw new ServiceException("用户不属于该任务的部门！");
				}
			}
			if(Func.isNotEmpty(task)&&"909".equals(task.getBillTypeCd())){
				CountHeader countHeader = countHeaderService.getById(task.getBillId());
				if(Func.isEmpty(countHeader)){
					throw new ServiceException(String.format("该任务盘点单不存在！"));
				}
				if(!countHeader.getCreateDept().equals(Func.toLong(user.getDeptId()))){
					throw new ServiceException("用户不属于该任务的部门！");
				}
			}
			task.setAllotTime(LocalDateTime.now());
			task.setUserId(user.getId());
			task.setUserCode(user.getAccount());
			task.setUserName(user.getRealName());
			super.updateById(task);
		}
		return true;
	}

	/**
	 * 关闭任务
	 *
	 * @param ids 多个任务主键ID拼接的字符串
	 * @return 是否成功
	 */
	@Override
	public boolean close(String ids) {
		List<Long> list = Func.toLongList(ids);
		for (Long id : list) {
			Task task = this.getById(id);
			if (Func.isEmpty(task)) {
				throw new ServiceException("任务不存在！");
			}
			// 创建系统日志
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.TASK);
			systemProcParam.setDataType(DataTypeEnum.TaskNo);
			systemProcParam.setAction(ActionEnum.CLOSE);
			systemProcParam.setBillNo(task.getTaskId().toString());
			systemProcParam.setWhId(task.getWhId());
			SystemProc systemProc = systemProcService.create(systemProcParam);
			TaskHistory taskHistory = BeanUtil.copy(task, TaskHistory.class);
			if (Func.isEmpty(taskHistory)) {
				throw new ServiceException("关闭任务失败！");
			}
			taskHistory.setUserId(AuthUtil.getUserId());
			taskHistory.setUserCode(AuthUtil.getUserAccount());
			taskHistory.setUserName(AuthUtil.getNickName());
			taskHistory.setBeginTime(task.getBeginTime());
			taskHistory.setAllotTime(task.getAllotTime());
			// 存到历史表
			taskHistory.setCloseTime(LocalDateTime.now());
			taskHistoryService.save(taskHistory);
		}
		super.removeByIds(list);
		autoTask(AuthUtil.getUserId());

		return true;
	}

	@Override
	public Task create(TaskDTO taskDTO) {
		Task task = new Task();
		Long userId = null;
		if (Func.isNotEmpty(taskDTO.getTaskPackage())) {
			task.setTtpId(taskDTO.getTaskPackage().getTtpId());
			task.setTaskProcType(taskDTO.getTaskPackage().getTaskProcType());
		} else {
			task.setTtpId(AppConstant.TOP_PARENT_ID);
			task.setTaskProcType(TaskProcTypeEnum.Cooperation.getIndex());
		}
		task.setTaskTypeCd(taskDTO.getTaskType());
		task.setWhId(taskDTO.getWhId());
		task.setBillTypeCd(taskDTO.getBillTypeCd());
		task.setBillId(taskDTO.getBillId());
		task.setBillNo(taskDTO.getBillNo());
		task.setTaskQty(taskDTO.getTaskQty());
		task.setAllotTime(taskDTO.getAllotTime());
		task.setConfirmDate(taskDTO.getConfirmDate());
		task.setWwaId(0L);
		task.setTaskRemark(taskDTO.getRemark());
		task.setTaskState(0);
		super.save(task);
		autoTask(userId);
		return task;
	}

	@Override
	public List<Task> getTaskListForType(Task task) {
		return baseMapper.selectList(new QueryWrapper<Task>().lambda()
			.eq(Task::getTaskTypeCd, task.getTaskTypeCd()));
	}

	/**
	 * 删除指定任务
	 *
	 * @param billId   订单ID
	 * @param taskType 任务类型
	 * @return 是否成功
	 */
	@Override
	public boolean delete(Long billId, TaskTypeEnum taskType) {
		// 封装查询条件
		Task taskQuery = new Task();
		taskQuery.setBillId(billId);
		taskQuery.setTaskTypeCd(taskType.getIndex());
		// 查询任务
		Task task = super.getOne(Condition.getQueryWrapper(taskQuery));
		if (Func.isEmpty(task)) {
			return false;
		}
		return super.removeById(task);
	}

	@Override
	public boolean updateBeginTime(String billNo, TaskTypeEnum taskTypeEnum) {
		Task task = super.list(Condition.getQueryWrapper(new Task()).lambda()
			.eq(Task::getBillNo, billNo)
			.eq(Task::getTaskTypeCd, taskTypeEnum.getIndex()))
			.stream().findFirst().orElse(null);
		if (Func.isNotEmpty(task)) {
			return this.updateBeginTime(task.getTaskId());
		}
		return false;
	}

	@Override
	public synchronized boolean updateBeginTime(Long taskId) {
		UpdateWrapper<Task> updateWrapper = new UpdateWrapper<>();
		updateWrapper.lambda()
			.set(Task::getBeginTime, LocalDateTime.now())
			.eq(Task::getTaskId, taskId)
			.isNull(Task::getBeginTime);
		return super.update(updateWrapper);
	}

	/**
	 * 根据单据id关闭任务
	 *
	 * @param billId       单据ID
	 * @param taskTypeEnum 任务类型枚举
	 * @return
	 */
	@Override
	public boolean closeTask(Long billId, TaskTypeEnum taskTypeEnum) {
		List<Task> taskList = super.list(Condition.getQueryWrapper(new Task())
			.lambda().eq(Task::getBillId, billId)
			.func(sql -> {
				if (taskTypeEnum.getIndex() != TaskTypeEnum.ALL.getIndex()) {
					sql.eq(Task::getTaskTypeCd, taskTypeEnum.getIndex());
				}
			}));
		if (Func.isNotEmpty(taskList)) {
			taskList.forEach(task -> {
				this.close(String.valueOf(task.getTaskId()));
			});
		}
		return true;
	}


	@Override
	public synchronized boolean autoTask(Long userId) {
		IUserService userService = SpringUtil.getBean(IUserService.class);
		// 是否开启自动分配
		Boolean autoTask = Func.toBoolean(ParamCache.getValue(ParamEnum.TASK_AUTO_ENABLE.getKey()));
		// 最大可分配数量
		Integer maxTaskSize = Func.toInt(ParamCache.getValue(ParamEnum.TASK_AUTO_UPPER_LIMIT.getKey()));
		if (!autoTask || maxTaskSize < 1) {
			return false;
		}
		// 先查询出所有任务
		List<Task> taskAllList = super.list();
		if (Func.isEmpty(taskAllList)) {
			return false;
		}
		// 获取未分配的任务
		List<Task> unTaskList = taskAllList.stream().filter(u -> {
			return Func.isEmpty(u.getUserId());
		}).collect(Collectors.toList());
		if (Func.isEmpty(unTaskList)) {
			return false;
		}
		// 获取所有在线人员
		List<UserOnline> userOnlineList = userOnlineService.list(Condition.getQueryWrapper(new UserOnline())
			.lambda()
			.eq(UserOnline::getLoginStatus, LoginStatusEnum.ON_LINE.getIndex()));
		// 获取人员信息
		List<User> userList = new ArrayList<>();
		if (Func.isEmpty(userId) && Func.isNotEmpty(userOnlineList)) {
			userList = userService.listByIds(NodesUtil.toList(userOnlineList, UserOnline::getUserId));
		} else {
			UserOnline userOnline = userOnlineList.stream().filter(u -> {
				return u.getUserId().equals(userId);
			}).findFirst().orElse(null);
			if (Func.isNotEmpty(userOnline)) {
				userList = new ArrayList() {{
					add(UserCache.getById(userId));
				}};
			}
		}
		if (Func.isEmpty(userList)) {
			return false;
		}
		// 获取在线人员的角色可执行的任务类型信息
		List<RoleTask> roleTaskAllList = roleTaskService.list(Condition.getQueryWrapper(new RoleTask())
			.lambda()
			.in(RoleTask::getRoleId, NodesUtil.toList(userList, User::getRoleId)));
		if (Func.isEmpty(roleTaskAllList)) {
			return false;
		}
		LocalDateTime today = LocalDateTime.now();
		// 获取当天的历史任务，根据人员分组
		Map<Long, List<TaskHistory>> taskHistoryMap = taskHistoryService.list(
			Condition.getQueryWrapper(new TaskHistory())
				.lambda()
				.ge(TaskHistory::getCloseTime, today.toLocalDate().atStartOfDay())
				.le(TaskHistory::getCloseTime, today.plusDays(1L).toLocalDate()
					.atTime(0, 0, 0)
					.plusSeconds(-1))
				.isNotNull(TaskHistory::getUserId))
			.stream().collect(Collectors.groupingBy(TaskHistory::getUserId));
		// 对人员进行排序，根据历史任务完成数量升序排序(执行的任务数量越少，则优先分配)
		userList = userList.stream().sorted((user1, user2) -> {
			Integer user1_size = taskHistoryMap.getOrDefault(user1.getId(), new ArrayList<>()).size();
			Integer user2_size = taskHistoryMap.getOrDefault(user2.getId(), new ArrayList<>()).size();
			return user1_size.compareTo(user2_size);
		}).collect(Collectors.toList());
		// 循环人员列表，开始分配任务
		for (User user : userList) {
			// 获取该人员可分配的任务类型
			List<RoleTask> roleTaskList = roleTaskAllList.stream().filter(u -> {
				return Func.isNotEmpty(user.getRoleId()) && user.getRoleId().indexOf(Func.toStr(u.getRoleId())) > -1;
			}).collect(Collectors.toList());
			if (Func.isEmpty(roleTaskList)) {
				// 该人员没有可分配的任务
				continue;
			}
			// 获取该人员已分配的任务
			List<Task> taskList = taskAllList.stream().filter(u -> {
				return Func.isNotEmpty(u.getUserId()) && u.getUserId().equals(user.getId());
			}).collect(Collectors.toList());
			// 如果已分配的任务数量 大于等于 最大可分配数量配置，则跳过
			if (Func.isNotEmpty(taskList) && taskList.size() >= maxTaskSize) {
				continue;
			}
			Integer currentSize = taskList.size();
			List<Task> saveTaskList = new ArrayList<>();
			for (Task task : unTaskList) {
				// 如果当前任务类型不再人员角色所属范围内，则跳过
				RoleTask roleTask = roleTaskList.stream().filter(u -> {
					return task.getTaskTypeCd().equals(u.getTaskTypeCd());
				}).findFirst().orElse(null);
				if (Func.isEmpty(roleTask)) {
					continue;
				}
				// 记录该任务
				saveTaskList.add(task);
				currentSize++;
				if (currentSize >= maxTaskSize) {
					// 分配足够了，则结束
					break;
				}
			}
			// 分配任务
			this.changeUser(NodesUtil.join(saveTaskList, "taskId"), user.getId());
			// 将分配的任务，从未分配的任务中移除(避免分配给下一个人员)
			unTaskList.removeAll(saveTaskList);
			log.info("自动分配任务");
		}
		return true;
	}

	@Override
	public List<Task> getTakesBybillNo(String billNo) {
		return baseMapper.getTakesBybillNo("%" + billNo + "%");
	}

	@Override
	public List<TaskVO> list(QueryWrapper<Task> queryWrapper) {
		return null;
	}

	@Override
	public TaskVO getDetail(Long taskId) {
		return null;
	}
}
