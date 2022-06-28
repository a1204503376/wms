package org.nodes.wms.dao.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.stock.entities.SerialLog;
import org.springframework.stereotype.Repository;

/**
 * 序列号日志
 */
@Repository("serialLogRepository")
public interface SerialLogMapper extends BaseMapper<SerialLog> {
}
