package org.nodes.wms.biz.instock.asn.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.suppliers.SupplierBiz;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.biz.common.utils.NoGeneratorUtil;
import org.nodes.wms.dao.basics.sku.dto.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierSelectResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.nodes.wms.dao.basics.warehouse.entites.Warehouse;
import org.nodes.wms.dao.instock.asn.dto.input.AddAsnBillRequest;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailEditResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnHeaderEditResponse;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
		asnDetail.setUmCode(asnDetailObj.getUmCode());

		SkuUm skuUm = skuBiz.findSkuUmByUmCode(asnDetailObj.getUmCode());
		asnDetail.setUmName(skuUm.getWsuName());
		asnDetail.setBaseUmName(skuUm.getWsuName());
		asnDetail.setBaseUmCode(skuUm.getWsuCode());
		asnDetail.setPlanQty(asnDetailObj.getPlanQty());
		asnDetail.setScanQty(new BigDecimal(0));
		asnDetail.setSurplusQty(new BigDecimal(0));
		return asnDetail;
	}

    public AsnHeaderEditResponse createAsnHeaderEditResponse(AsnHeader asnHeader) {
		AsnHeaderEditResponse headerResponse = new AsnHeaderEditResponse();
		SupplierSelectResponse supplierResponse = new SupplierSelectResponse();
		supplierResponse.setId(asnHeader.getSupplierId());
		supplierResponse.setCode(asnHeader.getSupplierCode());
		supplierResponse.setName(asnHeader.getSupplierName());
		// 头表返回对象 赋值
		headerResponse.setAsnBillId(asnHeader.getAsnBillId());
		headerResponse.setAsnBillNo(asnHeader.getAsnBillNo());
		headerResponse.setBillTypeCd(asnHeader.getBillTypeCd());
		headerResponse.setWhId(asnHeader.getWhId());
		headerResponse.setSupplierSelectResponse(supplierResponse);
		headerResponse.setAsnBillRemark(asnHeader.getAsnBillRemark());
		return headerResponse;
    }

	public List<AsnDetailEditResponse> createAsnDetailEditResponse(List<AsnDetail> asnDetailList) {
		List<AsnDetailEditResponse> detailResponseList = new ArrayList<>();
		//遍历实体集合，给dto赋值
		asnDetailList.forEach(item -> {
			AsnDetailEditResponse detailResponse = new AsnDetailEditResponse();
			SkuSelectResponse skuSelectResponse = new SkuSelectResponse();
			skuSelectResponse.setSkuId(item.getSkuId());
			skuSelectResponse.setSkuCode(item.getSkuCode());
			skuSelectResponse.setSkuName(item.getSkuName());

			detailResponse.setAsnDetailId(item.getAsnDetailId());
			detailResponse.setAsnLineNo(item.getAsnLineNo());
			detailResponse.setSkuSelectResponse(skuSelectResponse);
			detailResponse.setPlanQty(item.getPlanQty());
			detailResponse.setScanQty(item.getScanQty());
			detailResponse.setRemark(item.getRemark());
			detailResponseList.add(detailResponse);
		});
		return detailResponseList;
	}
}
