package org.nodes.wms.dao.basics.sku.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.sku.entities.SkuType;
import org.springframework.stereotype.Repository;

/**
 * 物品分类 Mapper 接口
 */
@Repository("skuTypeRepository")
public interface SkuTypeMapper extends BaseMapper<SkuType> {
}
