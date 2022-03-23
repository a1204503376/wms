
package org.nodes.wms.core.basedata.service.impl;

import org.nodes.wms.core.basedata.entity.SkuReplace;
import org.nodes.wms.core.basedata.mapper.SkuReplaceMapper;
import org.nodes.wms.core.basedata.service.ISkuReplaceService;
import org.nodes.wms.core.basedata.vo.SkuReplaceVO;
import org.nodes.wms.core.basedata.wrapper.SkuReplaceWrapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 物品替代 服务实现类
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SkuReplaceServiceImpl<M extends SkuReplaceMapper, T extends SkuReplace>
	extends BaseServiceImpl<SkuReplaceMapper, SkuReplace>
	implements ISkuReplaceService {

	@Override
	public List<SkuReplaceVO> listBySkuId(Long skuId) {
		return SkuReplaceWrapper.build().listVO(baseMapper.selectSkuReplacBySkuId(skuId));
	}
}
