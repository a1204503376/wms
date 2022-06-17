package org.nodes.wms.core.basedata.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.nodes.wms.dao.basics.skuType.entities.SkuType;
import org.nodes.wms.core.basedata.vo.SkuTypeVO;
import org.springframework.context.annotation.Primary;

import java.util.List;

/**
 *@program 物品分类Mapper接口
 *@description 物品分类SQLMapper映射接口
 *@author wanglei
 *@create 20191128
 */
@Primary
public interface SkuTypeMapper extends BaseMapper<SkuType> {
	/**
	 * 获取树形节点
	 *
	 * @param skuType
	 * @return
	 */
	List<SkuTypeVO> tree(SkuType skuType);
}
