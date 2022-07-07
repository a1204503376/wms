package org.nodes.wms.core.strategy.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.wms.core.strategy.entity.OutstockConfigUdf;
import org.nodes.wms.core.strategy.vo.OutstockConfigUdfVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.*;

public class OutstockConfigUdfWrapper extends BaseEntityWrapper<OutstockConfigUdf, OutstockConfigUdfVO> {
	public static OutstockConfigUdfWrapper build() {
		return new OutstockConfigUdfWrapper();
	}

	@Override
	public OutstockConfigUdfVO entityVO(OutstockConfigUdf entity) {
		OutstockConfigUdfVO outstockConfigUdfVO = BeanUtil.copy(entity, OutstockConfigUdfVO.class);
		if (ObjectUtil.isNotEmpty(outstockConfigUdfVO)) {
			outstockConfigUdfVO.setBillUdfNumberDesc("自定义属性" + entity.getBillUdfNumber());
			outstockConfigUdfVO.setBillUdfOperationDesc(
				DictCache.getValue(DictCodeConstant.BILL_UDF_OPERATION, entity.getBillUdfOperation()));
		}
		return outstockConfigUdfVO;
	}
}
