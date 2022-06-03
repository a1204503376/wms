package org.nodes.wms.core.instock.asn.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.UserCache;
import org.nodes.core.base.entity.User;
import org.nodes.core.constant.DictConstant;
import org.nodes.wms.core.basedata.cache.BillTypeCache;
import org.nodes.wms.dao.basics.billType.entities.BillType;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.instock.asn.dto.AsnDetailDTO;
import org.nodes.wms.core.instock.asn.dto.AsnHeaderDTO;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.nodes.wms.core.instock.asn.vo.AsnHeaderMinVO;
import org.nodes.wms.core.instock.asn.vo.AsnHeaderVO;
import org.nodes.wms.core.instock.purchase.dto.PoDetailDTO;
import org.nodes.wms.core.instock.purchase.dto.PoHeaderDTO;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.entity.Warehouse;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 收货单头表 封装类
 *
 * @Author zx
 * @Date 2019/12/16
 **/
public class AsnHeaderWrapper extends BaseEntityWrapper<AsnHeader, AsnHeaderVO> {

	public static AsnHeaderWrapper build() {
		return new AsnHeaderWrapper();
	}

	/**
	 * 收货单头表实体类转VO
	 *
	 * @param entity
	 * @return
	 */
	@Override
	public AsnHeaderVO entityVO(AsnHeader entity) {
		AsnHeaderVO asnHeaderVO = Objects.requireNonNull(BeanUtil.copy(entity, AsnHeaderVO.class));
		//单据状态名称
		asnHeaderVO.setAsnBillStateName(DictCache.getValue(DictConstant.ASN_BILL_STATE, asnHeaderVO.getAsnBillState()));
		//入库方式名称
		asnHeaderVO.setInstoreTypeName(DictCache.getValue(DictConstant.INSTORE_TYPE, asnHeaderVO.getInstoreType()));
		asnHeaderVO.setCreateTypeName(DictCache.getValue(DictConstant.CREATE_TYPE, asnHeaderVO.getCreateType()));
		//同步状态名称
//		asnHeaderVO.setSyncStateName(DictCache.getValue(DictConstant.SYNC_STATE, asnHeaderVO.getSyncState()));
		//货主名称
		if (Func.isNotEmpty(asnHeaderVO.getWoId())) {
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(asnHeaderVO.getWoId());
			if (Func.isNotEmpty(owner) && Func.isNotEmpty(owner.getOwnerName())) {
				asnHeaderVO.setOwnerName(owner.getOwnerName());
			}
		}
		if (Func.isNotEmpty(entity.getCreateUser())) {
			User user = UserCache.getById(entity.getCreateUser());
			if (Func.isNotEmpty(user)) {
				asnHeaderVO.setCreateUserName(user.getName());
			}
		}
		//单据类型名称
		if (Func.isNotEmpty(asnHeaderVO.getBillTypeCd())) {
			BillType billType = BillTypeCache.getByCode(asnHeaderVO.getBillTypeCd());
			if (Func.isNotEmpty(billType)) {
				if (Func.isNotEmpty(billType.getBillTypeName())) {
					asnHeaderVO.setBillTypeName(billType.getBillTypeName());
				}
			}
		}
		//过账类型
//		asnHeaderVO.setPostStateCd(DictCache.getValue("post_state", asnHeaderVO.getPostState()));
//		//过账人
//		if (Func.isNotEmpty(asnHeaderVO.getPostUser())) {
//			User user = UserCache.getById(asnHeaderVO.getPostUser());
//			if (Func.isNotEmpty(user)) {
//				asnHeaderVO.setPostUserCd(user.getName());
//			}
//		}
		//库房名称
		Warehouse warehouse = WarehouseCache.getById(asnHeaderVO.getWhId());
		if (Func.isNotEmpty(warehouse) && Func.isNotEmpty(warehouse.getWhName())) {
			asnHeaderVO.setWhName(warehouse.getWhName());
		}
		//供应商ID
//		if (Func.isNotEmpty(asnHeaderVO.getSCode())) {
//			IEnterpriseService enterpriseService = SpringUtil.getBean(IEnterpriseService.class);
//			//Enterprise enterprise = enterpriseService.getByCode(asnHeaderDTO.getSCode());
//			Enterprise enterprise = enterpriseService.list(Condition.getQueryWrapper(new Enterprise())
//				.lambda()
//				.eq(Enterprise::getEnterpriseCode, asnHeaderVO.getSCode()))
//				.stream().findFirst().orElse(null);
//			if (ObjectUtil.isNotEmpty(enterprise)) {
//				asnHeaderVO.setSId(enterprise.getPeId());
//			}
//		}
		User user = UserCache.getById(asnHeaderVO.getCreateUser());
		if (Func.isNotEmpty(user)) {
			asnHeaderVO.setCreateUserName(user.getName());
		}

		return asnHeaderVO;
	}

	public AsnHeaderDTO entityDTO(PoHeaderDTO poHeader) {
		AsnHeaderDTO asnHeader = BeanUtil.copy(poHeader, AsnHeaderDTO.class);

		if (Func.isNotEmpty(asnHeader)) {
//			asnHeader.setOrderNo(poHeader.getPoBillNo());
			if (Func.isNotEmpty(poHeader.getDetailList())) {
				asnHeader.setAsnDetailList(new ArrayList<>());
				for (PoDetailDTO poDetail : poHeader.getDetailList()) {
					AsnDetailDTO asnDetail = AsnDetailWrapper.build().entityDTO(poDetail);
					if (Func.isNotEmpty(asnDetail)) {
						asnDetail.setAsnLineNo(poDetail.getPoLineNo());
						asnHeader.getAsnDetailList().add(asnDetail);
					}
				}
			}
		}
		return asnHeader;
	}

	/**
	 * 封装
	 *
	 * @param asnHeader
	 * @return
	 */
	public static AsnHeaderMinVO toMinVO(AsnHeader asnHeader) {
		AsnHeaderMinVO vo = new AsnHeaderMinVO();
		vo.setAsnBillId(asnHeader.getAsnBillId());
		vo.setAsnBillNo(asnHeader.getAsnBillNo());
		vo.setWhId(asnHeader.getWhId());
		//货主名称
		if (Func.isNotEmpty(asnHeader.getWoId())) {
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(asnHeader.getWoId());
			if (Func.isNotEmpty(owner) && Func.isNotEmpty(owner.getOwnerName())) {
				vo.setOwnerName(owner.getOwnerName());
			}
		}
		//供应商名称
//		vo.setSName(asnHeader.getSName());
		//库房名称
		Warehouse warehouse = WarehouseCache.getById(asnHeader.getWhId());
		if (Func.isNotEmpty(warehouse) && Func.isNotEmpty(warehouse.getWhName())) {
			vo.setWhName(warehouse.getWhName());
		}
		return vo;
	}

}

