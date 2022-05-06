package org.nodes.wms.dao.warehouse.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.application.dto.output.WarehouseResponse;
import org.nodes.wms.dao.warehouse.WarehouseDao;
import org.nodes.wms.dao.warehouse.mapper.WarehousesMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 库房管理Dao实现类
 */
@Service
@RequiredArgsConstructor
public class WarehouseDaoImpl implements WarehouseDao {
	private final WarehousesMapper warehousesMapper;
	@Override
	public List<WarehouseResponse> getWarehouseSelectResponseList() {
		return warehousesMapper.getWarehouseSelectResponseList();
	}
}
