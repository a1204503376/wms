package org.nodes.wms.core.outstock.so.wrapper;

import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.entity.User;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
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
import org.nodes.wms.core.outstock.so.dto.SoDetailDTO;
import org.nodes.wms.core.outstock.so.dto.SoHeaderDTO;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.entity.WellenDetail;
import org.nodes.wms.core.outstock.so.service.IWellenDetailService;
import org.nodes.wms.core.outstock.so.vo.SoHeaderVO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.nodes.wms.core.outstock.sales.dto.SalesDetailDTO;
import org.nodes.wms.core.outstock.sales.dto.SalesHeaderDTO;
import org.nodes.wms.core.warehouse.service.IWarehouseService;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 出库单头表 封装类
 *
 * @Author zhonglianshuai
 * @Date 2020/02/11
 **/
public class SoHeaderWrapper extends BaseEntityWrapper<SoHeader, SoHeaderVO> {

	public static SoHeaderWrapper build() {
		return new SoHeaderWrapper();
	}

	/**
	 * 出库单头表实体类转VO
	 */
	@Override
	public SoHeaderVO entityVO(SoHeader entity) {
		SoHeaderVO soHeaderVO = Objects.requireNonNull(BeanUtil.copy(entity, SoHeaderVO.class));
		IWellenDetailService wellenDetailService = SpringUtil.getBean(IWellenDetailService.class);
		//单据状态名称
		soHeaderVO.setSoBillStateName(DictCache.getValue(DictConstant.SO_BILL_STATE, soHeaderVO.getSoBillState()));
		//出库方式名称
		soHeaderVO.setOutstockTypeName(DictCache.getValue(DictConstant.OUTSTORE_TYPE, soHeaderVO.getOutstockType()));
		//同步状态名称
		soHeaderVO.setSyncStateName(DictCache.getValue(DictConstant.SYNC_STATE, soHeaderVO.getSyncState()));
		//创建类型
		soHeaderVO.setCreateTypeName(DictCache.getValue(DictConstant.CREATE_TYPE, soHeaderVO.getCreateType()));
		//货主名称
		if (Func.isNotEmpty(soHeaderVO.getWoId())) {
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(soHeaderVO.getWoId());
			if (Func.isNotEmpty(owner) && Func.isNotEmpty(owner.getOwnerName())) {
				soHeaderVO.setOwnerName(owner.getOwnerName());
			}
		}
		//单据类型名称
		if (Func.isNotEmpty(soHeaderVO.getBillTypeCd())) {
			BillType billType = BillTypeCache.getByCode(soHeaderVO.getBillTypeCd());
			if (Func.isNotEmpty(billType)) {
				if (Func.isNotEmpty(billType.getBillTypeName()))
					soHeaderVO.setBillTypeName(billType.getBillTypeName());
			}
		}

		//过账类型
		soHeaderVO.setPostStateCd(DictCache.getValue(DictConstant.POST_STATE, soHeaderVO.getPostState()));
		//过账人
		if (Func.isNotEmpty(soHeaderVO.getPostUser())) {
			User user = UserCache.getById(soHeaderVO.getPostUser());
			if (Func.isNotEmpty(user)) {
				soHeaderVO.setPostUserCd(user.getName());
			}
		}
		//库房名称
		Warehouse warehouse = WarehouseCache.getById(soHeaderVO.getWhId());
		if (Func.isNotEmpty(warehouse) && Func.isNotEmpty(warehouse.getWhName())) {
			soHeaderVO.setWhName(warehouse.getWhName());
		}

		//客户ID
		if (Func.isNotEmpty(soHeaderVO.getCCode())) {
			IEnterpriseService iEnterpriseService = SpringUtil.getBean(IEnterpriseService.class);
			//Enterprise enterprise = EnterpriseCache.getByCode(soHeaderVO.getCCode());
			Enterprise enterprise = iEnterpriseService.list(Condition.getQueryWrapper(new Enterprise())
				.lambda()
				.eq(Enterprise::getEnterpriseCode, soHeaderVO.getCCode()))
				.stream().findFirst().orElse(null);
			if (Func.isNotEmpty(enterprise)) {
				soHeaderVO.setCId(enterprise.getPeId());
			}
		}
		//承运商ID
		if (Func.isNotEmpty(soHeaderVO.getExpressCode())) {
			//Enterprise enterprise = EnterpriseCache.getByCode(soHeaderVO.getExpressCode());
			IEnterpriseService iEnterpriseService = SpringUtil.getBean(IEnterpriseService.class);
			Enterprise enterprise = iEnterpriseService.list(Condition.getQueryWrapper(new Enterprise())
				.lambda()
				.eq(Enterprise::getEnterpriseCode, soHeaderVO.getExpressCode()))
				.stream().findFirst().orElse(null);
			if (Func.isNotEmpty(enterprise)) {
				soHeaderVO.setExpressId(enterprise.getPeId());
			}
		}
		//发货方式
		soHeaderVO.setTransportDesc(
			DictCache.getValue(DictConstant.SO_TRANSPORT_CODE, soHeaderVO.getTransportCode()));
		// 发运状态
		soHeaderVO.setShipStateDesc(DictCache.getValue(DictConstant.SHIP_STATE, soHeaderVO.getShipState()));

		return soHeaderVO;
	}

	public SoHeaderDTO entityDTO(SalesHeaderDTO salesHeader) {
		SoHeaderDTO soHeader = BeanUtil.copy(salesHeader, SoHeaderDTO.class);
		if (Func.isNotEmpty(soHeader)) {
			soHeader.setSoBillId(null);
			soHeader.setSoBillNo(null);
			soHeader.setOrderNo(salesHeader.getSoBillNo());
			soHeader.setDetailList(new ArrayList<>());
			if (Func.isNotEmpty(salesHeader.getDetailList())) {
				for (SalesDetailDTO salesDetail : salesHeader.getDetailList()) {
					SoDetailDTO soDetail = SoDetailWrapper.build().entityDTO(salesDetail);
					if (Func.isNotEmpty(soDetail)) {
						soHeader.getDetailList().add(soDetail);
					}
				}
			}
		}
		return soHeader;
	}
}
