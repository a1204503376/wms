package org.nodes.wms.pdaController.stock.stockManage;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.stock.StockManageBiz;
import org.nodes.wms.dao.stock.dto.input.*;
import org.nodes.wms.dao.stock.dto.output.*;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PDA库存管理: 移动Api
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/stock/stockManage")
public class PdaStockManageController {
	private StockManageBiz stockManageBiz;

	/**
	 * PDA库存管理:标准移动-查询根据条件查询库存，判断库存是否唯一，并且是否序列号管理
	 *
	 * @param request Pda判断库存是否可移动-请求对象
	 * @return 库存是否可移动响应对象
	 */
	@PostMapping("/estimateStockMove")
	public R<EstimateStockMoveResponse> estimateStockMove(@RequestBody EstimateStockMoveRequest request) {
		return R.data(stockManageBiz.estimateStockMoveAction(request));
	}

	/**
	 * PDA库存管理:标准移动
	 *
	 * @param request Pda库存移动-请求对象
	 */
	@ApiLog("PDA库存管理:标准移动")
	@PostMapping("/stockMove")
	public void stockMove(@RequestBody StockMoveRequest request) {
		stockManageBiz.stockMoveAction(request);
	}

	/**
	 * PDA库存管理:LPN移动-查询根据条件查询库存，判断库存是否唯一，并且是否可移动
	 *
	 * @param request Pda判断库存是否可移动-请求对象
	 * @return 库存是否可移动响应对象
	 */
	@PostMapping("/estimateStockMoveByLpn")
	public R<EstimateStockMoveByLpnCodeResponse> estimateStockMoveByLpn(@RequestBody EstimateStockMoveByLpnCodeRequest request) {
		return R.data(stockManageBiz.estimateStockMoveByLpnAction(request));
	}

	/**
	 * PDA库存管理:LPN移动
	 *
	 * @param request Pda库存移动-请求对象
	 */
	@ApiLog("PDA库存管理:LPN移动")
	@PostMapping("/stockMoveByLpn")
	public void stockMoveByLpn(@RequestBody StockMoveByLpnRequest request) {
		stockManageBiz.stockMoveByLpnAction(request);
	}

	/**
	 * PDA库存管理:按箱移动-查询根据条件查询库存，判断库存是否唯一，并且是否可移动
	 *
	 * @param request Pda判断库存是否可移动-请求对象
	 * @return 库存是否可移动响应对象
	 */
	@PostMapping("/estimateStockMoveByBoxCode")
	public R<EstimateStockMoveByBoxCodeResponse> estimateStockMoveByBoxCode(@RequestBody EstimateStockMoveByBoxCodeRequest request) {
		return R.data(stockManageBiz.estimateStockMoveByBoxCodeAction(request));
	}

	/**
	 * PDA库存管理:按箱移动
	 *
	 * @param request Pda库存按箱移动-请求对象
	 */
	@ApiLog("PDA库存管理:按箱移动")
	@PostMapping("/stockMoveByBoxCode")
	public void stockMoveByBoxCode(@RequestBody StockMoveByBoxCodeRequest request) {
		stockManageBiz.stockMoveByBoxCodeAction(request);
	}
}
