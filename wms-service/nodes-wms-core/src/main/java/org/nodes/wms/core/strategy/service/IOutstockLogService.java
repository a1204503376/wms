package org.nodes.wms.core.strategy.service;

import org.nodes.wms.core.strategy.dto.OutstockExecuteDTO;
import org.nodes.wms.core.strategy.entity.OutstockLog;
import org.springblade.core.mp.base.BaseService;

/**
 * 分配记录 服务类
 *
 * @author NodeX
 * @since 2020-05-06
 */
public interface IOutstockLogService extends BaseService<OutstockLog> {

	/**
	 * 保存分配日志
	 * @param outstockExecuteDTO 分配结果
	 * @return
	 */
	boolean save(OutstockExecuteDTO outstockExecuteDTO);
}
