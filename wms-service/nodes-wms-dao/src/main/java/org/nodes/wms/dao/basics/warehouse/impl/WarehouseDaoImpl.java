package org.nodes.wms.dao.basics.warehouse.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.dao.basics.warehouse.WarehouseDao;
import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.warehouse.mapper.WarehouseMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.tool.utils.Func;
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
	public List<Warehouse> findAll() {
		LambdaQueryWrapper<Warehouse> queryWrapper = new LambdaQueryWrapper<>();
		queryWrapper.eq(Warehouse::getIsDeleted, 0);
		return super.list(queryWrapper);
	}

	@Override
	public Warehouse findByCode(String whCode) {
		return super.getOne(new LambdaQueryWrapper<Warehouse>().eq(Warehouse::getWhCode, whCode));
	}

	@Override
	public List<Warehouse> list() {
		return super.list();
	}

	/**
	 * 根据机构ID集合查询库房集合
	 *
	 * @param deptIds 机构ID集合
	 * @return 库房集合
	 */
	@Override
	public List<Warehouse> getListByDeptId(List<Long> deptIds) {
		if (Func.isEmpty(deptIds)) {
			return null;
		}
		LambdaQueryWrapper<Warehouse> lambdaQueryWrapper = Wrappers.lambdaQuery(Warehouse.class);
		lambdaQueryWrapper.in(Warehouse::getDeptId, deptIds);
		return super.list(lambdaQueryWrapper);
	}

	@Override
	public int countWarehouse() {
		return super.count();
	}
}
