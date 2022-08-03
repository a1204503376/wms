package org.nodes.wms.pdaController.picking.pickingByBox;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.outstock.OutStockBiz;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.PickingByBoxRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.PickingByBoxResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 按箱拣货Api
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/pickingByBox")
public class PdaPickingByBoxController {
	private final OutStockBiz outStockBiz;

	/**
	 * PDA按箱拣货：按箱拣货
	 *
	 * @param request 按箱拣货请求对象
	 * @return 按箱拣货响应对象
	 */
	@ApiLog("PDA按箱拣货")
	@PostMapping("/pickingByBox")
	public R<PickingByBoxResponse> pickingByBox(@RequestBody PickingByBoxRequest request) {
		return R.data(outStockBiz.pickingByBox(request));
	}
}
