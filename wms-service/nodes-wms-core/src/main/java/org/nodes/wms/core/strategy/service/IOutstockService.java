
package org.nodes.wms.core.strategy.service;

import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.outstock.so.dto.CreatePickPlanByWellenDTO;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.strategy.dto.OutstockDTO;
import org.nodes.wms.core.strategy.dto.OutstockExecuteDTO;
import org.nodes.wms.core.strategy.entity.Outstock;
import org.nodes.wms.core.strategy.vo.OutstockVO;
import org.springblade.core.mp.base.BaseService;

/**
 * 服务类
 *
 * @author NodeX
 * @since 2019-12-10
 */
public interface IOutstockService extends BaseService<Outstock> {
	/**
	 * 新增分配策略
	 *
	 * @param outstockDTO OutstockDTO对象
	 * @return 是否成功
	 */
	boolean save(OutstockDTO outstockDTO);

	/**
	 * 修改分配策略
	 *
	 * @param outstockDTO OutstockDTO对象
	 * @return 是否成功
	 */
	boolean updateById(OutstockDTO outstockDTO);

	/**
	 * 新增或修改 分配策略
	 *
	 * @param outstockDTO 分配策略
	 * @return 是否成功
	 */
	boolean saveOrUpdate(OutstockDTO outstockDTO);

	/**
	 * 根据 ssoId 获取 OutstockDTO 对象
	 *
	 * @param ssoId OutstockDTO 主键ID
	 * @return OutstockDTO 对象
	 */
	OutstockVO getOne(Long ssoId);

	/**
	 * 执行分配策略
	 *
	 * @param soHeader 订单
	 * @param soDetail 订单明细
	 * @param sku      物品
	 * @return 执行结果
	 */
	OutstockExecuteDTO execute(SoHeader soHeader, SoDetail soDetail, Sku sku);
	/**
	 * 手动执行分配策略
	 *
	 * @param soHeader 订单
	 * @param soDetail 订单明细
	 * @param sku      物品
	 * @return 执行结果
	 */
	OutstockExecuteDTO manualExecute(SoHeader soHeader, SoDetail soDetail, Sku sku, boolean isWellen, CreatePickPlanByWellenDTO dto);

}
