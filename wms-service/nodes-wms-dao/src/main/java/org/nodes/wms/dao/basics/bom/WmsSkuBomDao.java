package org.nodes.wms.dao.basics.bom;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.basics.bom.dto.input.WmsSkuBomPageQuery;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomResponse;

/**
 * 物料清单 Dao层
 */
public interface WmsSkuBomDao {
	/**
	 * 分页查询
	 * @param page 底部分页参数
	 * @param skuBomPageQuery 查询条件
	 * @return 分页数据
	 */
	Page<WmsSkuBomResponse> selectPage(IPage<?> page, @Param("query") WmsSkuBomPageQuery skuBomPageQuery);
}
