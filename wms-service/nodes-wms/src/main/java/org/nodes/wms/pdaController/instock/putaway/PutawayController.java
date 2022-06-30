package org.nodes.wms.pdaController.instock.putaway;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.instock.putaway.dto.input.PutawayByBoxDetailRequest;
import org.nodes.wms.dao.instock.putaway.dto.input.PutawayByBoxRequest;
import org.nodes.wms.dao.instock.putaway.dto.output.PutawayByBoxDetailResponse;
import org.nodes.wms.dao.instock.putaway.dto.output.PutawayByBoxResponse;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;


/**
 * 按箱上架API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/putaway")
public class PutawayController {
	private final StockBiz stockBiz;


	/**
	 * PDA根据箱码查询库存
	 * @param request  包含箱码和库房ID
	 * @return 上架信息
	 */
	@ApiLog("PDA根据箱码查询库存")
	@PostMapping("/findPutawayDataByBoxCode")
	public R<PutawayByBoxResponse> findPutawayDataByBoxCode(@RequestBody PutawayByBoxRequest request) {
		List<Stock> stockList = stockBiz.findStockOnStageByBoxCode(request.getWhId(), request.getBoxCode());
		BigDecimal qty = StockUtil.getStockBalance(stockList);
		PutawayByBoxResponse response=new PutawayByBoxResponse();
		response.setSkuLot1(stockList.get(0).getLotNumber());
		response.setBoxCode(stockList.get(0).getBoxCode());
		response.setQty(qty);
		return R.data(response);
	}

	/**
	 * @param request 条件
	 * @return 库位信息
	 */
	public R<PutawayByBoxDetailResponse> findLocationByBoxCode(@RequestBody PutawayByBoxDetailRequest request) {
		//TODO 调用根据策略生成库位

		//返回库位信息
		PutawayByBoxDetailResponse response = new PutawayByBoxDetailResponse();
		return R.data(response);
	}





}
