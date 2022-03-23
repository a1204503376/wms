package org.nodes.modules.wms.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.nodes.wms.core.stock.core.vo.StockDetailVo;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 库存详细 控制器
 */
@RestController
@AllArgsConstructor
@RequestMapping("wms/core/stockDetail")
@Api(value = "库存详细",tags = "库存详细接口")
public class StockDetailController extends BladeController {

	IStockDetailService stockDetailService;
	/**
	 * 库存详细-锁定
	 */
	@ApiLog("库存详细接口-锁定")
	@PostMapping("/lock")
	@ApiOperation(value = "库存详细锁定")
	public R lock(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.data(stockDetailService.lock(Func.toLongList(ids)));
	}
	/**
	 * 分页查询库存
	 */
	@ApiLog("库存接口-库存明细分页")
	@GetMapping("/stockDetails")
	@ApiOperation(value = "库存明细分页", notes = "传入stock")
	public R<IPage<StockDetailVo>> stockDetails(@RequestParam Long stockId, Query query) {
		IPage<StockDetailVo> page = stockDetailService.selectStockDetailsByStockId(Condition.getPage(query), stockId);
		return R.data(page);
	}
	/**
	 * 库存详细-解锁
	 */
	@ApiLog("库存详细接口-库存解锁")
	@PostMapping("/unlock")
	@ApiOperation(value = "库存详细解锁")
	public R unlock(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.data(stockDetailService.unlock(Func.toLongList(ids)));
	}
}
