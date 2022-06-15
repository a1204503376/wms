package org.nodes.wms.dao.basics.sku.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.sku.entities.SkuInc;
import org.springframework.stereotype.Repository;

/**
 * 物品供应商关联 DAO接口
 **/
@Repository("skuIncRepository")
public interface SkuIncMapper extends BaseMapper<SkuInc> {
}
