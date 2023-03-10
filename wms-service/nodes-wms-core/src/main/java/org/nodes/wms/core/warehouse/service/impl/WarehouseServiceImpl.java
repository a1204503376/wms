package org.nodes.wms.core.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.entity.Tenant;
import org.nodes.core.base.service.IDeptService;
import org.nodes.core.base.service.ITenantService;
import org.nodes.core.constant.TenantConstant;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.core.utils.TokenUtil;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.core.common.entity.Address;
import org.nodes.wms.core.common.entity.Contacts;
import org.nodes.wms.core.common.enums.DefaultFlagEnum;
import org.nodes.wms.core.common.service.IAddressService;
import org.nodes.wms.core.common.service.IContactsService;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.dto.WarehouseDTO;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.warehouse.entity.PlatformInfo;
import org.nodes.wms.core.warehouse.entity.WorkArea;
import org.nodes.wms.core.warehouse.enums.LocTypeEnum;
import org.nodes.wms.core.warehouse.enums.ZoneTypeEnum;
import org.nodes.wms.core.warehouse.enums.ZoneVirtualTypeEnum;
import org.nodes.wms.core.warehouse.excel.WarehouseExcel;
import org.nodes.wms.core.warehouse.mapper.WarehouseMapper;
import org.nodes.wms.core.warehouse.service.*;
import org.nodes.wms.core.warehouse.vo.WarehouseVO;
import org.nodes.wms.core.warehouse.wrapper.WarehouseWrapper;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ?????? ???????????????
 *
 * @author Wangjw
 * @since 2019-12-06
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class WarehouseServiceImpl<M extends WarehouseMapper, T extends Warehouse>
	extends BaseServiceImpl<WarehouseMapper, Warehouse>
	implements IWarehouseService {

	@Autowired
	IAddressService addressService;
	@Autowired
	IContactsService contactsService;
	@Autowired
	IZoneService zoneService;
	@Autowired
	ILocationService locationService;
	@Autowired
	IPlatformInfoService platformInfoService;
	@Autowired
	IWorkAreaService workAreaService;
	@Autowired
	WarehouseBiz warehouseBiz;

	@Override
	public boolean save(WarehouseDTO whDTO) {
		// ??????????????????
		warehouseBiz.valiAuthorization();

		IContactsService contactsService = SpringUtil.getBean(IContactsService.class);
		IAddressService addressService = SpringUtil.getBean(IAddressService.class);
		if (Func.isNotEmpty(super.getById(whDTO.getWhId()))) {
			throw new ServiceException("????????????????????????");
		}

		QueryWrapper<Warehouse> wqw = new QueryWrapper<>();
		//????????????????????????
		wqw.lambda().eq(Warehouse::getWhCode, whDTO.getWhCode());
		if (Func.isNotEmpty(super.getOne(wqw))) {
			throw new ServiceException("????????????[" + whDTO.getWhCode() + "]?????????! ");
		}
		wqw.clear();
		//????????????????????????
		wqw.lambda().eq(Warehouse::getWhName, whDTO.getWhName());
		if (Func.isNotEmpty(super.getOne(wqw))) {
			throw new ServiceException("????????????????????????");
		}
		if (this.save((Warehouse) whDTO)) {
			//WarehouseCache.saveOrUpdate(whDTO);
		}

		warehouseBiz.initZoneAndLocAfterNewWarehouse(whDTO);
		boolean saveInitZoneIsSucceed = super.updateById(whDTO);
		if (!saveInitZoneIsSucceed) {
			throw new ServiceException("????????????????????????????????????");
		}
		//endregion

		if (whDTO.getAddressList() != null && whDTO.getAddressList().size() > 0) {
			for (Address address : whDTO.getAddressList()) {
				if (Func.isNotEmpty(addressService.getById(address.getPaId()))) {
					throw new ServiceException("?????????????????????:paId");
				}
				address.setCreateDept(whDTO.getDeptId());
				address.setAddressDataType(Warehouse.DATA_TYPE);
				address.setDataId(whDTO.getWhId());
				address.setCreateUser(whDTO.getCreateUser());
				if (!addressService.saveOrUpdate(address)) {
					throw new ServiceException("???????????????????????????");
				}
			}
		}
		if (whDTO.getContactsList() != null && whDTO.getContactsList().size() > 0) {
			for (Contacts contacts : whDTO.getContactsList()) {
				if (Func.isNotEmpty(contactsService.getById(contacts.getPcId()))) {
					throw new ServiceException("????????????????????????:pcId");
				}
				contacts.setCreateDept(whDTO.getDeptId());
				contacts.setContactsDataType(Warehouse.DATA_TYPE);
				contacts.setDataId(whDTO.getWhId());
				contacts.setCreateUser(whDTO.getCreateUser());
				if (!contactsService.saveOrUpdate(contacts)) {
					throw new ServiceException("????????????????????????");
				}
			}
		}
		return true;
	}

	@Override
	public boolean save(Warehouse warehouse) {
		ITenantService tenantService = SpringUtil.getBean(ITenantService.class);
		Tenant tenant = tenantService.getOne(Condition.getQueryWrapper(new Tenant())
			.lambda()
			.eq(Tenant::getTenantId, AuthUtil.getTenantId()));
		if (tenant == null) {
			throw new ServiceException(TokenUtil.USER_HAS_NO_TENANT);
		}
		if (!StringUtil.equalsIgnoreCase(tenant.getTenantId(), BladeConstant.ADMIN_TENANT_ID)) {
			String licenseKey = tenant.getLicenseKey();
			String decrypt = DesUtil.decryptFormHex(licenseKey, TenantConstant.DES_KEY);
			if (Func.isNotEmpty(decrypt) && JsonUtil.parse(decrypt, Tenant.class).getWhMax() <= WarehouseCache.size()) {
				throw new ServiceException(TokenUtil.WAREHOUSE_MAX_ERROR);
			}
		}

		return super.save(warehouse);
	}

	@Override
	public boolean updateById(WarehouseDTO whDTO) {
		if (Func.isEmpty(super.getById(whDTO.getWhId()))) {
			throw new ServiceException("?????????????????????:whId");
		}

		String whCode = whDTO.getWhCode();
		Warehouse warehouse = new Warehouse();
		warehouse.setWhCode(whCode);
		if (super.count(Condition.getQueryWrapper(warehouse).lambda().ne(Warehouse::getWhId, whDTO.getWhId())) > 0) {
			throw new ServiceException(String.format("????????????%s???????????????????????????????????????", whCode));
		}

		//???????????????????????????
		//??????
		if (Func.isNotEmpty(whDTO.getStage())) {
			updateLocation(whDTO, whDTO.getStage(), whDTO.getWhCode() + "-" + ZoneVirtualTypeEnum.STAGE);
		}
		//??????
		if (Func.isNotEmpty(whDTO.getPick())) {
			updateLocation(whDTO, whDTO.getPick(), whDTO.getWhCode() + "-" + ZoneVirtualTypeEnum.PICK);
		}
//		//??????
//		if (Func.isNotEmpty(whDTO.getPack())) {
//			updateLocation(whDTO, whDTO.getPack(), ZoneVirtualTypeEnum.PACK + whDTO.getWhCode());
//		}
//		//??????
//		if (Func.isNotEmpty(whDTO.getMove())) {
//			updateLocation(whDTO, whDTO.getMove(), ZoneVirtualTypeEnum.MOVE + whDTO.getWhCode());
//		}

		if (super.updateById(whDTO)) {
			//WarehouseCache.saveOrUpdate(whDTO);
		}
		if (Func.isNotEmpty(whDTO.getAddressDeletedList())) {
			for (Address address : whDTO.getAddressDeletedList()) {
				addressService.removeById(address.getPaId());
			}
		}
		// ????????????
		if (Func.isNotEmpty(whDTO.getAddressList())) {
			for (Address address : whDTO.getAddressList()) {
				//??????????????????????????????????????????
				address.setCreateDept(whDTO.getDeptId());
				if (address.getDefaultFlag() == 1) {
					//?????????????????????,?????????????????????????????????
					UpdateWrapper<Address> auw = new UpdateWrapper<>();
					//update???????????? ??????ID,??????,??????????????????
					auw.lambda().set(Address::getDefaultFlag, 0)
						.eq(Address::getDefaultFlag, 1)
						.eq(Address::getAddressDataType, Warehouse.DATA_TYPE)
						.eq(Address::getDataId, whDTO.getWhId());
					addressService.update(auw);
					address.setDataId(whDTO.getWhId());
					address.setAddressDataType(Warehouse.DATA_TYPE);
					addressService.saveOrUpdate(address);
				}
				address.setDataId(whDTO.getWhId());
				address.setAddressDataType(Warehouse.DATA_TYPE);
				addressService.saveOrUpdate(address);

			}
		}
		if (Func.isNotEmpty(whDTO.getContactsDeletedList())) {
			for (Contacts contacts : whDTO.getContactsDeletedList()) {
				contactsService.removeById(contacts.getPcId());
			}
		}
		// ???????????????
		if (Func.isNotEmpty(whDTO.getContactsList())) {
			for (Contacts contacts : whDTO.getContactsList()) {
				//?????????????????????????????????????????????
				contacts.setCreateDept(whDTO.getDeptId());
				if (contacts.getDefaultFlag() == 1) {
					//?????????????????????,?????????????????????????????????
					UpdateWrapper<Contacts> cuw = new UpdateWrapper<>();
					//update???????????? ??????ID,??????,?????????????????????
					cuw.lambda().set(Contacts::getDefaultFlag, 0)
						.eq(Contacts::getDefaultFlag, 1)
						.eq(Contacts::getContactsDataType, Warehouse.DATA_TYPE)
						.eq(Contacts::getDataId, whDTO.getWhId());
					contactsService.update(cuw);
					contacts.setDataId(whDTO.getWhId());
					contacts.setContactsDataType(Warehouse.DATA_TYPE);
					contactsService.saveOrUpdate(contacts);
				}
				contacts.setDataId(whDTO.getWhId());
				contacts.setContactsDataType(Warehouse.DATA_TYPE);
				contactsService.saveOrUpdate(contacts);
			}
		}

		return true;
	}

	@Override
	public boolean saveOrUpdate(WarehouseDTO warehouseDTO) {
		warehouseDTO.setCreateDept(warehouseDTO.getDeptId());
		if (Func.isNotEmpty(warehouseDTO.getWhId())) {
			return this.updateById(warehouseDTO);
		} else {
			return this.save(warehouseDTO);
		}
	}

	@Override
	public boolean removeByIds(Collection<? extends Serializable> idList) {
		for (Serializable whId : idList) {
			// ??????????????????????????????????????????????????????
			PlatformInfo platformInfo = new PlatformInfo();
			platformInfo.setWhId((Long) whId);
			if (platformInfoService.count(Condition.getQueryWrapper(platformInfo)) > 0) {
				throw new ServiceException("??????????????????????????????????????????????????????");
			}

			// ?????????????????????????????????????????????????????????
			WorkArea workArea = new WorkArea();
			workArea.setWhId((Long) whId);
			if (workAreaService.count(Condition.getQueryWrapper(workArea)) > 0) {
				throw new ServiceException("?????????????????????????????????????????????????????????");
			}

			// ??????????????????????????????????????????????????????????????????
			/*List<Zone> lstZoneNotIn = ZoneCache.list().stream().filter(zone -> {
				return zone.getWhId().equals(whId) && !ZoneVirtualTypeEnum.isVirtual(zone.getZoneCode());
			}).collect(Collectors.toList());*/
			List<Integer> zoneType = new ArrayList<>();
			zoneType.add(70);
			zoneType.add(72);
			zoneType.add(73);
			zoneType.add(74);
			IZoneService zoneService = SpringUtil.getBean(IZoneService.class);
			List<Zone> lstZoneNotIn = zoneService.list(Condition.getQueryWrapper(new Zone())
				.lambda()
				.eq(Zone::getWhId, whId)
				.notIn(Zone::getZoneType, zoneType)
			);
			if (Func.isNotEmpty(lstZoneNotIn)) {
				throw new ServiceException("?????????????????????????????????????????????????????????");
			}

			Location loc = new Location();
			loc.setWhId((Long) whId);

			/*List<Zone> lstZoneIn = ZoneCache.list().stream().filter(zone -> {
				return zone.getWhId().equals(whId) && ZoneVirtualTypeEnum.isVirtual(zone.getZoneCode());
			}).collect(Collectors.toList());*/
			List<Zone> lstZoneIn = zoneService.list(Condition.getQueryWrapper(new Zone())
				.lambda()
				.eq(Zone::getWhId, whId)
				.in(Zone::getZoneType, zoneType)
			);

			List<Long> zoneIdList = NodesUtil.toList(lstZoneIn, Zone::getZoneId);
			if (Func.isNotEmpty(zoneIdList)) {
				zoneService.removeByIds(zoneIdList);    //??????4?????????????????????????????????
			}

			//List<Location> lstLocIn = LocationCache.listByWhId((Long)whId);
			ILocationService locationService = SpringUtil.getBean(ILocationService.class);
			List<Location> lstLocIn = locationService.list(Condition.getQueryWrapper(new Location())
				.lambda()
				.eq(Location::getWhId, (long) whId)
			).stream().collect(Collectors.toList());
			if (Func.isNotEmpty(lstLocIn)) {
				locationService.removeByIds(NodesUtil.toList(lstLocIn, Location::getLocId));
			}

			// ????????????
			addressService.remove((Long) whId, Warehouse.DATA_TYPE, 0);
			// ???????????????
			contactsService.remove((Long) whId, Warehouse.DATA_TYPE, 0);
		}

		boolean result = super.removeByIds(idList);
		if (result) {
			//WarehouseCache.removeByIds(idList);
		}
		return result;
	}

	/**
	 * @param warehouse ??????
	 * @param zoneType  ????????????
	 */
	private long generateZoneAndLocation(Warehouse warehouse, ZoneVirtualTypeEnum zoneType) {
		long zoneId = 0l;
		Zone zone = new Zone();
		zone.setWhId(warehouse.getWhId());
		zone.setZoneCode(String.format("%s-%s", warehouse.getWhCode(), zoneType.toString()));
		zone.setZoneName(zoneType.getName());
		zone.setZoneType(zoneType.getIndex());
		zone.setCreateDept(warehouse.getDeptId());
		boolean saveZoneIsSucceed = zoneService.save(zone);
		if (!saveZoneIsSucceed) {
			throw new ServiceException(MessageFormat.format("?????????{0}???????????????", zoneType.getName()));
		}
		zoneId = zone.getZoneId();

		Location loc = new Location();
		loc.setWhId(warehouse.getWhId());
		loc.setZoneId(zoneId);
		loc.setLocCode(String.format("%s-%s", warehouse.getWhCode(), zoneType.toString()));
		loc.setLocType(LocTypeEnum.Virtual.getIndex());
		loc.setCreateDept(warehouse.getDeptId());
		boolean saveLocIsSucceed = locationService.save(loc);
		if (!saveLocIsSucceed) {
			throw new ServiceException(
				MessageFormat.format("?????????{0}??????{1}???????????????", zoneType.getName(), loc.getLocCode()));
		}
		return zoneId;
	}

	public void updateLocation(Warehouse warehouse, long zoneId, String locCode) {
		//Location location = LocationCache.listByZoneId(zoneId).stream().findFirst().orElse(null);
		ILocationService locationService = SpringUtil.getBean(ILocationService.class);
		Location location = locationService.list(Condition.getQueryWrapper(new Location())
			.lambda()
			.eq(Location::getZoneId, zoneId)
		).stream().findFirst().orElse(null);
		if (Func.isNotEmpty(location)) {
			location.setLocCode(locCode);
			location.setCreateDept(warehouse.getDeptId());
			locationService.updateById(location);
		}
	}


	@Override
	public List<WarehouseVO> selectList(Warehouse warehouse) {
		return WarehouseWrapper.build().listVO(super.list(this.getSelectQueryWrapper(warehouse)));
	}

	@Override
	public IPage<WarehouseVO> selectPage(IPage<Warehouse> page, Warehouse warehouse) {
		IPage<Warehouse> pages = super.page(page, this.getSelectQueryWrapper(warehouse));
		return WarehouseWrapper.build().pageVO(pages);
	}

	protected List<WarehouseExcel> getWarehouseExportDTOList(List<WarehouseVO> warehouseList) {
		// ????????????????????????????????? ?????????????????????????????????????????????????????????
		List<WarehouseExcel> warehouseExportList = new ArrayList<>();

		List<Long> whIdList = NodesUtil.toList(warehouseList, Warehouse::getWhId);
		// ??????????????????????????????
		/*List<Address> addressAll = AddressCache.list().stream().filter(address -> {
			return whIdList.contains(address.getDataId())
				&& address.getAddressDataType().equals(Warehouse.DATA_TYPE);
		}).collect(Collectors.toList());*/

		IAddressService addressService = SpringUtil.getBean(IAddressService.class);
		List<Address> addressAll = addressService.list(Condition.getQueryWrapper(new Address())
			.lambda()
			.in(Address::getDataId, whIdList)
			.eq(Address::getAddressDataType, Warehouse.DATA_TYPE)
		);
		/*List<Contacts> contactAll = ContactsCache.list().stream().filter(contacts -> {
			return whIdList.contains(contacts.getDataId())
				&& contacts.getContactsDataType().equals(Warehouse.DATA_TYPE);
		}).collect(Collectors.toList());*/
		IContactsService contactsService = SpringUtil.getBean(IContactsService.class);
		List<Contacts> contactAll = contactsService.list(Condition.getQueryWrapper(new Contacts())
			.lambda()
			.in(Contacts::getDataId, whIdList)
			.eq(Contacts::getContactsDataType, Warehouse.DATA_TYPE)
		);

		// ?????????????????????????????????
		for (Warehouse warehouse : warehouseList) {
			// ?????????????????????????????????
			List<Address> addressList = addressAll.stream().filter(address -> {
				return address.getDataId().equals(warehouse.getWhId());
			}).collect(Collectors.toList());
			// ????????????????????????????????????
			List<Contacts> contactsList = contactAll.stream().filter(contacts -> {
				return contacts.getDataId().equals(warehouse.getWhId());
			}).collect(Collectors.toList());
			// ?????????????????????????????????????????????
			Integer maxLength = 1;
			if (Func.isNotEmpty(addressList) || Func.isNotEmpty(contactsList)) {
				maxLength = addressList.size() > contactsList.size() ? addressList.size() : contactsList.size();
			}

			for (int i = 0; i < maxLength; i++) {
				Address address = i < addressList.size() ? addressList.get(i) : null;
				Contacts contacts = i < contactsList.size() ? contactsList.get(i) : null;

				WarehouseExcel warehouseExportDTO = WarehouseWrapper.build().warehouseToExportDTO(warehouse);
				// ??????????????????
				if (Func.isNotEmpty(address)) {
					warehouseExportDTO.setAddress(address.getAddress());
					String addressType = DictCache.getValue("address_type", address.getAddressType());
					if (Func.isNotEmpty(addressType)) {
						warehouseExportDTO.setAddressTypeDesc(addressType);
					}
					warehouseExportDTO.setLongitude(address.getLongitude());
					warehouseExportDTO.setLatitude(address.getLatitude());
					if (address.getDefaultFlag().equals(DefaultFlagEnum.TRUE.getIndex())) {
						warehouseExportDTO.setAddressDefaultFlag(DefaultFlagEnum.TRUE.getName());
					} else {
						warehouseExportDTO.setAddressDefaultFlag(DefaultFlagEnum.FALSE.getName());
					}
				}
				// ?????????????????????
				if (Func.isNotEmpty(contacts)) {
					warehouseExportDTO.setContactsName(contacts.getContactsName());
					warehouseExportDTO.setContactsEmail(contacts.getContactsEmail());
					warehouseExportDTO.setContactsNumber(contacts.getContactsNumber());
					warehouseExportDTO.setContactsFax(contacts.getContactsFax());
					if (contacts.getDefaultFlag().equals(DefaultFlagEnum.TRUE.getIndex())) {
						warehouseExportDTO.setContactsDefaultFlag(DefaultFlagEnum.TRUE.getName());
					} else {
						warehouseExportDTO.setContactsDefaultFlag(DefaultFlagEnum.FALSE.getName());
					}
				}
				//??????????????????list
				warehouseExportList.add(warehouseExportDTO);
			}
		}
		return warehouseExportList;
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		List<WarehouseVO> warehouseList = WarehouseWrapper.build().listVO(this.list(Condition.getQueryWrapper(params, Warehouse.class)));
		if (Func.isNotEmpty(warehouseList)) {
			List<WarehouseExcel> warehouseExportList = this.getWarehouseExportDTOList(warehouseList);
			ExcelUtil.export(response, "??????", "???????????????", warehouseExportList, WarehouseExcel.class);
		}
	}

	@Override
	public List<DataVerify> validExcel(List<WarehouseExcel> dataList) {
		IDeptService deptService = SpringUtil.getBean(IDeptService.class);
		List<DataVerify> dataVerifyList = new ArrayList<>();

		Map<String, WarehouseDTO> cache = new HashMap<>();
		int index = 1;
		for (WarehouseExcel warehouseExcel : dataList) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// ?????????DTO
			WarehouseDTO warehouseDTO = WarehouseWrapper.build().entityDTO(warehouseExcel);
			//??????????????????????????????

			//Dept dept= DeptCache.getByCode(warehouseExcel.getDeptCode());
			Dept dept = deptService.list(Condition.getQueryWrapper(new Dept())
				.lambda()
				.eq(Dept::getDeptCode, warehouseExcel.getDeptCode())
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(dept)) {
				throw new ServiceException("????????????[" + warehouseExcel.getDeptCode() + "]?????????");
			}
			warehouseDTO.setDeptId(dept.getId());
			// ??????????????????
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(warehouseDTO);
			// ???????????????????????????
			//Warehouse findWarehouse = WarehouseCache.getByCode(warehouseDTO.getWhCode());
			IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
			Warehouse findWarehouse = warehouseService.list(Condition.getQueryWrapper(new Warehouse())
				.lambda()
				.eq(Warehouse::getWhCode, warehouseDTO.getWhCode())
			).stream().findFirst().orElse(null);
			if (Func.isNotEmpty(findWarehouse)) {
				validResult.addError("whCode", "????????????[" + warehouseDTO.getWhCode() + "]?????????");
			}
			if (validResult.hasErrors()) {
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			} else {
				if (cache.containsKey(warehouseExcel.getWhCode())) {
					// ??????????????????????????????
					cache.get(warehouseDTO.getWhCode()).getAddressList().addAll(warehouseDTO.getAddressList());
					cache.get(warehouseDTO.getWhCode()).getContactsList().addAll(warehouseDTO.getContactsList());
				} else {
					// ???????????????map???
					cache.put(warehouseDTO.getWhCode(), warehouseDTO);
				}
				dataVerify.setCacheKey(warehouseDTO.getWhCode());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// ???????????????redis?????????
			for (String code : cache.keySet()) {
				WarehouseCache.put(code, cache.get(code));
			}
		}

		return dataVerifyList;
	}

	@Override
	public boolean importData(List<DataVerify> dataVerifyList) {
		if (Func.isEmpty(dataVerifyList)) {
			throw new ServiceException("???????????????????????????");
		}
		for (DataVerify dataVerify : dataVerifyList) {
			if (ObjectUtil.isEmpty(dataVerify)) {
				continue;
			}
			WarehouseDTO warehouseDTO = WarehouseCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(warehouseDTO)) {
				continue;
			}
			if (this.saveOrUpdate(warehouseDTO)) {
				WarehouseCache.remove(warehouseDTO.getWhCode());
				//	WarehouseCache.saveOrUpdate(warehouseDTO);
			}
		}
		return true;
	}

	/**
	 * ???????????????????????????
	 *
	 * @param warehouse ????????????
	 * @return ???????????????
	 */
	private QueryWrapper<Warehouse> getSelectQueryWrapper(Warehouse warehouse) {
		QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
		if (Func.isNotEmpty(warehouse.getWhCode())) {
			queryWrapper.lambda().like(Warehouse::getWhCode, warehouse.getWhCode());
		}
		if (Func.isNotEmpty(warehouse.getWhName())) {
			queryWrapper.lambda().like(Warehouse::getWhName, warehouse.getWhName());
		}
		if (Func.isNotEmpty(warehouse.getCountry())) {
			queryWrapper.lambda().eq(Warehouse::getCountry, warehouse.getCountry());
		}
		if (Func.isNotEmpty(warehouse.getProvince())) {
			queryWrapper.lambda().eq(Warehouse::getProvince, warehouse.getProvince());
		}
		if (Func.isNotEmpty(warehouse.getCity())) {
			queryWrapper.lambda().eq(Warehouse::getCity, warehouse.getCity());
		}
		return queryWrapper;
	}

	@Override
	public boolean uploadSave(List<DataVerify> list, Long userId) {
		if (Func.isEmpty(list)) {
			throw new ServiceException("???????????????????????????");
		}
		for (DataVerify dataVerify : list) {
			if (Func.isEmpty(dataVerify)) {
				continue;
			}
			WarehouseDTO warehouseDTO = WarehouseCache.getDTO(dataVerify.getCacheKey());
			if (Func.isEmpty(warehouseDTO)) {
				continue;
			}
			warehouseDTO.setCreateUser(userId);
			warehouseDTO.setUpdateUser(userId);

			if (this.save(warehouseDTO)) {
				WarehouseCache.removeDTO(warehouseDTO.getWhCode());
				//WarehouseCache.saveOrUpdate(warehouseDTO);
			}
		}

		return true;
	}

	@Override
	public boolean removeByIds(List<Long> ids) {
		/*if(super.removeByIds(ids)){
			//??????????????????????????????
			boolean deleteZone = zoneService.remove(Condition.getQueryWrapper(new Zone())
			.lambda()
			.in(Zone::getWhId,ids));
			//??????????????????
			boolean deleteLocation = locationService.remove(Condition.getQueryWrapper(new Location())
				.lambda()
				.in(Location::getWhId,ids));
		}*/

		for (long id : ids) {
			List<Zone> zoneList = zoneService.list(Condition.getQueryWrapper(new Zone())
				.lambda()
				.eq(Zone::getWhId, id)
				.ne(Zone::getZoneType, ZoneTypeEnum.VIRTUAL.getIndex()));

			List<Location> locationList = locationService.list(Condition.getQueryWrapper(new Location())
				.lambda()
				.eq(Location::getWhId, ids)
				.ne(Location::getLocType, LocTypeEnum.Virtual.getIndex()));

			if (Func.isEmpty(zoneList) && Func.isEmpty(locationList)) {
				super.removeById(id);
				zoneService.remove(Condition.getQueryWrapper(new Zone())
					.lambda()
					.eq(Zone::getWhId, id));
				locationService.remove(Condition.getQueryWrapper(new Location())
					.lambda()
					.eq(Location::getWhId, id));
			} else {
				Warehouse warehouse = super.getById(id);
				throw new ServiceException(String.format("[?????????%s] ???????????????????????????????????????????????????", warehouse.getWhName()));
			}
		}
		return true;
	}
}
