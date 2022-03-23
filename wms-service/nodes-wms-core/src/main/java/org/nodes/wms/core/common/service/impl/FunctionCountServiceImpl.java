package org.nodes.wms.core.common.service.impl;

import org.nodes.wms.core.common.entity.FunctionCount;
import org.nodes.wms.core.common.vo.FunctionCountVO;
import org.nodes.wms.core.common.mapper.FunctionCountMapper;
import org.nodes.wms.core.common.service.IFunctionCountService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.tool.utils.Func;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.nodes.wms.core.common.wrapper.FunctionCountWrapper;
/**
 * 功能计数	 服务实现类
 *
 * @author NodeX
 * @since 2021-07-23
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED,isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class FunctionCountServiceImpl<M extends FunctionCountMapper, T extends FunctionCount>
    extends BaseServiceImpl<FunctionCountMapper, FunctionCount>
    implements IFunctionCountService {

}
