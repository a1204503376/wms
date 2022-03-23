package org.nodes.modules.wms.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.stock.core.dto.SerialLogDTO;
import org.nodes.wms.core.stock.core.entity.SerialLog;
import org.nodes.wms.core.stock.core.service.ISerialLogService;
import org.nodes.wms.core.stock.core.vo.SerialLogVO;
import org.nodes.wms.core.stock.core.wrapper.SerialLogWrapper;
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
 * 序列号日志 控制器
 *
 * @author zx
 * @since 2020-02-24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/log/serialLog")
@Api(value = "序列号日志", tags = "序列号日志接口")
public class SerialLogController extends BladeController {

	private ISerialLogService serialLogService;


	/**
	 * 分页 序列号日志
	 */
	@ApiLog("序列号日志接口-分页")
	@GetMapping("/page")
	@ApiOperation(value = "序列号日志分页", notes = "传入serialLog")
	public R<IPage<SerialLogVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SerialLog> pages = serialLogService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SerialLog.class)
			.lambda()
			.orderByDesc(SerialLog::getWlsnlId));
		return R.data(SerialLogWrapper.build().pageVO(pages));
	}
}
