package org.nodes.wms.controller.outstock;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.outstock.OutStockBiz;
import org.nodes.wms.biz.outstock.logSoPick.LogSoPickBiz;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.LogSoPickIdListRequest;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.LogSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickPageResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * 拣货记录日志控制器
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "outstock/logSoPick")
@Api(value = "拣货记录接口", tags = "拣货记录接口")
public class LogSoPickController {

	private final LogSoPickBiz logSoPickBiz;

	private final OutStockBiz outStockBiz;

	/**
	 * 拣货记录：分页
	 */
	@ApiOperation(value = "分页", notes = "传入query、logSoPickPageQuery")
	@PostMapping("/page")
	public R<Page<LogSoPickPageResponse>> page(Query query, @RequestBody LogSoPickPageQuery logSoPickPageQuery) {
		Page<LogSoPickPageResponse> pageLogSoPick = logSoPickBiz.page(query, logSoPickPageQuery);
		return R.data(pageLogSoPick);
	}

	/**
	 * 拣货记录：服务端导出
	 */
	@ApiOperation(value = "导出", notes = "传入logSoPickPageQuery")
	@PostMapping("/export")
	public void export(@RequestBody LogSoPickPageQuery logSoPickPageQuery, HttpServletResponse response) {
		logSoPickBiz.export(logSoPickPageQuery, response);
	}

	/**
	 * 发货记录：撤销拣货
	 */
	@ApiLog("发货记录-撤销拣货")
	@ApiOperation(value = "撤销拣货", notes = "传入logSoPickIdListRequest")
	@PostMapping("/cancelOutstock")
	public R<String> cancelOutstock(@RequestBody @Valid LogSoPickIdListRequest logSoPickIdListRequest) {
		outStockBiz.cancelOutStock(logSoPickIdListRequest.getLsopIdList());
		return R.data("撤销成功");
	}
}
