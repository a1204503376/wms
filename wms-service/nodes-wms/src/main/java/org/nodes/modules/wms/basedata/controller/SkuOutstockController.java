package org.nodes.modules.wms.basedata.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.basedata.entity.SkuOutstock;
import org.nodes.wms.core.basedata.service.ISkuOutstockService;
import org.nodes.wms.core.basedata.vo.SkuOutstockVO;
import org.nodes.wms.core.basedata.wrapper.SkuOutstockWrapper;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 物品出库设置控制器
 * @create 20200330
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/basedata/skuOutstock")
@Api(value = "物品出库设置", tags = "物品出库设置接口")
public class SkuOutstockController {

	private ISkuOutstockService skuOutstockService;

	/**
	 * 物品出库设置列表查询
	 *
	 * @param skuOutstock
	 * @return
	 */
	@ApiLog("物品出库设置接口-列表")
	@GetMapping("/list")
	@ApiOperation(value = "列表", notes = "传入skuOutstock")
	public R<List<SkuOutstockVO>> list(SkuOutstock skuOutstock) {
		List<SkuOutstock> list = skuOutstockService.list(
			Condition.getQueryWrapper(skuOutstock).lambda().orderByDesc(SkuOutstock::getUpdateTime));
		return R.data(SkuOutstockWrapper.build().listVO(list));
	}

	/**
	 * 新增或修改 物品出库设置
	 */
	@ApiLog("物品出库设置接口-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入skuOutstock")
	public R submit(@Valid @RequestBody SkuOutstock skuOutstock) {
		return R.status(skuOutstockService.saveOrUpdate(skuOutstock));
	}

	/**
	 * 删除 物品出库设置
	 */
	@ApiLog("物品出库设置接口-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(skuOutstockService.removeByIds(Func.toLongList(ids)));
	}
}
