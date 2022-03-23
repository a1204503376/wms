/*
 *      Copyright (c) 2018-2028, Nodes All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Nodes
 */
package org.nodes.modules.wms.basedata.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.modules.wms.basedata.service.ISkuService;
import org.nodes.wms.core.basedata.dto.SkuDTO;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.excel.SkuExcel;
import org.nodes.wms.core.basedata.vo.SkuVO;
import org.nodes.wms.core.basedata.wrapper.SkuWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.nodes.wms.core.basedata.cache.SkuCache.SKU_CACHE;

/**
 * 物品 控制器
 *
 * @author pengwei
 * @since 2019-12-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/basedata/sku")
@Api(value = "物品", tags = "物品接口")
public class SkuController extends BladeController {

	ISkuService skuService;

	/**
	 * 详情
	 */
	@ApiLog("物品接口-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入sku")
	public R<SkuVO> detail(@NotNull SkuDTO sku) {
		return R.data(skuService.getDetail(sku));
	}

	/**
	 * 物品列表查询
	 *
	 * @param params
	 * @return
	 */
	@ApiLog("物品接口-列表")
	@GetMapping("/list")
	@ApiOperation(value = "列表", notes = "传入sku")
	public R<List<SkuVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<Sku> skuList = skuService.list(Condition.getQueryWrapper(params, Sku.class));
		return R.data(SkuWrapper.build().listVO(skuList));
	}

	/**
	 * 分页 物品
	 */
	@ApiLog("物品接口-自定义分页")
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入sku")
	public R<IPage<SkuVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Sku> pages = skuService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Sku.class));
		return R.data(SkuWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改 物品
	 */
	@ApiLog("物品接口-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入sku")
	public R submit(@Valid @RequestBody SkuDTO sku) {
		CacheUtil.clear(SKU_CACHE);
		return R.status(skuService.saveOrUpdate(sku));
	}

	/**
	 * 编辑时验证 物品
	 */
	@ApiLog("物品接口-获取物品是否允许编辑")
	@GetMapping("/edit-valid")
	@ApiOperation(value = "获取物品是否允许编辑", notes = "传入sku")
	public R editValid(@Valid @ApiParam(value = "物品ID", required = true) @RequestParam Long skuId) {
		return R.status(skuService.editValid(skuId));
	}

	/**qualityDateType
	 * 删除 物品
	 */
	@ApiLog("物品接口-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CacheUtil.clear(SKU_CACHE);
		return R.status(skuService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 导出
	 */
	@ApiLog("物品管理-导出")
	@GetMapping("export")
	@ApiOperation(value = "导出", notes = "查询条件")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		skuService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@ApiLog("物品管理-导出模板")
	@GetMapping("export-template")
	@ApiOperation(value = "导出模板")
	public void exportTemplate(HttpServletResponse response) {
		List<SkuExcel> skuExportList = new ArrayList<>();
		ExcelUtil.export(response, "物品", "物品数据表", skuExportList, SkuExcel.class);
	}

	/**
	 * 导入验证
	 */
	@ApiLog("物品管理-导入验证")
	@PostMapping("import-valid")
	@ApiOperation(value = "导入验证")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(skuService.validExcel(ExcelUtil.read(file, SkuExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@ApiLog("物品管理-导入数据")
	@PostMapping("import-data")
	@ApiOperation(value = "导入数据")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(skuService.importData(dataVerifyList));
	}
}
