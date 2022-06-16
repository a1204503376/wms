package org.nodes.wms.controller.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.dao.common.log.dto.LogActionPageQuery;
import org.nodes.wms.dao.common.log.dto.LogActionPageResponse;
import org.nodes.wms.dao.common.log.dto.LogMessageResponse;
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
public class LogController {
	private final LogBiz logBiz;

	@GetMapping("/getLogMsgCount")
	public R<Integer> getLogMsgCount() {
		return R.data(logBiz.getLogMsgCount());
	}
	@GetMapping("/getLogMsgList")
	public R<List<LogMessageResponse>> getLogMsgList(Long num) {
		return R.data(logBiz.getLogMsgList(num));
	}

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
	@PostMapping("/getLogActionLists")
	public R<Page<LogActionPageResponse>> getLogActionLists(@RequestBody LogActionPageQuery logActionPageQuery, Query query){
        return R.data(logBiz.getLists(logActionPageQuery,query));
	}

	/**
	 * 业务日志导出
	 * @param logActionPageQuery 业务日志条件
	 */
	@PostMapping("/export")
	public void export(@RequestBody LogActionPageQuery logActionPageQuery, HttpServletResponse response){
		logBiz.exportActionLists(logActionPageQuery,response);
	}


}
