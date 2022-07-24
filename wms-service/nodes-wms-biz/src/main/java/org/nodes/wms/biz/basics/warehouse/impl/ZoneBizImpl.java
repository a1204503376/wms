package org.nodes.wms.biz.basics.warehouse.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.dao.basics.zone.ZoneDao;
import org.nodes.wms.dao.basics.zone.dto.ZoneSelectResponse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 库位管理 业务类
 */
@Service
@RequiredArgsConstructor
public class ZoneBizImpl implements ZoneBiz {

    private final ZoneDao zoneRepository;

	@Override
	public List<ZoneSelectResponse> getZoneSelectData(List<Long> whIdList) {
		return zoneRepository.listSelectByWhIdList(whIdList);
	}

    @Override
    public Zone findByCode(String zoneCode) {
        return zoneRepository.getZoneByCode(zoneCode);
    }

    @Override
    public Zone findByCodeWhCode(String zoneCode, String whCode) {
        return zoneRepository.getZoneByCodeWhCode(zoneCode,whCode);
    }

    @Override
    public Zone findById(Long zoneId) {
        return zoneRepository.getZoneById(zoneId);
    }

    @Override
    public List<Zone> findByZoneType(List<String> zoneTypeList) {
		return zoneRepository.getByZoneType(zoneTypeList);
    }
}
