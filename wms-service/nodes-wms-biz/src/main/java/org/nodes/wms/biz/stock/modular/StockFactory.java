package org.nodes.wms.biz.stock.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.suppliers.SupplierBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.dto.input.StockImportRequest;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 库存管理工厂类
 */
@Service
@RequiredArgsConstructor
public class StockFactory {
	private final SkuBiz skuBiz;
	private final LocationBiz locationBiz;
	private final WarehouseBiz warehouseBiz;
	private final OwnerBiz ownerBiz;
	private final SupplierBiz supplierBiz;

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
