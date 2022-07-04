package org.nodes.modules.wms.core.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import org.nodes.wms.core.stock.core.service.IStockSnapshootService;
import org.nodes.wms.core.stock.core.vo.StockSnapshootVO;
import org.nodes.wms.core.stock.core.wrapper.StockSnapshootWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.stock.core.entity.StockSnapshoot;

import org.nodes.wms.core.stock.core.dto.StockSnapshootDTO;
import org.springblade.core.boot.ctrl.BladeController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 库存快照 控制器
 *
 * @author NodeX
 * @since 2021-05-28
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/core/stocksnapshoot")
@Api(value = "库存快照", tags = "库存快照接口")
public class StockSnapshootController extends BladeController {

	private IStockSnapshootService stockSnapshootService;

	/**
	 * 库存快照详情
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "库存快照详情", notes = "传入stockSnapshoot")
	public R<StockSnapshootVO> detail(StockSnapshootDTO stockSnapshoot) {
		StockSnapshoot detail = (StockSnapshoot) stockSnapshootService.getOne(Condition.getQueryWrapper(stockSnapshoot));
		return R.data(StockSnapshootWrapper.build().entityVO(detail));
	}

    /**
     * 库存快照列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "库存快照列表", notes = "传入stockSnapshoot")
    public R<List<StockSnapshootVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<StockSnapshoot> list = stockSnapshootService.list(Condition.getQueryWrapper(params, StockSnapshoot.class));
		return R.data(StockSnapshootWrapper.build().listVO(list));
    }

	/**
	 * 库存快照分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "库存快照分页", notes = "传入stockSnapshoot")
	public R<IPage<StockSnapshootVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<StockSnapshoot> pages = stockSnapshootService.page(Condition.getPage(query), Condition.getQueryWrapper(params, StockSnapshoot.class)
			.lambda().orderByDesc(StockSnapshoot::getSnapshootDate));
		return R.data(StockSnapshootWrapper.build().pageVO(pages));
	}


	/**
	 * 库存快照新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperation(value = "库存快照新增或修改", notes = "传入stockSnapshoot")
	public R submit(@Valid @RequestBody StockSnapshoot stockSnapshoot) {
		return R.status(stockSnapshootService.saveOrUpdate(stockSnapshoot));
	}


	/**
	 * 库存快照删除
	 */
	@PostMapping("/remove")
	@ApiOperation(value = "库存快照删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(stockSnapshootService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 库存快照
	 */
	@GetMapping("/stockSnapshoot")
	@ApiOperation(value = "库存快照", notes = "")
	public R stockSnapshoot() {
		return R.data(stockSnapshootService.stockSnapshoot());
	}

	/**
	 * 初始化库存快照
	 */
	@GetMapping("/initializeStockSnapshoot")
	@ApiOperation(value = "初始化库存快照", notes = "")
	public R initialize() {
		return R.data(stockSnapshootService.initializeStockSnapshoot());
	}


}
