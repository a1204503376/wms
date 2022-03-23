package org.nodes.modules.wms.stock;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.stock.core.entity.StockOccupy;
import org.nodes.wms.core.stock.core.service.IStockOccupyService;
import org.nodes.wms.core.stock.core.vo.StockOccupyVO;
import org.nodes.wms.core.stock.core.wrapper.StockOccupyWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 库存占表 控制层
 * @Author zx
 * @Date 2020/2/21
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/wms/stock/stockOccupy")
@Api(value = "库存占表", tags = "库存占表接口")
public class StockOccupyController extends BladeController {

	IStockOccupyService stockOccupyService;

	/**
	 * 分页 库存占表
	 */
	@ApiLog("库存占表接口-库存占表分页")
	@GetMapping("/list")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "库存占表分页", notes = "传入stockOccupy")
	public R<IPage<StockOccupyVO>> list(StockOccupy stockOccupy, Query query) {
		IPage<StockOccupy> pages = stockOccupyService.page(Condition.getPage(query), Condition.getQueryWrapper(stockOccupy));
		return R.data(StockOccupyWrapper.toStockOccupyVOPages(pages));
	}
}
