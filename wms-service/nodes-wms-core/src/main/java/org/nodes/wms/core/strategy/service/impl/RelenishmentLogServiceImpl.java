package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.core.strategy.entity.RelenishmentLog;
import org.nodes.wms.core.strategy.mapper.RelenishmentLogMapper;
import org.nodes.wms.core.strategy.service.IRelenishmentLogService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 补货记录 服务实现类
 *
 * @author NodeX
 * @since 2020-04-27
 */
@Service
@Primary
@Transactional(propagation= Propagation.NESTED,isolation= Isolation.DEFAULT,rollbackFor=Exception.class)
public class RelenishmentLogServiceImpl<M extends RelenishmentLogMapper, T extends RelenishmentLog>
	extends BaseServiceImpl<RelenishmentLogMapper, RelenishmentLog>
	implements IRelenishmentLogService {

}
