package org.nodes.wms.core.basedata.wrapper;

import com.sun.xml.bind.v2.model.core.ID;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.core.basedata.dto.EnterpriseDTO;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.core.basedata.excel.EnterpriseExcel;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.vo.EnterpriseVO;
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
import org.springblade.core.tool.utils.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 来往企业封装类
 * @create 20191206
 */
public class EnterpriseWrapper extends BaseEntityWrapper<Enterprise, EnterpriseVO> {

	public static EnterpriseWrapper build() {
		return new EnterpriseWrapper();
	}

	@Override
	public EnterpriseVO entityVO(Enterprise entity) {
		IContactsService contactsService = SpringUtil.getBean(IContactsService.class);
		IAddressService addressService = SpringUtil.getBean(IAddressService.class);
		EnterpriseVO enterpriseVO = BeanUtil.copy(entity, EnterpriseVO.class);
		if (Func.isNotEmpty(enterpriseVO)) {
			// 企业类型
			if (ObjectUtil.isNotEmpty(enterpriseVO.getEnterpriseType())) {
				Integer[] enterpriseTypeKeyList = Func.toIntArray(enterpriseVO.getEnterpriseType());
				if (Func.isNotEmpty(enterpriseTypeKeyList)) {
					List<String> enterpriseTypeList = new ArrayList<>();
					for (int i = 0; i < enterpriseTypeKeyList.length; i++) {
						enterpriseTypeList.add(
							DictCache.getValue(DictConstant.ENTERPRISE_TYPE, enterpriseTypeKeyList[i]));
					}
					enterpriseVO.setEnterpriseTypeDesc(Func.join(enterpriseTypeList));
				}
			}
			// 货主
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(entity.getWoId());
			if (!ObjectUtil.isEmpty(owner)){
				enterpriseVO.setOwnerName(owner.getOwnerName());
			}
			// 地址
			List<Address> addressList = addressService.list(Condition.getQueryWrapper(new Address())
			.lambda()
			.eq(Address::getDataId,entity.getPeId())
			.eq(Address::getAddressDataType,Enterprise.DATA_TYPE)
			);
			enterpriseVO.setAddressList(
				AddressWrapper.build().listVO(addressList));
			// 联系人
			List<Contacts> contactsList = contactsService.list(Condition.getQueryWrapper(new Contacts())
			.lambda()
			.eq(Contacts::getDataId,entity.getPeId())
			.eq(Contacts::getContactsDataType,Enterprise.DATA_TYPE));
			enterpriseVO.setContactsList(
				ContactsWrapper.build().listVO(contactsList)
			);
		}
		return enterpriseVO;
	}

	public EnterpriseDTO entityDTO(EnterpriseExcel enterpriseExcel) {
		EnterpriseDTO entityDTO = BeanUtil.copy(enterpriseExcel, EnterpriseDTO.class);
		if (Func.isNotEmpty(entityDTO)) {
			String [] enterpriseTypeValueList = Func.toStrArray(enterpriseExcel.getEnterpriseType());
			if (Func.isNotEmpty(enterpriseTypeValueList)) {
				List<Integer> enterpriseTypeKeyList = new ArrayList<>();
				List<Dict> dictList = DictCache.list(DictConstant.ENTERPRISE_TYPE);
				for (int i = 0; i < enterpriseTypeValueList.length; i++) {
					int finalI = i;
					Dict dict = dictList.stream().filter(u->{
						return Func.equals(u.getDictValue(), Func.toInt(enterpriseTypeValueList[finalI]));
					}).findFirst().orElse(null);
					if (Func.isEmpty(dict)) {
						continue;
					}
					enterpriseTypeKeyList.add(dict.getDictKey());
				}
				entityDTO.setEnterpriseType(Func.join(enterpriseTypeKeyList));
			}
			entityDTO.setCity(enterpriseExcel.getEnterpriseCity());
			entityDTO.setProvince(enterpriseExcel.getEnterpriseProvince());
			entityDTO.setZipCode(enterpriseExcel.getEnterpriseZipCode());
			entityDTO.setCountry(enterpriseExcel.getEnterpriseCountry());
			// 地址
			AddressDTO address = BeanUtil.copy(enterpriseExcel, AddressDTO.class);
			if (Func.isNotEmpty(address) && !NodesUtil.isAllNull(address)) {
				if(Func.isNotEmpty(enterpriseExcel)&&Func.isNotEmpty(enterpriseExcel.getAddressTypeDesc())){
					Dict dict = DictCache.list(DictConstant.ADDRESS_TYPE).stream()
					.filter(item->{
						return Func.equals(item.getDictValue(), enterpriseExcel.getAddressTypeDesc());
					}).findFirst().orElse(null);
					address.setAddressType(dict.getDictKey());
				}
				if(Func.isNotEmpty(enterpriseExcel)&&Func.isNotEmpty(enterpriseExcel.getAddressDefaultFlag())){
					if("是".equals(enterpriseExcel.getAddressDefaultFlag())){
						address.setDefaultFlag(1);
					}else{
						address.setDefaultFlag(0);
					}
				}else{
					address.setDefaultFlag(0);
				}
				address.setAddressDataType(Enterprise.DATA_TYPE);
				entityDTO.getAddressList().add(address);
			}
			// 联系人
			ContactsDTO contacts = BeanUtil.copy(enterpriseExcel, ContactsDTO.class);
			if (Func.isNotEmpty(contacts) && !NodesUtil.isAllNull(contacts)) {
				if(Func.isNotEmpty(enterpriseExcel)&&Func.isNotEmpty(enterpriseExcel.getContactsDefaultFlag())){
					if("是".equals(enterpriseExcel.getContactsDefaultFlag())){
						contacts.setDefaultFlag(1);
					}else{
						contacts.setDefaultFlag(0);
					}
				}else{
					address.setDefaultFlag(0);
				}
				contacts.setContactsDataType(Enterprise.DATA_TYPE);
				entityDTO.getContactsList().add(contacts);
			}
		}
		return entityDTO;
	}
}
