package org.nodes.wms.biz.basics.systemParam;

import java.math.BigDecimal;

public interface SystemParamBiz {
	/**
	 * 获取列的最大载重
	 * @return
	 */
	BigDecimal findMaxLoadWeightOfColumn();
}
