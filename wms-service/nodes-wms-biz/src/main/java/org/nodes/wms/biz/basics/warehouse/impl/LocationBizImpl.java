package org.nodes.wms.biz.basics.warehouse.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.poi.ss.formula.functions.T;
import org.nodes.core.base.entity.Dict;
import org.nodes.wms.biz.basics.dictionary.DictionaryBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.biz.basics.warehouse.modular.LocationFactory;
import org.nodes.wms.dao.basics.location.LocationDao;
import org.nodes.wms.dao.basics.location.constant.LocationConstant;
import org.nodes.wms.dao.basics.location.dto.input.LocationAddOrEditRequest;
import org.nodes.wms.dao.basics.location.dto.input.LocationExcelRequest;
import org.nodes.wms.dao.basics.location.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.location.dto.input.LocationSelectQuery;
import org.nodes.wms.dao.basics.location.dto.output.*;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.location.enums.LocTypeEnum;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.putway.dto.input.LpnTypeRequest;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 库位管理 业务类
 */
@Service
@RequiredArgsConstructor
public class LocationBizImpl implements LocationBiz {
	private final WarehouseBiz warehouseBiz;
	private final LocationDao locationDao;
	private final LocationFactory locationFactory;
	private final DictionaryBiz dictionaryBiz;

	@Override
	public List<LocationSelectResponse> getLocationSelectResponseTop10List(LocationSelectQuery locationSelectQuery) {
		return locationDao.listTop10ByCode(locationSelectQuery.getKey());
	}

	@Override
	public boolean importData(List<LocationExcelRequest> locationDataList) {
		if (Func.isEmpty(locationDataList)) {
			throw new ServiceException("导入失败，没有可导入的数据");
		}
		List<Location> locationList = locationFactory.createLocationListForImport(locationDataList);
		return locationDao.importData(locationList);
	}

	@Override
	public Page<LocationPageResponse> page(Query query, LocationPageQuery locationPageQuery) {
		IPage<T> page = Condition.getPage(query);
		return locationDao.selectPage(page, locationPageQuery);
	}

	@Override
	public void exportExcel(LocationPageQuery locationPageQuery, HttpServletResponse response) {
		List<LocationExcelResponse> locationList = locationDao.selectListByQuery(locationPageQuery);
		ExcelUtil.export(response, "库位", "库位数据表", locationList, LocationExcelResponse.class);
	}

	@Override
	public Location add(LocationAddOrEditRequest locationAddOrEditRequest) {
		Location location = locationFactory.createLocation(locationAddOrEditRequest);
		locationDao.saveOrUpdateLocation(location);
		return location;
	}

	@Override
	public LocationEditResponse findLocationById(Long locId) {
		Location location = locationDao.getLocationById(locId);
		return locationFactory.createLocationEditResponse(location);
	}

	@Override
	public Location findByLocId(long locId) {
		return locationDao.getLocationById(locId);
	}

	@Override
	public Location edit(LocationAddOrEditRequest locationAddOrEditRequest) {
		Location location = locationFactory.createLocation(locationAddOrEditRequest);
		locationDao.saveOrUpdateLocation(location);
		return location;
	}

	@Override
	public LocationDetailResponse getLocationDetailById(Long locId) {
		if (Func.isEmpty(locId)) {
			throw new ServiceException("库位id为空");
		}
		return locationDao.getDetailById(locId);
	}

	@Override
	public boolean remove(List<Long> idList) {
		for (Long id : idList
		) {
			Location location = locationDao.getLocationById(id);
			String locCode = location.getLocCode();
			if (location.getLocType().equals(LocTypeEnum.Virtual.key())
				&& StringUtil.contains(locCode, '-')
				&& ArrayUtils.contains(LocationConstant.getLocTypes(), StringUtil.subAfter(locCode, "-", true))
			) {
				throw new ServiceException(String.format("库位[编码：%s]是系统生成虚拟库位不可删除", location.getLocCode()));
			}
		}
		return locationDao.removeByIdList(idList);
	}

	private List<String> getLocCodeOfSystemCreated(String systemCreateCode) {
		List<Warehouse> warehouseList = warehouseBiz.findAll();
		return warehouseList.stream()
			.map(item -> String.format("%s-%s", item.getWhCode(), systemCreateCode))
			.collect(Collectors.toList());
	}

	@Override
	public List<Location> getAllStageLocation() {
		List<String> stageLocCodeList = getLocCodeOfSystemCreated(LocationConstant.LOC_STAGE);
		return locationDao.findLocation(stageLocCodeList);
	}

	@Override
	public List<Location> getAllQcLocation() {
		List<String> qcLocCodeList = getLocCodeOfSystemCreated(LocationConstant.LOC_QC);
		return locationDao.findLocation(qcLocCodeList);
	}

	@Override
	public List<Location> getAllPickToLocation() {
		List<String> pickToLocCodeList = getLocCodeOfSystemCreated(LocationConstant.LOC_PICKTO);
		return locationDao.findLocation(pickToLocCodeList);
	}


	@Override
	public List<Location> getAllPackLocation() {
		List<String> packLocCodeList = getLocCodeOfSystemCreated(LocationConstant.LOC_PACK);
		return locationDao.findLocation(packLocCodeList);
	}

	@Override
	public List<Location> getAllUnknownLocation() {
		List<String> unknownLocCodeList = getLocCodeOfSystemCreated(LocationConstant.LOC_UNKNOWN);
		return locationDao.findLocation(unknownLocCodeList);
	}

	@Override
	public List<Location> getAllInTransitLocation() {
		List<String> inTransitLocCodeList = getLocCodeOfSystemCreated(LocationConstant.LOC_INTRANSIT);
		return locationDao.findLocation(inTransitLocCodeList);
	}

	@Override
	public Location getStageLocation(Long whId) {
		if (Func.isEmpty(whId)) {
			return null;
		}
		List<Location> allStageLocation = getAllStageLocation();
		List<Location> locationList = allStageLocation.stream()
			.filter(item -> whId.equals(item.getWhId())).collect(Collectors.toList());
		return Func.isNotEmpty(locationList) ? locationList.get(0) : null;
	}

	@Override
	public Location getQcLocation(Long whId) {
		if (Func.isEmpty(whId)) {
			return null;
		}
		List<Location> allQcLocation = getAllQcLocation();
		List<Location> locationList = allQcLocation.stream()
			.filter(item -> whId.equals(item.getWhId())).collect(Collectors.toList());
		return Func.isNotEmpty(locationList) ? locationList.get(0) : null;
	}

	@Override
	public Location getPickToLocation(Long whId) {
		if (Func.isEmpty(whId)) {
			return null;
		}
		List<Location> allPickToLocation = getAllPickToLocation();
		List<Location> locationList = allPickToLocation.stream()
			.filter(item -> whId.equals(item.getWhId())).collect(Collectors.toList());
		return Func.isNotEmpty(locationList) ? locationList.get(0) : null;
	}

	@Override
	public Location getPackLocation(Long whId) {
		if (Func.isEmpty(whId)) {
			return null;
		}
		List<Location> allPackLocation = getAllPackLocation();
		List<Location> locationList = allPackLocation.stream()
			.filter(item -> whId.equals(item.getWhId())).collect(Collectors.toList());
		return Func.isNotEmpty(locationList) ? locationList.get(0) : null;
	}

	@Override
	public Location getUnknowLocation(Long whId) {
		if (Func.isEmpty(whId)) {
			return null;
		}
		List<Location> allUnknownLocation = getAllUnknownLocation();
		List<Location> locationList = allUnknownLocation.stream()
			.filter(item -> whId.equals(item.getWhId())).collect(Collectors.toList());
		return Func.isNotEmpty(locationList) ? locationList.get(0) : null;
	}

	@Override
	public Location getInTransitLocation(Long whId) {
		if (Func.isEmpty(whId)) {
			return null;
		}
		List<Location> allInTransitLocation = getAllInTransitLocation();
		List<Location> locationList = allInTransitLocation.stream()
			.filter(item -> whId.equals(item.getWhId())).collect(Collectors.toList());
		return Func.isNotEmpty(locationList) ? locationList.get(0) : null;
	}

	@Override
	public int countAll() {
		return locationDao.countAll();
	}

	@Override
	public Location findLocationByLocCode(Long whId, String locCode) {
		return locationDao.getLocationByLocCode(whId, locCode);
	}

	@Override
	public boolean isFrozen(Location location) {
		if (Func.isNull(location.getLocFlag())) {
			return false;
		}

		if (location.getLocFlag() == 10 || location.getLocFlag() == 20) {
			return true;
		}

		return Func.isNotEmpty(location.getOccupyFlag()) && !"0".equals(location.getOccupyFlag());
	}

	@Override
	public boolean isMixSku(Location location) {
		if (Func.isEmpty(location.getLocSkuMix())) {
			return true;
		}

		return "1".equals(location.getLocSkuMix());
	}

	@Override
	public boolean isMixSkuLot(Location location) {
		if (Func.isEmpty(location.getLocLotNoMix())) {
			return true;
		}

		return "1".equals(location.getLocLotNoMix());
	}

	@Override
	public void freezeByOccupyFlag(Long locId, String occupyFlag) {
		if (Func.isEmpty(occupyFlag)) {
			throw new NullArgumentException("库内库位冻结时冻结标识为空");
		}
		locationDao.updateOccupyFlag(locId, occupyFlag);
	}

	@Override
	public void unfreezeByOccupyFlag(Long locId) {
		locationDao.updateOccupyFlag(locId, null);
	}

	@Override
	public boolean isPickToLocation(Location location) {
		Location pickToLocation = getPickToLocation(location.getWhId());
		return location.getLocId().equals(pickToLocation.getLocId());
	}

	@Override
	public List<Location> findEnableAgvLocation(LpnType lpnType, String zoneType) {
		return locationDao.getLocationByLpnTypeId(lpnType.getId(), zoneType);
	}

	@Override
	public List<Location> getLocationByColumn(Location location) {
		return locationDao.getLocationByLocColumn(location.getLocColumn());
	}

	@Override
	public List<Location> findLocationByLpnType(LpnTypeRequest request) {
		return locationDao.getLocationByLpnType(request);
	}

	@Override
	public boolean isVirtualLocation(List<Location> locationList) {
		Dict dict = dictionaryBiz.findZoneTypeOfVirtual();
		// TODO
		List<Long> locIdList = locationList.stream()
			.map(Location::getLocId)
			.distinct()
			.collect(Collectors.toList());
		List<Location> locations = locationDao.getLocationByZoneType(locIdList, dict.getDictKey());
		return Func.isEmpty(locations);
	}
}
