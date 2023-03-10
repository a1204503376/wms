
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
import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.count.service.ICountHeaderService;
import org.nodes.wms.core.log.system.dto.SystemProcDTO;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.enums.ActionEnum;
import org.nodes.wms.core.log.system.enums.DataTypeEnum;
import org.nodes.wms.core.log.system.enums.SystemProcTypeEnum;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.nodes.wms.core.outstock.so.service.IWellenDetailService;
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
 * ???????????????
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
	 * ????????????
	 *
	 * @param ids    ????????????ID?????????
	 * @param userId ????????????ID
	 * @return ????????????
	 */
	@Override
	public boolean changeUser(String ids, Long userId) {
		User user = UserCache.getById(userId);
		if (Func.isEmpty(user)) {
			throw new ServiceException("????????????????????????");
		}
		List<Long> list = Func.toLongList(ids);
		ICountHeaderService countHeaderService = SpringUtil.getBean(ICountHeaderService.class);
		IWellenDetailService wellenDetailService = SpringUtil.getBean(IWellenDetailService.class);
		for (Long id : list) {
			Task task = getById(id);
			if (Func.isEmpty(task)) {
				throw new ServiceException(String.format("??????????????????????????????ID???%s??????", id));
			}
//			if(Func.isNotEmpty(task)&&"901".equals(task.getBillTypeCd())){
//				List<WellenSoHeaderVo> soHeaderByWellenId = wellenDetailService.getSoHeaderByWellenId(Func.toLongList(task.getBillId().toString()));
//				if(Func.isEmpty(soHeaderByWellenId)){
//					throw new ServiceException(String.format("??????????????????????????????"));
//				}
//				SoHeader soHeader = soHeaderByWellenId.get(0).getSoHeader();
//				if(!soHeader.getDeptId().equals(Func.toLong(user.getDeptId()))){
//					throw new ServiceException("????????????????????????????????????");
//				}
//			}
//			if(Func.isNotEmpty(task)&&"909".equals(task.getBillTypeCd())){
//				CountHeader countHeader = countHeaderService.getById(task.getBillId());
//				if(Func.isEmpty(countHeader)){
//					throw new ServiceException(String.format("??????????????????????????????"));
//				}
//				if(!countHeader.getCreateDept().equals(Func.toLong(user.getDeptId()))){
//					throw new ServiceException("????????????????????????????????????");
//				}
//			}
			task.setAllotTime(LocalDateTime.now());
			task.setUserId(user.getId());
			task.setUserCode(user.getAccount());
			task.setUserName(user.getRealName());
			super.updateById(task);
		}
		return true;
	}

	/**
	 * ????????????
	 *
	 * @param ids ??????????????????ID??????????????????
	 * @return ????????????
	 */
	@Override
	public boolean close(String ids) {
		List<Long> list = Func.toLongList(ids);
		for (Long id : list) {
			Task task = this.getById(id);
			if (Func.isEmpty(task)) {
				throw new ServiceException("??????????????????");
			}
			// ??????????????????
			SystemProcDTO systemProcParam = new SystemProcDTO();
			systemProcParam.setProcType(SystemProcTypeEnum.TASK);
			systemProcParam.setDataType(DataTypeEnum.TaskNo);
			systemProcParam.setAction(ActionEnum.CLOSE);
			systemProcParam.setBillNo(task.getTaskId().toString());
			systemProcParam.setWhId(task.getWhId());
			SystemProc systemProc = systemProcService.create(systemProcParam);
			TaskHistory taskHistory = BeanUtil.copy(task, TaskHistory.class);
			if (Func.isEmpty(taskHistory)) {
				throw new ServiceException("?????????????????????");
			}
			taskHistory.setUserId(AuthUtil.getUserId());
			taskHistory.setUserCode(AuthUtil.getUserAccount());
			taskHistory.setUserName(AuthUtil.getNickName());
			taskHistory.setBeginTime(task.getBeginTime());
			taskHistory.setAllotTime(task.getAllotTime());
			// ???????????????
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
			task.setTtpId(WmsAppConstant.TOP_PARENT_ID);
			task.setTaskProcType(TaskProcTypeEnum.Cooperation.getIndex());
		}
		task.setTaskTypeCd(taskDTO.getTaskType());
		task.setWhId(taskDTO.getWhId());
//		task.setBillTypeCd(taskDTO.getBillTypeCd());
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
	 * ??????????????????
	 *
	 * @param billId   ??????ID
	 * @param taskType ????????????
	 * @return ????????????
	 */
	@Override
	public boolean delete(Long billId, TaskTypeEnum taskType) {
		// ??????????????????
		Task taskQuery = new Task();
		taskQuery.setBillId(billId);
		taskQuery.setTaskTypeCd(taskType.getIndex());
		// ????????????
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
	 * ????????????id????????????
	 *
	 * @param billId       ??????ID
	 * @param taskTypeEnum ??????????????????
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
		// ????????????????????????
		Boolean autoTask = Func.toBoolean(ParamCache.getValue(ParamEnum.TASK_AUTO_ENABLE.getKey()));
		// ?????????????????????
		Integer maxTaskSize = Func.toInt(ParamCache.getValue(ParamEnum.TASK_AUTO_UPPER_LIMIT.getKey()));
		if (!autoTask || maxTaskSize < 1) {
			return false;
		}
		// ????????????????????????
		List<Task> taskAllList = super.list();
		if (Func.isEmpty(taskAllList)) {
			return false;
		}
		// ????????????????????????
		List<Task> unTaskList = taskAllList.stream().filter(u -> {
			return Func.isEmpty(u.getUserId());
		}).collect(Collectors.toList());
		if (Func.isEmpty(unTaskList)) {
			return false;
		}
		// ????????????????????????
		List<UserOnline> userOnlineList = userOnlineService.list(Condition.getQueryWrapper(new UserOnline())
			.lambda()
			.eq(UserOnline::getLoginStatus, LoginStatusEnum.ON_LINE.getIndex()));
		// ??????????????????
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
		// ?????????????????????????????????????????????????????????
		List<RoleTask> roleTaskAllList = roleTaskService.list(Condition.getQueryWrapper(new RoleTask())
			.lambda()
			.in(RoleTask::getRoleId, NodesUtil.toList(userList, User::getRoleId)));
		if (Func.isEmpty(roleTaskAllList)) {
			return false;
		}
		LocalDateTime today = LocalDateTime.now();
		// ????????????????????????????????????????????????
		Map<Long, List<TaskHistory>> taskHistoryMap = taskHistoryService.list(
			Condition.getQueryWrapper(new TaskHistory())
				.lambda()
				.ge(TaskHistory::getCloseTime, today.toLocalDate().atStartOfDay())
				.le(TaskHistory::getCloseTime, today.plusDays(1L).toLocalDate()
					.atTime(0, 0, 0)
					.plusSeconds(-1))
				.isNotNull(TaskHistory::getUserId))
			.stream().collect(Collectors.groupingBy(TaskHistory::getUserId));
		// ??????????????????????????????????????????????????????????????????(?????????????????????????????????????????????)
		userList = userList.stream().sorted((user1, user2) -> {
			Integer user1_size = taskHistoryMap.getOrDefault(user1.getId(), new ArrayList<>()).size();
			Integer user2_size = taskHistoryMap.getOrDefault(user2.getId(), new ArrayList<>()).size();
			return user1_size.compareTo(user2_size);
		}).collect(Collectors.toList());
		// ???????????????????????????????????????
		for (User user : userList) {
			// ???????????????????????????????????????
			List<RoleTask> roleTaskList = roleTaskAllList.stream().filter(u -> {
				return Func.isNotEmpty(user.getRoleId()) && user.getRoleId().indexOf(Func.toStr(u.getRoleId())) > -1;
			}).collect(Collectors.toList());
			if (Func.isEmpty(roleTaskList)) {
				// ?????????????????????????????????
				continue;
			}
			// ?????????????????????????????????
			List<Task> taskList = taskAllList.stream().filter(u -> {
				return Func.isNotEmpty(u.getUserId()) && u.getUserId().equals(user.getId());
			}).collect(Collectors.toList());
			// ?????????????????????????????? ???????????? ???????????????????????????????????????
			if (Func.isNotEmpty(taskList) && taskList.size() >= maxTaskSize) {
				continue;
			}
			Integer currentSize = taskList.size();
			List<Task> saveTaskList = new ArrayList<>();
			for (Task task : unTaskList) {
				// ?????????????????????????????????????????????????????????????????????
				RoleTask roleTask = roleTaskList.stream().filter(u -> {
					return task.getTaskTypeCd().equals(u.getTaskTypeCd());
				}).findFirst().orElse(null);
				if (Func.isEmpty(roleTask)) {
					continue;
				}
				// ???????????????
				saveTaskList.add(task);
				currentSize++;
				if (currentSize >= maxTaskSize) {
					// ???????????????????????????
					break;
				}
			}
			// ????????????
			this.changeUser(NodesUtil.join(saveTaskList, "taskId"), user.getId());
			// ???????????????????????????????????????????????????(??????????????????????????????)
			unTaskList.removeAll(saveTaskList);
			log.info("??????????????????");
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
