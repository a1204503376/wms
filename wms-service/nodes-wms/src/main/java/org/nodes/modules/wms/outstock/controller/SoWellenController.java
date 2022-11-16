package org.nodes.modules.wms.outstock.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.outstock.loading.wrapper.SoWellenWrapper;
import org.nodes.wms.core.outstock.so.entity.Wellen;
import org.nodes.wms.core.outstock.so.service.IWellenService;
import org.nodes.wms.core.outstock.so.vo.WellenVO;
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
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/wms/outstock/wellen")
@Api(value = "波次划分", tags = "波次划分接口")
public class SoWellenController extends BladeController {
	private IWellenService wellenService;

	/**
	 * 波次划分分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "波次划分分页", notes = "传入soWellen")
	public R<IPage<Wellen>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Wellen> pages = wellenService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Wellen.class));
		return R.data(pages);
	}

	/**
	 * 波次划分详细
	 */
	@ApiLog("波次划分-详细")
	@GetMapping("/detail")
	@ApiOperation(value = "波次划分详情", notes = "传入soWellen")
	public R<WellenVO> detail(Wellen wellen) {
		return R.data(wellenService.detail(wellen));
	}

	/**
	 * 波次划分列表
	 */
	@GetMapping("/list")
	@ApiOperation(value = "波次策略列表", notes = "传入wellen")
	public R<List<Wellen>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<Wellen> wellenList = wellenService.list(Condition.getQueryWrapper(params, Wellen.class));
		return R.data(wellenList);
	}
}
