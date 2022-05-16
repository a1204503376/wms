package org.nodes.wms.biz.basics.bom;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.nodes.wms.dao.basics.bom.dto.input.WmsSkuBomPageQuery;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomResponse;
import org.springblade.core.mp.support.Query;

/**
 * 物料清单Biz
 */
public interface WmsSkuBomBiz {
	/**
	 * 分页查询
	 * @param query 底部分页参数
	 * @param skuBomPageQuery 查询条件
	 * @return 分页数据
	 */
	Page<WmsSkuBomResponse> getSkuBomPage(Query query, WmsSkuBomPageQuery skuBomPageQuery);
}
