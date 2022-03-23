package org.nodes.wms.core.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.entity.Tenant;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.service.IDeptService;
import org.nodes.core.base.service.IDictService;
import org.nodes.core.base.service.ITenantService;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.constant.TenantConstant;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.core.utils.TokenUtil;
import org.nodes.wms.core.common.cache.AddressCache;
import org.nodes.wms.core.common.cache.ContactsCache;
import org.nodes.wms.core.common.entity.Address;
import org.nodes.wms.core.common.entity.Contacts;
import org.nodes.wms.core.common.enums.DefaultFlagEnum;
import org.nodes.wms.core.common.service.IAddressService;
import org.nodes.wms.core.common.service.IContactsService;
import org.nodes.wms.core.warehouse.cache.LocationCache;
import org.nodes.wms.core.warehouse.cache.WarehouseCache;
import org.nodes.wms.core.warehouse.cache.ZoneCache;
import org.nodes.wms.core.warehouse.dto.WarehouseDTO;
import org.nodes.wms.core.warehouse.entity.*;
import org.nodes.wms.core.warehouse.enums.LocTypeEnum;
import org.nodes.wms.core.warehouse.enums.ZoneTypeEnum;
import org.nodes.wms.core.warehouse.enums.ZoneVirtualTypeEnum;
import org.nodes.wms.core.warehouse.excel.WarehouseExcel;
import org.nodes.wms.core.warehouse.mapper.WarehouseMapper;
import org.nodes.wms.core.warehouse.service.*;
import org.nodes.wms.core.warehouse.vo.WarehouseVO;
import org.nodes.wms.core.warehouse.wrapper.WarehouseWrapper;
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
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 库房 服务实现类
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


	@Override
	public boolean save(WarehouseDTO whDTO) {

		IContactsService contactsService = SpringUtil.getBean(IContactsService.class);
		IAddressService addressService = SpringUtil.getBean(IAddressService.class);
		if (Func.isNotEmpty(super.getById(whDTO.getWhId()))) {
			throw new ServiceException("库房主键已存在！");
		}

		QueryWrapper<Warehouse> wqw = new QueryWrapper<>();
		//库房编号查询条件
		wqw.lambda().eq(Warehouse::getWhCode, whDTO.getWhCode());
		if (Func.isNotEmpty(super.getOne(wqw))) {
			throw new ServiceException("库房编码[" + whDTO.getWhCode() + "]已存在! ");
		}
		wqw.clear();
		//库房编号查询条件
		wqw.lambda().eq(Warehouse::getWhName, whDTO.getWhName());
		if (Func.isNotEmpty(super.getOne(wqw))) {
			throw new ServiceException("库房名称已存在！");
		}
		if (this.save((Warehouse) whDTO)) {
			//WarehouseCache.saveOrUpdate(whDTO);
		}
		//region 新增库房时自动生成入库暂存区stage、出库暂存区pick、包装暂存区pack、移动暂存区move
		//1.生成入库暂存区
		Long stageZoneId = this.generateZoneAndLocation(whDTO, ZoneVirtualTypeEnum.Stage);

		//2.生成出库暂存区
		Long pickZoneId = this.generateZoneAndLocation(whDTO, ZoneVirtualTypeEnum.Pick);

		//3.生成包装暂存区
		Long packZoneId = this.generateZoneAndLocation(whDTO, ZoneVirtualTypeEnum.Pack);

		//4.生成移动暂存区
		Long moveZoneId = this.generateZoneAndLocation(whDTO, ZoneVirtualTypeEnum.Move);

		//5.更新库房初时暂存区
		whDTO.setStage(stageZoneId);
		whDTO.setPick(pickZoneId);
		whDTO.setPack(packZoneId);
		whDTO.setMove(moveZoneId);
		boolean saveInitZoneIsSucceed = super.updateById(whDTO);
		if (!saveInitZoneIsSucceed) {
			throw new ServiceException("库房初始暂存区关联失败！");
		}
		//endregion

		if (whDTO.getAddressList() != null && whDTO.getAddressList().size() > 0) {
			for (Address address : whDTO.getAddressList()) {
				if (Func.isNotEmpty(addressService.getById(address.getPaId()))) {
					throw new ServiceException("地址主键已存在:paId");
				}
				address.setCreateDept(whDTO.getDeptId());
				address.setAddressDataType(Warehouse.DATA_TYPE);
				address.setDataId(whDTO.getWhId());
				address.setCreateUser(whDTO.getCreateUser());
				if (!addressService.saveOrUpdate(address)) {
					throw new ServiceException("创建地址信息失败！");
				}
			}
		}
		if (whDTO.getContactsList() != null && whDTO.getContactsList().size() > 0) {
			for (Contacts contacts : whDTO.getContactsList()) {
				if (Func.isNotEmpty(contactsService.getById(contacts.getPcId()))) {
					throw new ServiceException("联系人主键已存在:pcId");
				}
				contacts.setCreateDept(whDTO.getDeptId());
				contacts.setContactsDataType(Warehouse.DATA_TYPE);
				contacts.setDataId(whDTO.getWhId());
				contacts.setCreateUser(whDTO.getCreateUser());
				if (!contactsService.saveOrUpdate(contacts)) {
					throw new ServiceException("创建联系人失败！");
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
			throw new ServiceException("库房主键不存在:whId");
		}

		String whCode = whDTO.getWhCode();
		Warehouse warehouse = new Warehouse();
		warehouse.setWhCode(whCode);
		if (super.count(Condition.getQueryWrapper(warehouse).lambda().ne(Warehouse::getWhId, whDTO.getWhId())) > 0) {
			throw new ServiceException(String.format("库房编码%s已存在，请更换编码后重试！", whCode));
		}

		//更新暂存区库位编码
		//入库
		if (Func.isNotEmpty(whDTO.getStage())) {
			updateLocation(whDTO, whDTO.getStage(), ZoneVirtualTypeEnum.Stage + whDTO.getWhCode());
		}
		//出库
		if (Func.isNotEmpty(whDTO.getPick())) {
			updateLocation(whDTO, whDTO.getPick(), ZoneVirtualTypeEnum.Pick + whDTO.getWhCode());
		}
		//包装
		if (Func.isNotEmpty(whDTO.getPack())) {
			updateLocation(whDTO, whDTO.getPack(), ZoneVirtualTypeEnum.Pack + whDTO.getWhCode());
		}
		//移动
		if (Func.isNotEmpty(whDTO.getMove())) {
			updateLocation(whDTO, whDTO.getMove(), ZoneVirtualTypeEnum.Move + whDTO.getWhCode());
		}

		if (super.updateById(whDTO)) {
			//WarehouseCache.saveOrUpdate(whDTO);
		}
		if (Func.isNotEmpty(whDTO.getAddressDeletedList())) {
			for (Address address : whDTO.getAddressDeletedList()) {
				addressService.removeById(address.getPaId());
			}
		}
		// 更新地址
		if (Func.isNotEmpty(whDTO.getAddressList())) {
			for (Address address : whDTO.getAddressList()) {
				//判断参数中是否设置了默认地址
				address.setCreateDept(whDTO.getDeptId());
				if (address.getDefaultFlag() == 1) {
					//有默认的情况下,把原本的默认改为非默认
					UpdateWrapper<Address> auw = new UpdateWrapper<>();
					//update查询条件 库房ID,默认,地址数据类型
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
		// 更新联系人
		if (Func.isNotEmpty(whDTO.getContactsList())) {
			for (Contacts contacts : whDTO.getContactsList()) {
				//判断参数中是否设置了默认联系人
				contacts.setCreateDept(whDTO.getDeptId());
				if (contacts.getDefaultFlag() == 1) {
					//有默认的情况下,把原本的默认改为非默认
					UpdateWrapper<Contacts> cuw = new UpdateWrapper<>();
					//update查询条件 库房ID,默认,联系人数据类型
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
			// 验证该库房信息是否存在关联的月台信息
			PlatformInfo platformInfo = new PlatformInfo();
			platformInfo.setWhId((Long) whId);
			if (platformInfoService.count(Condition.getQueryWrapper(platformInfo)) > 0) {
				throw new ServiceException("该库房还有关联的月台信息，不可删除！");
			}

			// 验证该库房信息是否存在关联的工作区信息
			WorkArea workArea = new WorkArea();
			workArea.setWhId((Long) whId);
			if (workAreaService.count(Condition.getQueryWrapper(workArea)) > 0) {
				throw new ServiceException("该库房还有关联的工作区信息，不可删除！");
			}

			// 验证该库房信息是否存在暂存库区以外的库区信息
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
				throw new ServiceException("该库房还有非系统设定的库区，不可删除！");
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
				zoneService.removeByIds(zoneIdList);    //删除4个系统生成的虚拟暂存区
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

			// 删除地址
			addressService.remove((Long) whId, Warehouse.DATA_TYPE, 0);
			// 删除联系人
			contactsService.remove((Long) whId, Warehouse.DATA_TYPE, 0);
		}

		boolean result = super.removeByIds(idList);
		if (result) {
			//WarehouseCache.removeByIds(idList);
		}
		return result;
	}

	/**
	 * @param warehouse 库房
	 * @param zoneType  库区类型
	 */
	private long generateZoneAndLocation(Warehouse warehouse, ZoneVirtualTypeEnum zoneType) {
		long zoneId = 0l;
		Zone zone = new Zone();
		zone.setWhId(warehouse.getWhId());
		zone.setZoneCode(zoneType.toString());
		zone.setZoneName(zoneType.getName());
		zone.setZoneType(zoneType.getIndex());
		zone.setCreateDept(warehouse.getDeptId());
		boolean saveZoneIsSucceed = zoneService.save(zone);
		if (!saveZoneIsSucceed) {
			throw new ServiceException(MessageFormat.format("初始化{0}生成失败！", zoneType.getName()));
		}
		zoneId = zone.getZoneId();

		Location loc = new Location();
		loc.setWhId(warehouse.getWhId());
		loc.setZoneId(zoneId);
		loc.setLocCode(zoneType.toString() + warehouse.getWhCode());
		loc.setLocType(LocTypeEnum.Virtual.getIndex());
		loc.setCreateDept(warehouse.getDeptId());
		boolean saveLocIsSucceed = locationService.save(loc);
		if (!saveLocIsSucceed) {
			throw new ServiceException(
				MessageFormat.format("初始化{0}库位{1}生成失败！", zoneType.getName(), loc.getLocCode()));
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
		// 用来导出的最终仓库列表 其中包含重复的仓库信息以及地址、联系人
		List<WarehouseExcel> warehouseExportList = new ArrayList<>();

		List<Long> whIdList = NodesUtil.toList(warehouseList, Warehouse::getWhId);
		// 查询所有联系人和地址
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

		// 循环查询出来的仓库列表
		for (Warehouse warehouse : warehouseList) {
			// 查询当前企业的所有地址
			List<Address> addressList = addressAll.stream().filter(address -> {
				return address.getDataId().equals(warehouse.getWhId());
			}).collect(Collectors.toList());
			// 查询当前企业的所有联系人
			List<Contacts> contactsList = contactAll.stream().filter(contacts -> {
				return contacts.getDataId().equals(warehouse.getWhId());
			}).collect(Collectors.toList());
			// 计算最大循环次数，最少循环一次
			Integer maxLength = 1;
			if (Func.isNotEmpty(addressList) || Func.isNotEmpty(contactsList)) {
				maxLength = addressList.size() > contactsList.size() ? addressList.size() : contactsList.size();
			}

			for (int i = 0; i < maxLength; i++) {
				Address address = i < addressList.size() ? addressList.get(i) : null;
				Contacts contacts = i < contactsList.size() ? contactsList.get(i) : null;

				WarehouseExcel warehouseExportDTO = WarehouseWrapper.build().warehouseToExportDTO(warehouse);
				// 封装地址数据
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
				// 封装联系人数据
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
				//将仓库装入新list
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
			ExcelUtil.export(response, "库房", "库房数据表", warehouseExportList, WarehouseExcel.class);
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
			// 封装成DTO
			WarehouseDTO warehouseDTO = WarehouseWrapper.build().entityDTO(warehouseExcel);
			//查询所属机构是否存在

			//Dept dept= DeptCache.getByCode(warehouseExcel.getDeptCode());
			Dept dept = deptService.list(Condition.getQueryWrapper(new Dept())
				.lambda()
				.eq(Dept::getDeptCode, warehouseExcel.getDeptCode())
			).stream().findFirst().orElse(null);
			if (Func.isEmpty(dept)) {
				throw new ServiceException("机构编码[" + warehouseExcel.getDeptCode() + "]不存在");
			}
			warehouseDTO.setDeptId(dept.getId());
			// 开始效验数据
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(warehouseDTO);
			// 库房编码唯一性验证
			//Warehouse findWarehouse = WarehouseCache.getByCode(warehouseDTO.getWhCode());
			IWarehouseService warehouseService = SpringUtil.getBean(IWarehouseService.class);
			Warehouse findWarehouse = warehouseService.list(Condition.getQueryWrapper(new Warehouse())
				.lambda()
				.eq(Warehouse::getWhCode, warehouseDTO.getWhCode())
			).stream().findFirst().orElse(null);
			if (Func.isNotEmpty(findWarehouse)) {
				validResult.addError("whCode", "库房编码[" + warehouseDTO.getWhCode() + "]已存在");
			}
			if (validResult.hasErrors()) {
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			} else {
				if (cache.containsKey(warehouseExcel.getWhCode())) {
					// 更新地址、联系人信息
					cache.get(warehouseDTO.getWhCode()).getAddressList().addAll(warehouseDTO.getAddressList());
					cache.get(warehouseDTO.getWhCode()).getContactsList().addAll(warehouseDTO.getContactsList());
				} else {
					// 记录到缓存map中
					cache.put(warehouseDTO.getWhCode(), warehouseDTO);
				}
				dataVerify.setCacheKey(warehouseDTO.getWhCode());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// 存储数据到redis缓存中
			for (String code : cache.keySet()) {
				WarehouseCache.put(code, cache.get(code));
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
	 * 获取模糊查询构造器
	 *
	 * @param warehouse 查询条件
	 * @return 查询构造器
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
			throw new ServiceException("没有数据可以保存！");
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
	public boolean removeByIds(List<Long> ids){
		/*if(super.removeByIds(ids)){
			//删除库房下关联的库区
			boolean deleteZone = zoneService.remove(Condition.getQueryWrapper(new Zone())
			.lambda()
			.in(Zone::getWhId,ids));
			//删除关联库位
			boolean deleteLocation = locationService.remove(Condition.getQueryWrapper(new Location())
				.lambda()
				.in(Location::getWhId,ids));
		}*/

		for(long id : ids){
			List<Zone> zoneList = zoneService.list(Condition.getQueryWrapper(new Zone())
			.lambda()
			.eq(Zone::getWhId,id)
			.ne(Zone::getZoneType, ZoneTypeEnum.VIRTUAL.getIndex()));

			List<Location> locationList = locationService.list(Condition.getQueryWrapper(new Location())
			.lambda()
			.eq(Location::getWhId,ids)
			.ne(Location::getLocType,LocTypeEnum.Virtual.getIndex()));

			if(Func.isEmpty(zoneList) && Func.isEmpty(locationList)){
				super.removeById(id);
				zoneService.remove(Condition.getQueryWrapper(new Zone())
				.lambda()
				.eq(Zone::getWhId,id));
				locationService.remove(Condition.getQueryWrapper(new Location())
				.lambda()
				.eq(Location::getWhId,id));
			}
			else{
				Warehouse warehouse = super.getById(id);
				throw new ServiceException(String.format("[名称：%s] 库房的库区或库位被占用，不允许删除",warehouse.getWhName()));
			}
		}
		return true;
	}
}
