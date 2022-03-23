package org.nodes.wms.core.strategy.wrapper;

import org.nodes.wms.core.strategy.entity.OutstockDetail;
import org.nodes.wms.core.strategy.vo.OutstockDetailVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;

public class OutstockDetailWrapper extends BaseEntityWrapper<OutstockDetail, OutstockDetailVO> {

	public static OutstockDetailWrapper build() {
		return new OutstockDetailWrapper();
	}

	@Override
	public OutstockDetailVO entityVO(OutstockDetail entity) {
		return BeanUtil.copy(entity, OutstockDetailVO.class);
	}
}
