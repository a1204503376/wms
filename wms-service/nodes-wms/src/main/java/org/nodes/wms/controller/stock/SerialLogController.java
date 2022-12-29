package org.nodes.wms.controller.stock;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.stock.SerialBiz;
import org.nodes.wms.dao.stock.dto.input.SerialLogPageQuery;
import org.nodes.wms.dao.stock.dto.output.SerialLogPageResponse;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * 序列号日志控制器
 **/
@RestController
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "serialLog")
@RequiredArgsConstructor
@Api(value = "序列号日志接口",tags = "序列号日志接口")
public class SerialLogController {

	private final SerialBiz serialBiz;

	@ApiOperation(value = "分页")
	@PostMapping("/page")
	public R<Page<SerialLogPageResponse>> page(Query query, @RequestBody SerialLogPageQuery serialLogPageQuery) {
		return R.data(serialBiz.pageLog(query, serialLogPageQuery));
	}

	@ApiOperation(value = "导出")
	@PostMapping("/export")
	public void export(@RequestBody SerialLogPageQuery serialLogPageQuery, HttpServletResponse response){
		serialBiz.exportLog(serialLogPageQuery, response);
	}
}
