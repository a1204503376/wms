package org.nodes.wms.pdaController.stock.freezeOrUnFreeze;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.stockControl.StockControlBiz;
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
	private final StockControlBiz stockControlBiz;

	/**
	 * 按库位冻结
	 *
	 * @param request 请求对象-内部包含库位
	 */
	@ApiLog("PDA按库位冻结")
	@PostMapping("freezeByLocCode")
	public void freezeByLocCode(@RequestBody FreezeByLocCodeRequest request) {
		stockControlBiz.freezeByLocCodeAction(request);
	}

	/**
	 * 按批次号冻结
	 *
	 * @param request 请求对象-内部包含批次号
	 */
	@ApiLog("PDA按批次号冻结")
	@PostMapping("freezeByLotNumber")
	public void freezeByLotNumber(@RequestBody FreezeByLotNumberRequest request) {
		stockControlBiz.freezeByLotNumberAction(request);
	}

	/**
	 * 按序列号冻结
	 *
	 * @param request 请求对象-内部包含序列号
	 */
	@ApiLog("PDA按批次号冻结")
	@PostMapping("freezeBySerialNumber")
	public void freezeBySerialNumber(@RequestBody FreezeBySerialNumberRequest request) {
		stockControlBiz.freezeBySerialNumberAction(request);
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
	 * 按库位解冻
	 *
	 * @param request 请求对象-内部包含库位
	 */
	@ApiLog("PDA按库位解冻")
	@PostMapping("unFreezeByLocCode")
	public void unFreezeByLocCode(@RequestBody UnFreezeByLocCodeRequest request) {
		stockControlBiz.unFreezeByLocCodeAction(request);
	}

	/**
	 * 按批次号解冻
	 *
	 * @param request 请求对象-内部包含批次号
	 */
	@ApiLog("PDA按批次号解冻")
	@PostMapping("unFreezeByLotNumber")
	public void unFreezeByLotNumber(@RequestBody UnFreezeByLotNumberRequest request) {
		stockControlBiz.unFreezeByLotNumberAction(request);
	}

	/**
	 * 按序列号解冻
	 *
	 * @param request 请求对象-内部包含序列号
	 */
	@ApiLog("PDA按批次号解冻")
	@PostMapping("unFreezeBySerialNumber")
	public void unFreezeBySerialNumber(@RequestBody UnFreezeBySerialNumberRequest request) {
		stockControlBiz.unFreezeBySerialNumberAction(request);
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


}
