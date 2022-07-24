package org.nodes.wms.biz.stock;

import org.nodes.wms.dao.stock.dto.input.*;
import org.nodes.wms.dao.stock.dto.output.EstimateStockMoveByBoxCodeResponse;
import org.nodes.wms.dao.stock.dto.output.EstimateStockMoveByLpnCodeResponse;
import org.nodes.wms.dao.stock.dto.output.EstimateStockMoveResponse;

/**
 * 库存管理-移动Biz
 */
public interface StockManageBiz {
	/**
	 * PDA库存管理:标准移动-查询根据条件查询库存，判断库存是否唯一，并且是否序列号管理
	 *
	 * @param request Pda判断库存是否可移动-请求对象
	 * @return 库存是否可移动响应对象
	 */
	EstimateStockMoveResponse estimateStockMoveAction(EstimateStockMoveRequest request);

	/**
	 * PDA库存管理:标准移动
	 *
	 * @param request Pda库存移动-请求对象
	 */
	void stockMoveAction(StockMoveRequest request);

	/**
	 * PDA库存管理:LPN移动-查询根据条件查询库存，判断库存是否唯一，并且是否可移动
	 *
	 * @param request Pda判断库存是否可移动-请求对象
	 * @return 库存是否可移动响应对象
	 */
	EstimateStockMoveByLpnCodeResponse estimateStockMoveByLpnAction(EstimateStockMoveByLpnCodeRequest request);

	/**
	 * PDA库存管理:LPN移动
	 *
	 * @param request Pda库存移动-请求对象
	 */
	void stockMoveByLpnAction(StockMoveByLpnRequest request);

	/**
	 * PDA库存管理:按箱移动-查询根据条件查询库存，判断库存是否唯一，并且是否可移动
	 *
	 * @param request Pda判断库存是否可移动-请求对象
	 * @return 库存是否可移动响应对象
	 */
	EstimateStockMoveByBoxCodeResponse estimateStockMoveByBoxCodeAction(EstimateStockMoveByBoxCodeRequest request);

	/**
	 * PDA库存管理:按箱移动
	 *
	 * @param request Pda库存按箱移动-请求对象
	 */
	void stockMoveByBoxCodeAction(StockMoveByBoxCodeRequest request);
}
