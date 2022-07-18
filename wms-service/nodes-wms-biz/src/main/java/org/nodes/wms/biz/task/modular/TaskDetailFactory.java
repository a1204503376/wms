package org.nodes.wms.biz.task.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.task.dto.SyncTaskStateRequest;
import org.nodes.wms.dao.task.entities.TaskDetail;
import org.nodes.wms.dao.task.enums.TaskDetailStatusEnum;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskDetailFactory {
	public TaskDetail create(SyncTaskStateRequest request) {
		AssertUtil.notNull(request, "创建任务详情对象失败,请求对象为空");
		TaskDetail detail = new TaskDetail();
		detail.setId(request.getTaskDetailId());
		detail.setTaskDetailStatus(TaskDetailStatusEnum.valuesOf(request.getState()));
		if (Func.isNotEmpty(request.getTaskHeaderId())) {
			detail.setTaskHeaderId(request.getTaskHeaderId());
		}
		return detail;
	}
}
