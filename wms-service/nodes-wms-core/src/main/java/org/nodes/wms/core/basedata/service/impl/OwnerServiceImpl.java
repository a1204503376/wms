
package org.nodes.wms.core.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Region;
import org.nodes.core.base.service.IRegionService;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.core.tool.validation.Excel;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.core.basedata.dto.OwnerDTO;
import org.nodes.wms.core.basedata.excel.OwnerExcel;
import org.nodes.wms.core.basedata.mapper.OwnerMapper;
import org.nodes.wms.core.basedata.service.*;
import org.nodes.wms.core.basedata.vo.OwnerVO;
import org.nodes.wms.core.basedata.wrapper.OwnerWrapper;
import org.nodes.wms.core.common.dto.AddressDTO;
import org.nodes.wms.core.common.entity.Address;
import org.nodes.wms.core.common.entity.Contacts;
import org.nodes.wms.core.common.enums.DefaultFlagEnum;
import org.nodes.wms.core.common.service.IAddressService;
import org.nodes.wms.core.common.service.IContactsService;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ???????????? ???????????????
 *
 * @author zhongls
 * @since 2019-12-05
 */
@Slf4j
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class OwnerServiceImpl<M extends OwnerMapper, T extends Owner>
	extends BaseServiceImpl<OwnerMapper, Owner>
	implements IOwnerService {

	@Autowired
	IAddressService addressService;
	@Autowired
	IContactsService contactsService;
	@Autowired
	ISkuTypeService skuTypeService;
	@Autowired
	IEnterpriseService enterpriseService;
	@Autowired
	ISkuLotService skuLotService;

	@Override
	public boolean updateById(OwnerDTO ownerDTO) {
		IAddressService addressService = SpringUtil.getBean(IAddressService.class);
		IContactsService contactsService = SpringUtil.getBean(IContactsService.class);
		boolean result;
		//??????????????????????????????
		//Owner findExist = super.getByCode(ownerDTO.getOwnerCode());
		Owner findExist = super.list(Condition.getQueryWrapper(new Owner())
			.lambda()
			.eq(Owner::getOwnerCode, ownerDTO.getOwnerCode())
		).stream().findFirst().orElse(null);
		if (Func.isNotEmpty(findExist) && !findExist.getWoId().equals(ownerDTO.getWoId())) {
			throw new ServiceException("??????????????????");
		}
		result = this.updateById((Owner) ownerDTO);
		if (result) {
			//super.saveOrUpdate(ownerDTO);
		}
		//???????????????????????????????????????????????????
		addressService.update(Wrappers.<Address>update().lambda().set(Address::getIsDeleted, 1)
			.eq(Address::getDataId, ownerDTO.getWoId())
			.and(i -> i.eq(Address::getAddressDataType, Owner.DATA_TYPE)));
		//??????????????????????????????????????????????????????
		contactsService.update(Wrappers.<Contacts>update().lambda().set(Contacts::getIsDeleted, 1)
			.eq(Contacts::getDataId, ownerDTO.getWoId())
			.and(i -> i.eq(Contacts::getContactsDataType, Owner.DATA_TYPE)));

		//???????????????????????????
		if (Func.isNotEmpty(ownerDTO.getAddressList()))
			ownerDTO.getAddressList().stream().forEach(address -> {
				if (Func.isEmpty(addressService.getById(address.getPaId()))) {
					//??????????????????
					address.setDataId(ownerDTO.getWoId());
					address.setAddressDataType(Owner.DATA_TYPE);
				} else {
					//?????????????????????????????????????????????????????????????????????
					address.setIsDeleted(0);
				}
				addressService.saveOrUpdate(address);
			});
		//??????????????????????????????
		if (Func.isNotEmpty(ownerDTO.getContactsList())) {
			ownerDTO.getContactsList().stream().forEach(contacts -> {
				if (Func.isEmpty(contactsService.getById(contacts.getPcId()))) {
					//?????????????????????
					contacts.setDataId(ownerDTO.getWoId());
					contacts.setContactsDataType(Owner.DATA_TYPE);
				} else {
					//????????????????????????????????????????????????????????????????????????
					contacts.setIsDeleted(0);
				}
				contactsService.saveOrUpdate(contacts);
			});
		}
		//???????????????????????????????????????
		addressService.remove(ownerDTO.getWoId(), Owner.DATA_TYPE, 1);
		//??????????????????????????????????????????
		Contacts d_contacts = new Contacts();
		d_contacts.setDataId(ownerDTO.getWoId());
		d_contacts.setContactsDataType(Owner.DATA_TYPE);
		d_contacts.setIsDeleted(1);
		contactsService.remove(Condition.getQueryWrapper(d_contacts));

		return result;
	}

	@Override
	public boolean save(OwnerDTO ownerDTO) {
		boolean result = false;
		//???????????????????????????
		if (Func.isNotEmpty(ownerDTO.getWoId())) {
			updateById(ownerDTO);
			return true;
		}
		//??????????????????????????????
		//Owner findExist = super.getByCode(ownerDTO.getOwnerCode());
		Owner findExist = super.list(Condition.getQueryWrapper(new Owner())
			.lambda()
			.eq(Owner::getOwnerCode, ownerDTO.getOwnerCode())
		).stream().findFirst().orElse(null);
		if (Func.isNotEmpty(findExist) && !findExist.getWoId().equals(ownerDTO.getWoId())) {
			throw new ServiceException("??????????????????");
		}
		result = this.save((Owner) ownerDTO);
		//super.saveOrUpdate(ownerDTO);
		List<AddressDTO> addressList = ownerDTO.getAddressList();
		List<Contacts> contactsList = ownerDTO.getContactsList();
		//??????????????????
		if (Func.isNotEmpty(addressList)) {
			addressList.stream().forEach(address -> {
				Address _address = new Address();
				_address.setDataId(ownerDTO.getWoId());
				_address.setAddressDataType(Owner.DATA_TYPE);
				_address.setAddressType(address.getAddressType());
				_address.setAddress(address.getAddress());
				_address.setIsDeleted(0);
				/*if (addressService.count(Condition.getQueryWrapper(_address)) > 0) {
					throw new ServiceException("??????????????????");
				}*/
				address.setDataId(ownerDTO.getWoId());
				address.setCreateUser(ownerDTO.getCreateUser());
				address.setUpdateUser(ownerDTO.getCreateUser());
				address.setAddressDataType(Owner.DATA_TYPE);
				addressService.saveOrUpdate(address);
			});
		}
		//?????????????????????
		if (Func.isNotEmpty(contactsList)) {
			contactsList.stream().forEach(contacts -> {
				Contacts _contacts = new Contacts();
				_contacts.setDataId(ownerDTO.getWoId());
				_contacts.setContactsDataType(Owner.DATA_TYPE);
				_contacts.setContactsName(contacts.getContactsName());
				_contacts.setIsDeleted(0);
				if (contactsService.count(Condition.getQueryWrapper(_contacts)) > 0) {
					throw new ServiceException("?????????????????????");
				}
				contacts.setDataId(ownerDTO.getWoId());
				contacts.setCreateUser(ownerDTO.getCreateUser());
				contacts.setUpdateUser(ownerDTO.getCreateUser());
				contacts.setContactsDataType(Owner.DATA_TYPE);
				contactsService.saveOrUpdate(contacts);
			});
		}
		return result;
	}

	@Override
	public boolean saveOrUpdate(OwnerDTO entity) {
		return super.getIdVal(entity) == null ? this.save(entity) : this.updateById(entity);
	}

	/*@Override
	public boolean removeByIds(List<Long> ids) {
		for (Long id : ids){
			List<Enterprise> enterprises = enterpriseService.list(new LambdaQueryWrapper<Enterprise>().eq(Enterprise::getWoId, id));
			Owner owner = super.getById((id));
			if (Func.isNotEmpty(enterprises)){
				throw new ServiceException("?????????" + owner.getOwnerName() + " ???????????????,?????????????????????");
			}
			List<SkuLot> skuLots = skuLotService.list(new LambdaQueryWrapper<SkuLot>().eq(SkuLot::getWoId, id));
			if (Func.isNotEmpty(skuLots)){
				throw new ServiceException("?????????" + owner.getOwnerName() + " ??????????????????,?????????????????????");
			}
		}
		super.removeByIds(ids);
		return true;
	}*/

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		ISkuService skuService = SpringUtil.getBean(ISkuService.class);
		if (Func.isEmpty(idList)) {
			throw new ServiceException("??????????????????????????????");
		}
		List<Owner> ownerList = super.listByIds(idList);

		for (Owner owner : ownerList) {
			// ??????????????????
			//Owner owner = super.getById((Long) id);
			if (Func.isEmpty(owner)) {
				continue;
			}
			// ???????????????????????????????????????????????????
			SkuType skuType = new SkuType();
			skuType.setWoId((Long) owner.getWoId());
			if (skuTypeService.count(Condition.getQueryWrapper(skuType)) > 0) {
				throw new ServiceException("?????????" + owner.getOwnerName() + " ????????????????????????????????????????????????????????????????????????");
			}
			// ?????????????????????????????????????????????

			long cnt = skuService.count(Condition.getQueryWrapper(new Sku())
				.lambda()
				.eq(Sku::getWoId, owner.getWoId())
			);
			if (cnt > 0L) {
				throw new ServiceException("?????????" + owner.getOwnerName() + " ????????????????????????????????????????????????????????????");
			}
			if (super.removeById(owner.getWoId())) {
				super.removeById((Long) owner.getWoId());
				//???????????????????????????
				Address d_address = new Address();
				d_address.setDataId((Long) owner.getWoId());
				d_address.setAddressDataType(Owner.DATA_TYPE);
				addressService.remove(Condition.getQueryWrapper(d_address));
				//??????????????????????????????????????????
				contactsService.remove((Long) owner.getWoId(), Owner.DATA_TYPE, 0);
			}
		}

		return true;
	}

	@Override
	public IPage<OwnerVO> selectPage(IPage<Owner> page, Owner owner) {
		IPage<Owner> pages = super.page(page, this.getSelectQueryWrapper(owner));
		return OwnerWrapper.build().pageVO(pages);
	}

	@Override
	public List<OwnerVO> selectList(Owner owner) {
		return OwnerWrapper.build().listVO(super.list(this.getSelectQueryWrapper(owner)));
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		// ?????????????????????
		List<OwnerVO> ownerList = OwnerWrapper.build().listVO(super.list(Condition.getQueryWrapper(params, Owner.class)));
		// ???????????????????????????????????????????????????
		List<Long> woIdList = NodesUtil.toList(ownerList, Owner::getWoId);
		// ??????????????????????????????
		/*List<Address> addressAll = AddressCache.list().stream().filter(address -> {
			return woIdList.contains(address.getDataId())
				&& address.getAddressDataType().equals(Owner.DATA_TYPE);
		}).collect(Collectors.toList());*/
		IAddressService addressService = SpringUtil.getBean(IAddressService.class);
		IContactsService contactsService = SpringUtil.getBean(IContactsService.class);
		List<Address> addressAll = addressService.list(Condition.getQueryWrapper(new Address())
			.lambda()
			.in(Address::getDataId, woIdList)
			.eq(Address::getAddressDataType, Owner.DATA_TYPE)
		);
		/*List<Contacts> contactAll = ContactsCache.list().stream().filter(contacts -> {
			return woIdList.contains(contacts.getDataId())
				&& contacts.getContactsDataType().equals(Owner.DATA_TYPE);
		}).collect(Collectors.toList());*/
		List<Contacts> contactAll = contactsService.list(Condition.getQueryWrapper(new Contacts())
			.lambda()
			.in(Contacts::getDataId, woIdList)
			.eq(Contacts::getContactsDataType, Owner.DATA_TYPE)
		);

		List<OwnerExcel> ownerExportList = new ArrayList<>();
		// ?????????????????????????????????
		for (Owner owner : ownerList) {
			// ?????????????????????????????????
			List<Address> addressList = addressAll.stream().filter(address -> {
				return address.getDataId().equals(owner.getWoId());
			}).collect(Collectors.toList());
			// ????????????????????????????????????
			List<Contacts> contactsList = contactAll.stream().filter(contacts -> {
				return contacts.getDataId().equals(owner.getWoId());
			}).collect(Collectors.toList());
			// ?????????????????????????????????????????????
			Integer maxLength = 1;
			if (Func.isNotEmpty(addressList) || Func.isNotEmpty(contactsList)) {
				maxLength = addressList.size() > contactsList.size() ? addressList.size() : contactsList.size();
			}

			for (int i = 0; i < maxLength; i++) {
				Address address = i < addressList.size() ? addressList.get(i) : null;
				Contacts contacts = i < contactsList.size() ? contactsList.get(i) : null;

				OwnerExcel ownerExport = BeanUtil.copy(owner, OwnerExcel.class);
				// ??????????????????
				if (Func.isNotEmpty(address)) {
					ownerExport.setAddress(address.getAddress());
					ownerExport.setAddressTypeDesc(DictCache.getValue(DictCodeConstant.ADDRESS_TYPE, address.getAddressType()));
					ownerExport.setLongitude(address.getLongitude());
					ownerExport.setLatitude(address.getLatitude());
					if (address.getDefaultFlag().equals(DefaultFlagEnum.TRUE.getIndex())) {
						ownerExport.setAddressDefaultFlag(DefaultFlagEnum.TRUE.getName());
					} else {
						ownerExport.setAddressDefaultFlag(DefaultFlagEnum.FALSE.getName());
					}
				}
				// ?????????????????????
				if (Func.isNotEmpty(contacts)) {
					ownerExport.setContactsName(contacts.getContactsName());
					ownerExport.setContactsEmail(contacts.getContactsEmail());
					ownerExport.setContactsNumber(contacts.getContactsNumber());
					ownerExport.setContactsFax(contacts.getContactsFax());
					if (contacts.getDefaultFlag().equals(DefaultFlagEnum.TRUE.getIndex())) {
						ownerExport.setContactsDefaultFlag(DefaultFlagEnum.TRUE.getName());
					} else {
						ownerExport.setContactsDefaultFlag(DefaultFlagEnum.FALSE.getName());
					}
				}
				//??????????????????list
				ownerExportList.add(ownerExport);
			}
		}
		ExcelUtil.export(response, "??????", "???????????????", ownerExportList, OwnerExcel.class);
	}

	@Override
	public List<DataVerify> validExcel(List<OwnerExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();
		IRegionService regionService = SpringUtil.getBean(IRegionService.class);
		Map<String, OwnerDTO> cache = new HashMap<>();
		int index = 1;
		for (OwnerExcel ownerExcel : dataList) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// ?????????DTO
			OwnerDTO ownerDTO = OwnerWrapper.build().entityDTO(ownerExcel);
			// ??????????????????
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(ownerDTO, Excel.class);
			//Owner findOwner = OwnerCache.getByCode(ownerDTO.getOwnerCode());
			Owner findOwner = super.list(Condition.getQueryWrapper(new Owner())
				.lambda()
				.eq(Owner::getOwnerCode, ownerDTO.getOwnerCode())
			).stream().findFirst().orElse(null);
			// ??????????????????
			if (Func.isEmpty(ownerDTO.getOwnerName())) {
				validResult.addError("ownerName", "????????????????????????");
			}
			// ???????????????????????????
//			if (Func.isNotEmpty(findOwner)) {
//				validResult.addError("ownerCode", "????????????[" + ownerDTO.getOwnerCode() + "]?????????");
//			}
			if (Func.isNotEmpty(ownerDTO.getOwnerProvince())) {
				/*long provinceCount = CityCache.listProvince().stream().filter(u -> u.equals(ownerDTO.getOwnerProvince())).count();
				long provinceCount = regionService.list(Condition.getQueryWrapper(new Region())
				.lambda()
				.eq(Region::getParentCode,"00")
				).stream().filter(u -> u.equals(ownerDTO.getOwnerProvince())).count();*/
				List<Region> province = regionService.list(Condition.getQueryWrapper(new Region())
					.lambda()
					.eq(Region::getParentCode, "00")
				);
				long provinceCount = province.stream().filter(u -> u.getName().equals(ownerDTO.getOwnerProvince())).count();
				if (0 == provinceCount) {
					validResult.addError("ownerProvince", "?????????????????????");
				}
			}
			if (Func.isNotEmpty(ownerDTO.getOwnerCity())) {
				//long count = CityCache.listProvince().stream().filter(u -> u.equals(ownerDTO.getOwnerCity())).count();
				List<Region> city = regionService.list(Condition.getQueryWrapper(new Region())
					.lambda()
					.gt(Region::getParentCode, 10)
					.lt(Region::getParentCode, 500)
				);
				long count = city.stream().filter(u -> u.getName().equals(ownerDTO.getOwnerCity())).count();
				/*long count = regionService.list(Condition.getQueryWrapper(new Region())
					.lambda()
					.eq(Region::getParentCode,"00")
				).stream().filter(u -> u.equals(ownerDTO.getOwnerCity())).count();*/
				if (0 == count) {
					validResult.addError("ownerProvince", "?????????????????????");
				}
			}
			if (Func.isNotEmpty(ownerDTO.getAddressList()) && !NodesUtil.isAllNull(ownerDTO.getAddressList().get(0))) {
				ValidationUtil.ValidResult validResult1 = ValidationUtil.validateBean(ownerDTO.getAddressList().get(0), Excel.class);
				validResult.getAllErrors().addAll(validResult1.getAllErrors());
			}
			if (Func.isNotEmpty(ownerDTO.getContactsList()) && !NodesUtil.isAllNull(ownerDTO.getContactsList().get(0))) {
				ValidationUtil.ValidResult validResult1 = ValidationUtil.validateBean(ownerDTO.getContactsList().get(0), Excel.class);
				validResult.getAllErrors().addAll(validResult1.getAllErrors());
			}
			if (validResult.hasErrors()) {
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			} else {
				//???????????????????????????
				if (cache.containsKey(ownerExcel.getOwnerCode())) {
					// ??????????????????????????????
					cache.get(ownerDTO.getOwnerCode()).getAddressList().addAll(ownerDTO.getAddressList());
					cache.get(ownerDTO.getOwnerCode()).getContactsList().addAll(ownerDTO.getContactsList());
				} else {
					// ???????????????map???
					cache.put(ownerDTO.getOwnerCode(), ownerDTO);
				}
				dataVerify.setCacheKey(ownerDTO.getOwnerCode());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// ???????????????redis?????????
			for (String code : cache.keySet()) {
				OwnerCache.put(code, cache.get(code));
			}
		}
		return dataVerifyList;
	}

//	@Override
//	public boolean importData(List<DataVerify> dataVerifyList) {
//		if(Func.isEmpty(dataVerifyList)) {
//			throw new ServiceException("???????????????????????????");
//		}
//		for (DataVerify dataVerify : dataVerifyList) {
//			if (ObjectUtil.isEmpty(dataVerify)) {
//				continue;
//			}
//			OwnerDTO ownerDTO = OwnerCache.get(dataVerify.getCacheKey());
//			if (ObjectUtil.isEmpty(ownerDTO)) {
//				continue;
//			}
//			if (this.saveOrUpdate(ownerDTO)) {
//				OwnerCache.remove(ownerDTO.getOwnerCode());
//				OwnerCache.saveOrUpdate(ownerDTO);
//			}
//		}
//		return true;
//	}

	@Override
	public boolean importData(List<DataVerify> dataVerifyList) {
		if (Func.isEmpty(dataVerifyList)) {
			throw new ServiceException("???????????????????????????");
		}
		for (DataVerify dataVerify : dataVerifyList) {
			if (ObjectUtil.isEmpty(dataVerify)) {
				continue;
			}
			OwnerDTO ownerDTO = OwnerCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(ownerDTO)) {
				continue;
			}
			//?????????????????????
			super.saveOrUpdate(ownerDTO, new LambdaQueryWrapper<Owner>().eq(Owner::getOwnerCode, ownerDTO.getOwnerCode()));
			Owner owner = super.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getOwnerCode, ownerDTO.getOwnerCode()));
			if (Func.isNotEmpty(ownerDTO)) {
				//??????????????????
				addressService.remove(new LambdaQueryWrapper<Address>().eq(Address::getDataId, owner.getWoId()));
				//?????????????????????
				contactsService.remove(new LambdaQueryWrapper<Contacts>().eq(Contacts::getDataId, owner.getWoId()));
			}

			List<AddressDTO> addressList = ownerDTO.getAddressList();
			List<Contacts> contactsList = ownerDTO.getContactsList();
			//?????????????????????????????????
			List<Address> addresses = new ArrayList<>();
			List<Contacts> contacts = new ArrayList<>();

			//??????????????????
			if (Func.isNotEmpty(addressList)) {
				addressList.stream().forEach(address -> {
					address.setDataId(owner.getWoId());
					address.setAddressDataType(Owner.DATA_TYPE);
					if (Func.isEmpty(address.getAddressType())) {
						address.setAddressType(0);
					}
					addresses.add(address);
				});
			}
			//?????????????????????
			if (Func.isNotEmpty(contactsList)) {
				contactsList.stream().forEach(contact -> {
					contact.setDataId(owner.getWoId());
					contact.setContactsDataType(Owner.DATA_TYPE);
					contacts.add(contact);
				});
			}
			addressService.saveOrUpdateBatch(addresses);
			contactsService.saveOrUpdateBatch(contacts);
			//??????????????????????????????
			OwnerCache.remove(ownerDTO.getOwnerCode());
			//????????????
			//super.saveOrUpdate(owner);
		}
		return true;
	}

	/**
	 * ???????????????????????????
	 *
	 * @param owner ????????????
	 * @return ???????????????
	 */
	private QueryWrapper<Owner> getSelectQueryWrapper(Owner owner) {
		QueryWrapper<Owner> queryWrapper = new QueryWrapper<>();
		if (Func.isNotEmpty(owner.getOwnerCode())) {
			queryWrapper.lambda().like(Owner::getOwnerCode, owner.getOwnerCode());
		}
		if (Func.isNotEmpty(owner.getOwnerName())) {
			queryWrapper.lambda().like(Owner::getOwnerName, owner.getOwnerName());
		}
		if (Func.isNotEmpty(owner.getOwnerCountry())) {
			queryWrapper.lambda().eq(Owner::getOwnerCountry, owner.getOwnerCountry());
		}
		if (Func.isNotEmpty(owner.getOwnerProvince())) {
			queryWrapper.lambda().eq(Owner::getOwnerProvince, owner.getOwnerProvince());
		}
		if (Func.isNotEmpty(owner.getOwnerCity())) {
			queryWrapper.lambda().eq(Owner::getOwnerCity, owner.getOwnerCity());
		}
		if (Func.isNotEmpty(owner.getOwnerZipCode())) {
			queryWrapper.lambda().eq(Owner::getOwnerZipCode, owner.getOwnerZipCode());
		}
		return queryWrapper;
	}
}
