package org.nodes.wms.pdaController.instock.receive;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.instock.InStockBiz;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveDetailLpnPdaMultiRequest;
import org.springblade.core.log.annotation.ApiLog;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * PDA按箱收货API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/receiveByMultiBox")
public class receiveByMultiBoxCode {
	private final InStockBiz inStockBiz;
	/**
	 * PDA多箱收货
	 */
	@ApiLog("PDA多箱  箱收货")
	@PostMapping("/receiveByMultiBoxCode")
	public String receiveByMultiBoxCode(@RequestBody ReceiveDetailLpnPdaMultiRequest receiveDetailLpnPdaMultiRequest) {
		inStockBiz.receiveByMultiBoxCode(receiveDetailLpnPdaMultiRequest);
		return "操作成功";
	}
}
