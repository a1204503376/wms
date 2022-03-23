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
package org.nodes.modules.wms.log.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.stock.core.entity.LotLog;
import org.nodes.wms.core.stock.core.service.ILotLogService;
import org.nodes.wms.core.stock.core.vo.LotLogVO;
import org.nodes.wms.core.stock.core.wrapper.LotLogWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * 批次号日志 控制器
 *
 * @author pengwei
 * @since 2020-03-03
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/log/lotLog")
@Api(value = "批次号日志", tags = "批次号日志接口")
public class LotLogController extends BladeController {

	private ILotLogService lotLogService;

	/**
	 * 分页 批次号日志
	 */
	@ApiLog("批次号日志接口-分页")
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入lotLog")
	public R<IPage<LotLogVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<LotLog> page = lotLogService.page(Condition.getPage(query), Condition.getQueryWrapper(params, LotLog.class));
		return R.data(LotLogWrapper.build().pageVO(page));
	}
}
