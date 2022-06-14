package org.nodes.wms.core.system.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.wms.core.system.entity.BarcodeRule;
import org.nodes.wms.core.system.vo.BarcodeRuleVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

public class BarCodeRuleWrapper extends BaseEntityWrapper<BarcodeRule, BarcodeRuleVO> {
	public static BarCodeRuleWrapper build() {
		return new BarCodeRuleWrapper();
	}

	@Override
	public BarcodeRuleVO entityVO(BarcodeRule barcodeRule) {
		BarcodeRuleVO barcodeRuleVO = BeanUtil.copy(barcodeRule, BarcodeRuleVO.class);
		if (Func.isNotEmpty(barcodeRuleVO)) {
			Warehouse warehouse = WarehouseCache.getById(barcodeRule.getWhId());
			if (Func.isNotEmpty(warehouse)) {
				barcodeRuleVO.setWhName(warehouse.getWhName());
			}
			barcodeRuleVO.setBarcodeTypeDesc(DictCache.getValue("barcode_type",barcodeRuleVO.getBarcodeType()));
		}
		return barcodeRuleVO;
	}
}
