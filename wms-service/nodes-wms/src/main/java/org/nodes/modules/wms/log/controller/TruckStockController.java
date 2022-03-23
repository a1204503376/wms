package org.nodes.modules.wms.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.outstock.loading.entity.TruckStock;
import org.nodes.wms.core.outstock.loading.service.ITruckStockService;
import org.nodes.wms.core.outstock.loading.vo.TruckStockVO;
import org.nodes.wms.core.outstock.loading.wrapper.TruckStockWrapper;
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
 *  控制器
 *
 * @author pengwei
 * @since 2020-04-16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/log/truckStock")
@Api(value = "", tags = "接口")
public class TruckStockController extends BladeController {

	private ITruckStockService truckStockService;

	/**
	 * 分页
	 */
	@ApiLog("车次接口-分页")
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入truckStock")
	public R<IPage<TruckStockVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<TruckStock> pages = truckStockService.page(Condition.getPage(query), Condition.getQueryWrapper(params, TruckStock.class)
			.lambda().orderByDesc(TruckStock::getLtsId));
		return R.data(TruckStockWrapper.build().pageVO(pages));
	}
}
