
package org.nodes.wms.core.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.entity.Region;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.service.IRegionService;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.core.tool.validation.Excel;
import org.nodes.wms.core.basedata.cache.OwnerCache;
import org.nodes.wms.core.basedata.dto.OwnerDTO;
import org.nodes.wms.core.basedata.entity.*;
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
 * 货主管理 服务实现类
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
		//查询货主编码是否重复
		//Owner findExist = super.getByCode(ownerDTO.getOwnerCode());
		Owner findExist = super.list(Condition.getQueryWrapper(new Owner())
			.lambda()
			.eq(Owner::getOwnerCode, ownerDTO.getOwnerCode())
		).stream().findFirst().orElse(null);
		if (Func.isNotEmpty(findExist) && !findExist.getWoId().equals(ownerDTO.getWoId())) {
			throw new ServiceException("货主编码重复");
		}
		result = this.updateById((Owner) ownerDTO);
		if (result) {
			//super.saveOrUpdate(ownerDTO);
		}
		//将所有关联地址信息更新为待删除状态
		addressService.update(Wrappers.<Address>update().lambda().set(Address::getIsDeleted, 1)
			.eq(Address::getDataId, ownerDTO.getWoId())
			.and(i -> i.eq(Address::getAddressDataType, Owner.DATA_TYPE)));
		//将所有关联联系人信息更新为待删除状态
		contactsService.update(Wrappers.<Contacts>update().lambda().set(Contacts::getIsDeleted, 1)
			.eq(Contacts::getDataId, ownerDTO.getWoId())
			.and(i -> i.eq(Contacts::getContactsDataType, Owner.DATA_TYPE)));

		//添加或更新地址信息
		if (Func.isNotEmpty(ownerDTO.getAddressList()))
			ownerDTO.getAddressList().stream().forEach(address -> {
				if (Func.isEmpty(addressService.getById(address.getPaId()))) {
					//新增地址信息
					address.setDataId(ownerDTO.getWoId());
					address.setAddressDataType(Owner.DATA_TYPE);
				} else {
					//更新修改过的地址信息并将待删除状态改为正常状态
					address.setIsDeleted(0);
				}
				addressService.saveOrUpdate(address);
			});
		//添加或更新联系人信息
		if (Func.isNotEmpty(ownerDTO.getContactsList())) {
			ownerDTO.getContactsList().stream().forEach(contacts -> {
				if (Func.isEmpty(contactsService.getById(contacts.getPcId()))) {
					//新增联系人信息
					contacts.setDataId(ownerDTO.getWoId());
					contacts.setContactsDataType(Owner.DATA_TYPE);
				} else {
					//更新修改过的联系人信息并将待删除状态改为正常状态
					contacts.setIsDeleted(0);
				}
				contactsService.saveOrUpdate(contacts);
			});
		}
		//将待删除的相关地址信息删除
		addressService.remove(ownerDTO.getWoId(), Owner.DATA_TYPE, 1);
		//将待删除的相关联系人信息删除
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
		//如果主键存在则更新
		if (Func.isNotEmpty(ownerDTO.getWoId())) {
			updateById(ownerDTO);
			return true;
		}
		//查询货主编码是否重复
		//Owner findExist = super.getByCode(ownerDTO.getOwnerCode());
		Owner findExist = super.list(Condition.getQueryWrapper(new Owner())
			.lambda()
			.eq(Owner::getOwnerCode, ownerDTO.getOwnerCode())
		).stream().findFirst().orElse(null);
		if (Func.isNotEmpty(findExist) && !findExist.getWoId().equals(ownerDTO.getWoId())) {
			throw new ServiceException("货主编码重复");
		}
		result = this.save((Owner) ownerDTO);
		//super.saveOrUpdate(ownerDTO);
		List<AddressDTO> addressList = ownerDTO.getAddressList();
		List<Contacts> contactsList = ownerDTO.getContactsList();
		//添加地址信息
		if (Func.isNotEmpty(addressList)) {
			addressList.stream().forEach(address -> {
				Address _address = new Address();
				_address.setDataId(ownerDTO.getWoId());
				_address.setAddressDataType(Owner.DATA_TYPE);
				_address.setAddressType(address.getAddressType());
				_address.setAddress(address.getAddress());
				_address.setIsDeleted(0);
				/*if (addressService.count(Condition.getQueryWrapper(_address)) > 0) {
					throw new ServiceException("地址信息重复");
				}*/
				address.setDataId(ownerDTO.getWoId());
				address.setCreateUser(ownerDTO.getCreateUser());
				address.setUpdateUser(ownerDTO.getCreateUser());
				address.setAddressDataType(Owner.DATA_TYPE);
				addressService.saveOrUpdate(address);
			});
		}
		//添加联系人信息
		if (Func.isNotEmpty(contactsList)) {
			contactsList.stream().forEach(contacts -> {
				Contacts _contacts = new Contacts();
				_contacts.setDataId(ownerDTO.getWoId());
				_contacts.setContactsDataType(Owner.DATA_TYPE);
				_contacts.setContactsName(contacts.getContactsName());
				_contacts.setIsDeleted(0);
				if (contactsService.count(Condition.getQueryWrapper(_contacts)) > 0) {
					throw new ServiceException("联系人信息重复");
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
				throw new ServiceException("货主：" + owner.getOwnerName() + " 已关联企业,请先解除关联！");
			}
			List<SkuLot> skuLots = skuLotService.list(new LambdaQueryWrapper<SkuLot>().eq(SkuLot::getWoId, id));
			if (Func.isNotEmpty(skuLots)){
				throw new ServiceException("货主：" + owner.getOwnerName() + " 已关联批属性,请先解除关联！");
			}
		}
		super.removeByIds(ids);
		return true;
	}*/

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		ISkuService skuService = SpringUtil.getBean(ISkuService.class);
		if (Func.isEmpty(idList)) {
			throw new ServiceException("请选择要删除的货主！");
		}
		List<Owner> ownerList = super.listByIds(idList);

		for (Owner owner : ownerList) {
			// 获取货主信息
			//Owner owner = super.getById((Long) id);
			if (Func.isEmpty(owner)) {
				continue;
			}
			// 验证该货主下是否绑定了物品分类信息
			SkuType skuType = new SkuType();
			skuType.setWoId((Long) owner.getWoId());
			if (skuTypeService.count(Condition.getQueryWrapper(skuType)) > 0) {
				throw new ServiceException("货主：" + owner.getOwnerName() + " 已绑定物品分类，请先删除该货主下的物品分类信息！");
			}
			// 验证该货主下是否绑定了物品信息

			long cnt = skuService.count(Condition.getQueryWrapper(new Sku())
				.lambda()
				.eq(Sku::getWoId, owner.getWoId())
			);
			if (cnt > 0L) {
				throw new ServiceException("货主：" + owner.getOwnerName() + " 已绑定物品，请先删除该货主下的物品信息！");
			}
			if (super.removeById(owner.getWoId())) {
				super.removeById((Long) owner.getWoId());
				//将相关地址信息删除
				Address d_address = new Address();
				d_address.setDataId((Long) owner.getWoId());
				d_address.setAddressDataType(Owner.DATA_TYPE);
				addressService.remove(Condition.getQueryWrapper(d_address));
				//将待删除的相关联系人信息删除
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
		// 查询出货主信息
		List<OwnerVO> ownerList = OwnerWrapper.build().listVO(super.list(Condition.getQueryWrapper(params, Owner.class)));
		// 查询出与货主关联的联系地址、联系人
		List<Long> woIdList = NodesUtil.toList(ownerList, Owner::getWoId);
		// 查询所有联系人和地址
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
		// 循环查询出来的货主列表
		for (Owner owner : ownerList) {
			// 查询当前货主的所有地址
			List<Address> addressList = addressAll.stream().filter(address -> {
				return address.getDataId().equals(owner.getWoId());
			}).collect(Collectors.toList());
			// 查询当前货主的所有联系人
			List<Contacts> contactsList = contactAll.stream().filter(contacts -> {
				return contacts.getDataId().equals(owner.getWoId());
			}).collect(Collectors.toList());
			// 计算最大循环次数，最少循环一次
			Integer maxLength = 1;
			if (Func.isNotEmpty(addressList) || Func.isNotEmpty(contactsList)) {
				maxLength = addressList.size() > contactsList.size() ? addressList.size() : contactsList.size();
			}

			for (int i = 0; i < maxLength; i++) {
				Address address = i < addressList.size() ? addressList.get(i) : null;
				Contacts contacts = i < contactsList.size() ? contactsList.get(i) : null;

				OwnerExcel ownerExport = BeanUtil.copy(owner, OwnerExcel.class);
				// 封装地址数据
				if (Func.isNotEmpty(address)) {
					ownerExport.setAddress(address.getAddress());
					ownerExport.setAddressTypeDesc(DictCache.getValue(DictConstant.ADDRESS_TYPE, address.getAddressType()));
					ownerExport.setLongitude(address.getLongitude());
					ownerExport.setLatitude(address.getLatitude());
					if (address.getDefaultFlag().equals(DefaultFlagEnum.TRUE.getIndex())) {
						ownerExport.setAddressDefaultFlag(DefaultFlagEnum.TRUE.getName());
					} else {
						ownerExport.setAddressDefaultFlag(DefaultFlagEnum.FALSE.getName());
					}
				}
				// 封装联系人数据
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
				//将货主装入新list
				ownerExportList.add(ownerExport);
			}
		}
		ExcelUtil.export(response, "货主", "货主数据表", ownerExportList, OwnerExcel.class);
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
			// 封装成DTO
			OwnerDTO ownerDTO = OwnerWrapper.build().entityDTO(ownerExcel);
			// 开始效验数据
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(ownerDTO, Excel.class);
			//Owner findOwner = OwnerCache.getByCode(ownerDTO.getOwnerCode());
			Owner findOwner = super.list(Condition.getQueryWrapper(new Owner())
				.lambda()
				.eq(Owner::getOwnerCode, ownerDTO.getOwnerCode())
			).stream().findFirst().orElse(null);
			// 货主名称非空
			if (Func.isEmpty(ownerDTO.getOwnerName())) {
				validResult.addError("ownerName", "货主名称不能为空");
			}
			// 货主编码唯一性验证
//			if (Func.isNotEmpty(findOwner)) {
//				validResult.addError("ownerCode", "货主编码[" + ownerDTO.getOwnerCode() + "]已存在");
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
					validResult.addError("ownerProvince", "省份名称不正确");
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
					validResult.addError("ownerProvince", "城市名称不正确");
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
				//缓存中包含货主编码
				if (cache.containsKey(ownerExcel.getOwnerCode())) {
					// 更新地址、联系人信息
					cache.get(ownerDTO.getOwnerCode()).getAddressList().addAll(ownerDTO.getAddressList());
					cache.get(ownerDTO.getOwnerCode()).getContactsList().addAll(ownerDTO.getContactsList());
				} else {
					// 记录到缓存map中
					cache.put(ownerDTO.getOwnerCode(), ownerDTO);
				}
				dataVerify.setCacheKey(ownerDTO.getOwnerCode());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// 存储数据到redis缓存中
			for (String code : cache.keySet()) {
				OwnerCache.put(code, cache.get(code));
			}
		}
		return dataVerifyList;
	}

//	@Override
//	public boolean importData(List<DataVerify> dataVerifyList) {
//		if(Func.isEmpty(dataVerifyList)) {
//			throw new ServiceException("没有数据可以保存！");
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
			throw new ServiceException("没有数据可以保存！");
		}
		for (DataVerify dataVerify : dataVerifyList) {
			if (ObjectUtil.isEmpty(dataVerify)) {
				continue;
			}
			OwnerDTO ownerDTO = OwnerCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(ownerDTO)) {
				continue;
			}
			//添加或修改货主
			super.saveOrUpdate(ownerDTO, new LambdaQueryWrapper<Owner>().eq(Owner::getOwnerCode, ownerDTO.getOwnerCode()));
			Owner owner = super.getOne(new LambdaQueryWrapper<Owner>().eq(Owner::getOwnerCode, ownerDTO.getOwnerCode()));
			if (Func.isNotEmpty(ownerDTO)) {
				//删除地址信息
				addressService.remove(new LambdaQueryWrapper<Address>().eq(Address::getDataId, owner.getWoId()));
				//删除联系人信息
				contactsService.remove(new LambdaQueryWrapper<Contacts>().eq(Contacts::getDataId, owner.getWoId()));
			}

			List<AddressDTO> addressList = ownerDTO.getAddressList();
			List<Contacts> contactsList = ownerDTO.getContactsList();
			//待保存的地址、联系信息
			List<Address> addresses = new ArrayList<>();
			List<Contacts> contacts = new ArrayList<>();

			//添加地址信息
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
			//添加联系人信息
			if (Func.isNotEmpty(contactsList)) {
				contactsList.stream().forEach(contact -> {
					contact.setDataId(owner.getWoId());
					contact.setContactsDataType(Owner.DATA_TYPE);
					contacts.add(contact);
				});
			}
			addressService.saveOrUpdateBatch(addresses);
			contactsService.saveOrUpdateBatch(contacts);
			//从缓存中移除临时缓存
			OwnerCache.remove(ownerDTO.getOwnerCode());
			//更新缓存
			//super.saveOrUpdate(owner);
		}
		return true;
	}

	/**
	 * 获取模糊查询构造器
	 *
	 * @param owner 查询条件
	 * @return 查询构造器
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
