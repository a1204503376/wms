package org.nodes.wms.biz.basics.warehouse;

import org.nodes.wms.dao.basics.zone.dto.ZoneSelectResponse;
import org.nodes.wms.dao.basics.zone.entities.Zone;

import java.util.List;

/**
 * 库区业务层接口
 */
public interface ZoneBiz {
	/**
	 * 根据库房id集合获取所有库区下拉数据
	 *
	 * @param whIdList 库房id集合
	 * @return 库位下拉列表集合
	 */
	List<ZoneSelectResponse> getZoneSelectData(List<Long> whIdList);

	/**
	 * 根据库区编码查询库区信息
	 *
	 * @param zoneCode: 库区编码
	 * @return Zone
	 */
    Zone findByCode(String zoneCode);

	/**
	 * 根据库区编码和库房编码查询库区信息
	 *
	 * @param zoneCode: 库区编码
	 * @param whCode: 库房编码
	 * @return Zone
	 */
	Zone findByCodeWhCode(String zoneCode, String whCode);

	/**
	 * 根据库区id查询库区信息
	 *
	 * @param zoneId: 库区id
	 * @return Zone
	 */
    Zone findById(Long zoneId);

	/**
	 * 根据库区类型查询库区
	 * @param zoneTypeList
	 * @return
	 */
	List<Zone> findByZoneType(List<String> zoneTypeList);

	/**
	 * 根据库区名称集合获取库区id集合
	 *
	 * @param zoneNameList 库区名称集合
	 * @return 库区id集合
	 */
	List<Long> getZoneIdListByName(List<String> zoneNameList);
}
