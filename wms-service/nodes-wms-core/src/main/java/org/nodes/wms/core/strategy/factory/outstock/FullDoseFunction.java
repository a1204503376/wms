package org.nodes.wms.core.strategy.factory.outstock;

import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.strategy.enums.OutstockFuncEnum;
import org.nodes.wms.core.strategy.factory.IFunctionCode;
import org.nodes.wms.core.strategy.factory.annotation.Code;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @program: WmsCore
 * @description: 足量优先执行代码
 * @author: pengwei
 * @create: 2020-12-16 14:00
 **/
@Code(OutstockFuncEnum.FULL_DOSE)
public class FullDoseFunction extends BaseFunction implements IFunctionCode {
	@Override
	public List<Stock> execute(List<Stock> stockListAll, BigDecimal planQty) {

		BigDecimal surplusQty = planQty;
		// 处理足量清库位的逻辑

		// 足量不考虑混批，只需要筛选出库存足够的库存就可以了
		return stockListAll.stream().filter(u -> {
			BigDecimal qty = u.getStockQty().subtract(u.getPickQty())
				.subtract(u.getOccupyQty());
			return BigDecimalUtil.ge(qty, surplusQty);
		}).collect(Collectors.toList());
	}
}
