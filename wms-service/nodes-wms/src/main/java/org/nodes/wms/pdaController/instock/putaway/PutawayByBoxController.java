package org.nodes.wms.pdaController.instock.putaway;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.putway.PutwayBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.putway.constant.PutwayConstant;
import org.nodes.wms.dao.putway.dto.input.AddByBoxShelfRequest;
import org.nodes.wms.dao.putway.dto.input.PutawayByBoxRequest;
import org.nodes.wms.dao.putway.dto.output.PutawayByBoxResponse;
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
public class PutawayByBoxController {
	private final StockBiz stockBiz;
	private final PutwayBiz putwayBiz;

	/**
	 * PDA根据箱码查询库存
	 *
	 * @param request 包含箱码和库房ID
	 * @return 上架信息
	 */
	@ApiLog("PDA根据箱码查询库存")
	@PostMapping("/findPutawayDataByBoxCode")
	public R<PutawayByBoxResponse> findPutawayDataByBoxCode(@RequestBody PutawayByBoxRequest request) {
		List<Stock> stockList = stockBiz.findStockOnStageByBoxCode(request.getWhId(), request.getBoxCode());
		BigDecimal qty = StockUtil.getStockBalance(stockList);
		PutawayByBoxResponse response = new PutawayByBoxResponse();
		if (stockList.get(BigDecimal.ZERO.intValue()).getBoxCode().indexOf(PutwayConstant.BOX_CODE_A) == BigDecimal.ZERO.intValue() ||
			stockList.get(BigDecimal.ZERO.intValue()).getBoxCode().indexOf(PutwayConstant.BOX_CODE_B) == BigDecimal.ZERO.intValue() ||
			stockList.get(BigDecimal.ZERO.intValue()).getBoxCode().indexOf(PutwayConstant.BOX_CODE_C) == BigDecimal.ZERO.intValue() ||
			stockList.get(BigDecimal.ZERO.intValue()).getBoxCode().indexOf(PutwayConstant.BOX_CODE_D) == BigDecimal.ZERO.intValue()
		) {
			response.setLpnCode(stockList.get(BigDecimal.ZERO.intValue()).getLpnCode());
		}
		response.setStockId(stockList.get(BigDecimal.ZERO.intValue()).getStockId());
		response.setBoxCode(stockList.get(BigDecimal.ZERO.intValue()).getBoxCode());
		response.setQty(qty);
		return R.data(response);
	}

	/**
	 * @param request 新建按箱上架的请求参数
	 */
	@ApiLog("PDA按箱上架")
	@PostMapping("/submitPutawayByBox")
	public void submitPutawayByBox(@RequestBody AddByBoxShelfRequest request) {
		putwayBiz.addByBoxShelf(request);
	}


}
