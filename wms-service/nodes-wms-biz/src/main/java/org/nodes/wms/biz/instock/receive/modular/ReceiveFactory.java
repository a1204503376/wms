package org.nodes.wms.biz.instock.receive.modular;


import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.suppliers.SupplierBiz;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.biz.common.utils.NoGeneratorUtil;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.nodes.wms.dao.basics.warehouse.entites.Warehouse;
import org.nodes.wms.dao.instock.receive.dto.input.NewReceiveHeaderRequest;
import org.nodes.wms.dao.instock.receive.dto.input.ReceiveNewDetailRequest;
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
	public ReceiveHeader createReceiveHeader(NewReceiveHeaderRequest newReceiveHeaderRequest) {
		//根据仓库id获取仓库实体
		Warehouse warehouse = warehouseBiz.findById(newReceiveHeaderRequest.getWhId());
		//根据供应商id获取供应商实体
		Supplier supplier = supplierBiz.findById(newReceiveHeaderRequest.getSupplierId());
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
		receiveHeader.setSupplierId(supplier.getId());
		//设置供应商编码
		receiveHeader.setSupplierCode(supplier.getCode());
		//设置供应商名称
		receiveHeader.setSupplierName(supplier.getName());
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

	public ReceiveDetail createReceiveDetail(ReceiveNewDetailRequest receiveNewDetailRequest, Long receiveId, String receiveNo) {
		//根据物料id获取物料实体
		Sku sku = skuBiz.findById(receiveNewDetailRequest.getSkuId());
		//根据计量单位编码id查询计量单位实体
		SkuUm skuUm = skuBiz.findSkuUmByUmCode(receiveNewDetailRequest.getUmCode());
		//收货单明细实体
		ReceiveDetail receiveDetail = new ReceiveDetail();
		//设置明细表收货单id
		receiveDetail.setReceiveId(receiveId);
		//  设置订单行号
		receiveDetail.setLineNo(receiveNewDetailRequest.getLineNo());
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
		receiveDetail.setUmCode(skuUm.getWsuCode());
		//设置计量单位名称
		receiveDetail.setUmName(skuUm.getWsuName());
		//设置基础计量单位编码
		receiveDetail.setBaseUmCode(skuUm.getWsuCode());
		//设置基础计量单位名称
		receiveDetail.setBaseUmName(skuUm.getWsuName());

		return receiveDetail;
	}
}
