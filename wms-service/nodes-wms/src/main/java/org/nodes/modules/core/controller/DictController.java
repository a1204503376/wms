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

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.vo.DictVO;
import org.nodes.core.base.wrapper.DictWrapper;
import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springblade.core.cache.constant.CacheConstant.DICT_CACHE;
import static org.springblade.core.cache.constant.CacheConstant.SYS_CACHE;

/**
 * ?????? ?????????
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/dict")
public class DictController extends BladeController {

	private final IDictService dictService;

	/**
	 * ??????
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "??????", notes = "??????dict")
	public R<DictVO> detail(Dict dict) {
		if (Func.isNull(dict.getId())){
			return R.data(null);
		}
		return R.data(DictWrapper.build().entityVO(dictService.getOne(Condition.getQueryWrapper(dict))));
	}

	/**
	 * ??????
	 */
	@GetMapping("/list")
	@ApiOperation(value = "??????", notes = "??????dict")
	public R<List<DictVO>> list(@ApiIgnore @RequestParam Map<String, Object> params) {

		List<Dict> listAll = dictService.list(Condition.getQueryWrapper(params, Dict.class));
		if (!SecureUtil.isDeveloper()) {
			listAll = listAll.stream().filter(item -> {
				return item.getIsVisible().equals(0);
			}).collect(Collectors.toList());
		}
		// ??????
		listAll.sort((dict1, dict2) -> dict1.getSort().compareTo(dict2.getSort()));
		// ????????????????????????
		List<DictVO> dictVOList = DictWrapper.build().listVO(listAll);
		for (DictVO item : dictVOList) {
			Long count = listAll.stream().filter(u -> {
				return item.getId().equals(u.getParentId());
			}).count();
			item.setHasChildren(count > 0);
		}

		return R.data(dictVOList);
	}

	/**
	 * ??????
	 */
	@GetMapping("/page")
	@ApiOperation(value = "??????", notes = "??????dict")
	public R<IPage<DictVO>> page(@ApiIgnore @RequestParam Map<String, Object> params, Query query) {
		IPage<Dict> page = dictService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Dict.class)
			.lambda()
			.eq(Dict::getParentId, WmsAppConstant.TOP_PARENT_ID)
			.orderByAsc(Dict::getSort));
		IPage<DictVO> dictPage = DictWrapper.build().pageVO(page);
		if (Func.isNotEmpty(dictPage) && Func.isNotEmpty(dictPage.getRecords())) {
			List<Dict> childrenList = dictService.list(Condition.getQueryWrapper(new Dict())
				.lambda()
				.in(Dict::getParentId, NodesUtil.toList(dictPage.getRecords(), Dict::getId)));
			dictPage.getRecords().forEach(item -> {
				long hasChildren = childrenList.stream().filter(u -> {
					return u.getParentId().equals(item.getId());
				}).count();
				item.setHasChildren(hasChildren > WmsAppConstant.TOP_PARENT_ID);
			});
		}
		return R.data(dictPage);
	}

	/**
	 * ?????????
	 */
	@GetMapping("/child-list")
	@ApiOperation(value = "??????", notes = "??????dict")
	public R<IPage<DictVO>> childList(@ApiIgnore @RequestParam Dict dict, Query query) {
		IPage<Dict> page = dictService.page(Condition.getPage(query), Condition.getQueryWrapper(dict)
			.lambda()
			.orderByAsc(Dict::getSort));
		return R.data(DictWrapper.build().pageVO(page));
	}

	/**
	 * ????????????????????????
	 *
	 * @return
	 */
	@GetMapping("/tree")
	@ApiOperation(value = "????????????", notes = "????????????")
	public R<List<DictVO>> tree() {
		List<DictVO> tree = dictService.tree();
		return R.data(tree);
	}

	/**
	 * ???????????????
	 */
	@ApiLog("??????-???????????????")
	@PostMapping("/submit")
	@ApiOperation(value = "???????????????", notes = "??????dict")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	public R submit(@Valid @RequestBody Dict dict) {
		CacheUtil.clear(DICT_CACHE);
		return R.status(dictService.submit(dict));
	}


	/**
	 * ??????
	 */
	@ApiLog("??????-??????")
	@PostMapping("/remove")
	@ApiOperation(value = "??????", notes = "??????ids")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	public R remove(@ApiParam(value = "????????????", required = true) @RequestParam String ids) {
		CacheUtil.clear(DICT_CACHE);
		return R.status(dictService.removeDict(ids));
	}

	/**
	 * ????????????
	 *
	 * @return
	 */
	@GetMapping("/dictionary")
	@ApiOperation(value = "????????????", notes = "????????????")
	public R<List<Dict>> dictionary(String code) {
		IDictService dictService = SpringUtil.getBean(IDictService.class);
		/*List<Dict> dictList = DictCache.list().stream().filter(u -> {
			return u.getCode().equals(code)
				&& new Integer(0).equals(u.getIsSealed())
				&& !u.getParentId().equals(CommonConstant.TOP_PARENT_ID);
		}).sorted(Comparator.comparing(Dict::getDictKey)).collect(Collectors.toList());*/
		List<Dict> dictList = dictService.list(Condition.getQueryWrapper(new Dict())
			.lambda()
			.eq(Dict::getCode, code)
			.eq(Dict::getIsSealed, new Integer(0))
			.ne(Dict::getParentId, WmsAppConstant.TOP_PARENT_ID)
		);
		return R.data(dictList);
	}

	/**
	 * ??????????????????
	 */
	@ApiLog("??????-??????????????????")
	@GetMapping("/change-visible")
	@ApiOperation(value = "??????????????????", notes = "??????id,type")
	public R changeVisible(Long id, Integer type) {
		CacheUtil.clear(DICT_CACHE);
		return R.status(dictService.changeVisible(id, type));
	}
}
