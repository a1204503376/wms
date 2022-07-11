package org.nodes.wms.controller.stock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.stock.dto.input.StockPageQuery;
import org.nodes.wms.dao.stock.dto.output.StockPageResponse;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 库存管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "stock")
public class StockManagerController {
	private final StockBiz stockBiz;

	@PostMapping("/page")
	public R<IPage<StockPageResponse>> page(Query query, @RequestBody StockPageQuery stockPageQuery) {
		return R.data(stockBiz.getStockPage(query, stockPageQuery));
	}
}
