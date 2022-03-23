package org.nodes.modules.wms.api.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.stock.core.dto.StockMoveByBoxQueryDTO;
import org.nodes.wms.core.stock.core.dto.StockMoveByBoxSubmitDTO;
import org.nodes.wms.core.stock.core.dto.StockReplenishmentSubmitDTO;
import org.nodes.wms.core.stock.core.vo.StockMoveByBoxVO;
import org.nodes.wms.core.stock.core.vo.StockPackVO;
import org.nodes.wms.core.stock.transfer.service.IStockMoveByBoxService;
import org.nodes.wms.core.stock.transfer.service.IStockReplenishmentService;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 库内操作控制层
 *
 * @Author zx
 * @Date 2020/7/14
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/api/ApiPDA/stockInner")
@Api(value = "手持接口", tags = "库内操作接口")
public class StockInnerController {

	//库内打包
	private IStockMoveByBoxService stockMoveByBoxService;
	private IStockReplenishmentService stockReplenishmentService;

	/**
	 * 待拣货列表
	 */
	@ApiLog("PDA-待拣货列表")
	@GetMapping("/upPickList")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "待拣货列表", notes = "")
	public R upPickList(StockPackVO vo) {
		return R.data(null);
	}

	/**
	 * 提交拣货信息
	 */
	@ApiLog("PDA-提交拣货信息")
	@PostMapping("/submitPickInfo")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "提交拣货信息", notes = "")
	public R submitPickInfo(@RequestBody StockPackVO vo) {
		return R.data(null);
	}

	/**
	 * 待打包物品
	 */
	@ApiLog("PDA-待打包物品")
	@PostMapping("/upPack")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "待打包物品", notes = "")
	public R upPackList(@RequestBody StockPackVO vo) {
		return R.data(null);
	}

	/**
	 * 提交打包信息
	 */
	@ApiLog("PDA-提交打包信息")
	@PostMapping("/submitPackInfo")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "提交打包信息", notes = "")
	public R<List<StockPackVO>> submitPackInfo(@RequestBody(required = true) List<StockPackVO> list) {
		return R.data(null);
	}

	/**
	 * 打包物品列表
	 */
	@ApiLog("PDA-打包物品列表")
	@GetMapping("/packStockList")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "打包物品列表", notes = "")
	public R packStockList(StockPackVO vo) {
		return R.data(null);
	}

	/**
	 * 按箱移动 查询待移动库存
	 */
	@ApiLog("PDA-按箱移动查询待移动库存")
	@GetMapping("/getMoveStock")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "按箱移动-查询待移动库存", notes = "")
	public R getMoveStock(StockMoveByBoxQueryDTO stockMoveByBoxQueryDTO) {
		StockMoveByBoxVO moveStock = stockMoveByBoxService.getMoveStock(stockMoveByBoxQueryDTO);
		if (Func.isEmpty(moveStock)) {
			return R.fail("没有查到该物品库存或超出库存数量！");
		} else {
			return R.data(moveStock);
		}
	}

	/**
	 * 按箱移动 物品移动验证
	 */
	@ApiLog("PDA-按箱移动物品移动验证")
	@PostMapping("/verifyStockForMoveByBox")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "按箱移动-物品移动验证", notes = "")
	public R verifyStockForMoveByBox(@RequestBody List<StockMoveByBoxSubmitDTO> stockMoveByBoxSubmitDTOList) {
		return R.data(stockMoveByBoxService.verifyStockForMoveByBox(stockMoveByBoxSubmitDTOList));
	}

	/**
	 * 按箱移动 提交库存移动信息
	 */
	@ApiLog("PDA-按箱移动提交库存移动信息")
	@PostMapping("/submitStockForMoveByBox")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "按箱移动-提交库存移动信息", notes = "")
	public R submitStockForMoveByBox(@RequestBody List<StockMoveByBoxSubmitDTO> stockMoveByBoxSubmitDTOList) {
		return R.status(stockMoveByBoxService.submitStockForMoveByBox(stockMoveByBoxSubmitDTOList));
	}

	/**
	 * 补货-获得补货明细信息
	 */
	@ApiLog("PDA-补货获得补货明细信息")
	@GetMapping("/getReplenishmenInfo")
	@ApiOperation(value = "补货-获得补货明细信息", notes = "传入任务ID")
	public R getReplenishmenInfo(@Validated @NotEmpty @RequestParam String taskId) {
		return R.data(stockReplenishmentService.getReplenishmenInfo(taskId));
	}

	/**
	 * 补货-通过物品编码获得补货任务明细中的物品列表
	 */
	@ApiLog("PDA-补货通过物品编码获得补货任务明细中的物品列表")
	@GetMapping("/getSkuListByCode")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "补货-通过物品编码获得补货任务明细中的物品列表", notes = "传入物品编码,任务ID")
	public R getSkuListByCode(@Validated @NotEmpty @RequestParam String skuCode, @NotEmpty @RequestParam String taskId) {
		return R.data(stockReplenishmentService.getSkuListByCode(skuCode, taskId));
	}

	/**
	 * 补货-查询已完成补货任务明细
	 */
	@ApiLog("PDA-补货查询已完成补货任务明细")
	@GetMapping("/getUnfinishReplenishmenList")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "补货-查询已完成补货任务明细", notes = "传入任务ID")
	public R getUnfinishReplenishmenList(@Validated @NotEmpty @RequestParam String taskId) {
		return R.data(stockReplenishmentService.getRelRecords(taskId));
	}

	/**
	 * 补货-提交补货信息
	 */
	@ApiLog("PDA-补货提交补货信息")
	@PostMapping("/submitReplenishmen")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "补货-提交补货信息", notes = "")
	public R submitReplenishmen(@RequestBody StockReplenishmentSubmitDTO stockReplenishmentSubmitDTO) {
		return R.data(stockReplenishmentService.submitReplenishmen(stockReplenishmentSubmitDTO));
	}


}
