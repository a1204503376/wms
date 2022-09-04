package org.nodes.modules.wms.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.outstock.so.entity.SoPick;
import org.nodes.wms.core.outstock.so.service.ISoPickService;
import org.nodes.wms.core.outstock.so.vo.SoPickVO;
import org.nodes.wms.core.outstock.so.wrapper.SoPickWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;

/**
 * 拣货记录日志 控制器
 *
 * @author zx
 * @since 2020-03-04
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/log/soPick")
@Api(value = "拣货记录日志", tags = "拣货记录日志接口")
public class SoPickController extends BladeController {

	private ISoPickService soPickService;


	/**
	 * 自定义分页 拣货记录日志
	 */
	@GetMapping("/page")
	@ApiOperation(value = "拣货记录分页", notes = "传入soPick")
	public R<IPage<SoPickVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SoPick> pages = soPickService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SoPick.class));
		return R.data(SoPickWrapper.build().pageVO(pages));
	}


	/**
	 * 根据波次ID和单据ID获取拣货计划列表
	 */
	@GetMapping("/list")
	@ApiOperation(value = "拣货记录列表", notes = "传参SoPick根据前端订单ID或者波次ID封装来查询")
	public R<List<SoPick>> list(SoPick soPick) {
		List<SoPick> soPickList = soPickService.list(Condition.getQueryWrapper(soPick));
		return R.data(soPickList);
	}

}
