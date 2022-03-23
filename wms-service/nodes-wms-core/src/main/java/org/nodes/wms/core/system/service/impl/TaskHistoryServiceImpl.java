
package org.nodes.wms.core.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.system.entity.TaskHistory;
import org.nodes.wms.core.system.mapper.TaskHistoryMapper;
import org.nodes.wms.core.system.service.ITaskHistoryService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务履历 服务实现类
 *
 * @author pengwei
 * @since 2019-12-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class TaskHistoryServiceImpl<M extends TaskHistoryMapper, T extends TaskHistory>
	extends BaseServiceImpl<TaskHistoryMapper, TaskHistory>
	implements ITaskHistoryService {

}
