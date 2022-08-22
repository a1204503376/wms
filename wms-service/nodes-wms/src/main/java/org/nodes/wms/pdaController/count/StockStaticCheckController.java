package org.nodes.wms.pdaController.count;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.count.StockCountBiz;
import org.nodes.wms.dao.count.dto.input.*;
import org.nodes.wms.dao.count.dto.output.PdaSkuQtyResponse;
import org.nodes.wms.dao.count.dto.output.PdaStockCountDetailResponse;
import org.nodes.wms.dao.count.dto.output.PdaStockCountResponse;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 盘点: 静态盘点Api
 *
 * @author nodes
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API + "/count/StockStaticCheck")
public class StockStaticCheckController {
	private final StockCountBiz stockCountBiz;

	/**
	 * 静态盘点：根据盘点单编码查询盘点单列表
	 *
	 * @param request 盘点单编码 库房ID
	 * @return 响应对象
	 */
	@PostMapping("/getPdaStockCountResponseList")
	public R<List<PdaStockCountResponse>> findPdaStockCountResponseList(@RequestBody FindPdaStockCountResponseListRequest request) {
		return R.data(stockCountBiz.getPdaStockCountResponseList(request.getCountBillNo()));
	}


	/**
	 * 静态盘点：根据盘点单编码查询盘点单详情列表
	 *
	 * @param request 盘点单ID
	 * @return 响应对象
	 */
	@PostMapping("/getPdaStockCountDetailResponseList")
	public R<List<PdaStockCountDetailResponse>> findPdaStockCountDetailResponseList(@RequestBody FindPdaStockCountDetailResponseListRequest request) {
		return R.data(stockCountBiz.getPdaStockCountDetailResponseList(request.getCountBillId()));
	}

	/**
	 * 静态盘点：根据箱码查询库存详情
	 *
	 * @param request 箱码
	 * @return 响应对象
	 */
	@PostMapping("/findPdaSkuQtyResponseList")
	public R<List<PdaSkuQtyResponse>> findPdaSkuQtyResponseList(@RequestBody FindPdaSkuQtyResponseList request) {
		return R.data(stockCountBiz.getPdaSkuQtyResponseList(request.getBoxCode()));
	}

	/**
	 * 生成盘点记录/人工区
	 *
	 * @param request 生成盘点记录请求对象
	 * @return 成功消息
	 */
	@PostMapping("/generateCountReport")
	public R<String> generateCountReport(@RequestBody GenerateCountReportRequest request) {
		stockCountBiz.generateCountReport(request.getCountReportList());
		return R.success("生成盘点记录成功");
	}

	/**
	 * 生成盘点记录/自动区
	 *
	 * @param request 生成盘点记录请求对象
	 * @return 成功消息
	 */
	@PostMapping("/generateCountReportByAutoLocation")
	public R<String> generateCountReportByAutoLocation(@RequestBody GenerateCountReportByAutoLocationRequest request) {
        stockCountBiz.generateCountReportByAutoLocation(request.getBeChangedList(),request.getDefaultList());
		return R.success("生成盘点记录成功");
	}

}
