
package org.nodes.wms.core.basedata.service.impl;

import org.nodes.wms.dao.basics.sku.entities.SkuInc;
import org.nodes.wms.core.basedata.mapper.SkuIncMapper;
import org.nodes.wms.core.basedata.service.ISkuIncService;
import org.nodes.wms.core.basedata.vo.SkuIncVO;
import org.nodes.wms.core.basedata.wrapper.SkuIncWrapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 物品供应商关联 服务实现类
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SkuIncServiceImpl<M extends SkuIncMapper, T extends SkuInc>
	extends BaseServiceImpl<SkuIncMapper, SkuInc>
	implements ISkuIncService {

	@Override
	public List<SkuIncVO> listBySkuId(Long skuId) {
		return SkuIncWrapper.build().listVO(baseMapper.selectSkuIncBySkuId(skuId));
	}
}
