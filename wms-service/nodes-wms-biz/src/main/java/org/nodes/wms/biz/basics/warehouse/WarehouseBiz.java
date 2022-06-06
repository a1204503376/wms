package org.nodes.wms.biz.basics.warehouse;

import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.entites.Warehouse;

import java.util.List;

/**
 * 库房管理业务层接口
 */
public interface WarehouseBiz {
	/**
	 * 获取仓库下拉组件列表
	 * @return 仓库下拉列表集合
	 */
	List<WarehouseResponse> getWarehouseSelectResponseList();

	/**
	 * 根据仓库Id获取仓库实体
	 * @param warehouseId  仓库Id
	 * @return  仓库实体
	 */
	Warehouse findById(Long warehouseId);

	/**
	 * 根据仓库编码获取仓库实体
	 * @param whCode 仓库实体
	 * @return Warehouse 仓库实体
	 */
	Warehouse findByCode(String whCode);
}
