package org.nodes.wms.dao.basics.warehouse.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.application.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.WarehouseDao;
import org.nodes.wms.dao.basics.warehouse.mapper.WarehouseMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 库房管理Dao实现类
 */
@Service
@RequiredArgsConstructor
public class WarehouseDaoImpl implements WarehouseDao {
	private final WarehouseMapper warehouseMapper;
	@Override
	public List<WarehouseResponse> getWarehouseSelectResponseList() {
		return warehouseMapper.getWarehouseSelectResponseList();
	}
}
