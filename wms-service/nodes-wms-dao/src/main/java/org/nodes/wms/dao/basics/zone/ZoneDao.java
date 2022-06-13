package org.nodes.wms.dao.basics.zone;


import org.nodes.wms.dao.basics.zone.dto.ZoneSelectResponse;
import org.nodes.wms.dao.basics.zone.entities.Zone;

import java.util.List;

/**
 * 库区 DAO接口
 */
public interface ZoneDao {

	/**
	 * 根据库房id查询所有库区下拉数据
	 *
	 * @param whIdList       库房id集合
	 * @return List<zoneSelectResponse>
	 */
	List<ZoneSelectResponse> listSelectByWhIdList(List<Long> whIdList);

	/**
	 * 根据库区编码查询库区信息
	 *
	 * @param zoneCode: 库区编码
	 * @return Zone
	 */
    Zone getZoneByCode(String zoneCode);

	/**
	 * 根据库区id查询库区信息
	 *
	 * @param zoneId: 库区编码
	 * @return Zone
	 */
    Zone getZoneById(Long zoneId);
}
