package org.nodes.wms.biz.instock.receiveLog.modular;

import lombok.RequiredArgsConstructor;
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
import org.nodes.wms.dao.common.skuLot.SkuLotUtil;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveByPcDetailRequest;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receiveLog.entities.ReceiveLog;
import org.nodes.wms.dao.stock.dto.input.StockImportRequest;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReceiveLogFactory {
	private final SkuBiz skuBiz;
	private final ZoneBiz zoneBiz;
	private final LocationBiz locationBiz;
	private final WarehouseBiz warehouseBiz;
	private final OwnerBiz ownerBiz;
	private final SupplierBiz supplierBiz;


	/**
	 * 根据前端导入表格生成清点记录集合
	 *
	 * @param importDataList 导入数据集合
	 * @return 清点记录集合
	 */
	public List<ReceiveLog> createReceiveLogListForImport(List<StockImportRequest> importDataList) {
		List<ReceiveLog> receiveLogList = new ArrayList<>();
		for (StockImportRequest stockImportRequest : importDataList) {
			if (Func.isEmpty(stockImportRequest.getSkuCode()) || Func.isEmpty(stockImportRequest.getStockQty())
				|| Func.isEmpty(stockImportRequest.getWsuCode()) || Func.isEmpty(stockImportRequest.getLocCode())
				|| Func.isEmpty(stockImportRequest.getWhCode()) || Func.isEmpty(stockImportRequest.getOwnerCode())
				|| Func.isEmpty(stockImportRequest.getSkuCode())) {
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
			if (Func.isEmpty(packageDetail)) {
				throw new ServiceException("导入失败,物料" + sku.getSkuName() + "下不存在计量单位编码" + stockImportRequest.getWsuCode());
			}
			ReceiveLog receiveLog = new ReceiveLog();
			SkuLotUtil.setAllSkuLot(stockImportRequest, receiveLog);
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

	/**
	 * pc收货根据前端传入数据生成清点记录
	 *
	 * @param request       前端传入收货明细集合
	 * @param receiveHeader 收货单头表
	 * @param receiveDetail 收货单明细
	 * @return 清点记录实体
	 */
	public ReceiveLog createReceiveLog(ReceiveByPcDetailRequest request, ReceiveHeader receiveHeader, ReceiveDetail receiveDetail) {
		ReceiveLog receiveLog = new ReceiveLog();
		Location location = locationBiz.findByLocId(request.getLocId());
		receiveLog.setReceiveId(receiveHeader.getReceiveId());
		receiveLog.setReceiveNo(receiveHeader.getReceiveNo());
		receiveLog.setAsnBillId(receiveHeader.getAsnBillId());
		receiveLog.setAsnBillNo(receiveHeader.getAsnBillNo());
		receiveLog.setReceiveDetailId(receiveDetail.getReceiveDetailId());
		receiveLog.setSupplierId(receiveHeader.getSupplierId());
		receiveLog.setLineNo(request.getLineNumber());
		receiveLog.setQty(request.getScanQty());
		receiveLog.setLpnCode(request.getLpnCode());
		receiveLog.setLocId(location.getLocId());
		receiveLog.setSnCode(request.getSnCode());
		receiveLog.setLocCode(location.getLocCode());
		receiveLog.setSkuId(receiveDetail.getSkuId());
		receiveLog.setWsuCode(request.getUmCode());
		receiveLog.setSkuCode(receiveDetail.getSkuCode());
		receiveLog.setSkuName(receiveDetail.getSkuName());
		receiveLog.setWspId(receiveDetail.getWspId());
		receiveLog.setSkuLevel(receiveDetail.getSkuLevel());
		receiveLog.setSkuSpec(receiveDetail.getSkuSpec());
		receiveLog.setWhId(receiveDetail.getWhId());
		receiveLog.setWhCode(receiveDetail.getWhCode());
		receiveLog.setWoId(receiveHeader.getWoId());
		receiveLog.setOwnerCode(receiveHeader.getOwnerCode());
		SkuLotUtil.setAllSkuLot(request, receiveLog);
		receiveLog.setSkuLot3(Func.formatDate(new Date()));
		return receiveLog;
	}
}
