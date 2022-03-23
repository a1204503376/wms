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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.instock.asn.entity.Register;
import org.nodes.wms.core.instock.asn.service.IRegisterService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 到货登记 控制器
 *
 * @author zhonglianshuai
 * @since 2020-04-07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/instock/register")
@Api(value = "到货登记", tags = "到货登记接口")
public class RegisterController extends BladeController {

	private IRegisterService registerService;


	/**
	 * 新增 到货登记
	 */
	@ApiLog("到货登记接口-新增")
	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入register")
	public R save(@Valid @RequestBody Register register) {
		return R.status(registerService.save(register));
	}

}
