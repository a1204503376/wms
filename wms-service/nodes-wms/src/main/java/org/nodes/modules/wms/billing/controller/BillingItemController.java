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
import org.nodes.wms.core.billing.entity.BillingItem;
import org.nodes.wms.core.billing.vo.BillingItemVO;
import org.nodes.wms.core.billing.wrapper.BillingItemWrapper;
import org.nodes.wms.core.billing.dto.BillingItemDTO;
import org.nodes.wms.core.billing.service.IBillingItemService;
import org.springblade.core.boot.ctrl.BladeController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 计费项目 控制器
 *
 * @author NodeX
 * @since 2021-06-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/billing/item")
@Api(value = "计费项目", tags = "计费项目接口")
public class BillingItemController extends BladeController {

	private IBillingItemService billingItemService;

	/**
	 * 计费项目详情
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "计费项目详情", notes = "传入billingItem")
	public R<BillingItemVO> detail(BillingItemDTO billingItem) {
		BillingItem detail = billingItemService.getOne(Condition.getQueryWrapper(billingItem));
		return R.data(BillingItemWrapper.build().entityVO(detail));
	}

    /**
     * 计费项目列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "计费项目列表", notes = "传入billingItem")
    public R<List<BillingItemVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<BillingItem> list = billingItemService.list(Condition.getQueryWrapper(params, BillingItem.class));
		return R.data(BillingItemWrapper.build().listVO(list));
    }

	/**
	 * 计费项目分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "计费项目分页", notes = "传入billingItem")
	public R<IPage<BillingItemVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<BillingItem> pages = billingItemService.page(Condition.getPage(query), Condition.getQueryWrapper(params, BillingItem.class));
		return R.data(BillingItemWrapper.build().pageVO(pages));
	}


	/**
	 * 计费项目新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperation(value = "计费项目新增或修改", notes = "传入billingItem")
	public R submit(@Valid @RequestBody BillingItem billingItem) {
		return R.status(billingItemService.saveOrUpdate(billingItem));
	}


	/**
	 * 计费项目删除
	 */
	@PostMapping("/remove")
	@ApiOperation(value = "计费项目删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(billingItemService.removeByIds(Func.toLongList(ids)));
	}


}
