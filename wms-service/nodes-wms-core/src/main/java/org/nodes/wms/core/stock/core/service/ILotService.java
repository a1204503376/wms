
package org.nodes.wms.core.stock.core.service;

import org.nodes.wms.core.stock.core.dto.LotDTO;
import org.nodes.wms.core.stock.core.entity.Lot;
import org.springblade.core.mp.base.BaseService;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 批次号 服务类
 *
 * @author pengwei
 * @since 2019-12-25
 */
public interface ILotService extends BaseService<Lot> {

	/**
	 * 处理批次信息（如果提供了批次号，验证在数据库中是否存在，如果不存在则创建；提供了批属性，根据批属性查询批次信息）
	 * @param lotDTO 查询条件
	 * @return 批次号
	 */
	Lot chkLotNumber(LotDTO lotDTO);

	/**
	 *
	 * @param lotNumber
	 * @return
	 */
	boolean lock(String lotNumber);

	boolean unlock(String LotNumber);
}
