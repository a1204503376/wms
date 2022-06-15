package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.mapper.SkuMapper;
import org.nodes.wms.core.strategy.entity.Mission;
import org.nodes.wms.core.strategy.mapper.MissionMapper;
import org.nodes.wms.core.strategy.service.IMissionService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务实现类
 *
 * @author NodeX
 * @since 2021-05-24
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED,isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class MissionServiceImpl<M extends SkuMapper, T extends Sku> extends BaseServiceImpl<MissionMapper, Mission> implements IMissionService<T> {

}
