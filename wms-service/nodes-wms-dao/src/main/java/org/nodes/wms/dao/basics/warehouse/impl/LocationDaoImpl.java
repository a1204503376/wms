package org.nodes.wms.dao.basics.warehouse.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.warehouse.LocationDao;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationPageResponse;
import org.nodes.wms.dao.basics.warehouse.dto.output.LocationSelectResponse;
import org.nodes.wms.dao.basics.warehouse.entites.Location;
import org.nodes.wms.dao.basics.warehouse.mapper.LocationMapper;
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
}
