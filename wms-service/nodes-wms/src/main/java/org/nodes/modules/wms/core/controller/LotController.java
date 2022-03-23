package org.nodes.modules.wms.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.stock.core.entity.Lot;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.service.ILotService;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.nodes.wms.core.stock.core.vo.LotVO;
import org.nodes.wms.core.stock.core.wrapper.LotWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

/**
 * 批次号控制器
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/core/lot")
@Api(value = "批次号管理", tags = "批次号管理")
public class LotController extends BladeController {
	private ILotService lotService;

	/**
	 * 分页
	 */
	@ApiLog("批次号-分页")
	@GetMapping("/page")
	@ApiOperation(value = "批次号分页查询", notes = "传入条件、hasStock")
	@ApiImplicitParam(value = "hasStock", name = "hasStock为0时查询有库存， !=0查询无库存")
	public R<IPage<LotVO>> pages(@ApiIgnore @RequestParam Map<String, Object> params, Integer hasStock, Query query) {
		IStockDetailService stockDetailService = SpringUtil.getBean(IStockDetailService.class);
		List<StockDetail> stockDetailList = stockDetailService.list();
		LambdaQueryWrapper queryWrapper = Condition.getQueryWrapper(params, Lot.class)
			.lambda()
			.func(sql -> {
				if (Func.isNotEmpty(stockDetailList)) {
					List<String> lotNumberList = NodesUtil.toList(stockDetailList, StockDetail::getLotNumber);
					if (Func.equals(hasStock, 0)) {
						// 没有库存
						sql.notIn(Lot::getLotNumber, lotNumberList);
					} else {
						// 有库存
						sql.in(Lot::getLotNumber, lotNumberList);
					}
				}
			});

		IPage<Lot> pages = lotService.page(Condition.getPage(query), queryWrapper);
		//return R.(pages);
		return R.data(LotWrapper.build().pageVO(pages));
	}

	/**
	 * 批次锁定
	 */
	@ApiLog("批次接口—批次锁定")
	@PostMapping("/lock")
	@ApiOperation(value = "批次锁定")
	public R lock(@ApiParam(value = "批次号", required = true) @RequestParam String lotNumber){
		return R.data(lotService.lock(lotNumber));
	}

	/**
	 * 批次解锁
	 */
	@ApiLog("批次接口—批次解锁")
	@PostMapping("/unlock")
	@ApiOperation(value = "批次解锁")
	public R unlock(@ApiParam(value = "批次号", required = true) @RequestParam String lotNumber){
		return R.data(lotService.unlock(lotNumber));
	}
}
