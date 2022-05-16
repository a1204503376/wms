package org.nodes.wms.biz.basics.bom.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.bom.WmsSkuBomBiz;
import org.nodes.wms.dao.basics.bom.WmsSkuBomDao;
import org.nodes.wms.dao.basics.bom.dto.input.WmsSkuBomPageQuery;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomExcelResponse;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomResponse;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

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

	/**
	 * @param params   导出条件
	 * @param response 响应对象
	 */
	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		List<WmsSkuBomExcelResponse> wmsSkuBomList = skuBomDao.getWmsSkuBomList(params);
		ExcelUtil.export(response, "物料清单", "物料清单数据表",wmsSkuBomList,WmsSkuBomExcelResponse.class);
	}
}
