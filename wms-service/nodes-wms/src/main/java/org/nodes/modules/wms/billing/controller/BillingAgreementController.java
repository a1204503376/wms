package org.nodes.modules.wms.billing.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import org.nodes.wms.core.billing.dto.OwnerBillPageDTO;
import org.nodes.wms.core.billing.vo.OwnerBillVO;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.billing.entity.BillingAgreement;
import org.nodes.wms.core.billing.vo.BillingAgreementVO;
import org.nodes.wms.core.billing.wrapper.BillingAgreementWrapper;
import org.nodes.wms.core.billing.dto.BillingAgreementDTO;
import org.nodes.wms.core.billing.service.IBillingAgreementService;
import org.springblade.core.boot.ctrl.BladeController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 计费协议 控制器
 *
 * @author NodeX
 * @since 2021-06-19
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/billing/agreement")
@Api(value = "计费协议", tags = "计费协议接口")
public class BillingAgreementController extends BladeController {

	private IBillingAgreementService billingAgreementService;

	/**
	 * 计费协议详情
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "计费协议详情", notes = "传入billingAgreement")
	public R<BillingAgreementVO> detail(BillingAgreementDTO billingAgreement) {
		BillingAgreement detail = billingAgreementService.getOne(Condition.getQueryWrapper(billingAgreement));
		return R.data(BillingAgreementWrapper.build().entityVO(detail));
	}

    /**
     * 计费协议列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "计费协议列表", notes = "传入billingAgreement")
    public R<List<BillingAgreementVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<BillingAgreement> list = billingAgreementService.list(Condition.getQueryWrapper(params, BillingAgreement.class));
		return R.data(BillingAgreementWrapper.build().listVO(list));
    }

	/**
	 * 计费协议分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "计费协议分页", notes = "传入billingAgreement")
	public R<IPage<BillingAgreementVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<BillingAgreement> pages = billingAgreementService.page(
			Condition.getPage(query),
			Condition.getQueryWrapper(params, BillingAgreement.class));
		return R.data(BillingAgreementWrapper.build().pageVO(pages));
	}


	/**
	 * 计费协议新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperation(value = "计费协议新增或修改", notes = "传入billingAgreement")
	public R submit(@Valid @RequestBody BillingAgreement billingAgreement) {
		return R.status(billingAgreementService.saveOrUpdate(billingAgreement));
	}


	/**
	 * 计费协议删除
	 */
	@PostMapping("/remove")
	@ApiOperation(value = "计费协议删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(billingAgreementService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 货主账单
	 * @return
	 */
	@GetMapping("/owner/bill/page")
	@ApiOperation(value = "货主账单", notes = "传入ids")
	public R<IPage<OwnerBillVO>> ownerBillPage(OwnerBillPageDTO ownerBillPageDTO, Query query) {
		return R.data(billingAgreementService.selectPageByOwnerBill(ownerBillPageDTO, query));
	}
}
