package org.nodes.modules.wms.api.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.stock.transfer.service.IStockInnerService;
import org.nodes.wms.core.stock.core.vo.SerialVO;
import org.nodes.wms.core.stock.core.vo.StockSkuMoveSubmitVO;
import org.nodes.wms.core.stock.core.vo.StockSkuMoveVO;
import org.nodes.wms.core.outstock.so.service.IPickPlanService;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 库存移动
 *
 * @Author zx
 * @Date 2020/3/10
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/ApiPDA/StockMove")
@Api(value = "库存移动", tags = "手持库存移动接口")
public class StockSkuMoveController {

	//库存接口
	private IStockInnerService stockInnerService;
	//拣货
	private IPickPlanService pickService;

	/**
	 * 查询库房列表
	 * @param
	 * @return
	 */
	@ApiLog("PDA-查询库房列表")
	@GetMapping("/getWarehouses")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "查询库房列表", notes = "")
	public R<List<StockSkuMoveVO>> getWarehouses() {
		StockSkuMoveSubmitVO stockSkuMove = new StockSkuMoveSubmitVO();
		return R.data(stockInnerService.listWarehouse(stockSkuMove));
	}

	/**
	 * 查询库位编码
	 *
	 * @param stockSkuMove
	 * @return
	 */
	@ApiLog("PDA-查询库位编码")
	@GetMapping("/getLocCode")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "查询库位编码", notes = "传入容器编码")
	public R<StockSkuMoveVO> getLocCode(StockSkuMoveSubmitVO stockSkuMove) {
		return R.data(stockInnerService.getLocCodeByLpnCode(stockSkuMove));
	}

	/**
	 * 查询物品信息
	 * @param stockSkuMove
	 * @return
	 */
	@ApiLog("PDA-查询物品信息")
	@GetMapping("/getSkuInfo")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "查询物品信息", notes = "传入物品")
	public R<StockSkuMoveVO> getSkuInfo(StockSkuMoveSubmitVO stockSkuMove) {
		return R.data(stockInnerService.getSkuInfoBySkuId(stockSkuMove));
	}

	/**
	 * 提交物品信息
	 * @param stockSkuMove
	 * @return
	 */
	@ApiLog("PDA-提交物品信息")
	@PostMapping("/submitSku")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "提交物品信息", notes = "")
	public R<StockSkuMoveVO> submitSku(@RequestBody StockSkuMoveSubmitVO stockSkuMove) {
		return R.data(stockInnerService.verifySkuQty(stockSkuMove));
	}

	/**
	 * 提交批次信息
	 * @param stockSkuMove
	 * @return
	 */
	@ApiLog("PDA-提交批次信息")
	@PostMapping("/submitLotNumberSku")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "提交批次信息", notes = "")
	public R<StockSkuMoveVO> submitLotNumberSku(@RequestBody StockSkuMoveSubmitVO stockSkuMove) {
		return R.data(stockInnerService.verifyLotQty(stockSkuMove));
	}

	/**
	 * 物品移动提交
	 * @param stockSkuMove
	 * @return
	 */
	@ApiLog("PDA-物品移动提交")
	@PostMapping("/submitMoveStockSku")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "物品移动提交", notes = "")
	public R submitMoveStockSku(@Valid @RequestBody StockSkuMoveSubmitVO stockSkuMove) {
		return R.status(stockInnerService.moveStockSku(stockSkuMove));
	}

	/**
	 * 容器序列号验证
	 */
	@ApiLog("PDA-容器序列号验证")
	@GetMapping("/isHasSerial")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "容器序列号验证", notes = "传入容器编码，物品ID，序列号")
	public R isHasSerial(SerialVO serialVO) {
		return R.status(pickService.lpnHasSerial(serialVO));
	}
}
