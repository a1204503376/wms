package org.nodes.wms.pdaController.instock.putaway;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.putway.dto.input.PutawayByBoxRequest;
import org.nodes.wms.dao.putway.dto.output.CallAgvResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 呼叫AGV API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/putaway")
public class CallAgvController {
	private final StockBiz stockBiz;

	/**
	 * PDA根据箱码查询库存
	 *
	 * @param request 包含箱码和库房ID
	 * @return 上架信息
	 */
	@ApiLog("PDA根据箱码查询库存")
	@PostMapping("/findStockByBoxCode")
	public R<List<CallAgvResponse>> findStockByBoxCode(@RequestBody PutawayByBoxRequest request) {
		return R.data(stockBiz.findLpnStockOnStageLeftLikeByBoxCode(request.getWhId(), request.getBoxCode()));
	}

}
