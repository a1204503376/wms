package org.nodes.wms.biz.basics.systemParam.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.base.entity.Param;
import org.nodes.wms.biz.basics.systemParam.SystemParamBiz;
import org.nodes.wms.dao.basics.systemParam.SystemParamDao;
import org.nodes.wms.dao.basics.systemParam.constant.SystemParamConstant;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@RequiredArgsConstructor
public class SystemParamBizImpl implements SystemParamBiz {
	private final SystemParamDao systemParamDao;
	@Override
	public BigDecimal findMaxLoadWeightOfColumn() {
		Param param = systemParamDao.selectByKey(SystemParamConstant.MAX_LOAD_WEIGHT_OF_COLUMN);
		String value = param.getParamValue();
		if(Func.isEmpty(value)){
			new ServiceException("SystemParamBizImpl.findMaxLoadWeightOfColumn为空");
		}
		return new BigDecimal(value);
	}

	@Override
	public Boolean saveOrUpdate(Param param) {
		return systemParamDao.saveOrUpdate(param);
	}

	@Override
	public Param selectByKey(String key) {
		return systemParamDao.selectByKey(key);
	}

	@Override
	public Boolean delete(String ids) {
		return systemParamDao.delete(ids);
	}


}
