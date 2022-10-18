package org.nodes.wms.core.outstock.loading.service.impl;

import org.nodes.wms.dao.truck.entities.TruckStock;
import org.nodes.wms.dao.truck.mapper.TruckStockMapper;
import org.nodes.wms.core.outstock.loading.service.ITruckStockService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *  服务实现类
 *
 * @author pengwei
 * @since 2020-04-16
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class TruckStockServiceImpl<M extends TruckStockMapper, T extends TruckStock>
	extends BaseServiceImpl<TruckStockMapper, TruckStock>
	implements ITruckStockService {


}
