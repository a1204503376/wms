package org.nodes.wms.biz.instock.receive.modular;


import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.biz.common.utils.NoGeneratorUtil;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.dto.output.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageAggregate;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierSelectResponse;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.instock.receive.dto.input.EditReceiveDetailRequest;
import org.nodes.wms.dao.instock.receive.dto.input.EditReceiveHeaderRequest;
import org.nodes.wms.dao.instock.receive.dto.input.NewReceiveDetailRequest;
import org.nodes.wms.dao.instock.receive.dto.input.NewReceiveHeaderRequest;
import org.nodes.wms.dao.instock.receive.dto.output.EditReceiveDetailResponse;
import org.nodes.wms.dao.instock.receive.dto.output.EditReceiveHeaderResponse;
import org.nodes.wms.dao.instock.receive.entities.ReceiveDetail;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;
import org.nodes.wms.dao.instock.receive.enums.ReceiveHeaderStateEnum;
import org.springblade.core.tool.utils.Func;
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
	private final OwnerBiz ownerBiz;
	private final NoGeneratorUtil noGeneratorUtil;

	private final SkuBiz skuBiz;


	public static void main(String[] args) {

	}


	/**
	 * 新增收货单创建收货单头表实体
	 *
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
		receiveHeader.setOwnerCode(owner.getOwnerCode());
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
		receiveHeader.setBillState(ReceiveHeaderStateEnum.NOT_RECEIPT);

		return receiveHeader;
	}

	/**
	 * 新增收货单创建收货单明细实体
	 *
	 * @param newReceiveDetailRequest 前端传入收货单明细Request
	 * @param receiveHeader           收货单头表信息
	 * @return ReceiveDetail
	 */
	public ReceiveDetail createReceiveDetail(NewReceiveDetailRequest newReceiveDetailRequest, ReceiveHeader receiveHeader) {
		//根据物料id获取物料实体
		Sku sku = skuBiz.findById(newReceiveDetailRequest.getSku().getSkuId());
		//获取包装信息
		SkuPackageAggregate skuPackageAggregate = skuBiz.findSkuPackageAggregateBySkuId(sku.getSkuId());
		//获取当前包装明细
		SkuPackageDetail skuPackageDetail = skuPackageAggregate.findSkuPackageDetail(newReceiveDetailRequest.getUmCode());
		//获取基础包装明细
		SkuPackageDetail baseSkuPackageDetail = skuPackageAggregate.findBaseSkuPackageDetail();
		//收货单明细实体
		ReceiveDetail receiveDetail = new ReceiveDetail();
		//设置明细表收货单id
		receiveDetail.setReceiveId(receiveHeader.getReceiveId());
		//  设置订单行号
		receiveDetail.setLineNo(newReceiveDetailRequest.getLineNumber());
		//设置物料id
		receiveDetail.setSkuId(sku.getSkuId());
		//设置物料编码
		receiveDetail.setSkuCode(sku.getSkuCode());
		//设置物料名称
		receiveDetail.setSkuName(sku.getSkuName());
		//设置计划数量
		receiveDetail.setPlanQty(newReceiveDetailRequest.getPlanQty());
		//设置实收数量
		receiveDetail.setScanQty(BigDecimal.valueOf(0));
		//设置剩余数量
		receiveDetail.setSurplusQty(newReceiveDetailRequest.getPlanQty());
		//设置备注
		receiveDetail.setRemark(newReceiveDetailRequest.getRemark());
		//设置收货单编码
		receiveDetail.setReceiveNo(receiveHeader.getReceiveNo());
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
		//设置库房id
		receiveDetail.setWhId(receiveHeader.getWhId());
		//设置库房编码
		receiveDetail.setWhCode(receiveHeader.getWhCode());
		//设置货主id
		receiveDetail.setWoId(receiveHeader.getWoId());
		//设置生产批次
		receiveDetail.setSkuLot1(newReceiveDetailRequest.getSkuLot1());
		//设置客户
		receiveDetail.setSkuLot4(newReceiveDetailRequest.getSkuLot4());
		//设置钢背批次
		receiveDetail.setSkuLot5(newReceiveDetailRequest.getSkuLot5());
		//设置摩擦块批次
		receiveDetail.setSkuLot6(newReceiveDetailRequest.getSkuLot6());
		//设置CRCC
		receiveDetail.setSkuLot8(newReceiveDetailRequest.getSkuLot8());
		return receiveDetail;
	}

	/**
	 * 创建返回编辑页面收货单头表Response
	 *
	 * @param receiveHeader 收货单头表实体
	 * @return ReceiveHeaderEditResponse
	 */
	public EditReceiveHeaderResponse createReceiveHeaderEditResponse(ReceiveHeader receiveHeader) {
		EditReceiveHeaderResponse receiveHeaderEditResponse = new EditReceiveHeaderResponse();
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
	 *
	 * @param receiveDetail 收货单头表实体
	 * @return ReceiveDetailEditResponse
	 */
	public EditReceiveDetailResponse createReceiveDetailEditResponse(ReceiveDetail receiveDetail) {
		EditReceiveDetailResponse editReceiveDetailResponse = new EditReceiveDetailResponse();
		//设置sku对象信息
		editReceiveDetailResponse.setSku(new SkuSelectResponse());
		editReceiveDetailResponse.getSku().setSkuId(receiveDetail.getSkuId());
		editReceiveDetailResponse.getSku().setSkuCode(receiveDetail.getSkuCode());
		editReceiveDetailResponse.getSku().setSkuName(receiveDetail.getSkuName());
		//设置行号
		editReceiveDetailResponse.setLineNumber(receiveDetail.getLineNo());
		//设置计划数量
		editReceiveDetailResponse.setPlanQty(receiveDetail.getPlanQty());
		//设置实收数量
		editReceiveDetailResponse.setScanQty(receiveDetail.getScanQty());
		//设置计量单位编码
		editReceiveDetailResponse.setUmCode(receiveDetail.getUmCode());
		//设置规格
		editReceiveDetailResponse.setSkuSpec(receiveDetail.getSkuSpec());
		//设置备注
		editReceiveDetailResponse.setRemark(receiveDetail.getRemark());
		//设置收货单明细id
		editReceiveDetailResponse.setReceiveDetailId(receiveDetail.getReceiveDetailId());
		//设置批属性
		editReceiveDetailResponse.setSkuLot1(receiveDetail.getSkuLot1());
		editReceiveDetailResponse.setSkuLot4(receiveDetail.getSkuLot4());
		editReceiveDetailResponse.setSkuLot5(receiveDetail.getSkuLot5());
		editReceiveDetailResponse.setSkuLot6(receiveDetail.getSkuLot6());
		editReceiveDetailResponse.setSkuLot8(receiveDetail.getSkuLot8());
		return editReceiveDetailResponse;
	}

	public ReceiveHeader createEditReceiveHeader(EditReceiveHeaderRequest editReceiveHeaderRequest) {

		//收货单头表实体
		ReceiveHeader receiveHeader = new ReceiveHeader();
		//设置收货单id
		receiveHeader.setReceiveId(editReceiveHeaderRequest.getReceiveId());
		//根据仓库id获取仓库实体
		Warehouse warehouse = warehouseBiz.findById(editReceiveHeaderRequest.getWhId());
		//根据货主id获取货主实体
		Owner owner = ownerBiz.findById(editReceiveHeaderRequest.getWoId());
		//设置仓库id
		receiveHeader.setWhId(warehouse.getWhId());
		//设置仓库编码
		receiveHeader.setWhCode(warehouse.getWhCode());
		//设置货主id
		receiveHeader.setWoId(owner.getWoId());
		//设置货主编码
		receiveHeader.setOwnerCode(owner.getOwnerCode());
		//设置供应商id
		receiveHeader.setSupplierId(editReceiveHeaderRequest.getSupplier().getId());
		//设置供应商编码
		receiveHeader.setSupplierCode(editReceiveHeaderRequest.getSupplier().getCode());
		//设置供应商名称
		receiveHeader.setSupplierName(editReceiveHeaderRequest.getSupplier().getName());
		//设置单据类型
		receiveHeader.setBillTypeCd(editReceiveHeaderRequest.getBillTypeCd());
		//设置入库方式
		receiveHeader.setInStoreType(editReceiveHeaderRequest.getInStoreType());
		//设置备注
		receiveHeader.setRemark(editReceiveHeaderRequest.getRemark());
		return receiveHeader;

	}

	public ReceiveDetail createEditReceiveDetail(EditReceiveDetailRequest editReceiveDetailRequest,ReceiveHeader receiveHeader) {
		//收货单明细实体
		ReceiveDetail receiveDetail = new ReceiveDetail();
		//设置收货单明细id
		if(Func.isNotEmpty(editReceiveDetailRequest.getReceiveDetailId())){
			receiveDetail.setReceiveDetailId(editReceiveDetailRequest.getReceiveDetailId());
		}else{
			//设置收货单编码
			receiveDetail.setReceiveNo(receiveHeader.getReceiveNo());
			//设置收货单id
			receiveDetail.setReceiveId(receiveHeader.getReceiveId());
		}
		//根据物料id获取物料实体
		Sku sku = skuBiz.findById(editReceiveDetailRequest.getSku().getSkuId());
		//获取包装信息
		SkuPackageAggregate skuPackageAggregate = skuBiz.findSkuPackageAggregateBySkuId(sku.getSkuId());
		//获取当前包装明细
		SkuPackageDetail skuPackageDetail = skuPackageAggregate.findSkuPackageDetail(editReceiveDetailRequest.getUmCode());
		//获取基础包装明细
		SkuPackageDetail baseSkuPackageDetail = skuPackageAggregate.findBaseSkuPackageDetail();
		//设置明细表收货单id
		receiveDetail.setReceiveId(receiveHeader.getReceiveId());
		//  设置订单行号
		receiveDetail.setLineNo(editReceiveDetailRequest.getLineNumber());
		//设置物料id
		receiveDetail.setSkuId(sku.getSkuId());
		//设置物料编码
		receiveDetail.setSkuCode(sku.getSkuCode());
		//设置物料名称
		receiveDetail.setSkuName(sku.getSkuName());
		//设置计划数量
		receiveDetail.setPlanQty(editReceiveDetailRequest.getPlanQty());
		//设置实收数量
		receiveDetail.setScanQty(BigDecimal.valueOf(0));
		//设置剩余数量
		receiveDetail.setSurplusQty(editReceiveDetailRequest.getPlanQty());
		//设置备注
		receiveDetail.setRemark(editReceiveDetailRequest.getRemark());
		//设置收货单编码
		receiveDetail.setReceiveNo(receiveHeader.getReceiveNo());
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
		//设置库房id
		receiveDetail.setWhId(receiveHeader.getWhId());
		//设置库房编码
		receiveDetail.setWhCode(receiveHeader.getWhCode());
		//设置货主id
		receiveDetail.setWoId(receiveHeader.getWoId());
		//设置批属性信息
		receiveDetail.setSkuLot1(editReceiveDetailRequest.getSkuLot1());
		receiveDetail.setSkuLot4(editReceiveDetailRequest.getSkuLot4());
		receiveDetail.setSkuLot5(editReceiveDetailRequest.getSkuLot5());
		receiveDetail.setSkuLot6(editReceiveDetailRequest.getSkuLot6());
		receiveDetail.setSkuLot8(editReceiveDetailRequest.getSkuLot8());
		return receiveDetail;
	}
}
