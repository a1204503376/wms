package org.nodes.wms.biz.basics.warehouse.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.ss.formula.functions.T;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
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
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 库位管理 业务类
 */
@Service
@RequiredArgsConstructor
public class LocationBizImpl implements LocationBiz {
	private final LocationDao locationDao;

	private final LocationFactory locationFactory;

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

	@Override
	public List<Location> getAllStage() {
		return locationDao.getAllStage();
	}

	@Override
	public List<Location> getAllQc() {
		return locationDao.getAllQc();
	}

	@Override
	public List<Location> getAllPickTo() {
		return locationDao.getAllPickTo();
	}

	@Override
	public int countAll() {
		return locationDao.countAll();
	}

	@Override
	public Location findLocationByLocCode(Long whId, String locCode) {
		return locationDao.getLocationByLocCode(whId, locCode);
	}
}
