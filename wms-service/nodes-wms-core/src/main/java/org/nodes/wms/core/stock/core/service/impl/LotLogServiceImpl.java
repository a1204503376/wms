
package org.nodes.wms.core.stock.core.service.impl;

import org.nodes.wms.core.stock.core.entity.Lot;
import org.nodes.wms.core.stock.core.entity.LotLog;
import org.nodes.wms.core.stock.core.mapper.LotLogMapper;
import org.nodes.wms.core.stock.core.service.ILotLogService;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 批次号日志 服务实现类
 *
 * @author pengwei
 * @since 2020-03-03
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class LotLogServiceImpl<M extends LotLogMapper, T extends LotLog>
	extends BaseServiceImpl<LotLogMapper, LotLog>
	implements ILotLogService {

	@Autowired
	ISystemProcService systemProcService;

	@Override
	public Boolean save(Lot lot) {
		LotLog log = BeanUtil.copy(lot, LotLog.class);
		if (Func.isEmpty(log)) {
			return false;
		} else {
			log.setProType(1);
			return super.save(log);
		}
	}
}
