
package org.nodes.wms.core.stock.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.stock.core.entity.StockOccupy;
import org.springframework.context.annotation.Primary;

/**
 * 库存占用表 Mapper 接口
 *
 * @author pengwei
 * @since 2020-02-17
 */
@Primary
public interface StockOccupyMapper extends BaseMapper<StockOccupy> {

}
