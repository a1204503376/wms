package org.nodes.wms.core.strategy.service.impl;

import lombok.AllArgsConstructor;
import org.nodes.wms.core.strategy.dto.OutstockExecuteDTO;
import org.nodes.wms.core.strategy.entity.OutstockLog;
import org.nodes.wms.core.strategy.mapper.OutstockLogMapper;
import org.nodes.wms.core.strategy.service.IOutstockLogService;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 分配记录 服务实现类
 *
 * @author pengwei
 * @since 2020-05-06
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class OutstockLogServiceImpl<M extends OutstockLogMapper, T extends OutstockLog>
	extends BaseServiceImpl<OutstockLogMapper, OutstockLog>
	implements IOutstockLogService {



	@Override
	public boolean save(OutstockExecuteDTO outstockExecuteDTO) {
		// 存储分配记录
		OutstockLog outstockLog = new OutstockLog();
		outstockLog.setWellenNo(outstockExecuteDTO.getWellenNo());
		outstockLog.setSoBillNo(outstockExecuteDTO.getSoBillNo());
		outstockLog.setSsoId(outstockExecuteDTO.getSsoId());
		outstockLog.setSsoCode(outstockExecuteDTO.getSsoCode());
		outstockLog.setSsoName(outstockExecuteDTO.getSsoName());
		outstockLog.setWhId(outstockExecuteDTO.getWhId());
		outstockLog.setSsodProcOrder(outstockExecuteDTO.getSsodProcOrder());
		outstockLog.setOutstockFunction(outstockExecuteDTO.getOutstockFunction());
		outstockLog.setSolProcLog(outstockExecuteDTO.toString());
		outstockLog.setIsSuccess(outstockExecuteDTO.getSuccess() ? 1 : 0);
		return super.save(outstockLog);
	}

}
