package org.nodes.wms.pdaController.stockManagement.stockInquiry;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.stock.StockBiz;
import org.nodes.wms.dao.stock.dto.input.FindAllStockByNoRequest;
import org.nodes.wms.dao.stock.dto.output.FindAllStockByNoResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 库内查询Api
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/stock")
public class PdaStockController {
	private final StockBiz stockBiz;

	/**
	 * 库存分页查询
	 *
	 * @param request Pda根据编码查询库存-请求对象
	 * @param query   分页条件
	 * @return 库存响应对象
	 */
	@ApiLog("PDA按箱上架")
	@PostMapping("/findAllStockByNo")
	public IPage<FindAllStockByNoResponse> findAllStockByNo(@RequestBody FindAllStockByNoRequest request, Query query) {
		return stockBiz.selectStockList(request, query);
	}
}
