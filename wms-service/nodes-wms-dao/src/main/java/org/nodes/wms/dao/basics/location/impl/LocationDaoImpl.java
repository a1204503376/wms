package org.nodes.wms.dao.basics.location.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.NullArgumentException;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.basics.location.LocationDao;
import org.nodes.wms.dao.basics.location.dto.input.LocationPageQuery;
import org.nodes.wms.dao.basics.location.dto.output.LocationDetailResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationExcelResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationPageResponse;
import org.nodes.wms.dao.basics.location.dto.output.LocationSelectResponse;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.location.mapper.LocationMapper;
import org.nodes.wms.dao.putway.dto.input.LpnTypeRequest;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库位管理Dao实现类
 */
@Repository
@RequiredArgsConstructor
public class LocationDaoImpl extends BaseServiceImpl<LocationMapper, Location> implements LocationDao {
	private final LocationMapper locationMapper;

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
	public int countAll() {
		return super.count();
	}

	@Override
	public Location getLocationByLocCode(Long whId, String locCode) {
		if (Func.isEmpty(whId) || Func.isEmpty(locCode)) {
			throw new NullArgumentException("LocationDaoImpl.getLocationByLocCode方法的参数为空");
		}

		LambdaQueryWrapper<Location> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper
			.eq(Location::getLocCode, locCode)
			.eq(Location::getWhId, whId);
		return super.getOne(lambdaQueryWrapper);
	}

	@Override
	public List<Location> findLocation(List<String> locCode) {
		if (Func.isEmpty(locCode)) {
			throw new NullArgumentException("LocationDaoImpl.findLocation方法的参数为空");
		}

		LambdaQueryWrapper<Location> queryWrapper = Wrappers.lambdaQuery();
		queryWrapper.in(Location::getLocCode, locCode);
		return super.list(queryWrapper);
	}

	@Override
	public List<Location> getLocationByWhId(Long whId) {
		LambdaQueryWrapper<Location> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Location::getWhId, whId);
		return super.list(queryWrapper);
	}

	@Override
	public void updateOccupyFlag(Long locId, String occupyFlag) {
		UpdateWrapper<Location> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(Location::getLocId, locId)
			.set(Location::getOccupyFlag, occupyFlag);
		if (super.update(updateWrapper)) {
			throw new ServiceException("库位冻结/解冻更新失败");
		}
	}

	@Override
	public List<Location> getLocationByLpnTypeId(Long lpnTypeId, String zoneType) {
		if (Func.isEmpty(lpnTypeId)) {
			throw new NullArgumentException("LocationDaoImpl.getLocationByLpnTypeId方法的参数为空");
		}
		//根据库区类型查询查询库位，如果zongType可以为空
		return locationMapper.getLocationByLpnTypeIdAndZoneType(lpnTypeId, zoneType);
	}

	@Override
	public List<Location> getLocationByLocColumn(String locColumn) {
		if (Func.isEmpty(locColumn)) {
			throw new NullArgumentException("LocationDaoImpl.getLocationByLocColumn方法的参数为空");
		}
		LambdaQueryWrapper<Location> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(Location::getLocColumn, locColumn);
		return super.list(lambdaQueryWrapper);
	}

	@Override
	public List<Location> getLocationByZoneType(List<Long> locIdList, Integer zoneType) {
		AssertUtil.notEmpty(locIdList, "判断是否有虚拟库位失败，库位集合为空");

		return locationMapper.getLocationByZoneType(locIdList, zoneType);
	}

	@Override
	public List<Location> getLocationByLpnType(LpnTypeRequest request) {

		return super.baseMapper.selectLoctionByLpnType(request);
	}


}
