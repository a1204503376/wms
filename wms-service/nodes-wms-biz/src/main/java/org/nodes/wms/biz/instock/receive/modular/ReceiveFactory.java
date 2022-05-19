package org.nodes.wms.biz.instock.receive.modular;


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
import org.nodes.wms.dao.instock.receive.dto.input.NewReceiveHeaderRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveNewDetailRequest;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveDetailEditResponse;
import org.nodes.wms.dao.instock.receive.dto.output.ReceiveHeaderEditResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.enums.ReceiveBillStateEnum;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 收货单工厂
 * 主要用于创建收货单实体
 */
@Service
@RequiredArgsConstructor
public class ReceiveFactory {
	private final WarehouseBiz warehouseBiz;
	private final SupplierBiz supplierBiz;
	private final OwnerBiz ownerBiz;
	private final NoGeneratorUtil noGeneratorUtil;

	private final SkuBiz skuBiz;

	/**
	 * 新增收货单创建收货单头表实体
	 * @param newReceiveHeaderRequest 前端传入收货单头表Request
	 * @return ReceiveHeader
	 */
	public ReceiveHeader createReceiveHeader(NewReceiveHeaderRequest newReceiveHeaderRequest) {
		//根据仓库id获取仓库实体
		Warehouse warehouse = warehouseBiz.findById(newReceiveHeaderRequest.getWhId());
		//根据货主id获取货主实体
		Owner owner = ownerBiz.findById(newReceiveHeaderRequest.getWoId());
		//收货单头表实体
		ReceiveHeader receiveHeader = new ReceiveHeader();
		//设置仓库id
		receiveHeader.setWhId(warehouse.getWhId());
		//设置仓库编码
		receiveHeader.setWhCode(warehouse.getWhCode());
       //设置货主id
		receiveHeader.setWoId(owner.getWoId());
		//设置货主编码
		receiveHeader.setWhCode(owner.getOwnerCode());
		//设置供应商id
		receiveHeader.setSupplierId(newReceiveHeaderRequest.getSupplier().getId());
		//设置供应商编码
		receiveHeader.setSupplierCode(newReceiveHeaderRequest.getSupplier().getCode());
		//设置供应商名称
		receiveHeader.setSupplierName(newReceiveHeaderRequest.getSupplier().getName());
		//设置单据类型
		receiveHeader.setBillTypeCd(newReceiveHeaderRequest.getBillTypeCd());
		//设置入库方式
		receiveHeader.setInStoreType(newReceiveHeaderRequest.getInStoreType());
        //设置备注
		receiveHeader.setRemark(newReceiveHeaderRequest.getRemark());
		//设置收货单编码
		receiveHeader.setReceiveNo(noGeneratorUtil.createReceiveBillNo());
		//设置收货单状态
		receiveHeader.setBillState(ReceiveBillStateEnum.NOT_RECEIPT);

       return receiveHeader;
	}

	/**
	 * 新增收货单创建收货单明细实体
	 * @param receiveNewDetailRequest 前端传入收货单明细Request
	 * @param receiveId 收货单Id
	 * @param receiveNo 收货单编码
	 * @return ReceiveDetail
	 */
	public ReceiveDetail createReceiveDetail(ReceiveNewDetailRequest receiveNewDetailRequest, Long receiveId, String receiveNo) {
		//根据物料id获取物料实体
		Sku sku = skuBiz.findById(receiveNewDetailRequest.getSku().getSkuId());
		//获取包装信息
		SkuPackageAggregate skuPackageAggregate  = skuBiz.findSkuPackageAggregateBySkuId(sku.getSkuId());
		//获取当前包装明细
		SkuPackageDetail skuPackageDetail = skuPackageAggregate.findSkuPackageDetail(receiveNewDetailRequest.getUmCode());
		//获取基础包装明细
		SkuPackageDetail baseSkuPackageDetail = skuPackageAggregate.findBaseSkuPackageDetail();
		//收货单明细实体
		ReceiveDetail receiveDetail = new ReceiveDetail();
		//设置明细表收货单id
		receiveDetail.setReceiveId(receiveId);
		//  设置订单行号
		receiveDetail.setLineNo(receiveNewDetailRequest.getLineNumber());
		//设置物料id
		receiveDetail.setSkuId(sku.getSkuId());
		//设置物料编码
		receiveDetail.setSkuCode(sku.getSkuCode());
		//设置物料名称
		 receiveDetail.setSkuName(sku.getSkuName());
        //设置计划数量
	   	receiveDetail.setPlanQty(receiveNewDetailRequest.getPlanQty());
		//设置实收数量
		receiveDetail.setScanQty(BigDecimal.valueOf(0));
		//设置剩余数量
		receiveDetail.setSurplusQty(receiveNewDetailRequest.getPlanQty());
		//设置备注
		receiveDetail.setRemark(receiveNewDetailRequest.getRemark());
		//设置收货单编码
		receiveDetail.setReceiveNo(receiveNo);
		//设置包装id
		receiveDetail.setWspId(sku.getWspId());
		//设置计量单位编码
		receiveDetail.setUmCode(skuPackageDetail.getWsuCode());
		//设置计量单位名称
		receiveDetail.setUmName(skuPackageDetail.getWsuName());
		//设置基础计量单位编码
		receiveDetail.setBaseUmCode(baseSkuPackageDetail.getWsuCode());
		//设置基础计量单位名称
		receiveDetail.setBaseUmName(baseSkuPackageDetail.getWsuName());
		//设置换算倍率
		receiveDetail.setConvertQty(skuPackageDetail.getConvertQty());
		//设置层级
		receiveDetail.setSkuLevel(skuPackageDetail.getSkuLevel());
		//设置规格
		receiveDetail.setSkuSpec(sku.getSkuSpec());

		return receiveDetail;
	}

	/**
	 * 创建返回编辑页面收货单头表Response
	 * @param receiveHeader 收货单头表实体
	 * @return ReceiveHeaderEditResponse
	 */
	public ReceiveHeaderEditResponse createReceiveHeaderEditResponse(ReceiveHeader receiveHeader){
		ReceiveHeaderEditResponse receiveHeaderEditResponse = new ReceiveHeaderEditResponse();
		//设置收货单头表id
		receiveHeaderEditResponse.setReceiveId(receiveHeader.getReceiveId());
		//设置库房id
		receiveHeaderEditResponse.setWhId(receiveHeader.getWhId());
		//设置单据类型
		receiveHeaderEditResponse.setBillTypeCd(receiveHeader.getBillTypeCd());
		//设置供应商对象信息
		receiveHeaderEditResponse.setSupplier(new SupplierSelectResponse());
		receiveHeaderEditResponse.getSupplier().setId(receiveHeader.getSupplierId());
		receiveHeaderEditResponse.getSupplier().setCode(receiveHeader.getSupplierCode());
		receiveHeaderEditResponse.getSupplier().setName(receiveHeader.getSupplierName());
        //设置入库方式
		receiveHeaderEditResponse.setInStoreType(receiveHeader.getInStoreType());
		//设置货主id
		receiveHeaderEditResponse.setWoId(receiveHeader.getWoId());
		//设置备注
		receiveHeaderEditResponse.setRemark(receiveHeader.getRemark());
		return receiveHeaderEditResponse;
	}

	/**
	 * 创建返回前端编辑页面收货单明细Response
	 * @param receiveDetail 收货单头表实体
	 * @return ReceiveDetailEditResponse
	 */
	public ReceiveDetailEditResponse createReceiveDetailEditResponse(ReceiveDetail receiveDetail) {
		ReceiveDetailEditResponse receiveDetailEditResponse = new ReceiveDetailEditResponse();
		//设置sku对象信息
		receiveDetailEditResponse.setSku(new SkuSelectResponse());
		receiveDetailEditResponse.getSku().setSkuId(receiveDetail.getSkuId());
		receiveDetailEditResponse.getSku().setSkuCode(receiveDetail.getSkuCode());
		receiveDetailEditResponse.getSku().setSkuName(receiveDetail.getSkuName());
		//设置计划数量
		receiveDetailEditResponse.setPlanQty(receiveDetail.getPlanQty());
		//设置计量单位编码
		 receiveDetailEditResponse.setUmCode(receiveDetail.getUmCode());
		 //设置规格
		receiveDetailEditResponse.setSkuSpec(receiveDetail.getSkuSpec());
		//设置备注
		receiveDetailEditResponse.setRemark(receiveDetail.getRemark());
		return receiveDetailEditResponse;
	}
}
