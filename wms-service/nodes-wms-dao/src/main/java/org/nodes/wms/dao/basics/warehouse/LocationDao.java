package org.nodes.wms.dao.basics.warehouse;

import org.nodes.wms.dao.basics.warehouse.dto.output.LocationSelectResponse;

import java.util.List;

/**
 * 库位管理 Dao接口
 */
public interface LocationDao {
	List<LocationSelectResponse> listTop10ByCode(String code);
}
