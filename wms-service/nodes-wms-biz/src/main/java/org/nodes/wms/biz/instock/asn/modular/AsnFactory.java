package org.nodes.wms.biz.instock.asn.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.suppliers.SupplierBiz;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.biz.common.utils.NoGeneratorUtil;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.nodes.wms.dao.basics.warehouse.entites.Warehouse;
import org.nodes.wms.dao.instock.asn.dto.input.AddAsnBillRequest;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * ASN单工厂
 * 主要用于创建ASN单实体
 */
@Service
@RequiredArgsConstructor
public class AsnFactory {

	private final NoGeneratorUtil noGeneratorUtil;

	private final SupplierBiz supplierBiz;

	private final SkuBiz skuBiz;

	private final WarehouseBiz warehouseBiz;

	public AsnHeader createAsnHeader(AddAsnBillRequest addAsnBillRequest) {
		AsnHeader asnHeader = new AsnHeader();
		asnHeader.setAsnBillNo(noGeneratorUtil.createAsnBillNo());
		asnHeader.setBillTypeCd(addAsnBillRequest.getBillTypeCd());
		asnHeader.setAsnBillState(AsnBillStateEnum.NOT_RECEIPT);
		asnHeader.setAsnBillRemark(addAsnBillRequest.getAsnBillRemark());

		Supplier supplier = supplierBiz.findById(addAsnBillRequest.getSupplierId());
		asnHeader.setSupplierCode(supplier.getCode());
		asnHeader.setSupplierName(supplier.getName());

		Warehouse warehouse = warehouseBiz.findById(addAsnBillRequest.getWhId());
		asnHeader.setWhId(warehouse.getWhId());
		asnHeader.setWhCode(warehouse.getWhCode());
		return asnHeader;

	}

	public AsnDetail createAsnDetail(
		AddAsnBillRequest addAsnBillRequest, AsnHeader asnHeaderObj, AsnDetail asnDetailObj) {
		AsnDetail asnDetail = new AsnDetail();
		asnDetail.setAsnLineNo(asnDetailObj.getAsnLineNo());
		asnDetail.setAsnBillId(asnHeaderObj.getAsnBillId());
		asnDetail.setAsnBillNo(asnHeaderObj.getAsnBillNo());
		asnDetail.setSupplierId(addAsnBillRequest.getSupplierId());
		// 根据物品id查找物品信息
		Sku sku = skuBiz.findById(asnDetailObj.getSkuId());
		asnDetail.setSkuId(sku.getSkuId());
		asnDetail.setSkuCode(sku.getSkuCode());
		asnDetail.setSkuName(sku.getSkuName());
		asnDetail.setWspId(sku.getWspId());
		asnDetail.setUmCode("123");
		asnDetail.setUmName("计量单位123");
		asnDetail.setBaseUmName("基础计量单位123");
		asnDetail.setBaseUmCode("123");
		asnDetail.setPlanQty(new BigDecimal(0));
		asnDetail.setScanQty(new BigDecimal(0));
		asnDetail.setSurplusQty(new BigDecimal(0));
		return asnDetail;
	}
}
