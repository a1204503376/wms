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
package org.nodes.modules.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.nodes.core.utils.TokenUtil;
import org.nodes.core.base.cache.*;
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.service.IParamService;
import org.nodes.core.base.vo.ParamVO;
import org.nodes.core.base.wrapper.ParamWrapper;
import org.nodes.wms.biz.basics.systemParam.SystemParamBiz;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;


/**
 * 控制器
 *
 * @author Nodes
 */
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/param")
@Api(value = "参数管理", tags = "参数管理")
public class ParamController extends BladeController {

	private IParamService paramService;
	private SystemParamBiz systemParamBiz;


	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入param")
	public R<Param> detail(Param param) {
		return R.data(ParamWrapper.build().entityVO(paramService.getOne(Condition.getQueryWrapper(param))));
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "paramName", value = "参数名称", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "paramKey", value = "参数键名", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "paramValue", value = "参数键值", paramType = "query", dataType = "string")
	})
	@ApiOperation(value = "列表", notes = "传入param")
	public R<List<ParamVO>> list(@ApiIgnore @RequestParam Map<String, Object> param, Query query) {
		LambdaQueryWrapper<Param> wrapper = Condition.getQueryWrapper(param, Param.class).lambda();
		wrapper.func(sql->{
			if (!SecureUtil.isDeveloper()) {
				sql.eq(Param::getIsVisible, 0);
			}
		});
		List<Param> list = paramService.list(wrapper);
		return R.data(ParamWrapper.build().listVO(list));
	}

	/**
	 * 分页
	 */
	@GetMapping("/page")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "paramName", value = "参数名称", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "paramKey", value = "参数键名", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "paramValue", value = "参数键值", paramType = "query", dataType = "string")
	})
	@ApiOperation(value = "分页", notes = "传入param")
	public R<IPage<ParamVO>> page(@ApiIgnore @RequestParam Map<String, Object> param, Query query) {
		LambdaQueryWrapper<Param> wrapper = Condition.getQueryWrapper(param, Param.class).lambda();
		wrapper.func(sql->{
			if (!SecureUtil.isDeveloper()) {
				sql.eq(Param::getIsVisible, 0);
			}
		});
		IPage<Param> pageList = paramService.page(Condition.getPage(query), wrapper);
		return R.data(ParamWrapper.build().pageVO(pageList));
	}

	/**
	 * 新增或修改
	 */
	@ApiLog("参数管理-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入param")
	public R submit(@Valid @RequestBody Param param) {
		QueryWrapper<Param> paramQueryWrapper = new QueryWrapper<>();
		paramQueryWrapper.lambda()
			.eq(Param::getParamKey, param.getParamKey())
			.ne(Param::getId, param.getId());
		Param one = paramService.getOne(paramQueryWrapper);
		if (Func.isNotEmpty(one)) {
			return R.fail("该租户参数名已存在！");
		}
		if (Func.isEmpty(param.getTenantId())) {
			param.setTenantId(TokenUtil.DEFAULT_TENANT_ID);
		}
		ParamCache.reload();
		return R.status(systemParamBiz.saveOrUpdate(param));
	}


	/**
	 * 删除
	 */
	@ApiLog("参数管理-逻辑删除")
	@PostMapping("/remove")
	@ApiOperation(value = "逻辑删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		ParamCache.reload();
		return R.status(systemParamBiz.delete(ids));
	}

	/**
	 * 控制参数显隐
	 */
	@ApiLog("参数管理-控制参数显隐")
	@GetMapping("/change-visible")
	@ApiOperation(value = "控制参数显隐", notes = "传入id,type")
	public R changeVisible(Long id, Integer type) {
		ParamCache.reload();
		return R.status(paramService.changeVisible(id, type));
	}

}
