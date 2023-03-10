package org.nodes.wms.controller.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.dao.common.log.dto.input.LogActionPageQuery;
import org.nodes.wms.dao.common.log.dto.input.LogApiPageQuery;
import org.nodes.wms.dao.common.log.dto.input.LogErrorPageQuery;
import org.nodes.wms.dao.common.log.dto.output.LogActionPageResponse;
import org.nodes.wms.dao.common.log.dto.output.LogApiPageResponse;
import org.nodes.wms.dao.common.log.dto.output.LogErrorPageResponse;
import org.nodes.wms.dao.common.log.dto.output.LogMessageResponse;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 日志API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "log")
@Api(value = "日志管理", tags = "日志管理接口")
public class LogController {

	private final LogBiz logBiz;

	/**
	 * 消息总条数
	 */
	@ApiOperation(value = "获取消息总数")
	@GetMapping("/getLogMsgCount")
	public R<Integer> getLogMsgCount() {
		return R.data(logBiz.getLogMsgCount());
	}

	/**
	 * 根据消息已读状态获取消息列表
	 * @param num 消息已读状态 0:未读  1:已读
	 */
	@ApiOperation(value = "根据消息已读状态获取消息", notes = "传入num，0:未读  1:已读")
	@GetMapping("/getLogMsgList")
	public R<List<LogMessageResponse>> getLogMsgList(Long num) {
		return R.data(logBiz.getLogMsgList(num));
	}

	/**
	 * 根据消息ID和消息已读状态修改消息已读状态
	 * @param num   消息已读状态
	 * @param id 	消息ID
	 */
	@ApiOperation(value = "根据消息id修改消息已读状态", notes = "传入num、id")
	@GetMapping("/editLogMsgReaded")
	public R<String> editLogMsgReaded(Long num,Long id) {
		logBiz.editLogMsgReaded(num,id);
		return R.success("操作成功！");
	}

	/**
	 * 业务日志分页查询
	 * @param logActionPageQuery 业务日志查询条件
	 * @param query 分页参数
	 * @return 业务日志响应对象
	 */
	@ApiOperation(value = "业务日志分页查询", notes = "传入logActionPageQuery、query")
	@PostMapping("/getLogActionLists")
	public R<Page<LogActionPageResponse>> getLogActionLists(@RequestBody LogActionPageQuery logActionPageQuery, Query query){
        return R.data(logBiz.getLists(logActionPageQuery,query));
	}

	/**
	 * 业务日志导出
	 * @param logActionPageQuery 业务日志条件
	 */
	@ApiOperation(value = "业务日志导出", notes = "传入logActionPageQuery")
	@PostMapping("/export")
	public void export(@RequestBody LogActionPageQuery logActionPageQuery, HttpServletResponse response){
		logBiz.exportActionLists(logActionPageQuery,response);
	}

	/**
	 * 异常日志分页查询
	 */
	@ApiOperation(value = "异常日志分页查询", notes = "传入logErrorPageQuery、query")
	@PostMapping("/logErrorPage")
	public R<IPage<LogErrorPageResponse>> logErrorPage(@RequestBody LogErrorPageQuery logErrorPageQuery, Query query) {
		IPage<LogErrorPageResponse> pages = logBiz.getLogErrorPage(logErrorPageQuery, query);
		return R.data(pages);
	}

	/**
	 * 异常日志导出
	 */
	@ApiOperation(value = "异常日志导出", notes = "传入logActionPageQuery")
	@PostMapping("logErrorExport")
	public void logErrorExport(@RequestBody LogErrorPageQuery logErrorPageQuery, HttpServletResponse response) {
		logBiz.exportLogErrorExcel(logErrorPageQuery, response);
	}

	/**
	 * 请求日志：分页查询
	 */
	@ApiOperation(value = "请求日志分页查询", notes = "传入logErrorPageQuery、query")
	@PostMapping("/logApiPage")
	public R<IPage<LogApiPageResponse>> logApiPage(@RequestBody LogApiPageQuery logApiPageQuery, Query query) {
		IPage<LogApiPageResponse> pages = logBiz.getLogApiPage(logApiPageQuery, query);
		return R.data(pages);
	}

	/**
	 * 请求日志：服务端导出
	 */
	@ApiOperation(value = "请求日志导出", notes = "传入logApiPageQuery")
	@PostMapping("logApiExport")
	public void logApiExport(@RequestBody LogApiPageQuery logApiPageQuery, HttpServletResponse response) {
		logBiz.exportLogApiExcel(logApiPageQuery, response);
	}
}
