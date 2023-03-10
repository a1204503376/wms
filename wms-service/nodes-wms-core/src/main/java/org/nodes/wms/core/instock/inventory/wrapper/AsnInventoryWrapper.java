package org.nodes.wms.core.instock.inventory.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.entity.User;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.wms.core.basedata.cache.BillTypeCache;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.core.basedata.service.IEnterpriseService;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.instock.inventory.entity.AsnInventory;
import org.nodes.wms.core.instock.inventory.vo.AsnInventoryVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;





/**
 * 收货清单头表包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-06-09
 */
public class AsnInventoryWrapper extends BaseEntityWrapper<AsnInventory, AsnInventoryVO> {

	public static AsnInventoryWrapper build() {
		return new AsnInventoryWrapper();
	}

	@Override
	public AsnInventoryVO entityVO(AsnInventory asnInventory) {
		AsnInventoryVO asnInventoryVO = BeanUtil.copy(asnInventory, AsnInventoryVO.class);
		if (Func.isNotEmpty(asnInventoryVO)) {
			//单据状态名称
			asnInventoryVO.setAsnBillStateName(DictCache.getValue(DictCodeConstant.ASN_BILL_STATE, asnInventoryVO.getAsnBillState()));
			//入库方式名称
			asnInventoryVO.setInstoreTypeName(DictCache.getValue(DictCodeConstant.INSTORE_TYPE, asnInventoryVO.getInstoreType()));
			asnInventoryVO.setCreateTypeName(DictCache.getValue(DictCodeConstant.CREATE_TYPE, asnInventoryVO.getCreateType()));
			//同步状态名称
			asnInventoryVO.setSyncStateName(DictCache.getValue(DictCodeConstant.SYNC_STATE, asnInventoryVO.getSyncState()));
			//货主名称
			if (Func.isNotEmpty(asnInventoryVO.getWoId())) {
				IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
				Owner owner = ownerService.getById(asnInventoryVO.getWoId());
				if (Func.isNotEmpty(owner) && Func.isNotEmpty(owner.getOwnerName())) {
					asnInventoryVO.setOwnerName(owner.getOwnerName());
				}
			}
			if (Func.isNotEmpty(asnInventoryVO.getCreateUser())) {
				User user = UserCache.getById(asnInventoryVO.getCreateUser());
				if (Func.isNotEmpty(user)) {
					asnInventoryVO.setCreateUserName(user.getName());
				}
			}
			//单据类型名称
			if (Func.isNotEmpty(asnInventoryVO.getBillTypeCd())) {
				BillType billType = BillTypeCache.getByCode(asnInventoryVO.getBillTypeCd());
				if (Func.isNotEmpty(billType)) {
					asnInventoryVO.setBillTypeName(billType.getBillTypeName());
				}
			}
			//过账类型
			asnInventoryVO.setPostStateCd(DictCache.getValue(DictCodeConstant.POST_STATE, asnInventoryVO.getPostState()));
			//过账人
			if (Func.isNotEmpty(asnInventoryVO.getPostUser())) {
				User user = UserCache.getById(asnInventoryVO.getPostUser());
				if (Func.isNotEmpty(user)) {
					asnInventoryVO.setPostUserCd(user.getName());
				}
			}
			//库房名称
			Warehouse warehouse = WarehouseCache.getById(asnInventoryVO.getWhId());
			if (Func.isNotEmpty(warehouse) && Func.isNotEmpty(warehouse.getWhName())) {
				asnInventoryVO.setWhName(warehouse.getWhName());
			}
			//供应商ID
			if (Func.isNotEmpty(asnInventoryVO.getSCode())) {
				IEnterpriseService iEnterpriseService = SpringUtil.getBean(IEnterpriseService.class);
				Enterprise enterprise = iEnterpriseService.list(Condition.getQueryWrapper(new Enterprise())
					.lambda()
					.eq(Enterprise::getEnterpriseCode, asnInventoryVO.getSCode()))
					.stream().findFirst().orElse(null);
				if (ObjectUtil.isNotEmpty(enterprise)) {
					asnInventoryVO.setSId(enterprise.getPeId());
				}
			}
			User user = UserCache.getById(asnInventoryVO.getCreateUser());
			if (Func.isNotEmpty(user)) {
				asnInventoryVO.setCreateUserName(user.getName());
			}
		}
		return asnInventoryVO;
	}
}
