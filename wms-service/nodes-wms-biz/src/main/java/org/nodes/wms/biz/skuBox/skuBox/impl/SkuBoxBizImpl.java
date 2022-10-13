package org.nodes.wms.biz.skuBox.skuBox.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.skuBox.skuBox.SkuBoxBiz;
import org.nodes.wms.dao.tianyi.skubox.SkuBoxDao;
import org.nodes.wms.dao.tianyi.skubox.dto.input.SkuBoxPageQuery;
import org.nodes.wms.dao.tianyi.skubox.dto.output.SkuBoxPageResponse;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 天宜物品和型号对应箱型标识 业务实现
 */
@Service
@RequiredArgsConstructor
public class SkuBoxBizImpl implements SkuBoxBiz {

	private final SkuBoxDao skuBoxDao;

	@Override
	public Page<SkuBoxPageResponse> getPage(Query query, SkuBoxPageQuery skuBoxPageQuery) {
		return skuBoxDao.page(Condition.getPage(query), skuBoxPageQuery);
	}

	@Override
	public void exportExcel(SkuBoxPageQuery skuBoxPageQuery, HttpServletResponse response) {
		List<SkuBoxPageResponse> skuBoxList = skuBoxDao.listByQuery(skuBoxPageQuery);
		ExcelUtil.export(response,"","",skuBoxList, SkuBoxPageResponse.class);
	}
}
