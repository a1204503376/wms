package org.nodes.wms.dao.basics.zone;


import org.nodes.wms.dao.basics.zone.dto.ZoneSelectResponse;

import java.util.List;

/**
 * 库区 DAO接口
 */
public interface ZoneDao {

	/**
	 * 根据库区编码或者库区名称查询前10个库区信息
	 *
	 * @param whId       库房id   id为空:查询所有库区 否则查询id指定库房下的库区
	 * @param zoneCode 库区编码
	 * @param zoneName 库区名称
	 * @return List<zoneSelectResponse>
	 */
	List<ZoneSelectResponse> listTop10ByWhIdZoneCodeZoneName(String whId, String zoneCode,String zoneName);
}
