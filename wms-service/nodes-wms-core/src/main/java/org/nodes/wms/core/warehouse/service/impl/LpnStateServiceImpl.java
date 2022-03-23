package org.nodes.wms.core.warehouse.service.impl;

import org.nodes.wms.core.warehouse.entity.LpnState;
import org.nodes.wms.core.warehouse.vo.LpnStateVO;
import org.nodes.wms.core.warehouse.mapper.LpnStateMapper;
import org.nodes.wms.core.warehouse.service.ILpnStateService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.tool.utils.Func;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import org.nodes.wms.core.warehouse.wrapper.LpnStateWrapper;
/**
 * 容器状态表 服务实现类
 *
 * @author NodeX
 * @since 2021-10-12
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED,isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class LpnStateServiceImpl<M extends LpnStateMapper, T extends LpnState>
    extends BaseServiceImpl<LpnStateMapper, LpnState>
    implements ILpnStateService {

}
