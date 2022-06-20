package org.nodes.wms.dao.basics.skuType.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.springframework.stereotype.Repository;

/**
 * 物品分类 Mapper 接口
 */
@Repository("skuTypeRepository")
public interface SkuTypeMapper extends BaseMapper<SkuType> {
}
