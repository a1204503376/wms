package org.nodes.wms.core.instock.asn.service.impl;

import org.nodes.wms.core.instock.asn.entity.ContainerLog;
import org.nodes.wms.core.instock.asn.mapper.ContainerLogMapper;
import org.nodes.wms.core.instock.asn.service.IContainerLogService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 清点记录 服务实现类
 *
 * @author NodeX
 * @since 2020-01-15
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class ContainerLogServiceImpl<M extends ContainerLogMapper, T extends ContainerLog>
	extends BaseServiceImpl<ContainerLogMapper, ContainerLog>
	implements IContainerLogService {

}
