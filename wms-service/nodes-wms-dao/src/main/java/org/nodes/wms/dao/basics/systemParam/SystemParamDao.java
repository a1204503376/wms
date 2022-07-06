package org.nodes.wms.dao.basics.systemParam;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.core.base.mapper.ParamMapper;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;
import org.nodes.core.base.entity.Param;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SystemParamDao {
	private final ParamMapper paramMapper;
	// 编辑和删除的时候需要删除缓存中的值
	private static final Map<String, Param> store = new HashMap<>();
//    	 Param param=new Param();
	// 根据参数的key获取参数

	public void update(Param param) {
		if (Func.isEmpty(param)) {
			throw new NullArgumentException("SystemParamDao.update方法的参数为空");
		}
		if (paramMapper.updateById(param) <= 0) {
			throw new ServiceException("SystemParamDao.update方法出现异常");
		} else {
			store.put(param.getParamKey(), param);
		}
	}

	public void save(Param param) {

	}
}
