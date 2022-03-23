package org.nodes.wms.core.stock.transfer.service;


import org.nodes.wms.core.relenishment.vo.RelDetailVo;
import org.nodes.wms.core.stock.core.dto.StockReplenishmentSubmitDTO;
import org.nodes.wms.core.stock.core.vo.StockReplenishmentVO;

import java.util.List;

/**
 * 补货服务接口
 */
public interface IStockReplenishmentService {

	/**
	 * 补货 获得补货明细信息
	 * @param taskId
	 * @return
	 */
	StockReplenishmentVO getReplenishmenInfo(String taskId);

	/**
	 * 补货 提交补货信息
	 * @param stockReplenishmentSubmitDTO
	 * @return
	 */
	boolean submitReplenishmen(StockReplenishmentSubmitDTO stockReplenishmentSubmitDTO);

	/**
	 * 补货-获得物品列表
	 * @param skuCode
	 * @return
	 */
	List<RelDetailVo> getSkuListByCode(String skuCode, String taskId);

	/**
	 * 补货-查询补货记录
	 * @param taskId
	 * @return
	 */
	StockReplenishmentVO getRelRecords(String taskId);
}
