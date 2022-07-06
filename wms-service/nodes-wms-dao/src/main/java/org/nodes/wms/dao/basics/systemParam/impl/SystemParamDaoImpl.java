package org.nodes.wms.dao.basics.systemParam.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.service.IParamService;
import org.nodes.wms.dao.basics.systemParam.SystemParamDao;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SystemParamDaoImpl implements SystemParamDao {
	private final IParamService paramService;
	/**
	 * 编辑和删除的时候需要删除缓存中的值
	 */
	private static final Map<String, Param> store = new HashMap<>();


	@Override
	public Boolean saveOrUpdate(Param param) {
		if (Func.isEmpty(param)) {
			throw new NullArgumentException("SystemParamDao.update方法的参数为空");
		}
		Boolean saveOrUpdate = paramService.saveOrUpdate(param);
		if (saveOrUpdate) {
			store.put(param.getParamKey(), param);
		}
		return saveOrUpdate;
	}

	@Override
	public Param selectByKey(String key) {
		Param param = store.get(key);
		if (Func.isEmpty(param)) {
			return paramService.selectByKey(key);
		} else {
			return param;
		}
	}

	@Override
	public Boolean delete(String ids) {
		boolean logic = paramService.deleteLogic(Func.toLongList(ids));
		if (logic) {
			LambdaQueryWrapper<Param> lambdaQueryWrapper = new LambdaQueryWrapper<>();
			List<Long> paramListIds = Func.toLongList(ids);
			lambdaQueryWrapper.in(Param::getId, paramListIds);
			List<Param> paramList = paramService.list(lambdaQueryWrapper);
			for (Param params :
				paramList) {
				store.put(params.getParamKey(), params);
			}
		}
		return logic;
	}
}
