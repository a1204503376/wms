package org.nodes.wms.pdaController.stock.freezeOrUnFreeze;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.stockManage.StockManageBiz;
import org.nodes.wms.dao.stock.dto.input.*;
import org.springblade.core.log.annotation.ApiLog;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 库存控制API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/stock/freezeOrUnFreeze")
public class PdaFreezeOrUnFreezeController {
	private final StockManageBiz stockControlBiz;

	/**
	 * 按库位冻结
	 *
	 * @param request 请求对象-内部包含库位
	 */
	@ApiLog("PDA按库位冻结")
	@PostMapping("freezeByLocCode")
	public void freezeByLocCode(@RequestBody FreezeByLocCodeRequest request) {
		stockControlBiz.freezeByLocCodeAction(request.getLocCode(), request.getWhId());
	}

	/**
	 * 按批次号冻结
	 *
	 * @param request 请求对象-内部包含批次号
	 */
	@ApiLog("PDA按批次号冻结")
	@PostMapping("freezeByLotNumber")
	public void freezeByLotNumber(@RequestBody FreezeByLotNumberRequest request) {
		stockControlBiz.freezeByLotNumberAction(request.getLotNumber());
	}

	/**
	 * 按序列号冻结
	 *
	 * @param request 请求对象-内部包含序列号
	 */
	@ApiLog("PDA按批次号冻结")
	@PostMapping("freezeBySerialNumber")
	public void freezeBySerialNumber(@RequestBody FreezeBySerialNumberRequest request) {
		stockControlBiz.freezeBySerialNumberAction(request.getSerialNumber());
	}

	/**
	 * 按物品、批次、库位部分冻结：部分冻结
	 *
	 * @param request 请求对象-内部包含 物品编码-库位编码-批次号-序列号
	 */
	@ApiLog("PDA部分冻结")
	@PostMapping("portionFreeze")
	public void portionFreeze(@RequestBody PortionFreezeRequest request) {
		stockControlBiz.portionFreezeAction(request);
	}

	/**
	 * 按箱码冻结
	 *
	 * @param request 请求对象-内部包含箱码
	 */
	@ApiLog("PDA按箱码冻结")
	@PostMapping("freezeByBoxCode")
	public void freezeByBoxCode(@RequestBody FreezeByBoxCodeRequest request) {
		stockControlBiz.freezeStockByBoxCodeAction(request.getBoxCode());
	}

	/**
	 * 按库位解冻
	 *
	 * @param request 请求对象-内部包含库位
	 */
	@ApiLog("PDA按库位解冻")
	@PostMapping("unFreezeByLocCode")
	public void unFreezeByLocCode(@RequestBody UnFreezeByLocCodeRequest request) {
		stockControlBiz.unFreezeByLocCodeAction(request.getLocCode(),request.getWhId());
	}

	/**
	 * 按批次号解冻
	 *
	 * @param request 请求对象-内部包含批次号
	 */
	@ApiLog("PDA按批次号解冻")
	@PostMapping("unFreezeByLotNumber")
	public void unFreezeByLotNumber(@RequestBody UnFreezeByLotNumberRequest request) {
		stockControlBiz.unFreezeByLotNumberAction(request.getLotNumber());
	}

	/**
	 * 按序列号解冻
	 *
	 * @param request 请求对象-内部包含序列号
	 */
	@ApiLog("PDA按序列号解冻")
	@PostMapping("unFreezeBySerialNumber")
	public void unFreezeBySerialNumber(@RequestBody UnFreezeBySerialNumberRequest request) {
		stockControlBiz.unFreezeBySerialNumberAction(request.getSerialNumber());
	}

	/**
	 * 按物品、批次、库位部分解冻：部分解冻
	 *
	 * @param request 请求对象-内部包含 物品编码-库位编码-批次号-序列号
	 */
	@ApiLog("PDA部分解冻")
	@PostMapping("portionUnFreeze")
	public void portionUnFreeze(@RequestBody PortionUnFreezeRequest request) {
		stockControlBiz.portionUnFreezeAction(request);
	}

	/**
	 * 按箱码解冻
	 *
	 * @param request 请求对象-内部包含箱码
	 */
	@ApiLog("PDA按箱码解冻")
	@PostMapping("unfreezeByBoxCode")
	public void unfreezeByBoxCode(@RequestBody FreezeByBoxCodeRequest request) {
		stockControlBiz.unFreezeStockByBoxCodeAction(request.getBoxCode());
	}

}
