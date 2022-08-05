package org.nodes.wms.dao.count.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.count.entity.CountDetail;

import java.util.Collection;

/**
 * 盘点单明细表 Mapper 接口
 *
 */
public interface CountDetailMapper extends BaseMapper<CountDetail> {

	int deleteByCountDetailId(@Param("countDetailIdList") Collection<Long> countDetailIdList);
}
