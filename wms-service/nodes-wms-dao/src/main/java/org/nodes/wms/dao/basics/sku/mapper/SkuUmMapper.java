package org.nodes.wms.dao.basics.sku.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.springframework.stereotype.Repository;

/**
 * 计量单位 Mapper 接口
 */
@Repository("skuUmRepository")
public interface SkuUmMapper extends BaseMapper<SkuUm> {
}
