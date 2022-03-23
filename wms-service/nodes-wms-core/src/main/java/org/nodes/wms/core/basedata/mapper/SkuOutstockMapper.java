
package org.nodes.wms.core.basedata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.basedata.entity.SkuOutstock;
import org.springframework.context.annotation.Primary;

/**
 * 物品出库设置 Mapper 接口
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Primary
public interface SkuOutstockMapper extends BaseMapper<SkuOutstock> {
}
