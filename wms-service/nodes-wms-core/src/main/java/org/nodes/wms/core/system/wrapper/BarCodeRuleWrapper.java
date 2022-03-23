package org.nodes.wms.core.system.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.wms.core.system.entity.BarcodeRule;
import org.nodes.wms.core.system.vo.BarcodeRuleVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;

import java.util.Objects;

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
