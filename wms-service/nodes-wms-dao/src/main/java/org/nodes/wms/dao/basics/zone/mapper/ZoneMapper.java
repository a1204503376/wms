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
	 * 根据库房id集合获取所有库区下拉数据
	 *
	 * @param whIdList 库房id
	 * @return List<ZoneSelectResponse>
	 */
	List<ZoneSelectResponse> listSelectByWhIdList(@Param("whIdList") List<Long> whIdList);

	/**
	 * 根据库区编码和库房编码查询库区信息
	 *
	 * @param code:   库区编码
	 * @param whCode: 库房编码
	 * @return Zone
	 */
	Zone selectZoneByCodeWhCode(@Param("code") String code, @Param("whCode") String whCode);

	/**
	 * 根据库区名称集合获取库区id集合
	 *
	 * @param zoneNameList 库区名称集合
	 * @return 库区id集合
	 */
	List<Long> selectZoneIdListByName(@Param("zoneNameList") List<String> zoneNameList);
}
