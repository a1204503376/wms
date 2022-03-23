package org.nodes.wms.core.stock.transfer.service;

import org.nodes.wms.core.outstock.so.dto.NeedSkuDetailQueryDTO;
import org.nodes.wms.core.outstock.so.vo.NeedSkuDetailVO;
import org.nodes.wms.core.outstock.so.vo.NeedSkuVO;
import org.nodes.wms.core.stock.transfer.dto.ReplenishTaskDTO;
import org.nodes.wms.core.stock.transfer.entity.TransferHeader;
import org.springblade.core.mp.base.BaseService;

import java.util.List;

/**
 * 库内移动表头 服务类
 *
 * @author pengwei
 * @since 2020-08-03
 */
public interface ITransferHeaderService extends BaseService<TransferHeader> {

	/**
	 * 创建补货任务
	 *
	 * @param replenishTaskList 补货任务DTO
	 * @return 是否成功
	 */
	boolean createReplenishTask(List<ReplenishTaskDTO> replenishTaskList);

	/**
	 * 获取当前订单量
	 *
	 * @return 当前订单量
	 */
	List<NeedSkuVO> getNeedSku();

	/**
	 * 获取当前订单量指定物品的详细信息
	 *
	 * @param needSkuVO 当前订单量对象
	 * @return 指定物品详细订购信息
	 */
	List<NeedSkuDetailVO> getNeedSkuDetail(NeedSkuDetailQueryDTO needSkuVO);
}
