
package org.nodes.modules.wms.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.system.cache.BarcodeRuleCache;
import org.nodes.wms.core.system.entity.BarcodeRule;
import org.nodes.wms.core.system.service.IBarcodeRuleService;
import org.nodes.wms.core.system.vo.BarcodeRuleVO;
import org.nodes.wms.core.system.wrapper.BarCodeRuleWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *  条码规则控制器
 *
 * @author liangmei
 * @since 2019-12-16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/core/barcodeRule")
@Api(value = "条码规则", tags = "条码规则")
public class BarcodeRuleController extends BladeController {

	private IBarcodeRuleService barcodeRuleService;

	/**
	 * 条码规则详情
	 */
	@GetMapping("/detail")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "sbrId",value = "条码规则定义ID", paramType = "query", dataType = "string")
	})
	@ApiOperation(value = "条码规则详情", notes = "传入barcodeRule")
	public R<BarcodeRule> detail(@ApiIgnore @RequestParam Long sbrId) {
		IBarcodeRuleService barcodeRuleService = SpringUtil.getBean(IBarcodeRuleService.class);
		BarcodeRule barcodeRule = barcodeRuleService.getById(sbrId);

		return R.data(barcodeRule);
	}

	/**
	 * 条码规则列表查询
	 *
	 * @author zx
	 */
	@GetMapping("/list")
	@ApiOperation(value = "条码规则列表", notes = "传入barcodeRule")
	public R<List<BarcodeRuleVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<BarcodeRule> list =barcodeRuleService.list(Condition.getQueryWrapper(params, BarcodeRule.class));
		return R.data(BarCodeRuleWrapper.build().listVO(list));
	}

	/**
	 * 条码规则分页查询
	 */
	@GetMapping("/page")
	@ApiOperation(value = "条码规则分页", notes = "传入barcodeRule")
	public R<IPage<BarcodeRuleVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<BarcodeRule> page = barcodeRuleService.page(Condition.getPage(query), Condition.getQueryWrapper(params, BarcodeRule.class));

		return R.data(BarCodeRuleWrapper.build().pageVO(page));
	}
	/**
	 * 新增或修改条码规则
	 */
	@ApiLog("条码规则-新增或修改条码规则")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改条码规则", notes = "传入barcodeRule")
	public R submit(@Valid @RequestBody BarcodeRule barcodeRule) {
		boolean bool= barcodeRuleService.saveOrUpdate(barcodeRule);

		//if(bool)BarcodeRuleCache.saveOrUpdate(barcodeRule);

		return R.status(bool);
	}


	/**
	 * 删除条码规则
	 */
	@ApiLog("条码规则-删除条码规则")
	@PostMapping("/remove")
	@ApiOperation(value = "删除条码规则", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		boolean bool= barcodeRuleService.removeByIds(Func.toLongList(ids));

		//if(bool)BarcodeRuleCache.remove(Func.toLongList(ids));

		return R.status(bool);
	}


}
