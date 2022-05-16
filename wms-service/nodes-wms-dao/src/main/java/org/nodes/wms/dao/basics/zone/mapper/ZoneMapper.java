package org.nodes.wms.dao.basics.zone.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.zone.dto.ZoneSelectResponse;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 库区 Mapper 接口
 */
@Repository("zoneRepository")
public interface ZoneMapper extends BaseMapper<Zone> {

	/**
	 * 根据库区编码或者库区名称查询前10个库区信息
	 *
	 * @param whId 类型模式
	 * @param zoneCode 库区编码
	 * @param zoneName 库区名称
	 * @return List<ZoneSelectResponse>
	 */
	List<ZoneSelectResponse> listTop10ByWhIdZoneCodeZoneName(
		@Param("whId") String whId,
		@Param("zoneCode") String zoneCode,
		@Param("zoneName") String zoneName);
}