package org.nodes.wms.dao.common.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.nodes.wms.dao.common.log.entities.LogMessage;
import org.springframework.stereotype.Repository;

/**
 * 日志Mapper
 * @author 王智勇
 */
@Repository
@Mapper
public interface LogMessageMapper extends BaseMapper<LogMessage> {
}
