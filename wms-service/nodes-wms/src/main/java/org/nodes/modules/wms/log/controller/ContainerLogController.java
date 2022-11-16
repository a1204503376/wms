package org.nodes.modules.wms.log.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.wms.core.instock.asn.entity.ContainerLog;
import org.nodes.wms.core.instock.asn.service.IContainerLogService;
import org.nodes.wms.core.instock.asn.vo.ContainerLogVO;
import org.nodes.wms.core.instock.asn.wrapper.ContainerLogWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;

/**
 * 清点记录 控制器
 *
 * @author NodeX
 * @since 2020-01-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/log/containerLog")
@ApiIgnore
@Api(value = "清点记录", tags = "清点记录接口")
public class ContainerLogController extends BladeController {

	private IContainerLogService containerLogService;


	/**
	 * 分页 清点记录
	 */
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入containerLog")
	public R<IPage<ContainerLogVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<ContainerLog> page = containerLogService.page(Condition.getPage(query), Condition.getQueryWrapper(params, ContainerLog.class));

		return R.data(ContainerLogWrapper.build().pageVO(page));
	}
}
