package org.nodes.wms.biz.warehouse;

import org.nodes.wms.dao.application.dto.output.WarehouseResponse;

import java.util.List;

/**
 * 库房管理业务层接口
 */
public interface WarehouseBiz {

	List<WarehouseResponse> getWarehouseSelectResponseList();
}
