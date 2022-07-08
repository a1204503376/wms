package org.nodes.wms.dao.outstock.so.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.outstock.so.entities.SoDetail;
import org.springframework.stereotype.Repository;

/**
 * 收货单头表Mapper接口
 */
@Repository("soDetailRepository")
public interface SoDetailMapper extends BaseMapper<SoDetail> {

}
