
package org.nodes.wms.dao.putway.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.putway.entities.Instock;
import org.springframework.stereotype.Repository;

/**
 * 上架策略 Mapper 接口
 *
 */
@Repository("inStockRepository")
public interface InstockMapper extends BaseMapper<Instock> {

}
