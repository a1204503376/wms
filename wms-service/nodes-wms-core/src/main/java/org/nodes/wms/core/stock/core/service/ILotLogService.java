
package org.nodes.wms.core.stock.core.service;

import org.nodes.wms.core.stock.core.entity.Lot;
import org.nodes.wms.core.stock.core.entity.LotLog;
import org.springblade.core.mp.base.BaseService;

/**
 * 批次号日志 服务类
 *
 * @author pengwei
 * @since 2020-03-03
 */
public interface ILotLogService extends BaseService<LotLog> {
	/**
	 * 保存批次号日志
	 * @param lot 批次信息
	 * @return 是否成功
	 */
	Boolean save(Lot lot);
}
