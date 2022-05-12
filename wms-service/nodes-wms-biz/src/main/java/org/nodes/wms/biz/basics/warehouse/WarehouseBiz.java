package org.nodes.wms.biz.basics.warehouse;

import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;

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
}
