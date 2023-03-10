package org.nodes.wms.biz.basics.warehouse.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.service.IDeptService;
import org.nodes.core.constant.DictKVConstant;
import org.nodes.core.constant.WmsAppConstant;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.dao.basics.location.LocationDao;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.location.enums.LocTypeEnum;
import org.nodes.wms.dao.basics.warehouse.SysAuthDao;
import org.nodes.wms.dao.basics.warehouse.WarehouseDao;
import org.nodes.wms.dao.basics.warehouse.dto.output.WarehousePdaResponse;
import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.entities.SysAuth;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.ZoneDao;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.utils.AesUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 库房管理 业务类
 */
@Service
@RequiredArgsConstructor
public class WarehouseBizImpl implements WarehouseBiz {
	private final WarehouseDao warehouseDao;

	private final ZoneDao zoneDao;

	private final LocationDao locationDao;

	private final SysAuthDao sysAuthDao;

	private final IDeptService deptService;

	private final String AES_KEY = "mKzlYJ9tOXJn8uG16wTHXbQuD7i0lBF0";


	@Override
	public List<WarehouseResponse> getWarehouseSelectResponseList() {
		return warehouseDao.getWarehouseSelectResponseList();
	}

	@Override
	public Warehouse findById(Long warehouseId) {

		return warehouseDao.findById(warehouseId);
	}

	@Override
	public List<Warehouse> findAll() {
		return warehouseDao.findAll();
	}

	@Override
	public Warehouse findByCode(String whCode) {
		return warehouseDao.findByCode(whCode);
	}

	@Override

	public List<Warehouse> getWarehouseByUser(BladeUser user) {
		List<Dept> childDeptList = deptService.getAllChildDept(user.getDeptId());
		List<Long> deptIdList = null;
		if (Func.isEmpty(childDeptList)) {
			deptIdList = new ArrayList<>();
		} else {
			deptIdList = childDeptList.stream()
				.map(Dept::getId)
				.distinct()
				.collect(Collectors.toList());
		}

		deptIdList.add(Long.parseLong(user.getDeptId()));
		List<Warehouse> warehouseList = warehouseDao.getListByDeptId(deptIdList);
		return warehouseList;
	}

	@Override
	public List<WarehousePdaResponse> getWarehouseResponseByUser(BladeUser user) {
		List<Warehouse> warehouseList = getWarehouseByUser(user);
		if (Func.isEmpty(warehouseList)) {
			return null;
		}

		List<WarehousePdaResponse> result =
			BeanUtil.copy(warehouseList, WarehousePdaResponse.class);
		return result;
	}

	private Zone newZoneBySystem(Warehouse warehouse, Integer zoneType, String zoneCodeSuffix, String zoneName){
		Zone zone = new Zone();
		zone.setWhId(warehouse.getWhId());
		zone.setZoneCode(warehouse.getWhCode() + "-" + zoneCodeSuffix);
		zone.setZoneName(zoneName);
		zone.setZoneType(zoneType);
		zone.setCreateDept(warehouse.getDeptId());
		zoneDao.saveOrUpdateZone(zone);

		return zone;
	}

	private Location newLocationBySystem(Warehouse warehouse, Zone zone, String locCodeSuffix){
		Location location = new Location();
		location.setWhId(warehouse.getWhId());
		location.setZoneId(zone.getZoneId());
		location.setLocType(LocTypeEnum.Virtual.getCode());
		location.setLocFlag(DictKVConstant.LOC_FLAG_NORMAL);
		location.setLocCode(warehouse.getWhCode() + "-" + locCodeSuffix);
		location.setCreateDept(warehouse.getDeptId());
		locationDao.saveLocation(location);
		return location;
	}

	@Override
	public void initZoneAndLocAfterNewWarehouse(Warehouse warehouse) {
		// 创建默认的库区（入库暂存区、入库质检区、出库集货区、虚拟库区）
		Zone stageZone = newZoneBySystem(warehouse, DictKVConstant.ZONE_TYPE_STAGE,
			WmsAppConstant.LOC_STAGE, "入库暂存区");
		warehouse.setStage(stageZone.getZoneId());
		Zone pickToZone = newZoneBySystem(warehouse, DictKVConstant.ZONE_TYPE_PICK_TO,
			WmsAppConstant.LOC_PICKTO, "出库集货区");
		warehouse.setPick(pickToZone.getZoneId());
		Zone qcZone = newZoneBySystem(warehouse, DictKVConstant.ZONE_TYPE_INSTOCK_QC,
			WmsAppConstant.LOC_QC, "入库质检区");
		warehouse.setQc(qcZone.getZoneId());
		Zone virtualZone = newZoneBySystem(warehouse, DictKVConstant.ZONE_TYPE_VIRTUAL,
			WmsAppConstant.ZONE_VIRTUAL, "虚拟库区");
		warehouse.setVirtualZone(virtualZone.getZoneId());

		// 创建默认的库位（入库暂存区、入库质检区、出库集货区、在途库位、未知库位）
		newLocationBySystem(warehouse, stageZone, WmsAppConstant.LOC_STAGE);
		newLocationBySystem(warehouse, pickToZone, WmsAppConstant.LOC_PICKTO);
		newLocationBySystem(warehouse, qcZone, WmsAppConstant.LOC_QC);
		newLocationBySystem(warehouse, virtualZone, WmsAppConstant.LOC_UNKNOWN);
		newLocationBySystem(warehouse, virtualZone, WmsAppConstant.LOC_INTRANSIT);
	}

	@Override
	public String authorized(String authorizeString) {
		// 加密
		String encryptValue = AesUtil.encryptToHex(authorizeString, AES_KEY);

		SysAuth sysAuth = new SysAuth();
		sysAuth.setContent(encryptValue);
		sysAuthDao.insertOne(sysAuth);
		return encryptValue;
	}

	@Override
	public void valiAuthorization() {
		// 解密
		String decryptValue = AesUtil.decryptFormHexToString(sysAuthDao.getOne().getContent(), AES_KEY);
		JSONObject jsonObject = JSON.parseObject(decryptValue);
		// 库房授权个数
		int authorizations = jsonObject.getInteger("authorizations");
		// 库房总数
		int count = warehouseDao.countWarehouse() + 1;
		if (count >= authorizations) {
			throw new ServiceException("新增失败，库房数量已达到库房授权数量");
		}
	}
}
