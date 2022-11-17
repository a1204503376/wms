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
package org.nodes.modules.wms.count.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.service.ICountDetailService;
import org.nodes.wms.core.count.vo.CountDetailVO;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 盘点单明细表 控制器
 *
 * @author NodeX
 * @since 2020-01-02
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/count/countdetail")
@Api(value = "盘点单明细表", tags = "盘点单明细表接口")
public class CountDetailController extends BladeController {

	private ICountDetailService countDetailService;

	/**
	 * 盘点单明细表详情
	 */
	@ApiLog("盘点单明细表接口-盘点单明细表详情")
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "盘点单明细表详情", notes = "传入countDetail")
	public R<CountDetail> detail(CountDetail countDetail) {
		CountDetail detail = countDetailService.getOne(Condition.getQueryWrapper(countDetail));
		return R.data(detail);
	}

	/**
	 * 分页 盘点单明细表
	 */
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "盘点单明细表分页", notes = "传入countDetail")
	public R<IPage<CountDetail>> list(CountDetail countDetail, Query query) {
		IPage<CountDetail> pages = countDetailService.page(Condition.getPage(query), Condition.getQueryWrapper(countDetail));
		return R.data(pages);
	}

	/**
	 * 自定义分页 盘点单明细表
	 */
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "自定义分页盘点单明细表分页", notes = "传入countDetail")
	public R<IPage<CountDetailVO>> page(CountDetailVO countDetail, Query query) {
		IPage<CountDetailVO> pages = countDetailService.selectCountDetailPage(Condition.getPage(query), countDetail);
		return R.data(pages);
	}

	/**
	 * 新增 盘点单明细表
	 */
	@ApiLog("盘点单明细表接口-新增")
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入countDetail")
	public R save(@Valid @RequestBody CountDetail countDetail) {
		return R.status(countDetailService.save(countDetail));
	}

	/**
	 * 修改 盘点单明细表
	 */
	@ApiLog("盘点单明细表接口-修改盘点单明细表")
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改盘点单明细表", notes = "传入countDetail")
	public R update(@Valid @RequestBody CountDetail countDetail) {
		return R.status(countDetailService.updateById(countDetail));
	}

	/**
	 * 新增或修改 盘点单明细表
	 */
	@ApiLog("盘点单明细表接口-新增或修改 盘点单明细表")
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改 盘点单明细表", notes = "传入countDetail")
	public R submit(@Valid @RequestBody CountDetail countDetail) {
		return R.status(countDetailService.saveOrUpdate(countDetail));
	}


	/**
	 * 删除 盘点单明细表
	 */
	@ApiLog("盘点单明细表接口-删除盘点单明细表")
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除盘点单明细表", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(countDetailService.removeByIds(Func.toLongList(ids)));
	}


}
