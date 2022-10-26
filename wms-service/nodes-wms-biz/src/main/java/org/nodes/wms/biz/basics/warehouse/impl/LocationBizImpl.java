package org.nodes.wms.biz.basics.warehouse.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.ArrayUtils;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.constant.DictKVConstant;
import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.core.tool.utils.ExceptionUtil;
import org.nodes.core.udf.UdfEntity;
import org.nodes.wms.biz.basics.dictionary.DictionaryBiz;
import org.nodes.wms.biz.basics.lpntype.LpnTypeBiz;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.biz.basics.warehouse.modular.LocationFactory;
import org.nodes.wms.biz.stock.StockQueryBiz;
import org.nodes.wms.dao.basics.location.LocationDao;
import org.nodes.wms.dao.basics.location.dto.input.LocationAddOrEditRequest;
import org.nodes.wms.dao.basics.location.dto.input.LocationExcelRequest;
import org.nodes.wms.dao.basics.location.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.location.dto.input.LocationSelectQuery;
import org.nodes.wms.dao.basics.location.dto.output.*;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.location.enums.LocTypeEnum;
import org.nodes.wms.dao.basics.lpntype.entities.LpnType;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeCodeEnum;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.putaway.dto.input.LpnTypeRequest;
import org.nodes.wms.dao.stock.entities.Stock;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 库位管理 业务类
 *
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class LocationBizImpl implements LocationBiz {
	private final WarehouseBiz warehouseBiz;
	private final LocationDao locationDao;
	private final LocationFactory locationFactory;
	private final DictionaryBiz dictionaryBiz;
	private final LpnTypeBiz lpnTypeBiz;

	@Override
	public List<LocationSelectResponse> getLocationSelectResponseTop10List(LocationSelectQuery locationSelectQuery) {
		return locationDao.listTop10ByCode(locationSelectQuery.getKey());
	}

	@Override
	public boolean importData(MultipartFile file) {
		List<LocationExcelRequest> locationDataList = ExcelUtil.read(file, LocationExcelRequest.class);
		if (Func.isEmpty(locationDataList)) {
			throw new ServiceException("导入失败，没有可导入的数据");
		}
		List<Location> locationList = locationFactory.createLocationListForImport(locationDataList);
		return locationDao.importData(locationList);
	}

	@Override
	public Page<LocationPageResponse> page(Query query, LocationPageQuery locationPageQuery) {
		return locationDao.selectPage(Condition.getPage(query), locationPageQuery);
	}

	@Override
	public void exportExcel(LocationPageQuery locationPageQuery, HttpServletResponse response) {
		List<LocationExcelResponse> locationList = locationDao.selectListByQuery(locationPageQuery);
		ExcelUtil.export(response, "库位", "库位数据表", locationList, LocationExcelResponse.class);
	}

	@Override
	public Location add(LocationAddOrEditRequest locationAddOrEditRequest) {
		if (Func.notNull(locationDao.getLocationByLocCode(locationAddOrEditRequest.getWhId(),
			locationAddOrEditRequest.getLocCode()))) {
			throw new ServiceException("新增库位失败，该库位在当前库房已经存在");
		}

		Location location = locationFactory.createLocation(locationAddOrEditRequest);
		locationDao.saveLocation(location);
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
		AssertUtil.notNull(locationAddOrEditRequest.getLocId(), "编辑库位失败,库位主键为空");

		Location location = locationDao.getLocationById(locationAddOrEditRequest.getLocId());
		// 容器类别改变了，需要判断是否可以保存
		if (Func.isNotEmpty(location.getLpnTypeId())) {
			if (!location.getLpnTypeId().equals(locationAddOrEditRequest.getLpnTypeId())) {
				canEdit(locationAddOrEditRequest, location);
			}
		}
		locationDao.updateLocation(Func.copy(locationAddOrEditRequest, Location.class));
		return location;
	}

	public void canEdit(LocationAddOrEditRequest editLocation, Location location) {
		// 库位上有库存不可编辑
		StockQueryBiz stockQueryBiz = SpringUtil.getBean(StockQueryBiz.class);
		List<Stock> stock = stockQueryBiz.findStockByLocation(location.getLocId());
		if (Func.isNotEmpty(stock)) {
			throw ExceptionUtil.mpe("编辑失败，该库位上存在库存");
		}
		// 库位状态为业务系统冻结不可编辑
		if (DictKVConstant.LOC_FLAG_SYSTEM_FORZEN.equals(editLocation.getLocFlag())) {
			throw ExceptionUtil.mpe("编辑失败，系统冻结状态的库位不可修改容器类别");
		}
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
		for (Long id : idList) {
			Location location = locationDao.getLocationById(id);
			String locCode = location.getLocCode();
			if (Func.isNotEmpty(location.getLocType())
				&& location.getLocType().equals(LocTypeEnum.Virtual.key())
				&& StringUtil.contains(locCode, '-')
				&& ArrayUtils.contains(WmsAppConstant.LOC_BY_SYSTEM_CREATED, StringUtil.subAfter(locCode, "-", true))) {
				throw new ServiceException(String.format("库位[编码：%s]是系统生成虚拟库位不可删除", location.getLocCode()));
			}
		}
		return locationDao.removeByIdList(idList);
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void freezeBatch(List<Long> locIdList) {
		locIdList.forEach(id -> {
			Location location = findByLocId(id);
			if (!DictKVConstant.LOC_FLAG_NORMAL.equals(location.getLocFlag())) {
				throw ExceptionUtil.mpe("冻结失败，库位[编码：{}]的使用状态为[{}]", location.getLocCode(), location.getLocFlag());
			}
			freezeLoc(id);
		});
	}

	@Override
	@Transactional(propagation = Propagation.NESTED, rollbackFor = Exception.class)
	public void thawBatch(List<Long> locIdList) {
		locIdList.forEach(id -> {
			Location location = findByLocId(id);
			if (!DictKVConstant.LOC_FLAG_FORZEN.equals(location.getLocFlag())) {
				throw ExceptionUtil.mpe("解冻失败，库位[编码：{}]的使用状态为[{}]", location.getLocCode(), location.getLocFlag());
			}
			thawLoc(id);
		});
	}

	private List<String> getLocCodeOfSystemCreated(String systemCreateCode) {
		List<Warehouse> warehouseList = warehouseBiz.findAll();
		return warehouseList.stream()
			.map(item -> String.format("%s-%s", item.getWhCode(), systemCreateCode))
			.collect(Collectors.toList());
	}

	@Override
	public List<Location> getAllUnknownLocation() {
		List<String> unknownLocCodeList = getLocCodeOfSystemCreated(WmsAppConstant.LOC_UNKNOWN);
		return locationDao.findLocation(unknownLocCodeList);
	}

	@Override
	public List<Location> getAllInTransitLocation() {
		List<String> inTransitLocCodeList = getLocCodeOfSystemCreated(WmsAppConstant.LOC_INTRANSIT);
		return locationDao.findLocation(inTransitLocCodeList);
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
	public List<Location> getLocationByZoneType(Integer zoneType) {
		return locationDao.getLocationByZoneType(null, null, zoneType);
	}

	@Override
	public List<Location> getLocationByZoneType(Long whId, Integer zoneType) {
		return locationDao.getLocationByZoneTypeAndWhId(null, whId, zoneType);
	}

	@Override
	public Boolean getLocationByZoneType(Long whId, Long locId, Integer zoneType) {
		return locationDao.getLocationByZoneTypeAndLocId(whId, locId, zoneType);
	}

	@Override
	public int countAll() {
		return locationDao.countAll();
	}

	@Override
	public Location findLocationByLocCode(Long whId, String locCode) {
		AssertUtil.notNull(whId, "库房ID不能为空");
		AssertUtil.notNull(locCode, "库房编码不能为空");
		Location location = locationDao.getLocationByLocCode(whId, locCode);
		AssertUtil.notNull(location, "获取库位失败,请更换库位编码后重试");
		return location;
	}

	@Override
	public boolean isMixSku(Location location) {
		if (Func.isEmpty(location.getLocSkuMix())) {
			return true;
		}

		return WmsAppConstant.TRUE_DEFAULT_STRING.equals(location.getLocSkuMix());
	}

	@Override
	public boolean isMixSkuLot(Location location) {
		if (Func.isEmpty(location.getLocLotNoMix())) {
			return true;
		}

		return WmsAppConstant.TRUE_DEFAULT_STRING.equals(location.getLocLotNoMix());
	}

	@Override
	public boolean isPickToLocation(Location location) {
		return getLocationByZoneType(location.getWhId(), location.getLocId(), DictKVConstant.ZONE_TYPE_PICK_TO);
	}

	@Override
	public List<Location> findEnableAgvLocation(LpnType lpnType, String zoneType) {
		return locationDao.getLocationByLpnTypeId(lpnType.getId(), zoneType);
	}

	@Override
	public List<Location> getLocationByColumn(Location location) {
		if (Func.isEmpty(location.getLocColumn()) || Func.isEmpty(location.getLocBank())) {
			return null;
		}

		return locationDao.getLocationByLocColumn(location.getLocColumn(), location.getLocBank());
	}

	@Override
	public List<Location> findLocationByLpnType(LpnTypeRequest request) {
		return locationDao.getLocationByLpnType(request);
	}

	@Override
	public boolean isVirtualLocation(List<Location> locationList) {
		Dict dict = dictionaryBiz.findZoneTypeOfVirtual();
		List<Long> locIdList = locationList.stream()
			.map(Location::getLocId)
			.distinct()
			.collect(Collectors.toList());
		List<Location> locations = locationDao.getLocationByZoneType(locIdList, null, dict.getDictKey());
		AssertUtil.notNull(locations, "判断是否有虚拟库位失败，库位集合为空");
		return Func.isNotEmpty(locations);
	}

	@Override
	public List<Location> findLocationByZoneId(Long zoneId) {
		return locationDao.getLocationByZoneId(zoneId);
	}

	@Override
	public void unfreezeLocByTask(String taskId) {
		locationDao.updateLocFlag(taskId, DictKVConstant.LOC_FLAG_NORMAL, true);
	}

	@Override
	public void freezeLoc(Long locId) {
		locationDao.updateLocFlag(locId, DictKVConstant.LOC_FLAG_FORZEN, null);
	}

	public void thawLoc(Long locId) {
		locationDao.updateLocFlag(locId, DictKVConstant.LOC_FLAG_NORMAL, null);
	}

	@Override
	public UdfEntity judgeBoxTypeOfC(String boxCode, Location location) {
		if (Func.isEmpty(boxCode)) {
			return null;
		}

		LpnTypeCodeEnum lpnTypeCodeEnum = lpnTypeBiz.parseBoxCode(boxCode);
		if (!LpnTypeCodeEnum.C.equals(lpnTypeCodeEnum) || !isAgvTempOfZoneType(location.getLocId())) {
			return null;
		}

		AssertUtil.notNull(location, "判断C箱类别失败，库位参数不能为空");
		//（C1:WH1-R-02-33-02,WH1-R-02-34-02 C2:WH1-R-02-28-02 WH1-R-02-28-01 WH1-R-02-27-02 WH1-R-02-27-01)

		if ("WH1-R-02-33-02".equals(location.getLocCode())
			|| "WH1-R-02-34-02".equals(location.getLocCode())) {
			UdfEntity result = new UdfEntity();
			result.setUdf1("C1");
			return result;
		} else if ("WH1-R-02-28-02".equals(location.getLocCode())
			|| "WH1-R-02-28-01".equals(location.getLocCode())
			|| "WH1-R-02-27-02".equals(location.getLocCode())
			|| "WH1-R-02-27-01".equals(location.getLocCode())) {
			UdfEntity result = new UdfEntity();
			result.setUdf1("C2");
			return result;
		} else {
			throw ExceptionUtil.mpe("操作失败,库位：{}不能放C箱，请联系软件方进行修改", location.getLocCode());
		}
	}

	@Override
	public boolean isAgvTempOfZoneType(Long locId) {
		Integer zoneType = locationDao.getZoneTypeByLocId(locId);
		return DictKVConstant.ZONE_TYPE_AGV_TEMPORARY.equals(zoneType);
	}

	@Override
	public boolean isAgvZone(Long locId) {
		Integer zoneType = locationDao.getZoneTypeByLocId(locId);
		return DictKVConstant.ZONE_TYPE_AGV_STORAGE.equals(zoneType)
			|| DictKVConstant.ZONE_TYPE_AGV_PICK.equals(zoneType)
			|| DictKVConstant.ZONE_TYPE_AGV_CHOICE.equals(zoneType)
			|| DictKVConstant.ZONE_TYPE_AGV_TEMPORARY.equals(zoneType);
	}

	@Override
	public void freezeLocByTask(Long locationId, String taskId) {
		AssertUtil.notEmpty(taskId, "系统冻结库位失败,根据任务系统冻结库位时必须要指定系统任务标识");
		locationDao.updateLocFlag(locationId, DictKVConstant.LOC_FLAG_SYSTEM_FORZEN, taskId);
	}

	@Override
	public boolean isVirtualLocation(Location location) {
		return getLocationByZoneType(location.getWhId(), location.getLocId(), DictKVConstant.ZONE_TYPE_VIRTUAL);
	}

	@Override
	public boolean isPickLocation(Location location) {
		return getLocationByZoneType(location.getWhId(), location.getLocId(), DictKVConstant.ZONE_TYPE_PICK);
	}

	@Override
	public boolean isStageLocation(Location location) {
		return getLocationByZoneType(location.getWhId(), location.getLocId(), DictKVConstant.ZONE_TYPE_STAGE);
	}

	@Override
	public boolean isAgvTemporaryLocation(Location location) {
		return getLocationByZoneType(location.getWhId(), location.getLocId(), DictKVConstant.ZONE_TYPE_AGV_TEMPORARY);
	}

	@Override
	public boolean isAgvLocation(Location location) {
		if (getLocationByZoneType(location.getWhId(), location.getLocId(), DictKVConstant.ZONE_TYPE_AGV_STORAGE)
			|| getLocationByZoneType(location.getWhId(), location.getLocId(), DictKVConstant.ZONE_TYPE_AGV_PICK)
			|| getLocationByZoneType(location.getWhId(), location.getLocId(), DictKVConstant.ZONE_TYPE_AGV_CHOICE)
			|| getLocationByZoneType(location.getWhId(), location.getLocId(), DictKVConstant.ZONE_TYPE_AGV_TEMPORARY)) {
			return true;
		}
		return false;
	}
}
