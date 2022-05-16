package org.nodes.wms.dao.basics.bom.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.bom.dto.input.WmsSkuBomPageQuery;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomResponse;
import org.nodes.wms.dao.basics.bom.entites.WmsSkuBom;


/**
 * 物料清单 Mapper 接口
 */
public interface WmsSkuBomMapper extends BaseMapper<WmsSkuBom> {
	/**
	 * 分页查询
	 * @param page 底部分页参数
	 * @param skuBomPageQuery 查询条件
	 * @return 分页数据
	 */
	Page<WmsSkuBomResponse> getPage(IPage<?> page, @Param("query") WmsSkuBomPageQuery skuBomPageQuery);
}
