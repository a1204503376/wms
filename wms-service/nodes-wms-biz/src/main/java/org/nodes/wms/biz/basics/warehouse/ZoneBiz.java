package org.nodes.wms.biz.basics.warehouse;

import org.nodes.wms.dao.basics.zone.dto.ZoneSelectQuery;
import org.nodes.wms.dao.basics.zone.dto.ZoneSelectResponse;
import org.nodes.wms.dao.basics.zone.entities.Zone;

import java.util.List;

/**
 * 库区业务层接口
 */
public interface ZoneBiz {
	/**
	 * 获取库区下拉列表最近十条数据
	 *
	 * @param zoneSelectQuery 前端传传入查询条件
	 * @return 库位下拉列表集合
	 */
	List<ZoneSelectResponse> getZoneSelectResponseTop10List(ZoneSelectQuery zoneSelectQuery);

	/**
	 * 根据库区编码查询库区信息
	 *
	 * @param zoneCode: 库区编码
	 * @return Zone
	 */
    Zone findByCode(String zoneCode);
}
