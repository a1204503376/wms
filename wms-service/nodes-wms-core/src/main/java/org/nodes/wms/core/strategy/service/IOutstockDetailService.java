
package org.nodes.wms.core.strategy.service;

import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuOutstock;
import org.nodes.wms.core.common.entity.AttributeBase;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.strategy.entity.Outstock;
import org.nodes.wms.core.strategy.entity.OutstockDetail;
import org.springblade.core.mp.base.BaseService;

import java.math.BigDecimal;
import java.util.List;

/**
 * 服务类
 *
 * @author NodeX
 * @since 2019-12-10
 */
public interface IOutstockDetailService extends BaseService<OutstockDetail> {

	/**
	 * 从分配策略明细中找到合适的明细策略
	 *
	 * @param outstock      分配策略
	 * @param attributeBase 扩展字段基类
	 * @param billTypeCd    订单类型编码
	 * @param sku           物品
	 * @return 分配策略明细
	 */
	OutstockDetail find(Outstock outstock, AttributeBase attributeBase, String billTypeCd, Sku sku);

	/**
	 * 执行分配策略(过滤库存）
	 *
	 * @param outstockDetail 分配策略明细
	 * @param skuOutstock    物品出库设置
	 * @param stockList      库存列表
	 * @param planQty        计划量
	 * @return 满足策略的库存列表
	 */
	List<Stock> execute(OutstockDetail outstockDetail, SkuOutstock skuOutstock, List<Stock> stockList,
						BigDecimal planQty);
}
