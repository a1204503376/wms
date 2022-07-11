package org.nodes.wms.pdaController.instock.putaway;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.putway.PutwayBiz;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.putway.dto.input.CallAgvRequest;
import org.nodes.wms.dao.putway.dto.input.LpnTypeRequest;
import org.nodes.wms.dao.putway.dto.input.PutawayByBoxRequest;
import org.nodes.wms.dao.putway.dto.output.CallAgvResponse;
import org.nodes.wms.dao.putway.dto.output.LocResponse;
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
	private final PutwayBiz putwayBiz;
	private final StockBiz stockBiz;


	/**
	 * PDA根据箱码查询库存
	 *
	 * @param request 包含箱码和库房ID
	 * @return 上架信息
	 */
	@PostMapping("/findStockByBoxCode")
	public R<List<CallAgvResponse>> findStockByBoxCode(@RequestBody PutawayByBoxRequest request) {
		return R.data(stockBiz.findLpnStockOnStageLeftByCallAgv(request.getWhId(), request.getBoxCode()));
	}

	/**
	 * PDA根据箱型和库房查询库位
	 *
	 * @param request 包含箱型和库房ID
	 * @return 库位信息
	 */
	@PostMapping("/findLocByLpnType")
	public R<List<LocResponse>> findLocByLpnType(@RequestBody LpnTypeRequest request) {
		return R.data(putwayBiz.findLocByLpnType(request));
	}

	/**
	 * 呼叫Agv
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("/callAgv")
	public String callAgv(@RequestBody CallAgvRequest request) {
		putwayBiz.callAgv(request);
		return "操作成功";
	}

}
