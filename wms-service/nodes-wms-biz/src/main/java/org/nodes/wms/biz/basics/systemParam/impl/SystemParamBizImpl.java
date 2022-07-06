package org.nodes.wms.biz.basics.systemParam.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.systemParam.SystemParamBiz;
import org.nodes.wms.dao.basics.systemParam.constant.SystemParamConstant;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
@Service
@RequiredArgsConstructor
public class SystemParamBizImpl implements SystemParamBiz {
	@Override
	public BigDecimal findMaxLoadWeightOfColumn() {
//		SystemParamConstant.MAX_LOAD_WEIGHT_OF_COLUMN
		return null;
	}
}
