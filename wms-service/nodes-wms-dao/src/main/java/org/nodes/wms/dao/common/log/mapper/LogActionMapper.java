package org.nodes.wms.dao.common.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nodes.wms.dao.common.log.entities.LogAction;
import org.springframework.stereotype.Repository;

/**
 * 审计日志mapper
 * @author 王智勇
 */
@Repository
@Mapper
public interface LogActionMapper extends BaseMapper<LogAction> {
}
