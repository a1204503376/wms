package org.nodes.modules.wms.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.stock.core.dto.StockLogDTO;
import org.nodes.wms.core.stock.core.entity.StockLog;
import org.nodes.wms.core.stock.core.service.IStockLogService;
import org.nodes.wms.core.stock.core.vo.StockLogVO;
import org.nodes.wms.core.stock.core.wrapper.StockLogWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * 库存日志 控制器
 *
 * @author pengwei
 * @since 2020-02-14
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/log/stockLog")
@Api(value = "库存日志", tags = "库存日志接口")
public class StockLogController extends BladeController {

	private IStockLogService stockLogService;


	/**
	 * 分页 库存日志
	 */
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入stockLog")
	public R<IPage<StockLogVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<StockLog> pages = stockLogService.page(Condition.getPage(query), Condition.getQueryWrapper(params, StockLog.class)
			.lambda()
			.orderByDesc(StockLog::getWlslId));
		return R.data(StockLogWrapper.toStockLogVOPages(pages));
	}
}
