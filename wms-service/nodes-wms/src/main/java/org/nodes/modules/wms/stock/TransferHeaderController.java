package org.nodes.modules.wms.stock;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.outstock.so.dto.NeedSkuDetailQueryDTO;
import org.nodes.wms.core.outstock.so.vo.NeedSkuDetailVO;
import org.nodes.wms.core.outstock.so.vo.NeedSkuVO;
import org.nodes.wms.core.stock.transfer.dto.ReplenishTaskDTO;
import org.nodes.wms.core.stock.transfer.service.ITransferHeaderService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库内移动表头 控制器
 *
 * @author pengwei
 * @since 2020-08-03
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/stock/transfer-header")
@Api(value = "库内移动表头", tags = "库内移动表头接口")
public class TransferHeaderController extends BladeController {

	private ITransferHeaderService transferHeaderService;

	/**
	 * 创建补货任务
	 */
	@ApiLog("创建补货任务")
	@PostMapping("/createReplenishTask")
	@ApiOperation(value = "创建补货任务", notes = "传入需要补货的明细集合")
	public R createReplenishTask(@RequestBody List<ReplenishTaskDTO> list) {
		return R.data(transferHeaderService.createReplenishTask(list));
	}

	/**
	 * 获取当前订单量
	 *
	 * @return
	 */
	@ApiLog("出库管理主接口-获取当前订单量")
	@GetMapping("/getNeedSku")
	public R<List<NeedSkuVO>> getNeedSku() {
		return R.data(transferHeaderService.getNeedSku());
	}

	/**
	 * 获取当前订单量指定物品的详细信息
	 *
	 * @return
	 */
	@ApiLog("出库管理主接口-获取当前订单量指定物品的详细信息")
	@GetMapping("/getNeedSkuDetail")
	@ApiOperation(value = "物品详细订购信息", notes = "当前订单量对象")
	public R<List<NeedSkuDetailVO>> getNeedSkuDetail(NeedSkuDetailQueryDTO detailQuery) {
		return R.data(transferHeaderService.getNeedSkuDetail(detailQuery));
	}
}
