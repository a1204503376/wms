package org.nodes.wms.pdaController.picking.pickingByPcs;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.picking.pickingByPcs.PickingByPcsBiz;
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
 * 按件拣货Api
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/pickingByPcs")
public class PdaPickingController {
	private final PickingByPcsBiz pickingByPcsBiz;

	/**
	 * PDA拣货：拣货分页查询
	 *
	 * @param request Pda根据编码查询库存-请求对象
	 * @param query   分页条件
	 * @return 库存响应对象
	 */
	@PostMapping("/findAllPickingByNo")
	public R<IPage<FindAllPickingResponse>> findAllPickingByNo(@RequestBody FindAllPickingRequest request, Query query) {
		return R.data(pickingByPcsBiz.selectAllPickingByNo(request, query));
	}

	/**
	 * PDA按件拣货：按件拣货
	 *
	 * @param request 按件拣货请求对象
	 * @return 按件拣货响应对象
	 */
	@ApiLog("PDA按件拣货")
	@PostMapping("/pickingByPcs")
	public R<PickingByBoxResponse> pickingByPcs(@RequestBody PickingByBoxRequest request) {
		return R.data(pickingByPcsBiz.pickingByPcsAction(request));
	}

	/**
	 * PDA拣货：拣货详情查询
	 *
	 * @param request Pda根据发货单ID查询出库单明细-请求对象
	 * @return 拣货详情响应对象
	 */
	@PostMapping("/findAllPickingByNo")
	public R<IPage<FindAllPickingResponse>> findPickingBySoBillId(@RequestBody FindAllPickingRequest request) {
		return null;
	}

}
