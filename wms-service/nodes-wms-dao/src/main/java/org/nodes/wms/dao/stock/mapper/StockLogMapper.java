package org.nodes.wms.dao.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.stock.entities.StockLog;
import org.springframework.stereotype.Repository;

/**
 * 库存日志mapper接口
 **/
@Repository("stockLogRepository")
public interface StockLogMapper extends BaseMapper<StockLog> {

}
