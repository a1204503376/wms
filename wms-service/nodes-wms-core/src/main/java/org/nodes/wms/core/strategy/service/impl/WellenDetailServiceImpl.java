package org.nodes.wms.core.strategy.service.impl;

import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.mapper.SkuMapper;
import org.nodes.wms.core.strategy.entity.WellenDetail;
import org.nodes.wms.core.strategy.vo.WellenDetailVO;
import org.nodes.wms.core.strategy.mapper.WellenDetailMapper;
import org.nodes.wms.core.strategy.service.IWellenDetailService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.tool.utils.Func;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.nodes.wms.core.strategy.wrapper.WellenDetailWrapper;
/**
 * 波次策略明细 服务实现类
 *
 * @author NodeX
 * @since 2021-05-26
 */
@Service("stWellenDetailServiceImpl")
@Primary
@Transactional(propagation = Propagation.NESTED,isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class WellenDetailServiceImpl<M extends SkuMapper, T extends Sku> extends BaseServiceImpl<WellenDetailMapper, WellenDetail> implements IWellenDetailService<T> {

}
