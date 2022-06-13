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
}
