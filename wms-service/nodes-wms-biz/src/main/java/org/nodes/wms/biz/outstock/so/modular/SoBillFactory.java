package org.nodes.wms.biz.outstock.so.modular;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.customer.CustomerBiz;
import org.nodes.wms.biz.basics.owner.OwnerBiz;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.biz.common.utils.NoGeneratorUtil;
import org.nodes.wms.dao.basics.customer.entities.BasicsCustomer;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageAggregate;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.outstock.so.dto.input.SoBillAddOrEditRequest;
import org.nodes.wms.dao.outstock.so.dto.input.SoDetailAddOrEditRequest;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.nodes.wms.dao.outstock.so.entities.SoHeader;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.dao.outstock.so.enums.SoDetailStateEnum;
import org.nodes.wms.dao.outstock.so.enums.SyncStateEnum;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 发货单头表工厂类
 **/
@Service
@RequiredArgsConstructor
public class SoBillFactory {

	private final NoGeneratorUtil noGeneratorUtil;

	private final CustomerBiz customerBiz;

	private final WarehouseBiz warehouseBiz;

	private final OwnerBiz ownerBiz;

	private final SkuBiz skuBiz;

	public SoHeader createSoHeader(SoBillAddOrEditRequest soBillAddOrEditRequest) {
		SoHeader soHeader = new SoHeader();
		BeanUtil.copy(soBillAddOrEditRequest, soHeader);

		if (Func.isNotEmpty(soBillAddOrEditRequest.getCustomerId())) {
			BasicsCustomer customer = customerBiz.findCustomerById(soBillAddOrEditRequest.getCustomerId());
			// 客户编码和名称
			soHeader.setCustomerCode(customer.getCode());
			soHeader.setCustomerName(customer.getName());
		}
		Warehouse warehouse = warehouseBiz.findById(soHeader.getWhId());
		Owner owner = ownerBiz.findById(soHeader.getWoId());

		// id为空即新增时
		if (Func.isEmpty(soHeader.getSoBillId())) {
			// 单据编码
			soHeader.setSoBillNo(noGeneratorUtil.createSoBillNo());
			// 单据状态：10 单据创建
			soHeader.setSoBillState(SoBillStateEnum.NOT.getIndex());
			// 订单状态
			soHeader.setSyncState(SyncStateEnum.DEFAULT.getIndex());
		}
		// 库房编码
		soHeader.setWhCode(warehouse.getWhCode());
		// 货主编码
		soHeader.setOwnerCode(owner.getOwnerCode());

		return soHeader;
	}

	public List<SoDetail> createSoDetailList(SoHeader soHeader, List<SoDetailAddOrEditRequest> soBillAddOrEditRequestList) {
		List<SoDetail> soDetailListResult = Func.copy(soBillAddOrEditRequestList, SoDetail.class);
		soDetailListResult.forEach(detail -> {
			Sku sku = skuBiz.findById(detail.getSkuId());
			// 根据物品编码获取包装和包装明细信息
			SkuPackageAggregate skuPackageAggregate = skuBiz.findSkuPackageAggregateBySkuId(detail.getSkuId());
			// 根据计量单位编码从聚合类对象中获取包装明细信息
			SkuPackageDetail skuPackageDetail = skuPackageAggregate.findSkuPackageDetail(detail.getUmCode());
			// 从聚合类对象中获取基础包装明细信息
			SkuPackageDetail baseSkuPackageDetail = skuPackageAggregate.findBaseSkuPackageDetail();

			// id为空即新增时
			if (Func.isEmpty(detail.getSoDetailId())) {
				// 实收数量
				detail.setScanQty(BigDecimal.ZERO);
			}
			// 发货单id，编码，单据类型
			detail.setSoBillId(soHeader.getSoBillId());
			detail.setSoBillNo(soHeader.getSoBillNo());
			detail.setBillTypeCd(soHeader.getBillTypeCd());
			// 物品
			detail.setSkuCode(sku.getSkuCode());
			detail.setSkuName(sku.getSkuName());
			detail.setSkuSpec(sku.getSkuSpec());
			// 包装id，层级，换算倍率
			detail.setWspId(skuPackageDetail.getWspId());
			detail.setSkuLevel(skuPackageDetail.getSkuLevel());
			detail.setConvertQty(skuPackageDetail.getConvertQty());
			// 计量单位编码和名称
			detail.setUmCode(skuPackageDetail.getWsuCode());
			detail.setUmName(skuPackageDetail.getWsuName());
			// 基础计量单位编码和名称
			detail.setBaseUmCode(baseSkuPackageDetail.getWsuCode());
			detail.setBaseUmName(baseSkuPackageDetail.getWsuName());
			//剩余数量
			detail.setSurplusQty(detail.getPlanQty().subtract(detail.getScanQty()));
			// 发货库房
			detail.setPickWhCode(soHeader.getWhCode());
			// 单价
			detail.setDetailPrice(sku.getSkuPrice());
			// 明细总金额 计划数量×物品单价
			detail.setDetailAmount(
				Func.isEmpty(detail.getDetailPrice()) ?
					null : detail.getDetailPrice().multiply(detail.getPlanQty()));
			//单据状态
			detail.setBillDetailState(SoDetailStateEnum.UnAlloc.getIndex());
			// 批属性 生产批次、专用客户
		});
		return soDetailListResult;
	}

	public SoHeader createSoHeaderByCustom(Map<String, Object> soHeaderMap) {
		SoHeader soHeader = new SoHeader();
		BeanUtil.copy(soHeaderMap, soHeader);
		return soHeader;
	}
}
