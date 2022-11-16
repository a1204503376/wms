package org.nodes.modules.wms.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.modules.wms.log.vo.InstockLogVO;
import org.nodes.modules.wms.log.wrapper.InstockLogWrapper;
import org.nodes.wms.dao.putaway.entities.StInstockLog;
import org.nodes.wms.core.strategy.service.IInstockLogService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * 上架记录 控制器
 *
 * @author zx
 * @since 2020-04-27
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/log/instockLog")
@Api(value = "上架记录", tags = "上架记录接口")
public class InstockLogController extends BladeController {

	private IInstockLogService instockLogService;

	/**
	 * 上架记录分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "上架记录分页", notes = "传入putawayLog")
	public R<IPage<InstockLogVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<StInstockLog> pages = instockLogService.page(Condition.getPage(query), Condition.getQueryWrapper(params, StInstockLog.class));
		return R.data(InstockLogWrapper.build().pageVO(pages));
	}
}
