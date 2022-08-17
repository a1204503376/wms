package org.nodes.wms.pdaController.picking.pickingByPcs;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.outstock.OutStockBiz;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.EsitSerialNumberRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.FindOpenSoDetailRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.PickByPcsRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.findSoHeaderByNoRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.EsitSerialNumberResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindPickingBySoBillIdResponse;
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
@RequestMapping(WmsApiPath.WMS_PDA_API + "/pickByPcs")
public class PdaPickingController {
	private final OutStockBiz outStockBiz;

	/**
	 * PDA拣货：拣货分页查询
	 *
	 * @param request Pda根据编码查询库存-请求对象
	 * @param query   分页条件
	 * @return 库存响应对象
	 */
	@PostMapping("/findAllPickingByNo")
	public R<IPage<FindAllPickingResponse>> findAllPickingByNo(@RequestBody findSoHeaderByNoRequest request, Query query) {
		return R.data(outStockBiz.findSoHeaderByNo(request, query));
	}

	/**
	 * PDA按件拣货：按件拣货
	 *
	 * @param request 按件拣货请求对象
	 */
	@ApiLog("PDA按件拣货")
	@PostMapping("/pickByPcs")
	public R<String> pickByPcs(@RequestBody PickByPcsRequest request) {
		outStockBiz.pickByPcs(request);
		return R.success("PDA按件拣货,拣货成功");
	}

	/**
	 * PDA拣货：拣货详情查询
	 *
	 * @param request Pda根据发货单ID查询出库单明细-请求对象
	 * @param query   分页参数
	 * @return 拣货详情响应对象
	 */
	@PostMapping("/findPickingBySoBillId")
	public R<IPage<FindPickingBySoBillIdResponse>> findPickingBySoBillId(@RequestBody FindOpenSoDetailRequest request, Query query) {
		return R.data(outStockBiz.findOpenSoDetail(request, query));
	}

	/**
	 * 判断当前物品是否是序列号管理
	 *
	 * @param request 判断当前物品是否是序列号管理请求对象
	 * @return 是否是序列号管理
	 */
	@PostMapping("/findEsitSerialNumberBySkuCode")
	public R<EsitSerialNumberResponse> findEsitSerialNumberBySkuCode(@RequestBody EsitSerialNumberRequest request) {
		return null;
	}


}
