package org.nodes.modules.wms.outstock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.outstock.inventory.entity.SoInventory;
import org.nodes.wms.core.outstock.inventory.vo.SoInventoryVO;
import org.nodes.wms.core.outstock.inventory.wrapper.SoInventoryWrapper;
import org.nodes.wms.core.outstock.inventory.dto.SoInventoryDTO;
import org.nodes.wms.core.outstock.inventory.service.ISoInventoryService;
import org.springblade.core.boot.ctrl.BladeController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * 发货清单主表
 * 控制器
 *
 * @author NodeX
 * @since 2021-06-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/outstock/so-inventory")
@Api(value = "发货清单主表 ", tags = "发货清单主表接口")
public class SoInventoryController extends BladeController {

	private ISoInventoryService soInventoryService;

	/**
	 * 发货清单主表
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "发货清单主表详情", notes = "传入soInventory")
	public R<SoInventoryVO> detail(SoInventoryDTO soInventory) {
		return R.data(soInventoryService.getDetail(soInventory.getId()));
	}

	/**
	 * 发货清单主表
	 * 分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "发货清单主表分页", notes = "传入soInventory")
	public R<IPage<SoInventoryVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SoInventory> pages = soInventoryService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SoInventory.class));
		return R.data(SoInventoryWrapper.build().pageVO(pages));
	}
}
