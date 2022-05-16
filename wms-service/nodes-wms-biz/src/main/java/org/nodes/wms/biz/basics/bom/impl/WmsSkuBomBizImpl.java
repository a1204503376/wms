package org.nodes.wms.biz.basics.bom.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.bom.WmsSkuBomBiz;
import org.nodes.wms.dao.basics.bom.WmsSkuBomDao;
import org.nodes.wms.dao.basics.bom.dto.input.WmsSkuBomPageQuery;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomResponse;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

/**
 * 物料清单 Biz实现
 */
@Service
@RequiredArgsConstructor
public class WmsSkuBomBizImpl implements WmsSkuBomBiz {
	private final WmsSkuBomDao skuBomDao;
	/**
	 * 分页查询
	 * @param query 底部分页参数
	 * @param skuBomPageQuery 查询条件
	 * @return 分页数据
	 */
	@Override
	public Page<WmsSkuBomResponse> getSkuBomPage(Query query, WmsSkuBomPageQuery skuBomPageQuery) {
		return skuBomDao.selectPage(Condition.getPage(query),skuBomPageQuery);
	}
}
