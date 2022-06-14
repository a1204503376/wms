
package org.nodes.wms.core.allot.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.base.cache.SysCache;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.allot.cache.AllotCache;
import org.nodes.wms.core.allot.dto.AllotHeaderDTO;
import org.nodes.wms.core.allot.entity.AllotDetail;
import org.nodes.wms.core.allot.entity.AllotHeader;
import org.nodes.wms.core.allot.enums.AllotBillStateEnum;
import org.nodes.wms.core.allot.enums.SyncStateEnum;
import org.nodes.wms.core.allot.mapper.AllotHeaderMapper;
import org.nodes.wms.core.allot.service.IAllotDetailService;
import org.nodes.wms.core.allot.service.IAllotHeaderService;
import org.nodes.wms.core.allot.vo.AllotHeaderVO;
import org.nodes.wms.core.allot.wrapper.AllotDetailWrapper;
import org.nodes.wms.core.allot.wrapper.AllotHeaderWrapper;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.common.entity.Address;
import org.nodes.wms.core.common.entity.Contacts;
import org.nodes.wms.core.common.service.IAddressService;
import org.nodes.wms.core.common.service.IContactsService;
import org.nodes.wms.core.instock.asn.dto.AsnDetailDTO;
import org.nodes.wms.core.instock.asn.dto.AsnHeaderDTO;
import org.nodes.wms.core.instock.asn.service.IAsnHeaderService;
import org.nodes.wms.core.outstock.so.dto.SoDetailDTO;
import org.nodes.wms.core.outstock.so.dto.SoHeaderDTO;
import org.nodes.wms.core.outstock.so.entity.SoDetail;
import org.nodes.wms.core.outstock.so.entity.SoPick;
import org.nodes.wms.core.outstock.so.service.ISoDetailService;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.outstock.so.service.ISoPickService;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 调拨单头表 服务实现类
 *
 * @author Wangjw
 * @since 2020-01-23
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class AllotHeaderServiceImpl<M extends AllotHeaderMapper, T extends AllotHeader>
	extends BaseServiceImpl<AllotHeaderMapper, AllotHeader> implements IAllotHeaderService {

	@Autowired
	IAllotDetailService allotDetailService;
	@Autowired
	ISoHeaderService soHeaderService;
	@Autowired
	ISoDetailService soDetailService;
	@Autowired
	IAsnHeaderService asnHeaderService;
	@Autowired
	ISoPickService soPickService;

	@Override
	public AllotHeaderVO getDetail(Long allotBillId) {
		AllotHeaderVO allotHeader = AllotHeaderWrapper.build().entityVO(super.getById(allotBillId));
		if (Func.isNotEmpty(allotHeader)) {
			List<AllotDetail> detailList = allotDetailService.list(Condition.getQueryWrapper(new AllotDetail())
				.lambda()
				.eq(AllotDetail::getAllotBillId, allotBillId));
			allotHeader.setDetailList(AllotDetailWrapper.build().listVO(detailList));
		}
		return allotHeader;
	}

	@Override
	public boolean save(AllotHeaderDTO allotHeaderDTO) {
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(allotHeaderDTO.getWoId());
		if (ObjectUtil.isEmpty(owner)) {
			throw new ServiceException("货主不存在（ID：" + allotHeaderDTO.getWoId() + "）！");
		}
		if (allotHeaderDTO.getSourceWhId().equals(allotHeaderDTO.getTargetWhId())) {
			throw new ServiceException("调出与调入库房不应该相同，请修改调出或调入库房后重试！");
		}
		Warehouse sourceWarehouse = WarehouseCache.getById(allotHeaderDTO.getSourceWhId());
		if (Func.isNotEmpty(sourceWarehouse)) {
			allotHeaderDTO.setSourceWhCode(sourceWarehouse.getWhCode());
		}
		Warehouse targetWarehouse = WarehouseCache.getById(allotHeaderDTO.getTargetWhId());
		if (Func.isNotEmpty(targetWarehouse)) {
			allotHeaderDTO.setTargetWhCode(targetWarehouse.getWhCode());
		}
		String allotBillNo = AllotCache.getAllotBillNo();
		allotHeaderDTO.setAllotBillNo(allotBillNo);
		allotHeaderDTO.setBillKey(allotBillNo);

		allotHeaderDTO.setAllotBillState(AllotBillStateEnum.UN_OUTSTOCK.getIndex());
		allotHeaderDTO.setSyncState(SyncStateEnum.DEFAULT.getIndex());
		allotHeaderDTO.setLastUpdateDate(LocalDateTime.now());
		allotHeaderDTO.setPreCreateDate(LocalDateTime.now());

		boolean result = super.save(allotHeaderDTO) &&
			allotDetailService.save(allotHeaderDTO.getAllotBillId(), allotHeaderDTO.getDetailList());
		return result && this.updateOutstock(allotHeaderDTO);
	}

	@Override
	public boolean updateById(@Validated AllotHeaderDTO allotHeaderDTO) {
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(allotHeaderDTO.getWoId());
		if (ObjectUtil.isEmpty(owner)) {
			throw new ServiceException("货主不存在（ID：" + allotHeaderDTO.getWoId() + "）！");
		}
		if (allotHeaderDTO.getSourceWhId().equals(allotHeaderDTO.getTargetWhId())) {
			throw new ServiceException("调出与调入库房不应该相同，请修改调出或调入库房后重试！");
		}
		Warehouse sourceWarehouse = WarehouseCache.getById(allotHeaderDTO.getSourceWhId());
		if (Func.isNotEmpty(sourceWarehouse)) {
			allotHeaderDTO.setSourceWhCode(sourceWarehouse.getWhCode());
		}
		Warehouse targetWarehouse = WarehouseCache.getById(allotHeaderDTO.getTargetWhId());
		if (Func.isNotEmpty(targetWarehouse)) {
			allotHeaderDTO.setTargetWhCode(targetWarehouse.getWhCode());
		}
		boolean result = super.updateById(allotHeaderDTO);
		if (result) {
			allotDetailService.update(allotHeaderDTO.getAllotBillId(), allotHeaderDTO.getDetailList());
			this.updateOutstock(allotHeaderDTO);
		}
		return result;
	}

	@Override
	public boolean saveOrUpdate(@Validated AllotHeaderDTO allotHeaderDTO) {
		if (Func.isEmpty(allotHeaderDTO.getAllotBillId())) {
			return this.save(allotHeaderDTO);
		} else {
			return this.updateById(allotHeaderDTO);
		}
	}

	public boolean updateOutstock(AllotHeaderDTO allotHeader) {
		SoHeaderDTO soHeader = new SoHeaderDTO();
		soHeader.setWhId(allotHeader.getSourceWhId());
		soHeader.setWoId(allotHeader.getWoId());
		soHeader.setBillTypeCd("209");
		soHeader.setBillKey(allotHeader.getAllotBillNo());
		soHeader.setOrderNo(allotHeader.getAllotBillNo());
		soHeader.setSoRemark(allotHeader.getAllotRemark());
		soHeader.setLastUpdateDate(LocalDateTime.now());
		soHeader.setPreCreateDate(LocalDateTime.now());
		soHeader.setTransportCode(701);
		soHeader.setOutstockType(40);
		// 设置地址
		//List<Address> addressList = AddressCache.list(allotHeader.getSourceWhId(), Warehouse.DATA_TYPE);
		IAddressService addressService = SpringUtil.getBean(IAddressService.class);
		List<Address> addressList = addressService.list(Condition.getQueryWrapper(new Address())
		.lambda()
		.eq(Address::getDataId,allotHeader.getSourceWhId())
		.eq(Address::getAddressDataType,Warehouse.DATA_TYPE)
		);
		if (Func.isNotEmpty(addressList)) {
			for (Address address : addressList) {
				if (new Integer(1).equals(address.getDefaultFlag())) {
					soHeader.setAddress(address.getAddress());
					break;
				}
			}
			if (Func.isEmpty(soHeader.getAddress())) {
				soHeader.setAddress(addressList.get(0).getAddress());
			}
		}
		// 设置联系人
		IContactsService contactsService = SpringUtil.getBean(IContactsService.class);
		//List<Contacts> contactsList = ContactsCache.list(allotHeader.getSourceWhId(), Warehouse.DATA_TYPE);
		List<Contacts> contactsList = contactsService.list(Condition.getQueryWrapper(new Contacts())
		.lambda()
		.eq(Contacts::getDataId,allotHeader.getSourceWhId())
		.eq(Contacts::getContactsDataType,Warehouse.DATA_TYPE)
		);
		if (Func.isNotEmpty(contactsList)) {
			for (Contacts contact : contactsList) {
				if (new Integer(1).equals(contact.getDefaultFlag())) {
					soHeader.setContact(contact.getContactsName());
					soHeader.setPhone(contact.getContactsNumber());
					break;
				}
			}
			if (Func.isEmpty(soHeader.getContact()) && Func.isEmpty(soHeader.getPhone())) {
				Contacts contacts = contactsList.get(0);
				soHeader.setContact(contacts.getContactsName());
				soHeader.setPhone(contacts.getContactsNumber());
			}
		}
		Dept dept = SysCache.getDept(Func.toLong(AuthUtil.getDeptId()));
		if (Func.isNotEmpty(dept)) {
			soHeader.setDeptId(dept.getId());
			soHeader.setDeptCode(dept.getDeptCode());
			soHeader.setDeptName(dept.getDeptName());
		}
		List<AllotDetail> allotDetailList = allotDetailService.list(Condition.getQueryWrapper(new AllotDetail())
			.lambda()
			.eq(AllotDetail::getAllotBillId, allotHeader.getAllotBillId()));
		if (Func.isNotEmpty(allotDetailList)) {
			for (AllotDetail allotDetail : allotDetailList) {
				SoDetailDTO soDetail = BeanUtil.copy(allotDetail, SoDetailDTO.class);
				soDetail.setSoLineNo(allotDetail.getAllotLineNo());
				soDetail.setPlanQty(allotDetail.getAllotPlanQty());

				soHeader.getDetailList().add(soDetail);
			}
		}
		boolean result = soHeaderService.saveOrUpdateByAllot(soHeader);
		if (result) {
			UpdateWrapper<AllotHeader> updateWrapper = new UpdateWrapper<>();
			updateWrapper.lambda()
				.set(AllotHeader::getSoBillId, soHeader.getSoBillId())
				.eq(AllotHeader::getAllotBillId, allotHeader.getAllotBillId());
			super.update(updateWrapper);
		}
		return result;
	}

	public boolean createInstock(AllotHeaderDTO allotHeader) {
		AsnHeaderDTO asnHeader = new AsnHeaderDTO();
		asnHeader.setWhId(allotHeader.getTargetWhId());
		asnHeader.setWoId(allotHeader.getWoId());
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(allotHeader.getWoId());
		if (Func.isNotEmpty(owner)) {
//			asnHeader.setSCode(owner.getOwnerCode());
			asnHeader.setSName(owner.getOwnerName());
		}
		asnHeader.setBillTypeCd("109");
//		asnHeader.setBillKey(allotHeader.getAllotBillNo());
//		asnHeader.setOrderNo(allotHeader.getAllotBillNo());
//		asnHeader.setLastUpdateDate(LocalDateTime.now());
//		asnHeader.setPreCreateDate(LocalDateTime.now());
		asnHeader.setScheduledArrivalDate(LocalDateTime.now());
		asnHeader.setInstoreType(10);
		asnHeader.setAsnBillRemark(allotHeader.getAllotRemark());
		Dept dept = SysCache.getDept(Func.toLong(AuthUtil.getDeptId()));
		if (Func.isNotEmpty(dept)) {
//			asnHeader.setDeptId(dept.getId());
//			asnHeader.setDeptCode(dept.getDeptCode());
//			asnHeader.setDeptName(dept.getDeptName());
		}
		// 设置地址
		//List<Address> addressList = AddressCache.list(allotHeader.getTargetWhId(), Warehouse.DATA_TYPE);
		IAddressService addressService = SpringUtil.getBean(IAddressService.class);
		List<Address> addressList = addressService.list(Condition.getQueryWrapper(new Address())
			.lambda()
			.eq(Address::getDataId,allotHeader.getTargetWhId())
			.eq(Address::getAddressDataType,Warehouse.DATA_TYPE)
		);
		if (Func.isNotEmpty(addressList)) {
//			for (Address address : addressList) {
//				if (new Integer(1).equals(address.getDefaultFlag())) {
//					asnHeader.setSAddress(address.getAddress());
//					break;
//				}
//			}
//			if (Func.isEmpty(asnHeader.getSAddress())) {
//				asnHeader.setSAddress(addressList.get(0).getAddress());
//			}
		}
		// 设置联系人
		IContactsService contactsService = SpringUtil.getBean(IContactsService.class);
		//List<Contacts> contactsList = ContactsCache.list(allotHeader.getTargetWhId(), Warehouse.DATA_TYPE);
		List<Contacts> contactsList = contactsService.list(Condition.getQueryWrapper(new Contacts())
		.lambda()
		.eq(Contacts::getDataId,allotHeader.getTargetWhId())
		.eq(Contacts::getContactsDataType,Warehouse.DATA_TYPE)
		);
		if (Func.isNotEmpty(contactsList)) {
//			for (Contacts contact : contactsList) {
//				if (new Integer(1).equals(contact.getDefaultFlag())) {
//					asnHeader.setContact(contact.getContactsName());
//					asnHeader.setPhone(contact.getContactsNumber());
//					break;
//				}
//			}
//			if (Func.isEmpty(asnHeader.getContact()) && Func.isEmpty(asnHeader.getPhone())) {
//				Contacts contacts = contactsList.get(0);
//				asnHeader.setContact(contacts.getContactsName());
//				asnHeader.setPhone(contacts.getContactsNumber());
//			}
		}
		List<SoDetail> soDetails = soDetailService.list(Wrappers.lambdaQuery(SoDetail.class).eq(SoDetail::getSoBillId, allotHeader.getSoBillId()));
		List<SoPick> detailList = soPickService.list(Wrappers.lambdaQuery(SoPick.class).eq(SoPick::getSoBillId, allotHeader.getSoBillId()));
		if (Func.isNotEmpty(soDetails)) {
			List<SoDetail> soDetailList = soDetails.stream().map(soDetail -> {
				SoPick soPick1 = detailList.stream().filter(soPick -> soPick.getSoDetailId().equals(soDetail.getSoDetailId())).findFirst().orElse(null);
				for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
					if (soPick1.skuLotChk(i)) {
						soDetail.skuLotSet(i, soPick1.skuLotGet(i));
					}
				}
				return soDetail;
			}).collect(Collectors.toList());

			for (SoDetail detail : soDetailList) {
				AsnDetailDTO asnDetailDTO = BeanUtil.copy(detail, AsnDetailDTO.class);
				asnDetailDTO.setAsnLineNo(detail.getSoLineNo());
				asnDetailDTO.setPlanQty(detail.getPlanQty());
				asnHeader.getAsnDetailList().add(asnDetailDTO);
			}
		}

		boolean result = asnHeaderService.saveOrUpdateByAllot(asnHeader);
		if (result) {
			UpdateWrapper<AllotHeader> updateWrapper = new UpdateWrapper<>();
			updateWrapper.lambda()
				.set(AllotHeader::getAsnBillId, asnHeader.getAsnBillId())
				.eq(AllotHeader::getAllotBillId, allotHeader.getAllotBillId());
			super.update(updateWrapper);
		}
		return result;
	}

	/**
	 * 创建调拨回库单
	 *
	 * @param allotHeader
	 * @return
	 */
	public boolean createInstockBack(AllotHeader allotHeader) {
		AsnHeaderDTO asnHeader = new AsnHeaderDTO();
		asnHeader.setWhId(allotHeader.getSourceWhId());
		asnHeader.setWoId(allotHeader.getWoId());
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
		Owner owner = ownerService.getById(allotHeader.getWoId());
		if (Func.isNotEmpty(owner)) {
//			asnHeader.setSCode(owner.getOwnerCode());
			asnHeader.setSName(owner.getOwnerName());
		}
		asnHeader.setBillTypeCd("109");
//		asnHeader.setBillKey(allotHeader.getAllotBillNo());
//		asnHeader.setOrderNo(allotHeader.getAllotBillNo());
//		asnHeader.setLastUpdateDate(LocalDateTime.now());
//		asnHeader.setPreCreateDate(LocalDateTime.now());
		asnHeader.setScheduledArrivalDate(LocalDateTime.now());
		asnHeader.setInstoreType(10);
		asnHeader.setAsnBillRemark(allotHeader.getAllotRemark());
		Dept dept = SysCache.getDept(Func.toLong(AuthUtil.getDeptId()));
		if (Func.isNotEmpty(dept)) {
//			asnHeader.setDeptId(dept.getId());
//			asnHeader.setDeptCode(dept.getDeptCode());
//			asnHeader.setDeptName(dept.getDeptName());
		}
		// 设置地址
		//List<Address> addressList = AddressCache.list(allotHeader.getSourceWhId(), Warehouse.DATA_TYPE);
		IAddressService addressService = SpringUtil.getBean(IAddressService.class);
		List<Address> addressList = addressService.list(Condition.getQueryWrapper(new Address())
			.lambda()
			.eq(Address::getDataId,allotHeader.getSourceWhId())
			.eq(Address::getAddressDataType,Warehouse.DATA_TYPE)
		);
		if (Func.isNotEmpty(addressList)) {
//			for (Address address : addressList) {
//				if (new Integer(1).equals(address.getDefaultFlag())) {
//					asnHeader.setSAddress(address.getAddress());
//					break;
//				}
//			}
//			if (Func.isEmpty(asnHeader.getSAddress())) {
//				asnHeader.setSAddress(addressList.get(0).getAddress());
//			}
		}
		// 设置联系人
		IContactsService contactsService = SpringUtil.getBean(IContactsService.class);
		//List<Contacts> contactsList = ContactsCache.list(allotHeader.getSourceWhId(), Warehouse.DATA_TYPE);
		List<Contacts> contactsList = contactsService.list(Condition.getQueryWrapper(new Contacts())
		.lambda()
		.eq(Contacts::getDataId,allotHeader.getSourceWhId())
		.eq(Contacts::getContactsDataType,Warehouse.DATA_TYPE));
		if (Func.isNotEmpty(contactsList)) {
			for (Contacts contact : contactsList) {
				if (new Integer(1).equals(contact.getDefaultFlag())) {
//					asnHeader.setContact(contact.getContactsName());
//					asnHeader.setPhone(contact.getContactsNumber());
					break;
				}
			}
//			if (Func.isEmpty(asnHeader.getContact()) && Func.isEmpty(asnHeader.getPhone())) {
//				Contacts contacts = contactsList.get(0);
//				asnHeader.setContact(contacts.getContactsName());
//				asnHeader.setPhone(contacts.getContactsNumber());
//			}
		}
		// 查询出调拨出库单明细
		List<SoDetail> soDetailList = soDetailService.list(Condition.getQueryWrapper(new SoDetail())
			.lambda()
			.eq(SoDetail::getSoBillId, allotHeader.getSoBillId()));
		if (Func.isEmpty(soDetailList)) {
			throw new ServiceException("出库单明细为空！");
		}
		for (SoDetail soDetail : soDetailList) {
			AsnDetailDTO asnDetail = BeanUtil.copy(soDetail, AsnDetailDTO.class);
			asnDetail.setAsnLineNo(soDetail.getSoLineNo());
			asnDetail.setPlanQty(soDetail.getScanQty());
			asnHeader.getAsnDetailList().add(asnDetail);
		}
		return asnHeaderService.saveOrUpdate(asnHeader);
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {

		List<AllotHeader> allotHeaderList = super.listByIds(idList);
		for (AllotHeader allotHeader : allotHeaderList) {
			if (!AllotBillStateEnum.UN_OUTSTOCK.getIndex().equals(allotHeader.getAllotBillState())) {
				throw new ServiceException(String.format("单据[%s]已做业务处理，不可删除！", allotHeader.getAllotBillNo()));
			}
		}
		// 批量删除明细
		allotDetailService.remove(Condition.getQueryWrapper(new AllotDetail()).lambda()
			.in(AllotDetail::getAllotBillId, idList));
		// 批量删除出库单
		soHeaderService.removeByIds(NodesUtil.toList(allotHeaderList, AllotHeader::getSoBillId));
		return super.removeByIds(idList);
	}

	@Override
	public boolean updateBillState(Long allotBillId, AllotBillStateEnum allotBillStateEnum) {
		AllotHeader allotHeader = super.getById(allotBillId);
		if (Func.isEmpty(allotHeader)) {
			throw new ServiceException("指定调拨单[ID: " + allotBillId + "]不存在！");
		}
		return this.updateBillState(allotHeader, allotBillStateEnum);
	}

	@Override
	public boolean updateBillState(String allotBillNo, AllotBillStateEnum allotBillStateEnum) {
		AllotHeader allotHeader = super.getOne(Condition.getQueryWrapper(new AllotHeader()).lambda()
			.eq(AllotHeader::getAllotBillNo, allotBillNo)
			.last("limit 1"));
		if (Func.isEmpty(allotHeader)) {
			return false;
		}
		return this.updateBillState(allotHeader, allotBillStateEnum);
	}

	@Override
	public synchronized boolean updateBillState(AllotHeader allotHeader, AllotBillStateEnum allotBillStateEnum) {
		if (AllotBillStateEnum.CANCEL.getIndex().equals(allotHeader.getAllotBillState())) {
			throw new ServiceException("调拨单[" + allotHeader.getAllotBillNo() + "] 已取消，不允许执行此操作！");
		}
		if (AllotBillStateEnum.UN_INSTOCK.equals(allotBillStateEnum)) {
			// 创建入库单
			AllotHeaderDTO allotHeaderDTO = BeanUtil.copy(allotHeader, AllotHeaderDTO.class);
			List<AllotDetail> allotDetailList = allotDetailService.list(Condition.getQueryWrapper(new AllotDetail())
				.lambda()
				.eq(AllotDetail::getAllotBillId, allotHeader.getAllotBillId()));
			allotHeaderDTO.setDetailList(allotDetailList);
			this.createInstock(allotHeaderDTO);
		}
		UpdateWrapper<AllotHeader> updateWrapper = new UpdateWrapper<>();
		updateWrapper.lambda()
			.set(AllotHeader::getAllotBillState, allotBillStateEnum.getIndex())
			.eq(AllotHeader::getAllotBillId, allotHeader.getAllotBillId());
		return super.update(updateWrapper);
	}

	public boolean cancel(List<Long> allotBillIdList) {
		List<AllotHeader> allotHeaderList = super.listByIds(allotBillIdList);
		List<AllotHeader> complatedList = allotHeaderList.stream().filter(u -> {
			return AllotBillStateEnum.COMPLETED.getIndex().equals(u.getAllotBillState());
		}).collect(Collectors.toList());
		if (Func.isNotEmpty(complatedList)) {
			throw new ServiceException(
				"调拨单[" + NodesUtil.join(complatedList, "allotBillNo") + "] 已完成，不允许执行取消操作！");
		}
		for (AllotHeader allotHeader : allotHeaderList) {

			if (AllotBillStateEnum.UN_OUTSTOCK.getIndex().equals(allotHeader.getAllotBillState())
				|| AllotBillStateEnum.OUTSTOCKING.getIndex().equals(allotHeader.getAllotBillState())) {
				// 待出库 或者出库处理中
				soHeaderService.cancel(new ArrayList() {{
					add(allotHeader.getSoBillId());
				}});
			} else if (AllotBillStateEnum.UN_INSTOCK.getIndex().equals(allotHeader.getAllotBillState())) {
				// 待入库
				asnHeaderService.removeById(allotHeader.getAsnBillId());
				// 生成调拨回库单
				this.createInstockBack(allotHeader);
			} else if (AllotBillStateEnum.INSTOCKING.getIndex().equals(allotHeader.getAllotBillState())) {
				// 入库处理中
				asnHeaderService.cancel(new ArrayList() {{
					add(allotHeader.getAsnBillId());
				}});
			} else {
				throw new ServiceException(String.format(
					"调拨单当前状态[%s] 不允许执行取消操作！", AllotBillStateEnum.valueOf(allotHeader.getAllotBillState())));
			}
		}
		UpdateWrapper<AllotHeader> updateWrapper = new UpdateWrapper<>();
		updateWrapper.lambda()
			.set(AllotHeader::getAllotBillState, AllotBillStateEnum.CANCEL.getIndex())
			.in(AllotHeader::getAllotBillId, allotBillIdList);
		return super.update(updateWrapper);
	}

	@Override
	public boolean canEdit(Long allotBillId) {
		AllotHeader allotHeader = super.getById(allotBillId);
		if (Func.isEmpty(allotHeader)) {
			throw new ServiceException("调拨单不存在或已删除！");
		}
		if (!AllotBillStateEnum.UN_OUTSTOCK.getIndex().equals(allotHeader.getAllotBillState())) {
			throw new ServiceException("已执行的调拨单不允许编辑！");
		}
		return true;
	}
}
