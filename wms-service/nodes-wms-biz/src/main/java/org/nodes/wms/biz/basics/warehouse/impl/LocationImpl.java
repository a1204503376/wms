package org.nodes.wms.biz.basics.warehouse.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.biz.basics.warehouse.modular.LocationFactory;
import org.nodes.wms.dao.basics.warehouse.LocationDao;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationAddOrEditRequest;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationExcelRequest;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationSelectQuery;
import org.nodes.wms.dao.basics.warehouse.dto.output.*;
import org.nodes.wms.dao.basics.warehouse.entities.Location;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 库位管理 业务类
 */
@Service
@RequiredArgsConstructor
public class LocationImpl implements LocationBiz {
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
	public Page<LocationPageResponse> page(IPage<T> page, LocationPageQuery locationPageQuery) {
		return locationDao.selectPage(page, locationPageQuery);
	}

	@Override
	public void exportExcel(LocationPageQuery locationPageQuery, HttpServletResponse response) {
		List<LocationExcelResponse> locationList = locationDao.selectListByQuery(locationPageQuery);
		locationList.forEach(item->{
			item.setLocTypeDesc(item.getLpnType().getDesc());
			item.setLocCategoryDesc(item.getLocCategory().getDesc());
			item.setLocHandlingDesc(item.getLocHandling().getDesc());
			item.setAbcDesc(item.getAbc().getDesc());
			item.setLpnTypeDesc(item.getLpnType().getDesc());
		});
		ExcelUtil.export(response,"库位","库位数据表",locationList,LocationExcelResponse.class);
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
		if (Func.isEmpty(locId)){
			throw new ServiceException("库位id为空");
		}
		return locationDao.getDetailById(locId);
	}
}
