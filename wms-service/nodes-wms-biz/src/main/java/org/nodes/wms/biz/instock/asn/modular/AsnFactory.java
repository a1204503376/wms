package org.nodes.wms.biz.instock.asn.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.suppliers.SupplierBiz;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.biz.common.utils.NoGeneratorUtil;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.dto.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageAggregate;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierSelectResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.nodes.wms.dao.basics.warehouse.entites.Warehouse;
import org.nodes.wms.dao.instock.asn.dto.input.AddOrEditAsnBillRequest;
import org.nodes.wms.dao.instock.asn.dto.input.AsnDetailRequest;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailEditResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnHeaderEditResponse;
import org.nodes.wms.dao.instock.asn.entities.AsnDetail;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.springblade.core.tool.utils.Func;
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

	private final OwnerBiz ownerBiz;

	// 新增时，创建头表对象
	public AsnHeader createAsnHeader(AddOrEditAsnBillRequest addOrEditAsnBillRequest) {
		// 根据供应商id获取供应商信息
		Supplier supplier = supplierBiz.findById(addOrEditAsnBillRequest.getSupplierId());
		// 根据库房id获取库房信息
		Warehouse warehouse = warehouseBiz.findById(addOrEditAsnBillRequest.getWhId());
		// 根据货主id获取货主信息
		Owner owner = ownerBiz.findById(addOrEditAsnBillRequest.getWoId());

		AsnHeader asnHeader = new AsnHeader();
		// ASN单id为空表示新增操作，否则为编辑操作
		if (Func.isNotEmpty(addOrEditAsnBillRequest.getAsnBillId())) {
			// ASN单id
			asnHeader.setAsnBillId(addOrEditAsnBillRequest.getAsnBillId());
			// ASN单编码
			asnHeader.setAsnBillNo(addOrEditAsnBillRequest.getAsnBillNo());
		} else {
			// ASN单编码
			asnHeader.setAsnBillNo(noGeneratorUtil.createAsnBillNo());
			// 单据状态
			asnHeader.setAsnBillState(AsnBillStateEnum.NOT_RECEIPT);
		}
		// 单据类型编码
		asnHeader.setBillTypeCd(addOrEditAsnBillRequest.getBillTypeCd());
		// 备注
		asnHeader.setAsnBillRemark(addOrEditAsnBillRequest.getAsnBillRemark());
		// 供应商id
		asnHeader.setSupplierId(supplier.getId());
		// 供应商编码
		asnHeader.setSupplierCode(supplier.getCode());
		// 供应商名称
		asnHeader.setSupplierName(supplier.getName());
		// 库房id
		asnHeader.setWhId(warehouse.getWhId());
		// 库房编码
		asnHeader.setWhCode(warehouse.getWhCode());
		// 货主id
		asnHeader.setWoId(owner.getWoId());
		// 货主编码
		asnHeader.setOwnerCode(owner.getOwnerCode());
		// 入库方式，默认为常规入库
		asnHeader.setInstoreType(10);
		return asnHeader;
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
			skuSelectResponse.setSkuSpec(item.getSkuSpec());

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

	//创建明细对象
	public AsnDetail createAsnDetail(AsnHeader asnHeaderObj, AsnDetailRequest asnDetailObj) {
		AsnDetail asnDetail = new AsnDetail();
		// 明细id
		if (Func.isNotEmpty(asnDetailObj.getAsnDetailId())) {
			asnDetail.setAsnDetailId(asnDetailObj.getAsnDetailId());
		} else {
			// 实收数量
			asnDetail.setScanQty(new BigDecimal(0));
			// 剩余数量
			asnDetail.setSurplusQty(asnDetailObj.getPlanQty());
		}
		// 行号
		asnDetail.setAsnLineNo(asnDetailObj.getAsnLineNo());
		// ASN单id
		asnDetail.setAsnBillId(asnHeaderObj.getAsnBillId());
		// ASN单编码
		asnDetail.setAsnBillNo(asnHeaderObj.getAsnBillNo());
		// 供应商id
		asnDetail.setSupplierId(asnHeaderObj.getSupplierId());
		// 根据物品id查找物品信息
		Sku sku = skuBiz.findById(asnDetailObj.getSkuId());
		// 物品id
		asnDetail.setSkuId(sku.getSkuId());
		// 物品编码
		asnDetail.setSkuCode(sku.getSkuCode());
		// 物品名称
		asnDetail.setSkuName(sku.getSkuName());
		// 物品规格
		asnDetail.setSkuSpec(sku.getSkuSpec());

		// 根据计量单位编码 获取计量单位信息
		SkuUm skuUm = skuBiz.findSkuUmByUmCode(asnDetailObj.getUmCode());
		asnDetail.setUmCode(skuUm.getWsuCode());
		asnDetail.setUmName(skuUm.getWsuName());

		// 根据物品编码 获取包装和包装明细信息
		SkuPackageAggregate skuPackageAggregate = skuBiz.findSkuPackageAggregateBySkuId(asnDetailObj.getSkuId());
		// 根据计量单位编码从聚合类对象中获取 包装明细信息
		SkuPackageDetail skuPackageDetail = skuPackageAggregate.findSkuPackageDetail(asnDetailObj.getUmCode());
		// 从聚合类对象中获取 基础包装明细信息
		SkuPackageDetail baseSkuPackageDetail = skuPackageAggregate.findBaseSkuPackageDetail();

		// 包装id
		asnDetail.setWspId(skuPackageDetail.getWspId());
		// 层级
		asnDetail.setSkuLevel(skuPackageDetail.getSkuLevel());
		// 换算倍率
		asnDetail.setConvertQty(skuPackageDetail.getConvertQty());
		// 计量单位编码
		asnDetail.setUmCode(skuUm.getWsuCode());
		// 计量单位名称
		asnDetail.setUmName(skuUm.getWsuName());
		// 基础计量单位编码
		asnDetail.setBaseUmCode(baseSkuPackageDetail.getWsuCode());
		// 基础计量单位名称
		asnDetail.setBaseUmName(baseSkuPackageDetail.getWsuName());
		// 计划数量
		asnDetail.setPlanQty(asnDetailObj.getPlanQty());
		// 库房id
		asnDetail.setWhId(asnHeaderObj.getWhId());
		// 备注
		asnDetail.setRemark(asnDetailObj.getRemark());
		return asnDetail;
	}
}
