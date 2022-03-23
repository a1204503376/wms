package org.nodes.modules.wms.billing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.billing.entity.BillingRuleHeader;
import org.nodes.wms.core.billing.vo.BillingRuleHeaderVO;
import org.nodes.wms.core.billing.wrapper.BillingRuleHeaderWrapper;
import org.nodes.wms.core.billing.dto.BillingRuleHeaderDTO;
import org.nodes.wms.core.billing.service.IBillingRuleHeaderService;
import org.springblade.core.boot.ctrl.BladeController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 计费规则主表 控制器
 *
 * @author NodeX
 * @since 2021-06-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/billing/rule-header")
@Api(value = "计费规则主表", tags = "计费规则主表接口")
public class BillingRuleHeaderController extends BladeController {

	private IBillingRuleHeaderService billingRuleHeaderService;

	/**
	 * 计费规则主表详情
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "计费规则主表详情", notes = "传入billingRuleHeader")
	public R<BillingRuleHeaderVO> detail(BillingRuleHeaderDTO billingRuleHeader) {
		return R.data(billingRuleHeaderService.getDetail(billingRuleHeader.getId()));
	}

    /**
     * 计费规则主表列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "计费规则主表列表", notes = "传入billingRuleHeader")
    public R<List<BillingRuleHeaderVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<BillingRuleHeader> list = billingRuleHeaderService.list(Condition.getQueryWrapper(params, BillingRuleHeader.class));
		return R.data(BillingRuleHeaderWrapper.build().listVO(list));
    }

	/**
	 * 计费规则主表分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "计费规则主表分页", notes = "传入billingRuleHeader")
	public R<IPage<BillingRuleHeaderVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<BillingRuleHeader> pages = billingRuleHeaderService.page(
			Condition.getPage(query),
			Condition.getQueryWrapper(params, BillingRuleHeader.class));
		return R.data(BillingRuleHeaderWrapper.build().pageVO(pages));
	}


	/**
	 * 计费规则主表新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperation(value = "计费规则主表新增或修改", notes = "传入billingRuleHeader")
	public R submit(@Valid @RequestBody BillingRuleHeaderDTO billingRuleHeader) {
		return R.status(billingRuleHeaderService.saveOrUpdate(billingRuleHeader));
	}


	/**
	 * 计费规则主表删除
	 */
	@PostMapping("/remove")
	@ApiOperation(value = "计费规则主表删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(billingRuleHeaderService.removeByIds(Func.toLongList(ids)));
	}
}
