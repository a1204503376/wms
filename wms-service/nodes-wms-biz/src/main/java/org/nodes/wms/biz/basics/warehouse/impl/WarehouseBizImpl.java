package org.nodes.wms.biz.basics.warehouse.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.WarehouseDao;
import org.nodes.wms.dao.basics.warehouse.entites.Warehouse;
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

    @Override
    public Warehouse findById(Long warehouseId) {

		return warehouseDao.findById(warehouseId);
    }

    @Override
    public Warehouse findByCode(String whCode) {
        return warehouseDao.findByCode(whCode);
    }
}
