package org.nodes.modules.wms.core.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.cache.SerialNoCache;
import org.nodes.wms.core.relenishment.dto.RelHeaderDTO;
import org.nodes.wms.core.relenishment.entity.RelHeader;
import org.nodes.wms.core.relenishment.service.IRelHeaderService;
import org.nodes.wms.core.relenishment.vo.RelHeaderVo;
import org.nodes.wms.core.relenishment.wrapper.RelHeaderWrapper;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * 补货 控制器
 *
 * @author whj
 * @since 2019-12-24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/core/relenishment")
@Api(value = "补货", tags = "补货接口")
public class ReplenishmentController extends BladeController {

	private IStockService stockService;
	private IRelHeaderService relHeaderService;

	/**
	 * 分页查询补货单据列表
	 */
	@ApiLog("补货接口-分页查询补货单据列表")
	@GetMapping("/page")
	@ApiOperation(value = "库存分页", notes = "传入stock")
	public R<IPage<RelHeaderVo>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<RelHeader> page = relHeaderService.page(Condition.getPage(query), Condition.getQueryWrapper(params, RelHeader.class));
		return R.data(RelHeaderWrapper.build().pageVO(page));
	}

	/**
	 * 创建补货单
	 */
	@ApiLog("补货接口-创建补货单")
	@PostMapping("/create")
	@ApiOperation(value = "创建补货单", notes = "创建补货单")
	public R createRelenishOrder(@RequestBody RelHeaderDTO relHeaderDTO) {
		relHeaderService.createRelenishOrder(relHeaderDTO);
		return R.success("创建补货单已成功！");
	}
	/**
	 * 详情 补货单头表
	 */
	@ApiLog("补货接口-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "补货单头表详情", notes = "传入入库单ID")
	public R<RelHeaderVo> detail(@RequestParam Long relBillId) {
		return R.data(relHeaderService.getDetail(relBillId));
	}

	/**
	 * 获得补货单编号
	 *
	 * @return
	 */
	@ApiLog("补货接口-获得补货单编号")
	@GetMapping("/getRelBillNo")
	@ApiOperation(value = "获得补货单编号", notes = "")
	public R<String> getRelBillNo() {
		return R.data("REL" + SerialNoCache.getSerialDateNo("REL", 8));
	}

	@ApiLog("补货接口-获取创建人")
	@GetMapping("/defaultUser")
	@ApiOperation(value = "创建人", notes = "创建人")
	public R<BladeUser> defaultUser() {
		return R.data(AuthUtil.getUser());
	}
	/**
	 * 新增或修改 收货单头表
	 */
	@ApiLog("补货接口-补货单头表新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "补货单头表新增或修改", notes = "补货单对象")
	public R submit(@Valid @RequestBody RelHeaderVo relHeaderVo) {
		return R.status(relHeaderService.saveOrUpdate(relHeaderVo));
	}
	/**
	 * 获取订单是否允许编辑
	 *
	 * @param relBillId
	 * @return
	 */
	@ApiLog("补货接口-获取订单是否允许编辑")
	@GetMapping("/canEdit")
	@ApiOperation(value = "获取订单是否允许编辑", notes = "传入补库单ID")
	public R canEdit(@Valid @ApiParam(value = "入库单ID", required = true) @RequestParam Long relBillId) {
		return R.data(relHeaderService.canEdit(relBillId));
	}

	/**
	 * 分配任务
	 */
	@ApiLog("补货接口-分配任务")
	@PostMapping("/relTask")
	@ApiOperation(value = "分配任务", notes = "countHeaderVO")
	public R relTask(String ids) {
		return R.data(relHeaderService.relTask(ids));
	}
	/**
	 * 校验自动创建补货任务明细是否需要人工干预
	 */
	@ApiLog("补货接口-校验自动创建补货任务明细是否需要人工干预")
	@PostMapping("/checkTask")
	@ApiOperation(value = "校验自动创建补货任务明细是否需要人工干预", notes = "countHeaderVO")
	public R checkTask(String ids) {
		return R.data(relHeaderService.checkTask(ids));
	}
	/**
	 * 删除 补货单头表
	 */
	@ApiLog("补货接口-补货单头表删除")
	@PostMapping("/remove")
	@ApiOperation(value = "补货单头表删除", notes = "传入IDS")
	public R remove(String ids) {
		return R.status(relHeaderService.removeDataByIds(ids));
	}
}

