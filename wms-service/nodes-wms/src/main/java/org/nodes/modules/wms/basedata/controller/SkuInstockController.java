package org.nodes.modules.wms.basedata.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.nodes.wms.core.basedata.service.ISkuInstockService;
import org.nodes.wms.core.basedata.vo.SkuInstockVO;
import org.nodes.wms.core.basedata.wrapper.SkuInstockWrapper;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 物品入库设置控制器
 * @create 20200330
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/basedata/skuInstock")
@Api(value = "物品入库设置", tags = "物品入库设置接口")
public class SkuInstockController {

	private ISkuInstockService skuInstockService;

	/**
	 * 物品入库设置列表查询
	 *
	 * @param skuInstock
	 * @return
	 */
	@ApiLog("物品入库设置接口-列表")
	@GetMapping("/list")
	@ApiOperation(value = "列表", notes = "传入skuInstock")
	public R<List<SkuInstockVO>> list(SkuInstock skuInstock) {
		List<SkuInstock> list = skuInstockService.list(
			Condition.getQueryWrapper(skuInstock).lambda().orderByDesc(SkuInstock::getUpdateTime));
		return R.data(SkuInstockWrapper.build().listVO(list));
	}

	/**
	 * 新增或修改 物品入库设置
	 */
	@ApiLog("物品入库设置接口-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入skuInstock")
	public R submit(@Valid @RequestBody SkuInstock skuInstock) {
		return R.status(skuInstockService.saveOrUpdate(skuInstock));
	}

	/**
	 * 删除 物品入库设置
	 */
	@ApiLog("物品入库设置接口-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(skuInstockService.removeByIds(Func.toLongList(ids)));
	}
}
