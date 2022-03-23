
package org.nodes.wms.core.basedata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.basedata.entity.SkuInc;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 物品供应商关联 Mapper 接口
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Primary
public interface SkuIncMapper extends BaseMapper<SkuInc> {

	/**
	 * 根据 SkuId 获取物品供应商关联集合
	 * @param skuId Sku 主键ID
	 * @return 物品供应商关联集合
	 */
	List<SkuInc> selectSkuIncBySkuId(Long skuId);
}
