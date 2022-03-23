package org.nodes.modules.wms.outstock.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.outstock.loading.wrapper.SoRegisterWrapper;
import org.nodes.wms.core.outstock.so.entity.SoRegister;
import org.nodes.wms.core.outstock.so.service.ISoRegisterService;
import org.nodes.wms.core.outstock.so.vo.SoRegisterVO;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * 装车登记 控制器
 *
 * @author zhongls
 * @since 2020-05-07
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/outstock/soRegister")
@Api(value = "装车登记", tags = "装车登记接口")
public class SoRegisterController extends BladeController {

	private ISoRegisterService registerService;

	/**
	 * 装车登记详情
	 */
	@ApiLog("装车登记接口-装车登记详情")
	@GetMapping("/detail")
	@ApiOperation(value = "装车登记详情", notes = "传入register")
	public R<SoRegister> detail(SoRegister soRegister) {
		SoRegister detail = registerService.getOne(Condition.getQueryWrapper(soRegister));
		return R.data(detail);
	}

	/**
	 * 装车登记列表
	 */
	@ApiLog("装车登记接口-装车登记列表")
	@GetMapping("/list")
	@ApiOperation(value = "装车登记列表", notes = "传入register")
	public R<List<SoRegister>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<SoRegister> list = registerService.list(Condition.getQueryWrapper(params, SoRegister.class));
		return R.data(list);
	}

	/**
	 * 装车登记分页
	 */
	@ApiLog("装车登记接口-装车登记分页")
	@GetMapping("/page")
	@ApiOperation(value = "装车登记分页", notes = "传入register")
	public R<IPage<SoRegisterVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SoRegister> pages = registerService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SoRegister.class));
		return R.data(SoRegisterWrapper.build().pageVO(pages));
	}

	/**
	 * 装车登记新增或修改
	 */
	@ApiLog("装车登记接口-装车登记新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "装车登记新增或修改", notes = "传入register")
	public R submit(@Valid @RequestBody SoRegister soRegister) {
		return R.status(registerService.saveOrUpdate(soRegister));
	}


	/**
	 * 装车登记删除
	 */
	@ApiLog("装车登记接口-装车登记删除")
	@PostMapping("/remove")
	@ApiOperation(value = "装车登记删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(registerService.removeByIds(Func.toLongList(ids)));
	}


}
