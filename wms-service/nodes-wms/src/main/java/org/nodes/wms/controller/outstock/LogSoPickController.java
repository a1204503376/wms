package org.nodes.wms.controller.outstock;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.outstock.logSoPick.LogSoPickBiz;
import org.nodes.wms.dao.outstock.logSoPick.dto.input.LogSoPickPageQuery;
import org.nodes.wms.dao.outstock.logSoPick.dto.output.LogSoPickPageResponse;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 拣货记录日志控制器
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "outstock/logSoPick")
public class LogSoPickController {

	private final LogSoPickBiz logSoPickBiz;

	@PostMapping("/page")
	public R<Page<LogSoPickPageResponse>> page(Query query, @RequestBody LogSoPickPageQuery logSoPickPageQuery) {
		Page<LogSoPickPageResponse> pageLogSoPick = logSoPickBiz.page(query, logSoPickPageQuery);
		return R.data(pageLogSoPick);
	}

	@PostMapping("/export")
	public void export(@RequestBody LogSoPickPageQuery logSoPickPageQuery, HttpServletResponse response) {
		logSoPickBiz.export(logSoPickPageQuery, response);
	}
}
