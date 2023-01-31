package org.nodes.wms.dao.basics.location.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
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
import org.nodes.wms.dao.putaway.dto.input.LpnTypeRequest;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库位管理Dao实现类
 *
 * @author nodesc
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
		return super.saveOrUpdateBatch(locationList);
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
	public void saveLocation(Location location) {
		super.save(location);
	}

	@Override
	public void updateLocation(Location location) {
		UpdateWrapper<Location> updateWrapper = Wrappers.update();
		updateWrapper.lambda()
			.eq(Location::getLocId, location.getLocId())
			.set(Location::getLpnTypeId, location.getLpnTypeId())
			.set(Location::getLocType, location.getLocType())
			.set(Location::getLocCategory, location.getLocCategory())
			.set(Location::getLocHandling, location.getLocHandling())
			.set(Location::getLocFlag, location.getLocFlag())
			.set(Location::getLocSkuMix, location.getLocSkuMix())
			.set(Location::getLocLotNoMix, location.getLocLotNoMix());
		if (!super.update(location, updateWrapper)) {
			throw new ServiceException("更新库位失败,请再次重试");
		}
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
	public List<Location> getLocationByLpnTypeId(Long lpnTypeId, String zoneType) {
		if (Func.isEmpty(lpnTypeId)) {
			throw new NullArgumentException("LocationDaoImpl.getLocationByLpnTypeId方法的参数为空");
		}
		// 根据库区类型查询查询库位，如果zongType可以为空
		return locationMapper.getLocationByLpnTypeIdAndZoneType(lpnTypeId, zoneType);
	}

	@Override
	public List<Location> getLocationByLocColumn(String locColumn, String locBank) {
		if (Func.isEmpty(locColumn) || Func.isEmpty(locBank)) {
			throw new NullArgumentException("根据列获取货位失败,货架的排或列编码不能位空");
		}

		LambdaQueryWrapper<Location> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(Location::getLocColumn, locColumn)
			.eq(Location::getLocBank, locBank);
		return super.list(lambdaQueryWrapper);
	}

	@Override
	public List<Location> getLocationByZoneType(List<Long> locIdList, Long whId, Integer zoneType) {
		return locationMapper.getLocationByZoneType(locIdList, whId, zoneType);
	}

	@Override
	public List<Location> getLocationByZoneTypeAndWhId(List<Long> locIdList, Long whId, Integer zoneType) {
		return locationMapper.getLocationByZoneTypeAndWhId(locIdList, whId, zoneType);
	}

	@Override
	public Boolean getLocationByZoneTypeAndLocId(Long whId, Long locId, Integer zoneType) {
		Location location = locationMapper.getLocationByZoneTypeAndLocId(whId, locId, zoneType);
		return Func.isNotEmpty(location);
	}

	@Override
	public List<Location> getLocationByZoneId(Long zoneId) {
		AssertUtil.notNull(zoneId, "获取库位失败，库区ID为空");
		LambdaQueryWrapper<Location> lambdaQueryWrapper = new LambdaQueryWrapper<>();
		lambdaQueryWrapper.eq(Location::getZoneId, zoneId);
		return super.list(lambdaQueryWrapper);
	}

	@Override
	public List<Location> getLocationByLpnType(LpnTypeRequest request) {
		return super.baseMapper.selectLoctionByLpnType(request);
	}

	@Override
	public void updateLocFlag(Long locId, Integer locFlag, String taskId) {
		AssertUtil.notNull(locId, "冻结解冻库位时locId不能为空");
		AssertUtil.notNull(locFlag, "冻结解冻库位时locFlag不能为空");

		if (Func.isNull(taskId)) {
			taskId = "";
		}
		LambdaUpdateWrapper<Location> updateWrapper = Wrappers.lambdaUpdate();
		updateWrapper.eq(Location::getLocId, locId)
			.set(Location::getLocFlag, locFlag)
			.set(Location::getLocFlagDesc, taskId);
		if (!super.update(updateWrapper)) {
			throw new ServiceException(String.format("根据locId:[%d]更新库位使用状态失败", locId));
		}
	}

	@Override
	public void updateLocFlag(String taskId, Integer locFlag, boolean isResetLocFlagDesc) {
		AssertUtil.notNull(taskId, "冻结解冻库位时任务编号不能为空");
		AssertUtil.notNull(locFlag, "冻结解冻库位时状态不能为空");

		LambdaUpdateWrapper<Location> updateWrapper = Wrappers.lambdaUpdate();
		updateWrapper.eq(Location::getLocFlagDesc, taskId)
			.set(Location::getLocFlag, locFlag)
			.set(isResetLocFlagDesc, Location::getLocFlagDesc, "");

		super.update(updateWrapper);
	}

	@Override
	public Integer getZoneTypeByLocId(Long locId) {
		return super.baseMapper.selectZoneTypeByLocId(locId);
	}

	@Override
	public List<Location> getAll() {
		return super.list();
	}
}
