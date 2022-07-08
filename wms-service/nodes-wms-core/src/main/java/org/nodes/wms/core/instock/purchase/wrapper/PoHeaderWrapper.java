package org.nodes.wms.core.instock.purchase.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.entity.User;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.wms.core.basedata.cache.BillTypeCache;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.core.basedata.service.IEnterpriseService;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.instock.purchase.entity.PoHeader;
import org.nodes.wms.core.instock.purchase.vo.PoHeaderVO;
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
 * 采购单头表包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-05-31
 */
public class PoHeaderWrapper extends BaseEntityWrapper<PoHeader, PoHeaderVO> {

	public static PoHeaderWrapper build() {
		return new PoHeaderWrapper();
	}

	@Override
	public PoHeaderVO entityVO(PoHeader poHeader) {
		PoHeaderVO poHeaderVO = BeanUtil.copy(poHeader, PoHeaderVO.class);
		if (Func.isNotEmpty(poHeaderVO)) {
			//单据状态名称
			poHeaderVO.setPoBillStateName(
				DictCache.getValue(DictCodeConstant.ASN_BILL_STATE, poHeaderVO.getPoBillState()));
			//入库方式名称
			poHeaderVO.setInstoreTypeName(
				DictCache.getValue(DictCodeConstant.INSTORE_TYPE, poHeaderVO.getInstoreType()));
			poHeaderVO.setCreateTypeName(
				DictCache.getValue(DictCodeConstant.CREATE_TYPE, poHeaderVO.getCreateType()));
			//同步状态名称
			poHeaderVO.setSyncStateName(
				DictCache.getValue(DictCodeConstant.SYNC_STATE, poHeaderVO.getSyncState()));
			//货主名称
			if (Func.isNotEmpty(poHeaderVO.getWoId())) {
				IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
				Owner owner = ownerService.getById(poHeaderVO.getWoId());
				if (Func.isNotEmpty(owner) && Func.isNotEmpty(owner.getOwnerName())) {
					poHeaderVO.setOwnerName(owner.getOwnerName());
				}
			}
			if (Func.isNotEmpty(poHeaderVO.getCreateUser())) {
				User user = UserCache.getById(poHeaderVO.getCreateUser());
				if (Func.isNotEmpty(user)) {
					poHeaderVO.setCreateUserName(user.getName());
				}
			}
			//单据类型名称
			if (Func.isNotEmpty(poHeaderVO.getBillTypeCd())) {
				BillType billType = BillTypeCache.getByCode(poHeaderVO.getBillTypeCd());
				if (Func.isNotEmpty(billType)) {
					if (Func.isNotEmpty(billType.getBillTypeName())) {
						poHeaderVO.setBillTypeName(billType.getBillTypeName());
					}
				}
			}
			//过账类型
			poHeaderVO.setPostStateCd(
				DictCache.getValue(DictCodeConstant.POST_STATE, poHeaderVO.getPostState()));
			//过账人
			if (Func.isNotEmpty(poHeaderVO.getPostUser())) {
				User user = UserCache.getById(poHeaderVO.getPostUser());
				if (Func.isNotEmpty(user)) {
					poHeaderVO.setPostUserCd(user.getName());
				}
			}
			//库房名称
			Warehouse warehouse = WarehouseCache.getById(poHeaderVO.getWhId());
			if (Func.isNotEmpty(warehouse) && Func.isNotEmpty(warehouse.getWhName())) {
				poHeaderVO.setWhName(warehouse.getWhName());
			}
			//供应商ID
			if (Func.isNotEmpty(poHeaderVO.getSCode())) {
				IEnterpriseService enterpriseService = SpringUtil.getBean(IEnterpriseService.class);
				Enterprise enterprise = enterpriseService.list(Condition.getQueryWrapper(new Enterprise())
					.lambda()
					.eq(Enterprise::getEnterpriseCode, poHeaderVO.getSCode()))
					.stream().findFirst().orElse(null);
				if (ObjectUtil.isNotEmpty(enterprise)) {
					poHeaderVO.setSId(enterprise.getPeId());
				}
			}
			User user = UserCache.getById(poHeaderVO.getCreateUser());
			if (Func.isNotEmpty(user)) {
				poHeaderVO.setCreateUserName(user.getName());
			}
		}
		return poHeaderVO;
	}
}
