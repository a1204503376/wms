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
package org.nodes.core.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.nodes.core.base.mapper.UserMapper;
import org.nodes.core.utils.TokenUtil;
import org.nodes.core.base.cache.*;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.node.ForestNodeMerger;
import org.springblade.core.tool.utils.Func;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.base.mapper.DictMapper;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.vo.DictVO;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服务实现类
 *
 * @author Nodes
 */
@Service
@AllArgsConstructor
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class DictServiceImpl <M extends DictMapper, T extends Dict> extends BaseServiceImpl<DictMapper, Dict> implements IDictService {

	@Override
	public List<DictVO> tree() {
		return ForestNodeMerger.merge(baseMapper.tree(null));
	}

	@Override
	public boolean submit(Dict dict) {
		LambdaQueryWrapper<Dict> lqw = Wrappers.<Dict>query().lambda()
			.eq(Dict::getCode, dict.getCode())
			.eq(Dict::getDictKey, dict.getDictKey());
		Integer cnt = baseMapper.selectCount((Func.isEmpty(dict.getId())) ? lqw : lqw.notIn(Dict::getId, dict.getId()));
		if (cnt > 0) {
			throw new ServiceException("当前字典键值已存在!");
		}
		if (Func.isEmpty(dict.getParentId())) {
			dict.setParentId(BladeConstant.TOP_PARENT_ID);
		}
		dict.setIsDeleted(BladeConstant.DB_NOT_DELETED);

		if(saveOrUpdate(dict)) {
			//DictCache.saveOrUpdate(dict);
			return true;
		}

		return false;
	}

	@Override
	public boolean removeDict(String ids) {
		Integer cnt = baseMapper.selectCount(Wrappers.<Dict>query().lambda().in(Dict::getParentId, Func.toLongList(ids)));
		if (cnt > 0) {
			throw new ServiceException("请先删除子节点!");
		}
		Long[] idList = Func.toLongArray(ids);
		if (Func.isNotEmpty(idList)) {
			for (Long id : idList) {
				Dict dict = super.getById(id);
				if (Func.isEmpty(dict)) {
					continue;
				}
				baseMapper.deleteById(id);
				//DictCache.remove(id);
			}
		}
		return true;
	}

	@Override
	public boolean changeVisible(Long id, Integer type) {
		if (SecureUtil.isDeveloper()) {
			boolean hasSon = true;
			List<Long> idList = new ArrayList<>(),
				currentIds = new ArrayList<>();
			idList.add(id);
			currentIds.add(id);
			while (hasSon) {
				QueryWrapper<Dict> queryWrapper = new QueryWrapper<>();
				queryWrapper.lambda().in(Dict::getParentId, currentIds);
				List<Dict> dictSon = baseMapper.selectList(queryWrapper);
				if (dictSon.size() > 0) {
					currentIds = dictSon.stream().map(Dict::getId).collect(Collectors.toList());
					idList.addAll(currentIds);
				} else {
					hasSon = false;
				}
			}
			boolean dictBool= super.update(Wrappers.<Dict>update().lambda()
				.set(Dict::getIsVisible, type)
				.in(Dict::getId, idList));
			//更新缓存
			if(dictBool){
				for (Long  itemId : idList) {
					Dict dict = super.getById(itemId);
					dict.setIsVisible(type);
					if (Func.isEmpty(dict)) {
						continue;
					}
					//DictCache.saveOrUpdate(dict);
				}
			}
			return dictBool;
		} else {
			throw new ServiceException("您没有权限操作！");
		}
	}
}
