package org.nodes.wms.core.outstock.sales.wrapper;

import io.github.classgraph.json.Id;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.entity.User;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.service.IUserService;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.basedata.cache.BillTypeCache;
import org.nodes.wms.core.basedata.cache.EnterpriseCache;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.core.basedata.entity.BillType;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.core.basedata.service.IEnterpriseService;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.nodes.wms.core.outstock.sales.entity.SalesHeader;
import org.nodes.wms.core.outstock.sales.vo.SalesHeaderVO;


import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;


/**
 * 销售单主表
 * 包装类,返回视图层所需的字段
 *
 * @author NodeX
 * @since 2021-05-31
 */
public class SalesHeaderWrapper extends BaseEntityWrapper<SalesHeader, SalesHeaderVO> {

	public static SalesHeaderWrapper build() {
		return new SalesHeaderWrapper();
	}

	@Override
	public SalesHeaderVO entityVO(SalesHeader salesHeader) {
		SalesHeaderVO salesHeaderVO = BeanUtil.copy(salesHeader, SalesHeaderVO.class);
		if (Func.isNotEmpty(salesHeaderVO)) {
			//单据状态名称
			salesHeaderVO.setSoBillStateName(
				DictCache.getValue(DictConstant.SO_BILL_STATE, salesHeader.getSoBillState()));
			//出库方式名称
			salesHeaderVO.setOutstockTypeName(
				DictCache.getValue(DictConstant.OUTSTORE_TYPE, salesHeader.getOutstockType()));
			//同步状态名称
			salesHeaderVO.setSyncStateName(
				DictCache.getValue(DictConstant.SYNC_STATE, salesHeader.getSyncState()));
			//创建类型
			salesHeaderVO.setCreateTypeName(
				DictCache.getValue(DictConstant.CREATE_TYPE, salesHeader.getCreateType()));
			//货主名称
			if (Func.isNotEmpty(salesHeader.getWoId())) {
				IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
				Owner owner = ownerService.getById(salesHeader.getWoId());
				if (Func.isNotEmpty(owner) && Func.isNotEmpty(owner.getOwnerName())) {
					salesHeaderVO.setOwnerName(owner.getOwnerName());
				}
			}
			//单据类型名称
			if (Func.isNotEmpty(salesHeader.getBillTypeCd())) {
				BillType billType = BillTypeCache.getByCode(salesHeader.getBillTypeCd());
				if (Func.isNotEmpty(billType)) {
					if (Func.isNotEmpty(billType.getBillTypeName())) {
						salesHeaderVO.setBillTypeName(billType.getBillTypeName());
					}
				}
			}
			//过账类型
			salesHeaderVO.setPostStateCd(
				DictCache.getValue(DictConstant.POST_STATE, salesHeader.getPostState()));
			//过账人
			if (Func.isNotEmpty(salesHeader.getPostUser())) {
				User user = UserCache.getById(salesHeader.getPostUser());
				if (Func.isNotEmpty(user)) {
					salesHeaderVO.setPostUserCd(user.getName());
				}
			}
			//库房名称
			Warehouse warehouse = WarehouseCache.getById(salesHeader.getWhId());
			if (Func.isNotEmpty(warehouse) && Func.isNotEmpty(warehouse.getWhName())) {
				salesHeaderVO.setWhName(warehouse.getWhName());
			}

			//客户ID
			if (Func.isNotEmpty(salesHeader.getCCode())) {
				IEnterpriseService iEnterpriseService = SpringUtil.getBean(IEnterpriseService.class);
				//Enterprise enterprise = EnterpriseCache.getByCode(salesHeader.getCCode());
				Enterprise enterprise = iEnterpriseService.list(Condition.getQueryWrapper(new Enterprise())
					.lambda()
					.eq(Enterprise::getEnterpriseCode, salesHeader.getCCode()))
					.stream().findFirst().orElse(null);
				if (Func.isNotEmpty(enterprise)) {
					salesHeaderVO.setCId(enterprise.getPeId());
				}
			}
			//承运商ID
			if (Func.isNotEmpty(salesHeader.getExpressCode())) {
				//Enterprise enterprise = EnterpriseCache.getByCode(salesHeader.getExpressCode());
				IEnterpriseService iEnterpriseService = SpringUtil.getBean(IEnterpriseService.class);
				Enterprise enterprise = iEnterpriseService.list(Condition.getQueryWrapper(new Enterprise())
					.lambda()
					.eq(Enterprise::getEnterpriseCode, salesHeader.getExpressCode()))
					.stream().findFirst().orElse(null);
				if (Func.isNotEmpty(enterprise)) {
					salesHeaderVO.setExpressId(enterprise.getPeId());
				}
			}
			//发货方式
			salesHeaderVO.setTransportDesc(
				DictCache.getValue(DictConstant.SO_TRANSPORT_CODE, salesHeader.getTransportCode()));
			// 发运状态
			salesHeaderVO.setShipStateDesc(
				DictCache.getValue(DictConstant.SHIP_STATE, salesHeader.getShipState()));
		}
		return salesHeaderVO;
	}
}
