
package org.nodes.wms.core.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.nodes.core.base.enums.LoginStatusEnum;
import org.nodes.wms.core.system.entity.Task;
import org.nodes.wms.core.system.entity.UserOnline;
import org.nodes.wms.core.system.entity.UserRegister;
import org.nodes.wms.core.system.enums.UserTypeEnum;
import org.nodes.wms.core.system.mapper.UserOnlineMapper;
import org.nodes.wms.core.system.service.ITaskService;
import org.nodes.wms.core.system.service.IUserOnlineService;
import org.nodes.wms.core.system.service.IUserRegisterService;
import org.nodes.wms.core.system.vo.UserRegisterVO;
import org.nodes.wms.core.log.system.enums.PlatformEnum;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 在线用户 服务实现类
 *
 * @author pengwei
 * @since 2020-04-01
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class UserOnlineServiceImpl<M extends UserOnlineMapper, T extends UserOnline>
	extends BaseServiceImpl<UserOnlineMapper, UserOnline>
	implements IUserOnlineService {

	@Autowired
	IUserRegisterService userRegisterService;

	public UserRegisterVO sign(UserOnline userOnline) {
		UserRegisterVO uservo = new UserRegisterVO();
		//新增或修改在线用户
		userOnline.setUserId(AuthUtil.getUserId());
		userOnline.setUserCode(AuthUtil.getUserAccount());
		userOnline.setUserName(AuthUtil.getNickName());
		userOnline.setUserType(UserTypeEnum.CUSTOM.getIndex());
		userOnline.setOnlineTerminal(PlatformEnum.PDA.getName());
		userOnline.setToken(userOnline.getToken());
		userOnline.setLastLoginTime(LocalDateTime.now());
		userOnline.setLoginStatus(userOnline.getLoginStatus());
		this.saveOrUpdate(userOnline);
		//新增日志在线
		UserRegister reg = new UserRegister();
		reg.setUserId(AuthUtil.getUserId());
		reg.setUserCode(StringPool.ONE);
		reg.setUserName(AuthUtil.getNickName());
		reg.setUserType(UserTypeEnum.CUSTOM.getIndex());
		reg.setOnlineTerminal(PlatformEnum.PDA.getName());
		reg.setToken(userOnline.getToken());
		reg.setLastLoginTime(LocalDateTime.now());
		reg.setLoginStatus(userOnline.getLoginStatus());
		userRegisterService.save(reg);
		//获取插入对象和日志列表
		uservo.setUserOnline(this.getById(userOnline.getSuoId()));

		List<UserRegister> userRegisterList = userRegisterService.list(
			Condition.getQueryWrapper(new UserRegister()).lambda()
				.eq(UserRegister::getUserId, AuthUtil.getUserId())
				.apply("DATE_FORMAT(last_login_time, '%Y-%m-%d') = DATE_FORMAT({0}, '%Y-%m-%d')",
					LocalDateTime.now())
				.orderByDesc(UserRegister::getLastLoginTime));

		uservo.setUserRegister(userRegisterList);
		ITaskService taskService = SpringUtil.getBean(ITaskService.class);
		if (LoginStatusEnum.ON_LINE.getIndex().equals(userOnline.getLoginStatus())) {
			// 分配任务
			taskService.autoTask(AuthUtil.getUserId());
		} else {
			// 清空任务
			UpdateWrapper<Task> taskUpdateWrapper = new UpdateWrapper<>();
			taskUpdateWrapper.lambda()
				.set(Task::getUserId, null)
				.set(Task::getUserCode, null)
				.set(Task::getUserName, null)
				.eq(Task::getUserId, AuthUtil.getUserId());
			taskService.update(taskUpdateWrapper);
		}
		return uservo;
	}
}
