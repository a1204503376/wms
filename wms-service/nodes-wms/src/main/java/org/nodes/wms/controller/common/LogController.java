package org.nodes.wms.controller.common;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.dao.common.log.dto.LogMessageResponse;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
