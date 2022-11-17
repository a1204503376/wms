package org.nodes.modules.wms.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.stock.core.entity.SerialLog;
import org.nodes.wms.core.stock.core.service.ISerialLogService;
import org.nodes.wms.core.stock.core.vo.SerialLogVO;
import org.nodes.wms.core.stock.core.wrapper.SerialLogWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * 序列号日志 控制器  由SerialLogController更名为SerialLogController2
 *
 * @author zx
 * @since 2020-02-24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/log/serialLog")
@Api(value = "序列号日志", tags = "序列号日志接口")
public class SerialLogController2 extends BladeController {

	private ISerialLogService serialLogService;


	/**
	 * 分页 序列号日志
	 */
	@GetMapping("/page")
	@ApiOperation(value = "序列号日志分页", notes = "传入serialLog")
	public R<IPage<SerialLogVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SerialLog> pages = serialLogService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SerialLog.class)
			.lambda()
			.orderByDesc(SerialLog::getWlsnlId));
		return R.data(SerialLogWrapper.build().pageVO(pages));
	}
}
