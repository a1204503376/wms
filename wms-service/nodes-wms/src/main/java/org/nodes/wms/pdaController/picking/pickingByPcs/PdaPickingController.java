package org.nodes.wms.pdaController.picking.pickingByPcs;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.outstock.OutStockBiz;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.*;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.EsitSerialNumberResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindAllPickingResponse;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.FindPickingBySoBillIdResponse;
import org.nodes.wms.dao.outstock.soPickPlan.dto.input.FindPickPlanBySoBillIdAndBoxCodeRequest;
import org.nodes.wms.dao.outstock.soPickPlan.dto.output.FindPickPlanBySoBillIdAndBoxCodeResponse;
import org.nodes.wms.dao.stock.dto.output.FindBoxCountResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


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
	public R<Boolean> pickByPcs(@RequestBody PickByPcsRequest request) {
		return R.data(outStockBiz.pickByPcs(request));
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
	 * PDA零散拣货：拣货详情查看包含拣货计划详情
	 *
	 * @param request Pda根据发货单ID查询出库单明细-请求对象
	 * @param query   分页参数
	 * @return 拣货详情响应对象
	 */
	@PostMapping("/findPickPlanBySoBillId")
	public R<IPage<FindPickingBySoBillIdResponse>> findPickPlanBySoBillId(@RequestBody FindOpenSoDetailRequest request, Query query) {
		return R.data(outStockBiz.findPickPlanBySoBillId(request, query));
	}

	/**
	 * PDA零散拣货：根据发货单和箱码查询对应拣货计划数据
	 *
	 * @param request 根据发货单和箱码查询对应数据-请求对象
	 * @return 拣货计划详情响应对象
	 */
	@PostMapping("/findPickPlanBySoBillIdAndBoxCode")
	public R<List<FindPickPlanBySoBillIdAndBoxCodeResponse>> findPickPlanBySoBillIdAndBoxCode(@RequestBody FindPickPlanBySoBillIdAndBoxCodeRequest request) {
		return R.data(outStockBiz.getPickPlanBySoBillIdAndBoxCode(request));
	}

	/**
	 * 零散拣货
	 *
	 * @param request 请求对象
	 * @return true:全部拣货 false:部分拣货
	 */
	@PostMapping("/bulkPick")
	public R<Boolean> bulkPick(@RequestBody BulkPickRequest request) {
		return R.data(outStockBiz.bulkPick(request));
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

	/**
	 * PDA复核: 查询发货单
	 *
	 * @param request Pda根据编码查询库存-请求对象
	 * @param query   分页条件
	 * @return 库存响应对象
	 */
	@PostMapping("/outStockCheckoutFindSoBill")
	public R<IPage<FindAllPickingResponse>> outStockCheckoutFindSoBill(@RequestBody findSoHeaderByNoRequest request, Query query) {
		return R.data(outStockBiz.outStockCheckoutFindSoBill(request, query));
	}

	/**
	 * 出库复核
	 */
	@PostMapping("/outStockCheckout")
	public R<String> outStockCheckout(@RequestBody OutStockCheckoutRequest request) {
		outStockBiz.outStockCheckout(request.getSoBillId(), request.getBoxCode(), request.getBoxCodeList());
		return R.data(request.getBoxCode());
	}

	/**
	 * 出库复核查询箱子有几个的接口
	 */
	@PostMapping("/selectBoxCountBySoHeaderId")
	public R<FindBoxCountResponse> selectBoxCountBySoHeaderId(@RequestBody FindBoxCountBySoHeaderIdRequest request) {
		return R.data(outStockBiz.findBoxCountBySoHeaderIds(request.getSoBillId()));
	}

}
