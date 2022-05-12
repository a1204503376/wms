package org.nodes.wms.biz.basics.warehouse;

import org.nodes.wms.dao.basics.warehouse.dto.input.LocationSelectQuery;
import org.nodes.wms.dao.basics.warehouse.dto.output.LocationSelectResponse;

import java.util.List;

/**
 * 客户管理业务层接口
 */
public interface LocationBiz {
	/**
	 * 获取库位下拉列表最近十条数据
	 * @param locationSelectQuery 前端传传入查询条件
	 * @return 库位下拉列表集合
	 */
	List<LocationSelectResponse> getLocationSelectResponseTop10List(LocationSelectQuery locationSelectQuery);
}
