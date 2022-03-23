
package org.nodes.wms.core.basedata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.core.basedata.entity.SkuReplace;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 * 物品替代 Mapper 接口
 *
 * @author pengwei
 * @since 2019-12-23
 */
@Primary
public interface SkuReplaceMapper extends BaseMapper<SkuReplace> {
	/**
	 * 根据 skuId 查询物品替代集合
	 * @param skuId Sku 主键ID
	 * @return 物品替代集合
	 */
	List<SkuReplace> selectSkuReplacBySkuId(Long skuId);
}
