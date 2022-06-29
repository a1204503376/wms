package org.nodes.wms.dao.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.stock.entities.Serial;
import org.springframework.stereotype.Repository;

/**
 * 序列号mapper接口
 **/
@Repository("stockSerialRepository")
public interface SerialMapper extends BaseMapper<Serial> {

}
