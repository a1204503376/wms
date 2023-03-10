package org.nodes.wms.core.strategy.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.wms.dao.putaway.entities.StInstockConfigLot;
import org.nodes.wms.core.strategy.vo.InstockConfigLotVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.*;

public class InstockConfigLotWrapper extends BaseEntityWrapper<StInstockConfigLot, InstockConfigLotVO> {
	public static InstockConfigLotWrapper build() {
		return new InstockConfigLotWrapper();
	}

	@Override
	public InstockConfigLotVO entityVO(StInstockConfigLot entity) {
		InstockConfigLotVO configLot = BeanUtil.copy(entity, InstockConfigLotVO.class);
		if (ObjectUtil.isNotEmpty(configLot)) {
			configLot.setSkuLotNumberDesc("批属性" + configLot.getSkuLotNumber());
			configLot.setSkuLotOperationDesc(
				DictCache.getValue("sku_lot_operation", configLot.getSkuLotOperation()));
		}

		return configLot;
	}
}
