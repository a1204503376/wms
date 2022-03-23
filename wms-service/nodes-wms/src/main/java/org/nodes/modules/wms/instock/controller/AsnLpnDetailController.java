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
package org.nodes.modules.wms.instock.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.instock.asn.entity.AsnLpnDetail;
import org.nodes.wms.core.instock.asn.service.IAsnLpnDetailService;
import org.nodes.wms.core.instock.asn.vo.AsnLpnDetailVO;
import org.nodes.wms.core.instock.asn.wrapper.AsnLpnDetailWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 入库容器明细 控制器
 *
 * @author zhonglianshuai
 * @since 2020-02-14
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/instock/asnLpnDetail")
@Api(value = "", tags = "接口")
public class AsnLpnDetailController extends BladeController {

	private IAsnLpnDetailService lpnDetailService;

	/**
	 * 入库容器明细详情
	 */
	@ApiLog("入库容器接口-入库容器明细详情")
	@GetMapping("/detail")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "详情", notes = "传入lpnDetail")
	public R<AsnLpnDetail> detail(AsnLpnDetail lpnDetail) {
		AsnLpnDetail detail = lpnDetailService.getOne(Condition.getQueryWrapper(lpnDetail));
		return R.data(detail);
	}

	/**
	 * 入库容器明细列表，传回AsnLpnDetail列表
	 */
	@ApiLog("入库容器接口-入库容器明细列表，传回AsnLpnDetail列表")
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "分页", notes = "传入lpnDetail")
	public R<IPage<AsnLpnDetail>> list(AsnLpnDetail lpnDetail, Query query) {
		IPage<AsnLpnDetail> pages = lpnDetailService.page(Condition.getPage(query), Condition.getQueryWrapper(lpnDetail));
		return R.data(pages);
	}

	/**
	 * 入库容器明细自定义分页，传回AsnLpnDetailVO 列表
	 */
	@ApiLog("入库容器接口-入库容器明细自定义分页，传回AsnLpnDetailVO 列表")
	@GetMapping("/page")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "分页", notes = "传入lpnDetail")
	public R<IPage<AsnLpnDetailVO>> page(AsnLpnDetailVO lpnDetail, Query query) {
		IPage<AsnLpnDetailVO> pages = lpnDetailService.selectLpnDetailPage(Condition.getPage(query), lpnDetail);
		return R.data(pages);
	}

	/**
	 * 入库容器明细新增
	 */
	@ApiLog("入库容器接口-入库容器明细新增")
	@PostMapping("/save")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "新增", notes = "传入lpnDetail")
	public R save(@Valid @RequestBody AsnLpnDetail lpnDetail) {
		return R.status(lpnDetailService.save(lpnDetail));
	}

	/**
	 * 入库容器明细修改
	 */
	@ApiLog("入库容器接口-入库容器明细修改")
	@PostMapping("/update")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "修改", notes = "传入lpnDetail")
	public R update(@Valid @RequestBody AsnLpnDetail lpnDetail) {
		return R.status(lpnDetailService.updateById(lpnDetail));
	}

	/**
	 * 入库容器明细新增或修改
	 */
	@ApiLog("入库容器接口-入库容器明细新增或修改")
	@PostMapping("/submit")
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "新增或修改", notes = "传入lpnDetail")
	public R submit(@Valid @RequestBody AsnLpnDetail lpnDetail) {
		return R.status(lpnDetailService.saveOrUpdate(lpnDetail));
	}


	/**
	 * 入库容器明细删除
	 */
	@ApiLog("入库容器接口-入库容器明细删除")
	@PostMapping("/remove")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(lpnDetailService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 生成入库单LPN编码，传入入库单ID
	 */
	@ApiLog("入库容器接口-生成入库单LPN")
	@PostMapping("/creatLpn")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "生成入库单LPN", notes = "传入ids")
	public R creatLpn(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(lpnDetailService.creatLpn(ids));
	}

	/**
	 * 根据订单IDS获取数据
	 */
	@ApiLog("入库容器接口-根据订单IDS获取数据")
	@GetMapping("/listByBillIds")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "根据订单IDS获取数据", notes = "传入billIds")
	public R<IPage<AsnLpnDetailVO>> listByBillIds(String billIds, Query query) {
		QueryWrapper<AsnLpnDetail> asnLpnDetailQueryWrapper = new QueryWrapper<>();
		if (Func.isNotEmpty(billIds))
			asnLpnDetailQueryWrapper.in("asn_bill_id", billIds.split(","));
		IPage<AsnLpnDetail> pages = lpnDetailService.page(Condition.getPage(query), asnLpnDetailQueryWrapper);
		return R.data(AsnLpnDetailWrapper.build().pageVO(pages));
	}
}
