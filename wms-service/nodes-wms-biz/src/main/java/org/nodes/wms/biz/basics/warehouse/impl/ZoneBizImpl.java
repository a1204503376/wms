package org.nodes.wms.biz.basics.warehouse.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.warehouse.ZoneBiz;
import org.nodes.wms.dao.basics.zone.ZoneDao;
import org.nodes.wms.dao.basics.zone.dto.ZoneSelectQuery;
import org.nodes.wms.dao.basics.zone.dto.ZoneSelectResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 库位管理 业务类
 */
@Service
@RequiredArgsConstructor
public class ZoneBizImpl implements ZoneBiz {

    private final ZoneDao zoneRepository;

	/**
	 * 获取库区下拉列表最近十条数据
	 *
	 * @param zoneSelectQuery 前端传传入查询条件
	 * @return 库位下拉列表集合
	 */
	@Override
	public List<ZoneSelectResponse> getZoneSelectResponseTop10List(ZoneSelectQuery zoneSelectQuery) {
		return zoneRepository.listTop10ByWhIdZoneCodeZoneName(
			zoneSelectQuery.getWhId(),
			zoneSelectQuery.getKey(),
			zoneSelectQuery.getKey());
	}
}
