package org.nodes.wms.core.stock.core.wrapper;


import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.core.basedata.cache.SkuCache;
import org.nodes.wms.core.basedata.cache.SkuPackageCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.basedata.entity.SkuPackage;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuPackageService;
import org.nodes.wms.core.basedata.service.ISkuService;
import org.nodes.wms.core.stock.core.entity.ErpStock;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.vo.ErpStockVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;


import javax.swing.*;
import java.math.BigDecimal;

/**
 * @author pengwei
 * @program WmsCore
 * @description ERP库存封装类
 * @create 20200331
 */
public class ErpStockWrapper extends BaseEntityWrapper<ErpStock, ErpStockVO> {

	public static ErpStockWrapper build() {
		return new ErpStockWrapper();
	}

	@Override
	public ErpStockVO entityVO(ErpStock entity) {
		ErpStockVO erpStockVO = BeanUtil.copy(entity, ErpStockVO.class);
		if (ObjectUtil.isNotEmpty(erpStockVO)) {
			// 库房
			Warehouse warehouse = WarehouseCache.getById(entity.getWhId());
			if (ObjectUtil.isNotEmpty(warehouse)) {
				erpStockVO.setWhName(warehouse.getWhName());
			}
			// 物品
			Sku sku = SkuCache.getById(entity.getSkuId());
			if (ObjectUtil.isNotEmpty(sku)) {
				erpStockVO.setSkuCode(sku.getSkuCode());
				erpStockVO.setSkuName(sku.getSkuName());
			}
			// 包装
			SkuPackage skuPackage = SkuPackageCache.getById(entity.getWspId());
			if (ObjectUtil.isNotEmpty(skuPackage)) {
				erpStockVO.setWspName(skuPackage.getWspName());
			}
			// 层级
			erpStockVO.setSkuLevelDesc(DictCache.getValue(DictConstant.SKU_LEVEL, entity.getSkuLevel()));
			// 货主
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(entity.getWoId());
			if (ObjectUtil.isNotEmpty(owner)) {
				erpStockVO.setOwnerName(owner.getOwnerName());
			}
		}
		return erpStockVO;
	}

	public ErpStockVO entityVO(Stock stock) {

		ErpStockVO erpStockVO = entityVO(BeanUtil.copy(stock, ErpStock.class));
		if (ObjectUtil.isNotEmpty(erpStockVO)) {
			erpStockVO.setStockQty(BigDecimal.ZERO);
			erpStockVO.setWmsStockQty(stock.getStockQty());
		}
		return erpStockVO;
	}
}
