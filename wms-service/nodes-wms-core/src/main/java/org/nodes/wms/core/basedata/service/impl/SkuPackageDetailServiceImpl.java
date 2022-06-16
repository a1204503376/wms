package org.nodes.wms.core.basedata.service.impl;

import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.basedata.mapper.SkuPackageDetailMapper;
import org.nodes.wms.core.basedata.service.ISkuPackageDetailService;
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
 * @since 2019-12-06
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SkuPackageDetailServiceImpl<M extends SkuPackageDetailMapper, T extends SkuPackageDetail>
	extends BaseServiceImpl<SkuPackageDetailMapper, SkuPackageDetail>
	implements ISkuPackageDetailService {

}
