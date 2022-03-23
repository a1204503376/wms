package org.nodes.modules.wms.log.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.modules.wms.log.dto.OutstockLogDTO;
import org.nodes.wms.core.strategy.entity.OutstockLog;
import org.nodes.wms.core.strategy.service.IOutstockLogService;
import org.nodes.wms.core.strategy.vo.OutstockLogVO;
import org.nodes.wms.core.strategy.wrapper.OutstockLogWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.jackson.JsonUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;

/**
 * 分配记录 控制器
 *
 * @author pengwei
 * @since 2020-05-06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/log/outstockLog")
@Api(value = "分配记录", tags = "分配记录接口")
public class OutstockLogController extends BladeController {

	private IOutstockLogService outstockLogService;

	/**
	 * 分配记录分页
	 */
	@ApiLog("分配记录接口-分配记录分页")
	@GetMapping("/page")
	@ApiOperation(value = "分配记录分页", notes = "传入outstockLog")
	public R<IPage<OutstockLogVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<OutstockLog> pages = outstockLogService.page(Condition.getPage(query), Condition.getQueryWrapper(params, OutstockLog.class));
		return R.data(OutstockLogWrapper.build().pageVO(pages));
	}
}
