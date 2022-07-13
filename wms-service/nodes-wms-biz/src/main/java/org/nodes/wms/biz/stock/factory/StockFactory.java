package org.nodes.wms.biz.stock.factory;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.suppliers.SupplierBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.common.stock.StockUtil;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.dto.input.StockImportRequest;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StockFactory {
	private final SkuBiz skuBiz;
	private final ZoneBiz zoneBiz;
	private final LocationBiz locationBiz;
	private final WarehouseBiz warehouseBiz;
	private final OwnerBiz ownerBiz;
	private final SupplierBiz supplierBiz;

	public Stock create(ReceiveLog receiveLog, Location location) {
		AssertUtil.notNull(receiveLog, "创建库存失败，清点记录为空");
		AssertUtil.notNull(location, "创建库存失败，目标为空");

		Stock stock = new Stock();
		SkuLotUtil.setAllSkuLot(receiveLog, stock);
		stock.setLastInTime(LocalDateTime.now());
		// TODO 如果没有库存状态则默认
		if (Func.isNull(receiveLog.getStockStatus())) {
			stock.setStockStatus(StockStatusEnum.NORMAL);
		} else {
			stock.setStockStatus(receiveLog.getStockStatus());
		}

		stock.setSkuLevel(receiveLog.getSkuLevel());
		stock.setWsuCode(receiveLog.getWsuCode());
		Sku sku = skuBiz.findById(receiveLog.getSkuId());
		if (Func.isNotEmpty(sku)) {
			stock.setWspName(sku.getWspName());
		}
		stock.setWspId(receiveLog.getWspId());
		stock.setSkuId(receiveLog.getSkuId());
		stock.setSkuCode(receiveLog.getSkuCode());
		stock.setSkuName(receiveLog.getSkuName());
		stock.setStayStockQty(BigDecimal.ZERO);
		stock.setStockQty(receiveLog.getQty());
		stock.setPickQty(BigDecimal.ZERO);
		stock.setOccupyQty(BigDecimal.ZERO);
		stock.setBoxCode(receiveLog.getBoxCode());
		stock.setLpnCode(receiveLog.getLpnCode());
		stock.setLocCode(receiveLog.getLocCode());
		stock.setLocId(receiveLog.getLocId());
		stock.setZoneId(location.getZoneId());
		Zone zone = zoneBiz.findById(location.getZoneId());
		if (Func.isNotEmpty(zone)) {
			stock.setZoneCode(zone.getZoneCode());
		}
		stock.setWhId(receiveLog.getWhId());
		stock.setWhCode(receiveLog.getWhCode());
		stock.setWoId(receiveLog.getWoId());

		return stock;
	}

	public Stock create(Stock sourceStock, Location targetLocation,
						String targetLpnCode, String targetBoxCode, BigDecimal qty) {
		AssertUtil.notNull(sourceStock, "创建库存失败，原库存为空");
		AssertUtil.notNull(targetLocation, "创建库存失败，目标库位为空");
		AssertUtil.notNull(qty, "创建库存失败，数量为空");

		Stock targetStock = new Stock();
		BeanUtil.copy(sourceStock, targetStock);
		StockUtil.resetStockInfo(targetStock);

		targetStock.setStockQty(qty);
		targetStock.setLocId(targetLocation.getLocId());
		targetStock.setLocCode(targetLocation.getLocCode());
		targetStock.setZoneId(targetLocation.getZoneId());
		targetStock.setLpnCode(targetLpnCode);
		targetStock.setBoxCode(targetBoxCode);
		Zone zone = zoneBiz.findById(targetLocation.getZoneId());
		targetStock.setZoneCode(zone.getZoneCode());
		targetStock.setLastInTime(LocalDateTime.now());

		return targetStock;
	}

	public List<ReceiveLog> createStockListForImport(List<StockImportRequest> importDataList) {
		List<ReceiveLog> receiveLogList = new ArrayList<>();
		for (StockImportRequest stockImportRequest : importDataList) {
			if (Func.isEmpty(stockImportRequest.getSkuCode()) || Func.isEmpty(stockImportRequest.getStockQty())
				|| Func.isEmpty(stockImportRequest.getWsuCode()) || Func.isEmpty(stockImportRequest.getLocCode())
				|| Func.isEmpty(stockImportRequest.getWhCode()) || Func.isEmpty(stockImportRequest.getOwnerCode())
				|| Func.isEmpty(stockImportRequest.getSkuCode()) || Func.isEmpty(stockImportRequest.getZoneCode())) {
				throw new ServiceException("导入失败,必填字段不能为空");
			}
			Sku sku = skuBiz.findByCode(stockImportRequest.getSkuCode());
			if (Func.isEmpty(sku)) {
				throw new ServiceException("导入失败,物料编码" + stockImportRequest.getSkuCode() + "不存在");
			}
			Warehouse warehouse = warehouseBiz.findByCode(stockImportRequest.getWhCode());
			if (Func.isEmpty(warehouse)) {
				throw new ServiceException("导入失败,仓库编码" + stockImportRequest.getWhCode() + "不存在");
			}
			Location location = locationBiz.findLocationByLocCode(warehouse.getWhId(), stockImportRequest.getLocCode());
			if (Func.isEmpty(location)) {
				throw new ServiceException("导入失败,库房" + warehouse.getWhName() + "下不存在库位" + stockImportRequest.getLocCode());
			}
			Owner owner = ownerBiz.findByCode(stockImportRequest.getOwnerCode());
			if (Func.isEmpty(owner)) {
				throw new ServiceException("导入失败,货主编码" + stockImportRequest.getOwnerCode() + "不存在");
			}
			if (skuBiz.getSkuUmCountByUmCode(stockImportRequest.getWsuCode()) < 1) {
				throw new ServiceException("导入失败,计量单位编码" + stockImportRequest.getWsuCode() + "不存在");
			}
			Supplier supplier = new Supplier();
			if (Func.isNotEmpty(stockImportRequest.getSupplierCode())) {
				supplier = supplierBiz.findByCode(stockImportRequest.getSupplierCode());
				if (Func.isEmpty(supplier)) {
					throw new ServiceException("导入失败,供应商编码" + stockImportRequest.getSupplierCode() + "不存在");
				}
			}
			if (Func.isNotEmpty(stockImportRequest.getStockStatus())
				&& !stockImportRequest.getStockStatus().equals(StockStatusEnum.NORMAL.getCode())
				&& !stockImportRequest.getStockStatus().equals(StockStatusEnum.FREEZE.getCode())) {
				throw new ServiceException("导入失败,库存状态:" + stockImportRequest.getStockStatus() + "不合法");
			}
			SkuPackageDetail packageDetail = skuBiz.getSkuPackageDetailBySkuId(sku.getSkuId(), stockImportRequest.getWsuCode());
			ReceiveLog receiveLog = new ReceiveLog();
			if (Func.isEmpty(stockImportRequest.getStockStatus()) || stockImportRequest.getStockStatus().equals(0)) {
				receiveLog.setStockStatus(StockStatusEnum.NORMAL);

			} else {
				receiveLog.setStockStatus(StockStatusEnum.FREEZE);
			}
			receiveLog.setSupplierId(supplier.getId());
			receiveLog.setQty(stockImportRequest.getStockQty());
			receiveLog.setLpnCode(stockImportRequest.getLpnCode());
			receiveLog.setBoxCode(stockImportRequest.getBoxCode());
			receiveLog.setLocId(location.getLocId());
			receiveLog.setLocCode(location.getLocCode());
			receiveLog.setSkuId(sku.getSkuId());
			receiveLog.setSkuCode(sku.getSkuCode());
			receiveLog.setSkuName(sku.getSkuName());
			receiveLog.setSkuSpec(sku.getSkuSpec());
			receiveLog.setWsuCode(stockImportRequest.getWsuCode());
			receiveLog.setWspId(sku.getWspId());
			receiveLog.setSkuLevel(packageDetail.getSkuLevel());
			receiveLog.setWhId(warehouse.getWhId());
			receiveLog.setWhCode(warehouse.getWhCode());
			receiveLog.setWoId(owner.getWoId());
			receiveLog.setOwnerCode(owner.getOwnerCode());
			receiveLogList.add(receiveLog);
		}
		return receiveLogList;
	}
}
