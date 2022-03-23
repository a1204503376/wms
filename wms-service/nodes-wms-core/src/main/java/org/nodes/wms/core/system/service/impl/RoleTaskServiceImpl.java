package org.nodes.wms.core.system.service.impl;

import lombok.AllArgsConstructor;
import org.nodes.wms.core.system.entity.RoleTask;
import org.nodes.wms.core.system.mapper.RoleTaskMapper;
import org.nodes.wms.core.system.service.IRoleTaskService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务角色关联表 服务实现类
 *
 * @author NodeX
 * @since 2020-06-08
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class RoleTaskServiceImpl<M extends RoleTaskMapper, T extends RoleTask>
	extends BaseServiceImpl<RoleTaskMapper, RoleTask>
	implements IRoleTaskService {

}
