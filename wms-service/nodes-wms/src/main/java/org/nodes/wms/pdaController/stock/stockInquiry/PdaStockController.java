package org.nodes.wms.pdaController.stock.stockInquiry;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.stock.dto.input.FindAllStockByNoRequest;
import org.nodes.wms.dao.stock.dto.output.FindAllStockByNoResponse;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 库内查询Api
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/stock/stockInterior")
public class PdaStockController {
	private final StockBiz stockBiz;

	/**
	 * PDA库存查询:库存分页查询
	 *
	 * @param request Pda根据编码查询库存-请求对象
	 * @param query   分页条件
	 * @return 库存响应对象
	 */
	@PostMapping("/findAllStockByNo")
	public R<IPage<FindAllStockByNoResponse>> findAllStockByNo(@RequestBody FindAllStockByNoRequest request, Query query) {
		return R.data(stockBiz.selectStockList(request, query));
	}
}
