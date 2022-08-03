package org.nodes.wms.biz.basics.systemParam.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.base.entity.Param;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.systemParam.SystemParamBiz;
import org.nodes.wms.dao.basics.systemParam.SystemParamDao;
import org.nodes.core.constant.SystemParamConstant;
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
		AssertUtil.notEmpty(value,"系统错误，没有配置列最大载重的参数");
		return new BigDecimal(value);
	}

	@Override
	public String findScheduleUrl() {
		Param param = systemParamDao.selectByKey(SystemParamConstant.SCHEDULE_URL);
		AssertUtil.notNull(param, "系统错误，没有配置调度系统的url参数");
		return param.getParamValue();
	}

	@Override
	public int findSkuLotNumberOfOpen() {
		Param param = systemParamDao.selectByKey(SystemParamConstant.SKU_LOT_NUMBER_OPEN);
		if (Func.isNull(param)) {
			return 30;
		}

		return Integer.parseInt(param.getParamValue());
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
