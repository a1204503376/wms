package org.nodes.wms.core.strategy.factory;

import org.nodes.wms.core.basedata.entity.SkuOutstock;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.strategy.entity.OutstockDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: WmsCore
 * @description: 分配策略接口
 * @author: pengwei
 * @create: 2020-12-16 13:36
 **/
public interface IFunctionCode {

	/**
	 * 执行分配策略
	 *
	 * @param stockListAll 执行分配策略前的库存
	 * @param planQty		计划量
	 * @return 执行分配策略后的库存
	 */
	List<Stock> execute(List<Stock> stockListAll, BigDecimal planQty);

	/**
	 * 设置物品出库设置
	 *
	 * @param skuOutstock
	 */
	void setSkuOutstock(SkuOutstock skuOutstock);

	/**
	 * 设置分配策略明细
	 *
	 * @param outstockDetail 分配策略明细
	 */
	void setOutstockDetail(OutstockDetail outstockDetail);
}
