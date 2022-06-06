package org.nodes.wms.dao.basics.warehouse.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.warehouse.WarehouseDao;
import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.entites.Warehouse;
import org.nodes.wms.dao.basics.warehouse.mapper.WarehouseMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库房管理Dao实现类
 */
@Repository
@RequiredArgsConstructor
public class WarehouseDaoImpl extends BaseServiceImpl<WarehouseMapper, Warehouse> implements WarehouseDao {
	private final WarehouseMapper warehouseMapper;

	@Override
	public List<WarehouseResponse> getWarehouseSelectResponseList() {
		return warehouseMapper.getWarehouseSelectResponseList();
	}

    @Override
    public Warehouse findById(Long warehouseId) {
		return super.getById(warehouseId);
    }

    @Override
    public Warehouse findByCode(String whCode) {
		return super.getOne(new LambdaQueryWrapper<Warehouse>().eq(Warehouse::getWhCode,whCode));
    }
}
