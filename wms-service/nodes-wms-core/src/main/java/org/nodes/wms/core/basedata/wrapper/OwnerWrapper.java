package org.nodes.wms.core.basedata.wrapper;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.dto.OwnerDTO;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.core.basedata.entity.Owner;
import org.nodes.wms.core.basedata.excel.OwnerExcel;
import org.nodes.wms.core.basedata.vo.OwnerVO;
import org.nodes.wms.core.common.cache.AddressCache;
import org.nodes.wms.core.common.cache.ContactsCache;
import org.nodes.wms.core.common.dto.AddressDTO;
import org.nodes.wms.core.common.dto.ContactsDTO;
import org.nodes.wms.core.common.entity.Address;
import org.nodes.wms.core.common.entity.Contacts;
import org.nodes.wms.core.common.service.IAddressService;
import org.nodes.wms.core.common.service.IContactsService;
import org.nodes.wms.core.common.wrapper.AddressWrapper;
import org.nodes.wms.core.common.wrapper.ContactsWrapper;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 货主封装类
 * @create 20200220
 */
public class OwnerWrapper extends BaseEntityWrapper<Owner, OwnerVO> {

	public static OwnerWrapper build() {
		return new OwnerWrapper();
	}

	@Override
	public OwnerVO entityVO(Owner entity) {
		OwnerVO ownerVO = BeanUtil.copy(entity, OwnerVO.class);
		IAddressService addressService = SpringUtil.getBean(IAddressService.class);
		IContactsService contactsService = SpringUtil.getBean(IContactsService.class);
		List<Address> addressList = addressService.list(Condition.getQueryWrapper(new Address())
			.lambda()
			.eq(Address::getDataId, entity.getWoId())
			.eq(Address::getAddressDataType, Owner.DATA_TYPE)
		);
		if (Func.isNotEmpty(ownerVO)) {
			ownerVO.setAddressList(
				AddressWrapper.build().listVO(addressList));

			List<Contacts> contactsList = contactsService.list(Condition.getQueryWrapper(new Contacts())
				.lambda()
				.eq(Contacts::getDataId, entity.getWoId())
				.eq(Contacts::getContactsDataType, Owner.DATA_TYPE)
			);
			ownerVO.setContactsList(
				ContactsWrapper.build().listVO(contactsList));
		}
		return ownerVO;
	}

	public OwnerDTO entityDTO(OwnerExcel ownerExcel) {
		OwnerDTO ownerDTO = BeanUtil.copy(ownerExcel, OwnerDTO.class);
		if (Func.isNotEmpty(ownerDTO)) {
			// 地址
			AddressDTO address = BeanUtil.copy(ownerExcel, AddressDTO.class);
			if (Func.isNotEmpty(address) && !NodesUtil.isAllNull(address)) {
				address.setAddressDataType(Owner.DATA_TYPE);
				if (Func.isNotEmpty(ownerExcel) && Func.isNotEmpty(ownerExcel.getAddressTypeDesc())) {
					Dict dict = DictCache.list(DictConstant.ADDRESS_TYPE).stream().filter(u->{
						return Func.equals(u.getDictValue(), ownerExcel.getAddressTypeDesc());
					}).findFirst().orElse(null);
					address.setAddressType(Func.isEmpty(dict) ? null : dict.getDictKey());
				}
				if (Func.isNotEmpty(ownerExcel) && Func.isNotEmpty(ownerExcel.getAddressDefaultFlag())) {
					if ("是".equals(ownerExcel.getAddressDefaultFlag())) {
						address.setDefaultFlag(1);
					} else {
						address.setDefaultFlag(0);
					}
				} else {
					address.setDefaultFlag(0);
				}
				ownerDTO.getAddressList().add(address);
			}
			// 联系人
			ContactsDTO contacts = BeanUtil.copy(ownerExcel, ContactsDTO.class);
			if (Func.isNotEmpty(contacts) && !NodesUtil.isAllNull(contacts)) {
				if (Func.isNotEmpty(ownerExcel) && Func.isNotEmpty(ownerExcel.getAddressDefaultFlag())) {
					if ("是".equals(ownerExcel.getAddressDefaultFlag())) {
						contacts.setDefaultFlag(1);
					} else {
						contacts.setDefaultFlag(0);
					}
				} else {
					contacts.setDefaultFlag(0);
				}
				contacts.setContactsDataType(Owner.DATA_TYPE);
				ownerDTO.getContactsList().add(contacts);
			}
		}
		return ownerDTO;
	}
}
