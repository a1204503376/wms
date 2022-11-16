package org.nodes.modules.wms.outstock.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.outstock.loading.entity.SoTruckDetail;
import org.nodes.wms.core.outstock.loading.entity.SoTruckHeader;
import org.nodes.wms.core.outstock.loading.service.ISoTruckDetailService;
import org.nodes.wms.core.outstock.loading.service.impl.SoTruckDetailServiceImpl;
import org.nodes.wms.core.outstock.loading.vo.SoTruckDetailVO;
import org.nodes.wms.core.outstock.loading.vo.SoTruckHeaderVO;
import org.nodes.wms.core.outstock.loading.wrapper.SoTruckDetailWrapper;
import org.nodes.wms.core.outstock.loading.wrapper.SoTruckHeaderWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;

/**
 * 车次明细 控制器
 *
 * @author zhux
 * @since 2022-3
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/outstock/soTruckDetail")
@Api(value = "车次明细", tags = "车次明细接口")
public class SoTruckDetailController extends BladeController {

	private ISoTruckDetailService soTruckDetailService;

	/**
	 * 删除车次明细
	 */
	@ApiLog("车次明细接口-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(soTruckDetailService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 通过头表id获取所有明细
	 */
	@GetMapping("/list")
	@ApiOperation(value = "分页", notes = "传入头表id")
	public R<List<SoTruckDetailVO>> list(@ApiParam(value = "头表主键", required = true) @RequestParam String truckId) {
		List<SoTruckDetail> list = soTruckDetailService.list(Condition.getQueryWrapper(new SoTruckDetail())
			.lambda()
			.eq(SoTruckDetail::getTruckId, Func.toLong(truckId)));
		return R.data(SoTruckDetailWrapper.build().listVO(list));
	}

}
