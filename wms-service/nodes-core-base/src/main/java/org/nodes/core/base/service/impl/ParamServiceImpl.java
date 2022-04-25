package org.nodes.core.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.AllArgsConstructor;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.constant.DictConstant;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.secure.utils.SecureUtil;
import org.springblade.core.tool.utils.Func;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.enums.ParamEnum;
import org.nodes.core.base.mapper.ParamMapper;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.service.IParamService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.springblade.core.cache.constant.CacheConstant.DICT_CACHE;

/**
 * 系统参数服务实现类
 *
 * @author Nodes
 */
@Service
@AllArgsConstructor
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class ParamServiceImpl <M extends ParamMapper, T extends Param> extends BaseServiceImpl<ParamMapper, Param> implements IParamService {

	IDictService dictService;

	private final ParamMapper paramMapper;

	@Override
	public boolean changeVisible(Long id, Integer type) {
		if (SecureUtil.isDeveloper()) {
			return super.update(Wrappers.<Param>update().lambda().set(Param::getIsVisible, type).eq(Param::getId, id));
		} else {
			throw new ServiceException("您没有权限操作！");
		}
	}

	@Override
	public Param selectByKey(String key) {
		QueryWrapper<Param> queryWrapper = Wrappers.query();
		queryWrapper.eq("param_key",key);
		return paramMapper.selectOne(queryWrapper);
	}

	@Override
	public boolean saveOrUpdate(Param entity) {
		boolean result = super.saveOrUpdate(entity);
		if (entity.getParamKey().equals(ParamEnum.SKU_LOT_COUNT.getKey())) {
			// 如果修改批属性开放数量，需要修改字典(周转方式)
			List<Dict> dictList = DictCache.list(DictConstant.TURNOVER_ITEM).stream().filter(u->{
				return u.getDictKey() > ParamCache.LOT_COUNT;
			}).collect(Collectors.toList());
			if (Func.isNotEmpty(dictList)) {
				CacheUtil.clear(DICT_CACHE);
				dictList.forEach(dict -> {
					dict.setIsVisible(1);
					dictService.submit(dict);
				});
			}
		}
		return result;
	}
}
