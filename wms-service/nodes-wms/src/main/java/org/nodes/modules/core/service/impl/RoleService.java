package org.nodes.modules.core.service.impl;

import lombok.AllArgsConstructor;
import org.nodes.core.base.entity.Role;
import org.nodes.core.base.mapper.RoleMapper;
import org.nodes.core.base.vo.RoleVO;
import org.nodes.wms.core.system.entity.RoleTask;
import org.nodes.wms.core.system.service.IRoleTaskService;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.nodes.modules.core.service.IRoleService;

import java.io.Serializable;
import java.util.Collection;

/**
 * author: pengwei
 * date: 2021/5/14 10:49
 * description: RoleService
 */
@Service
@AllArgsConstructor
public class RoleService extends org.nodes.core.base.service.impl.RoleServiceImpl<RoleMapper, Role> implements IRoleService {

	@Autowired
	IRoleTaskService roleTaskService;

	@Override
	public boolean submit(RoleVO role) {
		boolean result = super.submit(role);

		roleTaskService.remove(Condition.getQueryWrapper(new RoleTask()).lambda()
			.eq(RoleTask::getRoleId, role.getId()));
		if (Func.isNotEmpty(role.getTaskType())) {
			for (Integer taskType : role.getTaskType()) {
				RoleTask roleTask = new RoleTask();
				roleTask.setRoleId(role.getId());
				roleTask.setTaskTypeCd(taskType);
				roleTaskService.save(roleTask);
			}
		}
		return result;
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		boolean result = super.removeByIds(idList);
		if (result) {
			roleTaskService.remove(Condition.getQueryWrapper(new RoleTask()).lambda()
				.in(RoleTask::getRoleId, idList));
		}
		return result;
	}
}
