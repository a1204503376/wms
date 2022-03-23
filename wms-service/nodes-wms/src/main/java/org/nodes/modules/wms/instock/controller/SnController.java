package org.nodes.modules.wms.instock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.wms.core.instock.asn.entity.Sn;
import org.nodes.wms.core.instock.asn.service.ISnService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 入库货品序列号 控制器
 *
 * @author NodeX
 * @since 2020-01-15
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/instock/sn")
@Api(value = "入库货品序列号", tags = "入库货品序列号接口")
public class SnController extends BladeController {

	private ISnService snService;

	/**
	 * 分页 入库货品序列号
	 */
	@ApiLog("入库货品序列号接口-分页")
	@GetMapping("/list")
	@ApiOperation(value = "分页", notes = "传入sn")
	public R<List<String>> list(Sn sn) {
		List<Sn> list = snService.list(Condition.getQueryWrapper(sn));
		return R.data(NodesUtil.toList(list, Sn::getSnDetailCode));
	}
}
