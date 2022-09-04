package org.nodes.wms.pdaController.instock.putaway;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.putaway.PutawayBiz;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.putaway.dto.input.CallAgvRequest;
import org.nodes.wms.dao.putaway.dto.input.LpnTypeRequest;
import org.nodes.wms.dao.putaway.dto.input.FindPutawayDataByBoxCodeRequest;
import org.nodes.wms.dao.putaway.dto.output.CallAgvResponse;
import org.nodes.wms.dao.putaway.dto.output.LocResponse;
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
	private final PutawayBiz putawayBiz;
	private final StockQueryBiz stockQueryBiz;


	/**
	 * PDA根据箱码查询库存
	 *
	 * @param request 包含箱码和库房ID
	 * @return 上架信息
	 */
	@PostMapping("/findStockByBoxCode")
	public R<List<CallAgvResponse>> findStockByBoxCode(@RequestBody FindPutawayDataByBoxCodeRequest request) {
		return R.data(stockQueryBiz.findLpnStockOnStageLeftByCallAgv(request.getWhId(), request.getBoxCode()));
	}

	/**
	 * PDA根据箱型和库房查询库位
	 *
	 * @param request 包含箱型和库房ID
	 * @return 库位信息
	 */
	@PostMapping("/findLocByLpnType")
	public R<List<LocResponse>> findLocByLpnType(@RequestBody LpnTypeRequest request) {
		return R.data(putawayBiz.findLocByLpnType(request));
	}

	/**
	 * 呼叫Agv
	 *
	 * @param request
	 * @return
	 */
	@PostMapping("/callAgv")
	public String callAgv(@RequestBody CallAgvRequest request) {
		putawayBiz.callAgv(request);
		return "操作成功";
	}

}
