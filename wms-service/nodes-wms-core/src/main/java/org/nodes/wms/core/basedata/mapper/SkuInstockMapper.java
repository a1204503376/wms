
package org.nodes.wms.core.basedata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.basedata.entity.SkuInstock;
import org.springframework.context.annotation.Primary;

/**
 * 物品入库设置 Mapper 接口
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Primary
public interface SkuInstockMapper extends BaseMapper<SkuInstock> {
}
