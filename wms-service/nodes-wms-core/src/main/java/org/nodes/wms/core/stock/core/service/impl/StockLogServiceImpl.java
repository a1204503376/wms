
package org.nodes.wms.core.stock.core.service.impl;

import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.entity.StockLog;
import org.nodes.wms.core.stock.core.enums.StockProcTypeEnum;
import org.nodes.wms.core.stock.core.mapper.StockLogMapper;
import org.nodes.wms.core.stock.core.service.IStockLogService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库存日志 服务实现类
 *
 * @author pengwei
 * @since 2020-02-14
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class StockLogServiceImpl<M extends StockLogMapper, T extends StockLog>
	extends BaseServiceImpl<StockLogMapper, StockLog>
	implements IStockLogService {

	/**
	 * 记录库存日志
	 * @param stock 库存信息
	 * @param stockProcType 处理类型
	 * @param systemProcId 系统日志ID
	 * @return 库存日志
	 */
	@Override
	public StockLog create(Stock stock, StockDetail stockDetail, StockProcTypeEnum stockProcType, Long systemProcId) {
		StockLog stockLog = BeanUtil.copy(stock, StockLog.class);
		if (Func.isNotEmpty(stockDetail)) {
			stockLog.setStockDetailId(stockDetail.getId());
			stockLog.setLpnCode(stockDetail.getLpnCode());
			stockLog.setStockQty(stockDetail.getStockQty());
			stockLog.setBoxCode(stockDetail.getBoxCode());
			stockLog.setBillDetailId(stockDetail.getBillDetailId());
			stockLog.setSoBillId(stockDetail.getSoBillId());
			stockLog.setWellenId(stockDetail.getWellenId());
			stockLog.setUnreceivedQty(stockDetail.getUnreceivedQty());
			stockLog.setStockQty(stockDetail.getStockQty());
			stockLog.setPickQty(stockDetail.getPickQty());
		}
		Warehouse warehouse = WarehouseCache.getById(stock.getWhId());
		if (Func.isNotEmpty(warehouse)) {
			stockLog.setWhCode(warehouse.getWhCode());
			stockLog.setWhName(warehouse.getWhName());
		}
		Location location = LocationCache.getById(stock.getLocId());
		if (Func.isNotEmpty(location)) {
			stockLog.setLocCode(location.getLocCode());
		}
		Sku sku = SkuCache.getById(stock.getSkuId());
		if (Func.isNotEmpty(sku)){
			stockLog.setWoId(sku.getWoId());
			stockLog.setSkuCode(sku.getSkuCode());
			stockLog.setSkuName(sku.getSkuName());
		}
		SkuPackage skuPackage = SkuPackageCache.getById(stock.getWspId());
		if (Func.isNotEmpty(skuPackage)) {
			stockLog.setWspName(skuPackage.getWspName());
		}
		stockLog.setProType(stockProcType.getIndex());
		stockLog.setSystemProcId(systemProcId);

		super.save(stockLog);

		return stockLog;
	}
}
