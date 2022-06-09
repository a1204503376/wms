package org.nodes.wms.dao.basics.zone.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
	public List<ZoneSelectResponse>
	listTop10ByWhIdZoneCodeZoneName(String whId, String zoneCode, String zoneName) {
		return super.baseMapper.listTop10ByWhIdZoneCodeZoneName(whId, zoneCode, zoneName);
	}

    @Override
    public Zone getZoneByCode(String zoneCode) {
        return super.getOne(new LambdaQueryWrapper<Zone>().eq(Zone::getZoneCode,zoneCode));
    }

    @Override
    public Zone getZoneById(Long zoneId) {
        return super.getById(zoneId);
    }
}
