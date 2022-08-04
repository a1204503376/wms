package org.nodes.wms.pdaController.basics;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.basics.skulot.SkuLotBiz;
import org.nodes.wms.biz.basics.systemParam.SystemParamBiz;
import org.nodes.wms.dao.basics.skulot.dto.input.FindAllSkuLotRequest;
import org.nodes.wms.dao.basics.skulot.dto.output.FindAllSkuLotResponse;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * PDA批属性API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/skuLot")
public class PdaSkuLotController {
	private final SystemParamBiz systemParamBiz;
	private final SkuLotBiz skuLotBiz;

	@PostMapping("/findAllSkuLotByWoId")
	public R<FindAllSkuLotResponse> findAllSkuLotByWoId(@RequestBody FindAllSkuLotRequest request) {
			FindAllSkuLotResponse response = skuLotBiz.selectsAllSkuLotByWoId(request);
		int numberOfOpen = systemParamBiz.findSkuLotNumberOfOpen();
		response.setNumberOfOpen(numberOfOpen);
		return R.data(response);
	}
}
