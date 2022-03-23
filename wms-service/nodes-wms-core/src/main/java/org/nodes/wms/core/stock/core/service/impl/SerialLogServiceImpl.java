package org.nodes.wms.core.stock.core.service.impl;

import org.nodes.wms.core.stock.core.entity.Serial;
import org.nodes.wms.core.stock.core.entity.SerialLog;
import org.nodes.wms.core.stock.core.enums.SerialLogProcTypeEnum;
import org.nodes.wms.core.stock.core.mapper.SerialLogMapper;
import org.nodes.wms.core.stock.core.service.ISerialLogService;
import org.nodes.wms.core.stock.core.wrapper.SerialLogWrapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 序列号日志 服务实现类
 *
 * @author zx
 * @since 2020-02-24
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class SerialLogServiceImpl<E extends SerialLogMapper, T extends SerialLog>
	extends BaseServiceImpl<SerialLogMapper, SerialLog>
	implements ISerialLogService {

}
