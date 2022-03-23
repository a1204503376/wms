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
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.core.basedata.dto.EnterpriseDTO;
import org.nodes.wms.core.basedata.excel.EnterpriseExcel;
import org.nodes.wms.core.basedata.service.IEnterpriseService;
import org.nodes.wms.core.basedata.vo.EnterpriseVO;
import org.nodes.wms.core.basedata.wrapper.EnterpriseWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 来往企业 控制器
 *
 * @author pengwei
 * @since 2019-12-06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/basedata/enterprise")
@Api(value = "来往企业", tags = "来往企业接口")
public class EnterpriseController extends BladeController {

	private IEnterpriseService enterpriseService;

	/**
	 * 详情
	 */
	@ApiLog("来往企业接口-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入enterprise 的主键ID")
	public R<EnterpriseVO> detail(Long id) {

		EnterpriseVO detail = EnterpriseWrapper.build().entityVO(enterpriseService.getById(id));
		return R.data(detail);
	}

	/**
	 * 来往企业 列表
	 *
	 * @param params 查询条件
	 * @return
	 */
	@ApiLog("来往企业接口-来往企业列表")
	@GetMapping("/list")
	@ApiOperation(value = "来往企业列表", notes = "传入enterprise")
	public R<List<EnterpriseVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<Enterprise> list = enterpriseService.list(Condition.getQueryWrapper(params, Enterprise.class));

		return R.data(EnterpriseWrapper.build().listVO(list));
	}

	/**
	 * 自定义分页 来往企业
	 */
	@ApiLog("来往企业接口-自定义分页")
	@GetMapping("/page")
	@ApiOperation(value = "来往企业分页", notes = "传入enterprise")
	public R<IPage<EnterpriseVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Enterprise> pages = enterpriseService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Enterprise.class));
		return R.data(EnterpriseWrapper.build().pageVO(pages));
	}

	/**
	 * 新增 来往企业
	 */
	@ApiLog("来往企业接口-新增")
	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入enterprise")
	public R save(@Valid @RequestBody EnterpriseDTO enterprise) {
		return R.status(enterpriseService.save(enterprise));
	}

	/**
	 * 修改 来往企业
	 */
	@ApiLog("来往企业接口-修改")
	@PostMapping("/update")
	@ApiOperation(value = "修改", notes = "传入enterprise")
	public R update(@Valid @RequestBody EnterpriseDTO enterprise) {
		return R.status(enterpriseService.updateById(enterprise));
	}

	/**
	 * 新增或修改 来往企业
	 */
	@ApiLog("来往企业接口-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入enterprise")
	public R submit(@Valid @RequestBody EnterpriseDTO enterprise) {
		return R.status(enterpriseService.saveOrUpdate(enterprise));
	}


	/**
	 * 删除 来往企业
	 */
	@ApiLog("来往企业接口-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(enterpriseService.removeByIds(ids));
	}

	/**
	 * 导出
	 */
	@ApiLog("来往企业-导出")
	@GetMapping("export")
	@ApiOperation(value = "导出", notes = "查询条件")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		enterpriseService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@ApiLog("来往企业-导出模板")
	@GetMapping("export-template")
	@ApiOperation(value = "导出模板")
	public void exportTemplate(HttpServletResponse response) {
		List<EnterpriseExcel> enterpriseExportList = new ArrayList<>();
		ExcelUtil.export(response, "来往企业", "来往企业数据表", enterpriseExportList, EnterpriseExcel.class);
	}

	/**
	 * 导入验证
	 */
	@ApiLog("来往企业-导入验证")
	@PostMapping("import-valid")
	@ApiOperation(value = "导入验证")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(enterpriseService.validExcel(ExcelUtil.read(file, EnterpriseExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@ApiLog("企业管理-导入数据")
	@PostMapping("import-data")
	@ApiOperation(value = "导入数据")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(enterpriseService.importData(dataVerifyList));
	}
}
