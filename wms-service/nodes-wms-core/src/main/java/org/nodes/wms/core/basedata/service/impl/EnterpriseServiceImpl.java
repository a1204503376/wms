
package org.nodes.wms.core.basedata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.core.tool.validation.Group;
import org.nodes.wms.core.basedata.cache.EnterpriseCache;
import org.nodes.wms.core.basedata.dto.EnterpriseDTO;
import org.nodes.wms.core.basedata.entity.Enterprise;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.sku.entities.SkuInc;
import org.nodes.wms.core.basedata.excel.EnterpriseExcel;
import org.nodes.wms.core.basedata.mapper.EnterpriseMapper;
import org.nodes.wms.core.basedata.service.IEnterpriseService;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.service.ISkuIncService;
import org.nodes.wms.core.basedata.wrapper.EnterpriseWrapper;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 来往企业 服务实现类
 *
 * @author pengwei
 * @since 2019-12-06
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class EnterpriseServiceImpl<M extends EnterpriseMapper, T extends Enterprise>
	extends BaseServiceImpl<EnterpriseMapper, Enterprise>
	implements IEnterpriseService {

	@Autowired
	IAddressService addressService;
	@Autowired
	IContactsService contactsService;
	@Autowired
	ISkuIncService skuIncSerivce;

	@Override
	public List<Enterprise> selectList(EnterpriseDTO enterpriseDTO) {
		return super.list(this.getQueryWrapper(enterpriseDTO));
	}

	@Override
	public IPage<Enterprise> selectPage(IPage<Enterprise> page, EnterpriseDTO enterpriseDTO) {
		return super.page(page, this.getQueryWrapper(enterpriseDTO));
	}

	@Override
	public boolean save(EnterpriseDTO enterpriseDTO) {
		IAddressService addressService = SpringUtil.getBean(IAddressService.class);
		IContactsService contactsService = SpringUtil.getBean(IContactsService.class);
		Enterprise enterprise = super.list(Condition.getQueryWrapper(new Enterprise())
			.lambda()
			.eq(Enterprise::getEnterpriseCode, enterpriseDTO.getEnterpriseCode()))
			.stream().findFirst().orElse(null);
		if (Func.isNotEmpty(enterprise)) {
			throw new ServiceException("企业编码：" + enterpriseDTO.getEnterpriseCode() + " 已存在！");
		}

		if (this.save((Enterprise) enterpriseDTO)) {
		}
		if (Func.isNotEmpty(enterpriseDTO.getAddressList())) {
			// 如果没有设置默认地址，则将第一条地址设置为默认
			int defaultSize = enterpriseDTO.getAddressList().stream().filter(address -> {
				return Func.isNotEmpty(address.getDefaultFlag()) && address.getDefaultFlag() == 1;
			}).collect(Collectors.toList()).size();
			if (defaultSize == 0) {
				enterpriseDTO.getAddressList().get(0).setDefaultFlag(1);
			}
			for (Address address : enterpriseDTO.getAddressList()) {
				if (Func.isNotEmpty(addressService.getById(address.getPaId()))) {
					throw new ServiceException("地址主键已存在:paId");
				}
				address.setDataId(enterpriseDTO.getPeId());
				address.setAddressDataType(Enterprise.DATA_TYPE);
				address.setCreateUser(enterpriseDTO.getCreateUser());
				if (!addressService.saveOrUpdate(address)) {
					throw new ServiceException("创建地址信息失败！");
				}
			}
		}
		if (Func.isNotEmpty(enterpriseDTO.getContactsList())) {
			// 如果没有设置默认联系人，则将第一条联系人设置为默认
			List<Contacts> filterList = enterpriseDTO.getContactsList().stream().filter(contacts -> {
				return Func.isNotEmpty(contacts.getDefaultFlag()) && contacts.getDefaultFlag() == 1;
			}).collect(Collectors.toList());
			int defaultSize = Func.isEmpty(filterList) ? 0 : filterList.size();
			if (defaultSize == 0) {
				enterpriseDTO.getContactsList().get(0).setDefaultFlag(1);
			} else if (defaultSize > 1) {
				for (int i = 1; i < filterList.size() - 1; i++) {
					filterList.get(i).setDefaultFlag(0);
				}
			}
			for (Contacts contacts : enterpriseDTO.getContactsList()) {
				if (Func.isNotEmpty(contactsService.getById(contacts.getPcId())))
					throw new ServiceException("联系人主键已存在:pcId");
				contacts.setDataId(enterpriseDTO.getPeId());
				contacts.setContactsDataType(Enterprise.DATA_TYPE);
				contacts.setCreateUser(enterpriseDTO.getCreateUser());
				if (!contactsService.saveOrUpdate(contacts)) {
					throw new ServiceException("创建联系人失败！");
				}
			}
		}
		return true;
	}

	@Override
	public boolean updateById(EnterpriseDTO enterpriseDTO) {
		if (baseMapper.selectList(Condition.getQueryWrapper(new Enterprise()).lambda()
			.eq(Enterprise::getEnterpriseCode, enterpriseDTO.getEnterpriseCode())
			.ne(Enterprise::getPeId, enterpriseDTO.getPeId())).size() > 0) {
			throw new ServiceException("企业编码：" + enterpriseDTO.getEnterpriseCode() + " 已存在！");
		}
		if (super.updateById(enterpriseDTO)) {

		}
		// 更新地址
		if (Func.isNotEmpty(enterpriseDTO.getAddressList())) {
			// 如果没有设置默认地址，则将第一条地址设置为默认
			int defaultSize = enterpriseDTO.getAddressList().stream().filter(address -> {
				return address.getDefaultFlag() == 1;
			}).collect(Collectors.toList()).size();
			if (defaultSize == 0) {
				enterpriseDTO.getAddressList().get(0).setDefaultFlag(1);
			}
			for (Address address : enterpriseDTO.getAddressList()) {
				Address find = addressService.getById(address.getPaId());
				if (Func.isEmpty(find)) {
					address.setDataId(enterpriseDTO.getPeId());
					address.setAddressDataType(Enterprise.DATA_TYPE);
					address.setCreateUser(enterpriseDTO.getCreateUser());
				}
				address.setUpdateUser(enterpriseDTO.getUpdateUser());
				addressService.saveOrUpdate(address);
			}
		}
		if (Func.isNotEmpty(enterpriseDTO.getAddressDeletedList())) {
			for (Address address : enterpriseDTO.getAddressDeletedList()) {
				addressService.removeById(address.getPaId());
			}
		}
		// 更新联系人
		if (Func.isNotEmpty(enterpriseDTO.getContactsList())) {
			// 如果没有设置默认联系人，则将第一条联系人设置为默认
			int defaultSize = enterpriseDTO.getContactsList().stream().filter(contacts -> {
				return contacts.getDefaultFlag() == 1;
			}).collect(Collectors.toList()).size();
			if (defaultSize == 0) {
				enterpriseDTO.getContactsList().get(0).setDefaultFlag(1);
			}
			for (Contacts contacts : enterpriseDTO.getContactsList()) {
				Contacts find = contactsService.getById(contacts.getPcId());
				if (Func.isEmpty(find)) {
					contacts.setDataId(enterpriseDTO.getPeId());
					contacts.setContactsDataType(Enterprise.DATA_TYPE);
					contacts.setCreateUser(enterpriseDTO.getCreateUser());
				}
				contacts.setUpdateUser(enterpriseDTO.getUpdateUser());
				contactsService.saveOrUpdate(contacts);
			}
		}
		if (Func.isNotEmpty(enterpriseDTO.getContactsDeletedList())) {
			for (Contacts contacts : enterpriseDTO.getContactsDeletedList()) {
				contactsService.removeById(contacts.getPcId());
			}
		}

		return true;
	}

	@Override
	public boolean saveOrUpdate(EnterpriseDTO enterpriseDTO) {
		if (Func.isEmpty(enterpriseDTO.getPeId())) {
			return this.save(enterpriseDTO);
		} else {
			return this.updateById(enterpriseDTO);
		}
	}

	@Override
	public boolean removeByIds(String ids) {
		List<Long> list = Func.toLongList(ids);

		for (Long id : list) {
			// 获取对应的企业实体
			Enterprise enterprise = super.getById(id);
			if (Func.isEmpty(enterprise)) {
				continue;
			}
			// 验证该企业信息在物品中是否有关联
			SkuInc skuInc = new SkuInc();
			skuInc.setPeId(id);
			if (skuIncSerivce.count(Condition.getQueryWrapper(skuInc)) > 0) {
				throw new ServiceException("企业：" + enterprise.getEnterpriseName() + "已关联物品，请删除关联后再执行删除操作");
			}
			// 修改地址删除标记
			addressService.remove(id, Enterprise.DATA_TYPE, 0);
			// 修改联系人删除标记
			contactsService.remove(id, Enterprise.DATA_TYPE, 0);
		}
		boolean result = super.removeByIds(list);
		if (result) {

		}
		return result;
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		IContactsService contactsService = SpringUtil.getBean(IContactsService.class);
		// 查询出企业信息
		List<Enterprise> enterpriseList = this.list(Condition.getQueryWrapper(params, Enterprise.class));
		// 查询出与企业关联的联系地址、联系人
		List<Long> peIdList = NodesUtil.toList(enterpriseList, Enterprise::getPeId);
		//查询所有与企业相关的货主信息
		List<Long> woIdList = NodesUtil.toList(enterpriseList, Enterprise::getWoId);
		IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
	/*	List<Owner> ownerAll = ownerService.list().stream().filter(owner -> {
			return woIdList.contains(owner.getWoId());
		}).collect(Collectors.toList());*/
		List<Owner> ownerAll = ownerService.listByIds(woIdList);

		// 查询所有联系人和地址
		/*List<Address> addressAll = AddressCache.list().stream().filter(address -> {
			return peIdList.contains(address.getDataId())
				&& address.getAddressDataType().equals(Enterprise.DATA_TYPE);
		}).collect(Collectors.toList());*/

		IAddressService addressService = SpringUtil.getBean(IAddressService.class);
		List<Address> addressAll = addressService.list(Condition.getQueryWrapper(new Address())
			.lambda()
			.in(Address::getDataId, peIdList)
			.eq(Address::getAddressDataType, Enterprise.DATA_TYPE)
		);
		/*List<Contacts> contactAll = ContactsCache.list().stream().filter(contacts -> {
			return peIdList.contains(contacts.getDataId())
				&& contacts.getContactsDataType().equals(Enterprise.DATA_TYPE);
		}).collect(Collectors.toList());*/

		List<Contacts> contactAll = contactsService.list(Condition.getQueryWrapper(new Contacts())
			.lambda()
			.in(Contacts::getDataId, peIdList)
			.eq(Contacts::getContactsDataType, Enterprise.DATA_TYPE)
		);

		List<EnterpriseExcel> enterpriseExportList = new ArrayList<>();
		// 循环查询出来的企业列表
		for (Enterprise enterprise : enterpriseList) {
			// 查询当前企业的所有地址
			List<Address> addressList = addressAll.stream().filter(address -> {
				return address.getDataId().equals(enterprise.getPeId());
			}).collect(Collectors.toList());
			// 查询当前企业的所有联系人
			List<Contacts> contactsList = contactAll.stream().filter(contacts -> {
				return contacts.getDataId().equals(enterprise.getPeId());
			}).collect(Collectors.toList());

			// 查询当前企业的货主
			List<Owner> ownerList = ownerAll.stream().filter(owner -> {
				return owner.getWoId().equals(enterprise.getWoId());
			}).collect(Collectors.toList());
			// 计算最大循环次数，最少循环一次
			Integer maxLength = 1;
			if (Func.isNotEmpty(addressList) || Func.isNotEmpty(contactsList)) {
				maxLength = addressList.size() > contactsList.size() ? addressList.size() : contactsList.size();
			}

			for (int i = 0; i < maxLength; i++) {
				Address address = i < addressList.size() ? addressList.get(i) : null;
				Contacts contacts = i < contactsList.size() ? contactsList.get(i) : null;

				EnterpriseExcel enterpriseExport = BeanUtil.copy(enterprise, EnterpriseExcel.class);
				// 封装地址数据
				if (Func.isNotEmpty(address)) {
					enterpriseExport.setAddress(address.getAddress());
					String addressType = DictCache.getValue(DictConstant.ADDRESS_TYPE, address.getAddressType());
					if (Func.isNotEmpty(addressType)) {
						enterpriseExport.setAddressTypeDesc(addressType);
					}
					enterpriseExport.setLongitude(address.getLongitude());
					enterpriseExport.setLatitude(address.getLatitude());
					if (address.getDefaultFlag().equals(DefaultFlagEnum.TRUE.getIndex())) {
						enterpriseExport.setAddressDefaultFlag(DefaultFlagEnum.TRUE.getName());
					} else {
						enterpriseExport.setAddressDefaultFlag(DefaultFlagEnum.FALSE.getName());
					}
				}
				// 封装联系人数据
				if (Func.isNotEmpty(contacts)) {
					enterpriseExport.setContactsName(contacts.getContactsName());
					enterpriseExport.setContactsEmail(contacts.getContactsEmail());
					enterpriseExport.setContactsNumber(contacts.getContactsNumber());
					enterpriseExport.setContactsFax(contacts.getContactsFax());
					if (contacts.getDefaultFlag().equals(DefaultFlagEnum.TRUE.getIndex())) {
						enterpriseExport.setContactsDefaultFlag(DefaultFlagEnum.TRUE.getName());
					} else {
						enterpriseExport.setContactsDefaultFlag(DefaultFlagEnum.FALSE.getName());
					}
				}
				//封装货主
				if (Func.isNotEmpty(ownerList)) {
					enterpriseExport.setOwnerCode(ownerList.get(0).getOwnerCode());
					enterpriseExport.setOwnerName(ownerList.get(0).getOwnerName());
				}
				//企业类型
				if (Func.isNotEmpty(enterprise.getEnterpriseType())) {
					Integer[] enterpriseTypeKeyList = Func.toIntArray(enterprise.getEnterpriseType());
					if (Func.isNotEmpty(enterpriseTypeKeyList)) {
						List<String> enterpriseTypeList = new ArrayList<>();
						for (int j = 0; j < enterpriseTypeKeyList.length; j++) {
							enterpriseTypeList.add(DictCache.getValue(DictConstant.ENTERPRISE_TYPE, enterpriseTypeKeyList[j]));
						}
						enterpriseExport.setEnterpriseType(Func.join(enterpriseTypeList));
					}
				}
				//将企业装入新list
				enterpriseExportList.add(enterpriseExport);
			}
		}
		ExcelUtil.export(response, "来往企业", "来往企业数据表", enterpriseExportList, EnterpriseExcel.class);
	}

	@Override
	public List<DataVerify> validExcel(List<EnterpriseExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();

		Map<String, EnterpriseDTO> cache = new HashMap<>();
		int index = 1;
		for (EnterpriseExcel enterpriseExcel : dataList) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// 封装成DTO
			EnterpriseDTO enterpriseDTO = EnterpriseWrapper.build().entityDTO(enterpriseExcel);
			// 开始效验数据
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(enterpriseDTO, Group.class);

			// 企业类型
			if (ObjectUtil.isNotEmpty(enterpriseDTO.getEnterpriseType())) {
				Integer[] enterpriseTypeKey = Func.toIntArray(enterpriseDTO.getEnterpriseType());
				if (Func.isNotEmpty(enterpriseTypeKey)) {
					for (int i = 0; i < enterpriseTypeKey.length; i++) {
						System.out.println(enterpriseTypeKey[i]);
						if (Func.equals(enterpriseTypeKey[i], 0)) {
							validResult.addError("enterpriseType", "企业类型不正确");
						}
					}
				}
			}

			// 企业编码唯一性验证
			Enterprise findEnterprise = super.list(Condition.getQueryWrapper(new Enterprise())
				.lambda()
				.eq(Enterprise::getEnterpriseCode, enterpriseDTO.getEnterpriseCode()))
				.stream().findFirst().orElse(null);

			if (Func.isNotEmpty(findEnterprise)) {
				validResult.addError("enterpriseCode", "企业编码[" + enterpriseDTO.getEnterpriseCode() + "]已存在");
			}
			//查询货主是否存在
			//Owner owner = OwnerCache.getByCode(enterpriseExcel.getOwnerCode());
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.list(Condition.getQueryWrapper(new Owner())
				.lambda()
				.eq(Owner::getOwnerCode, enterpriseExcel.getOwnerCode())
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(owner)) {
				validResult.addError("enterpriseCode", "货主编码[" + enterpriseExcel.getOwnerCode() + "]不存在");
			} else {
				enterpriseDTO.setWoId(owner.getWoId());
			}
//			enterpriseDTO.setEnterpriseType(DictCache.getValue(DictConstant.ENTERPRISE_TYPE, Func.toInt(enterpriseExcel.getEnterpriseType())));
			if (Func.isNotEmpty(enterpriseDTO.getAddressList()) && !NodesUtil.isAllNull(enterpriseDTO.getAddressList().get(0))) {
				ValidationUtil.ValidResult validResult1 = ValidationUtil.validateBean(enterpriseDTO.getAddressList().get(0));
				validResult.getAllErrors().addAll(validResult1.getAllErrors());
			}
			if (Func.isNotEmpty(enterpriseDTO.getContactsList()) && !NodesUtil.isAllNull(enterpriseDTO.getContactsList().get(0))) {
				ValidationUtil.ValidResult validResult1 = ValidationUtil.validateBean(enterpriseDTO.getContactsList().get(0));
				validResult.getAllErrors().addAll(validResult1.getAllErrors());
			}
			if (validResult.hasErrors()) {
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			} else {
				if (cache.containsKey(enterpriseExcel.getEnterpriseCode())) {
					// 更新地址、联系人信息
					cache.get(enterpriseDTO.getEnterpriseCode()).getAddressList().addAll(enterpriseDTO.getAddressList());
					cache.get(enterpriseDTO.getEnterpriseCode()).getContactsList().addAll(enterpriseDTO.getContactsList());
				} else {
					// 记录到缓存map中
					cache.put(enterpriseDTO.getEnterpriseCode(), enterpriseDTO);
				}
				dataVerify.setCacheKey(enterpriseDTO.getEnterpriseCode());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// 存储数据到redis缓存中
			for (String code : cache.keySet()) {
				EnterpriseCache.put(code, cache.get(code));
			}
		}

		return dataVerifyList;
	}

	@Override
	public boolean importData(List<DataVerify> dataVerifyList) {
		if (Func.isEmpty(dataVerifyList)) {
			throw new ServiceException("没有数据可以保存！");
		}
		for (DataVerify dataVerify : dataVerifyList) {
			if (ObjectUtil.isEmpty(dataVerify)) {
				continue;
			}
			EnterpriseDTO enterpriseDTO = EnterpriseCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(enterpriseDTO)) {
				continue;
			}
			if (this.saveOrUpdate(enterpriseDTO)) {
				EnterpriseCache.remove(enterpriseDTO.getEnterpriseCode());
			}
		}
		return true;
	}


	private QueryWrapper<Enterprise> getQueryWrapper(Enterprise enterprise) {
		QueryWrapper<Enterprise> queryWrapper = new QueryWrapper<>();

		if (Func.isNotEmpty(enterprise.getEnterpriseCode())) {
			queryWrapper.lambda().like(Enterprise::getEnterpriseCode, enterprise.getEnterpriseCode());
		}
		if (Func.isNotEmpty(enterprise.getEnterpriseName())) {
			queryWrapper.lambda().like(Enterprise::getEnterpriseName, enterprise.getEnterpriseName());
		}
		if (Func.isNotEmpty(enterprise.getEnterpriseNameS())) {
			queryWrapper.lambda().like(Enterprise::getEnterpriseNameS, enterprise.getEnterpriseNameS());
		}
		if (Func.isNotEmpty(enterprise.getEnterpriseType())) {
			queryWrapper.lambda().like(Enterprise::getEnterpriseType, enterprise.getEnterpriseType());
		}
		if (Func.isNotEmpty(enterprise.getWoId())) {
			queryWrapper.lambda().eq(Enterprise::getWoId, enterprise.getWoId());
		}
		if (Func.isNotEmpty(enterprise.getCountry())) {
			queryWrapper.lambda().eq(Enterprise::getCountry, enterprise.getCountry());
		}
		if (Func.isNotEmpty(enterprise.getProvince())) {
			queryWrapper.lambda().eq(Enterprise::getProvince, enterprise.getProvince());
		}
		if (Func.isNotEmpty(enterprise.getCity())) {
			queryWrapper.lambda().eq(Enterprise::getCity, enterprise.getCity());
		}
		if (Func.isNotEmpty(enterprise.getZipCode())) {
			queryWrapper.lambda().like(Enterprise::getZipCode, enterprise.getZipCode());
		}

		return queryWrapper;
	}
}
