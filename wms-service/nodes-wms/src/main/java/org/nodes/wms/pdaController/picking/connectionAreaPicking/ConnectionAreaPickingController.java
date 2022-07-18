package org.nodes.wms.pdaController.picking.connectionAreaPicking;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.dao.picking.dto.input.ConnectionAreaPickingRequest;
import org.nodes.wms.dao.picking.dto.input.OutboundAccessAreaLocationQueryRequest;
import org.nodes.wms.dao.picking.dto.output.OutboundAccessAreaLocationQueryResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接驳区拣货API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/picking")
public class ConnectionAreaPickingController {

	/**
	 * 出库接驳区拣货库位查询
	 *
	 * @param request Pda根据编码查询库存-请求对象
	 * @return 出库接驳区拣货库位查询
	 */
	@ApiLog("PDA拣货分页查询")
	@PostMapping("/OutboundAccessAreaLocationQuery")
	public R<IPage<OutboundAccessAreaLocationQueryResponse>> outboundAccessAreaLocationQuery(@RequestBody OutboundAccessAreaLocationQueryRequest request) {
		return null;
	}


	/**
	 * 接驳区拣货
	 *
	 * @param request 接驳区拣货-请求对象
	 */
	@ApiLog("PDA拣货分页查询")
	@PostMapping("/ConnectionAreaPicking")
	public void ConnectionAreaPicking(@RequestBody ConnectionAreaPickingRequest request) {

	}


	/**
	 * 接驳区移动
	 *
	 * @param request 接驳区移动-请求对象
	 */
	@ApiLog("PDA拣货分页查询")
	@PostMapping("/ConnectionAreaMove")
	public void ConnectionAreaMove(@RequestBody ConnectionAreaPickingRequest request) {

	}

}
