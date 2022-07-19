package org.nodes.wms.pdaController.picking.picking;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.dao.picking.dto.input.FindAllPickingRequest;
import org.nodes.wms.dao.picking.dto.input.PickingByBoxRequest;
import org.nodes.wms.dao.picking.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.picking.dto.output.PickingByBoxResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 拣货Api
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/picking")
public class PdaPickingController {

	/**
	 * 拣货分页查询
	 *
	 * @param request Pda根据编码查询库存-请求对象
	 * @param query   分页条件
	 * @return 库存响应对象
	 */
	@ApiLog("PDA拣货分页查询")
	@PostMapping("/findAllPickingByNo")
	public R<IPage<FindAllPickingResponse>> findAllPickingByNo(@RequestBody FindAllPickingRequest request, Query query) {
		return null;
	}

	/**
	 * 按箱拣货
	 *
	 * @param request 按箱拣货请求对象
	 * @return 按箱拣货响应对象
	 */
	@ApiLog("PDA按箱拣货")
	@PostMapping("/pickingByBox")
	public R<PickingByBoxResponse> pickingByBox(PickingByBoxRequest request) {
		return null;
	}

	/**
	 * 按件拣货
	 *
	 * @param request 按件拣货请求对象
	 * @return 按件拣货响应对象
	 */
	@ApiLog("PDA按件拣货")
	@PostMapping("/pickingByPcs")
	public R<PickingByBoxResponse> pickingByPcs(PickingByBoxRequest request) {
		return null;
	}
}