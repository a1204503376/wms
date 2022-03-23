package org.nodes.wms.core.basedata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.basedata.entity.SkuLog;
import org.springframework.context.annotation.Primary;

/**
 * 物品操作记录表 Mapper 接口
 *
 * @author pengwei
 * @since 2020-06-29
 */
@Primary
public interface SkuLogMapper extends BaseMapper<SkuLog> {

}
