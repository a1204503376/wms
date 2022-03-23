package org.nodes.wms.core.strategy.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.strategy.entity.OutstockConfigLot;
import org.nodes.wms.core.strategy.vo.OutstockConfigLotVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;

import javax.swing.*;

public class OutstockConfigLotWrapper extends BaseEntityWrapper<OutstockConfigLot, OutstockConfigLotVO> {
	public static OutstockConfigLotWrapper build() {
		return new OutstockConfigLotWrapper();
	}

	@Override
	public OutstockConfigLotVO entityVO(OutstockConfigLot entity) {
		OutstockConfigLotVO outstockConfigLotVO = BeanUtil.copy(entity, OutstockConfigLotVO.class);
		if (ObjectUtil.isNotEmpty(outstockConfigLotVO)) {
			outstockConfigLotVO.setSkuLotNumberDesc("批属性" + entity.getSkuLotNumber());
			outstockConfigLotVO.setSkuLotOperationDesc(
				DictCache.getValue(DictConstant.SKU_LOT_OPERATION,entity.getSkuLotOperation()));
		}
		return outstockConfigLotVO;
	}
}
