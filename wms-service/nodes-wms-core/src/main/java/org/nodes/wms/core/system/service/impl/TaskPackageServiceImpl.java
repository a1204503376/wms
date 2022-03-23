
package org.nodes.wms.core.system.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.system.entity.TaskPackage;
import org.nodes.wms.core.system.mapper.TaskPackageMapper;
import org.nodes.wms.core.system.service.ITaskPackageService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工作任务包 服务实现类
 *
 * @author pengwei
 * @since 2019-12-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class TaskPackageServiceImpl<M extends TaskPackageMapper, T extends TaskPackage>
	extends BaseServiceImpl<TaskPackageMapper, TaskPackage>
	implements ITaskPackageService {

}
