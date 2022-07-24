package org.nodes.wms.dao.basics.zone.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.nodes.core.tool.utils.AssertUtil;
import org.nodes.wms.dao.basics.zone.ZoneDao;
import org.nodes.wms.dao.basics.zone.dto.ZoneSelectResponse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.basics.zone.mapper.ZoneMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库区 DAO实现类
 */
@Repository
public class ZoneDaoImpl
	extends BaseServiceImpl<ZoneMapper, Zone>
	implements ZoneDao {

	@Override
	public List<ZoneSelectResponse> listSelectByWhIdList(List<Long> whIdList) {
		return super.baseMapper.listSelectByWhIdList(whIdList);
	}

	@Override
	public Zone getZoneByCode(String zoneCode) {
		return super.getOne(new LambdaQueryWrapper<Zone>().eq(Zone::getZoneCode, zoneCode));
	}

	@Override
	public Zone getZoneById(Long zoneId) {
		return super.getById(zoneId);
	}

	@Override
	public Zone getZoneByCodeWhCode(String code, String whCode) {
		return super.baseMapper.selectZoneByCodeWhCode(code, whCode);
	}

	@Override
	public Zone saveOrUpdateZone(Zone zone) {
		super.saveOrUpdate(zone);
		return zone;
	}

    @Override
    public List<Zone> getByZoneType(List<String> zoneTypeList) {
		AssertUtil.notEmpty(zoneTypeList, "根据库区类型查找库区失败，因为库区类型集合为空");

        LambdaQueryWrapper<Zone> queryWrapper = Wrappers.lambdaQuery(Zone.class);
        queryWrapper.in(Zone::getZoneType, zoneTypeList);
        return super.list(queryWrapper);
    }
}
