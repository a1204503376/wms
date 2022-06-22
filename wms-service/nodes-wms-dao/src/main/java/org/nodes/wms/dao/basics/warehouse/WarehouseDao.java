package org.nodes.wms.dao.basics.warehouse;

import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;

import java.util.List;

/**
 * 库房管理 Dao接口
 */
public interface WarehouseDao {
	List<WarehouseResponse> getWarehouseSelectResponseList();

	Warehouse findById(Long warehouseId);

	/**
	 * 根据仓库编码获取仓库实体
	 *
	 * @param whCode 仓库编码
	 * @return Warehouse
	 */
	Warehouse findByCode(String whCode);

	/**
	 * @return 所有库房的集合
	 */
	List<Warehouse> list();

	/**
	 * 根据机构ID集合查询库房集合
	 *
	 * @param deptIds 机构ID集合
	 * @return 库房集合
	 */
	List<Warehouse> getListByDeptId(List<Long> deptIds);

	/**
	 * 获取库房数量
	 *
	 * @return int
	 */
	int countWarehouse();
}
