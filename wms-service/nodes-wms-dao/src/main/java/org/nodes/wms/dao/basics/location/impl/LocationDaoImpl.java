package org.nodes.wms.dao.basics.location.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.location.LocationDao;
import org.nodes.wms.dao.basics.location.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.location.dto.output.LocationDetailResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationExcelResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationPageResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationSelectResponse;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.location.mapper.LocationMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库位管理Dao实现类
 */
@Repository
@RequiredArgsConstructor
public class LocationDaoImpl extends BaseServiceImpl<LocationMapper, Location> implements LocationDao {
	@Override
	public List<LocationSelectResponse> listTop10ByCode(String code) {
		return super.baseMapper.listTop10ByCode(code);
	}

	@Override
	public boolean importData(List<Location> locationList) {
		return super.saveBatch(locationList);
	}

	@Override
	public boolean isExistLocationCode(String locCode) {
		LambdaQueryWrapper<Location> lambdaQueryWrapper = Wrappers.lambdaQuery();
		lambdaQueryWrapper.eq(Location::getLocCode, locCode);
		return super.count(lambdaQueryWrapper) > 0;
	}

	@Override
	public Page<LocationPageResponse> selectPage(IPage<?> page, LocationPageQuery locationPageQuery) {
		return super.baseMapper.listByPage(page, locationPageQuery);
	}

	@Override
	public List<LocationExcelResponse> selectListByQuery(LocationPageQuery locationPageQuery) {
		return super.baseMapper.listByQuery(locationPageQuery);
	}

	@Override
	public void saveOrUpdateLocation(Location location) {
		super.saveOrUpdate(location);
	}

	@Override
	public Location getLocationById(Long id) {
		return super.getById(id);
	}

	@Override
	public LocationDetailResponse getDetailById(Long id) {
		return super.baseMapper.selectDetailById(id);
	}

	@Override
	public boolean removeByIdList(List<Long> idList) {
		return super.deleteLogic(idList);
	}

	@Override
	public List<Location> getAllStage() {
		LambdaQueryWrapper<Location> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.likeLeft(Location::getLocCode, "STAGE");
		return super.list(queryWrapper);
	}

	@Override
	public List<Location> getAllQc() {
		LambdaQueryWrapper<Location> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.likeLeft(Location::getLocCode, "QC");
		return super.list(queryWrapper);
	}

	@Override
	public List<Location> getAllPickTo() {
		LambdaQueryWrapper<Location> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.likeLeft(Location::getLocCode, "PICKTO");
		return super.list(queryWrapper);
	}

	@Override
	public int countAll() {
		return super.count();
	}

	@Override
	public Location getLocationByLocCode(String locCode) {
		LambdaQueryWrapper<Location> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(Location::getLocCode, locCode);
		return super.getOne(lambdaQueryWrapper);
	}
}
