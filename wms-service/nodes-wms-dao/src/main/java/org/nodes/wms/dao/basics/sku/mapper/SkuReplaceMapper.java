package org.nodes.wms.dao.basics.sku.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.sku.entities.SkuReplace;
import org.springframework.stereotype.Repository;

/**
 * 物品替代 Mapper 接口
 */
@Repository("skuReplaceRepository")
public interface SkuReplaceMapper extends BaseMapper<SkuReplace> {
}
