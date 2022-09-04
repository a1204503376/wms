package org.nodes.wms.pdaController.instock.putaway;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.putaway.PutawayBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.putaway.dto.input.FindPutawayDataByBoxCodeRequest;
import org.nodes.wms.dao.putaway.dto.input.PutwayByBoxRequest;
import org.nodes.wms.dao.putaway.dto.output.PutawayByBoxResponse;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 按箱上架API
 * @author nodes
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/putaway")
public class PutawayByBoxController {
	private final StockQueryBiz stockQueryBiz;
	private final PutawayBiz putawayBiz;

	/**
	 * PDA按箱上架：根据箱码查询库存
	 *
	 * @param request 包含箱码和库房ID
	 * @return 上架信息
	 */
	@PostMapping("/findPutawayDataByBoxCode")
	public R<PutawayByBoxResponse> findPutawayDataByBoxCode(@RequestBody FindPutawayDataByBoxCodeRequest request) {
		List<Stock> stockList = stockQueryBiz.findStockOnStageByBoxCode(request.getWhId(), request.getBoxCode());
		if (Func.isEmpty(stockList)) {
			throw new ServiceException("按箱上架失败,该箱不在入库暂存区无法上架");
		}

		BigDecimal qty = StockUtil.getStockBalance(stockList);
		PutawayByBoxResponse response = new PutawayByBoxResponse();
		response.setLpnCode(stockList.get(0).getLpnCode());
		List<Long> stockIds = stockList
			.stream()
			.map(Stock::getStockId)
			.filter(Func::isNotEmpty)
			.distinct()
			.collect(Collectors.toList());
		response.setStockId(stockIds.get(0));
		response.setBoxCode(stockList.get(0).getBoxCode());
		response.setQty(qty);
		return R.data(response);
	}

	/**
	 * PDA按箱上架：按箱上架提交
	 *
	 * @param request 新建按箱上架的请求参数
	 */
	@ApiLog("PDA按箱上架")
	@PostMapping("/submitPutawayByBox")
	public void submitPutawayByBox(@RequestBody PutwayByBoxRequest request) {
		putawayBiz.putawayByBox(request);
	}

}
