
package org.nodes.wms.core.stock.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.stock.core.entity.LotLog;
import org.springframework.context.annotation.Primary;

/**
 * 批次号日志 Mapper 接口
 *
 * @author pengwei
 * @since 2020-03-03
 */
@Primary
public interface LotLogMapper extends BaseMapper<LotLog> {

}
