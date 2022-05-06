package org.nodes.wms.biz.warehouse.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.warehouse.WarehouseBiz;
import org.nodes.wms.dao.application.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.customers.CustomerDao;
import org.nodes.wms.dao.warehouse.WarehouseDao;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 库房管理 业务类
 */
@Service
@RequiredArgsConstructor
public class WarehouseBizImpl implements WarehouseBiz {
	private  final WarehouseDao warehouseDao;
	@Override
	public List<WarehouseResponse> getWarehouseSelectResponseList() {
		return warehouseDao.getWarehouseSelectResponseList();
	}
}
