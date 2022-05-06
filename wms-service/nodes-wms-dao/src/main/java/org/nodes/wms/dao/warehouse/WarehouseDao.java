package org.nodes.wms.dao.warehouse;

import org.nodes.wms.dao.application.dto.output.WarehouseResponse;

import java.util.List;

/**
 * 库房管理 Dao接口
 */
public interface WarehouseDao {
	List<WarehouseResponse> getWarehouseSelectResponseList();
}
