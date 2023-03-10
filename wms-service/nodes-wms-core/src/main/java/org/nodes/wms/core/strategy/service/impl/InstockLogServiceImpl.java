package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.dao.putaway.entities.StInstockLog;
import org.nodes.wms.dao.putaway.mapper.StInstockLogMapper;
import org.nodes.wms.core.strategy.service.IInstockLogService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 上架记录 服务实现类
 *
 * @author NodeX
 * @since 2020-04-27
 */
@Service
@Primary
@Transactional(propagation= Propagation.NESTED,isolation= Isolation.DEFAULT,rollbackFor=Exception.class)
public class InstockLogServiceImpl<M extends StInstockLogMapper, T extends StInstockLog>
	extends BaseServiceImpl<StInstockLogMapper, StInstockLog>
	implements IInstockLogService {

}
