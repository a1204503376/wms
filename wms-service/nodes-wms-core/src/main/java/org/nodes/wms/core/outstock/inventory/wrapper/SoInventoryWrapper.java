package org.nodes.wms.core.outstock.inventory.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.basedata.cache.BillTypeCache;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.core.basedata.service.IEnterpriseService;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.outstock.inventory.entity.SoInventory;
import org.nodes.wms.core.outstock.inventory.vo.SoInventoryVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;


/**
 * 发货清单主表
 * 包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-06-09
 */
public class SoInventoryWrapper extends BaseEntityWrapper<SoInventory, SoInventoryVO> {

	public static SoInventoryWrapper build() {
		return new SoInventoryWrapper();
	}

	@Override
	public SoInventoryVO entityVO(SoInventory soInventory) {
		SoInventoryVO soInventoryVO = BeanUtil.copy(soInventory, SoInventoryVO.class);
		if (Func.isNotEmpty(soInventoryVO)) {
			//单据状态名称
			soInventoryVO.setSoBillStateName(
				DictCache.getValue(DictConstant.SO_BILL_STATE, soInventoryVO.getSoBillState()));
			//出库方式名称
			soInventoryVO.setOutstockTypeName(
				DictCache.getValue(DictConstant.OUTSTORE_TYPE, soInventoryVO.getOutstockType()));
			//同步状态名称
			soInventoryVO.setSyncStateName(
				DictCache.getValue(DictConstant.SYNC_STATE, soInventoryVO.getSyncState()));
			//创建类型
			soInventoryVO.setCreateTypeName(
				DictCache.getValue(DictConstant.CREATE_TYPE, soInventoryVO.getCreateType()));
			//货主名称
			if (Func.isNotEmpty(soInventoryVO.getWoId())) {
				IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
				Owner owner = ownerService.getById(soInventoryVO.getWoId());
				if (Func.isNotEmpty(owner) && Func.isNotEmpty(owner.getOwnerName())) {
					soInventoryVO.setOwnerName(owner.getOwnerName());
				}
			}
			//单据类型名称
			if (Func.isNotEmpty(soInventoryVO.getBillTypeCd())) {
				BillType billType = BillTypeCache.getByCode(soInventoryVO.getBillTypeCd());
				if (Func.isNotEmpty(billType)) {
					if (Func.isNotEmpty(billType.getBillTypeName())) {
						soInventoryVO.setBillTypeName(billType.getBillTypeName());
					}
				}
			}
			//库房名称
			Warehouse warehouse = WarehouseCache.getById(soInventoryVO.getWhId());
			if (Func.isNotEmpty(warehouse) && Func.isNotEmpty(warehouse.getWhName())) {
				soInventoryVO.setWhName(warehouse.getWhName());
			}

			//客户ID
			if (Func.isNotEmpty(soInventoryVO.getCCode())) {
				IEnterpriseService iEnterpriseService = SpringUtil.getBean(IEnterpriseService.class);

				//Enterprise enterprise = iEnterpriseService.getByCode(soInventoryVO.getCCode());
				Enterprise enterprise = iEnterpriseService.list(Condition.getQueryWrapper(new Enterprise())
					.lambda()
					.eq(Enterprise::getEnterpriseCode, soInventoryVO.getCCode()))
					.stream().findFirst().orElse(null);
				if (Func.isNotEmpty(enterprise)) {
					soInventoryVO.setCId(enterprise.getPeId());
				}
			}
			//承运商ID
			if (Func.isNotEmpty(soInventoryVO.getExpressCode())) {
				//Enterprise enterprise = EnterpriseCache.getByCode(soInventoryVO.getExpressCode());
				IEnterpriseService iEnterpriseService = SpringUtil.getBean(IEnterpriseService.class);
				Enterprise enterprise = iEnterpriseService.list(Condition.getQueryWrapper(new Enterprise())
					.lambda()
					.eq(Enterprise::getEnterpriseCode, soInventoryVO.getExpressCode()))
					.stream().findFirst().orElse(null);
				if (Func.isNotEmpty(enterprise)) {
					soInventoryVO.setExpressId(enterprise.getPeId());
				}
			}
			//发货方式
			soInventoryVO.setTransportDesc(
				DictCache.getValue(DictConstant.SO_TRANSPORT_CODE, soInventoryVO.getTransportCode()));
			// 发运状态
			soInventoryVO.setShipStateDesc(
				DictCache.getValue(DictConstant.SHIP_STATE, soInventoryVO.getShipState()));
		}
		return soInventoryVO;
	}
}
