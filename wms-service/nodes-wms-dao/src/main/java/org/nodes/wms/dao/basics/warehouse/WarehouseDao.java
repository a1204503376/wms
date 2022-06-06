package org.nodes.wms.dao.basics.warehouse;

import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.entites.Warehouse;

import java.util.List;

/**
 * 库房管理 Dao接口
 */
public interface WarehouseDao {
	List<WarehouseResponse> getWarehouseSelectResponseList();

    Warehouse findById(Long warehouseId);

	/**
	 * 根据仓库编码获取仓库实体
	 * @param whCode 仓库编码
	 * @return Warehouse
	 */
	Warehouse findByCode(String whCode);
}
