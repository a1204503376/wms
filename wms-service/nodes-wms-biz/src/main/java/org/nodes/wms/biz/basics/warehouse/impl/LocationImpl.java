package org.nodes.wms.biz.basics.warehouse.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.warehouse.LocationBiz;
import org.nodes.wms.dao.basics.warehouse.LocationDao;
import org.nodes.wms.dao.basics.warehouse.dto.input.LocationSelectQuery;
import org.nodes.wms.dao.basics.warehouse.dto.output.LocationSelectResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 库位管理 业务类
 */
@Service
@RequiredArgsConstructor
public class LocationImpl implements LocationBiz {
    private final  LocationDao locationDao;
	@Override
	public List<LocationSelectResponse> getLocationSelectResponseTop10List(LocationSelectQuery locationSelectQuery) {
		return  locationDao.listTop10ByCode(locationSelectQuery.getKey());
	}
}
