package org.nodes.wms.pdaController.instock.receive;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.instock.InStockBiz;
import org.nodes.wms.biz.instock.receive.ReceiveBiz;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailLpnPdaRequest;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailLpnPdaResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

/**
 * PDA按箱收货API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/receiveByBox")
public class ReceiveByBoxController {
	private final ReceiveBiz receiveBiz;
	private final InStockBiz inStockBiz;

	/**
	 * PDA按箱收货查询
	 */
	@ApiLog("PDA按箱收货查询")
	@GetMapping("/query")
	public R<ReceiveDetailLpnPdaResponse> query(String boxCode) {
        return R.data(receiveBiz.getReceiveDetailLpnByBoxCode(boxCode));
	}

	/**
	 * PDA按箱收货
	 */
	@ApiLog("PDA按箱收货")
	@PostMapping("/receiveByCode")
	public String receiveByBoxCode(@RequestBody ReceiveDetailLpnPdaRequest receiveDetailLpnPdaRequest) {
		inStockBiz.receiveByBoxCode(receiveDetailLpnPdaRequest,null);
		return "操作成功";
	}



}
