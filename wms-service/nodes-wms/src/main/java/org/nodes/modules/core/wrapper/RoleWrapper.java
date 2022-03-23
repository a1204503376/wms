package org.nodes.modules.core.wrapper;

import org.nodes.core.base.entity.Role;
import org.nodes.core.base.vo.RoleVO;
import org.nodes.wms.core.system.entity.RoleTask;
import org.nodes.wms.core.system.service.IRoleTaskService;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * author: pengwei
 * date: 2021/5/14 11:35
 * description: RoleWrapper
 */
public class RoleWrapper extends org.nodes.core.base.wrapper.RoleWrapper {

	public static RoleWrapper build(){
		return new RoleWrapper();
	}

	@Override
	public RoleVO entityVO(Role role) {
		RoleVO roleVO = super.entityVO(role);
		if (Func.isNotEmpty(roleVO)) {
			IRoleTaskService roleTaskService = SpringUtil.getBean(IRoleTaskService.class);
			List<RoleTask> roleTask = roleTaskService.list(Condition.getQueryWrapper(new RoleTask()).lambda()
				.eq(RoleTask::getRoleId, roleVO.getId()));
			String taskTypeCd = "";
			List<Integer> taskTypeList = new ArrayList<>();
			if (roleTask.size() > 0) {
				for (RoleTask task : roleTask) {
					taskTypeList.add(task.getTaskTypeCd());
					taskTypeCd += task.getTaskTypeCd() + ",";
				}
				taskTypeCd.substring(0, taskTypeCd.length() - 1);
			}
			roleVO.setTaskTypeCd(taskTypeCd);
			if (Func.isNotEmpty(roleTask)) {
				roleVO.setTaskType(taskTypeList);
			}
		}
		return roleVO;
	}
}
