package org.nodes.modules.wms.outstock.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.outstock.so.dto.CreatePickPlanDTO;
import org.nodes.wms.core.outstock.so.dto.PickPlanDTO;
import org.nodes.wms.core.outstock.so.dto.SavePickPlanDTO;
import org.nodes.wms.core.outstock.so.entity.PickPlan;
import org.nodes.wms.core.outstock.so.service.IPickPlanService;
import org.nodes.wms.core.outstock.so.service.impl.ISwitchAllocStrategyMode;
import org.nodes.wms.core.outstock.so.service.impl.SwitchAllocStrategyModeUtil;
import org.nodes.wms.core.outstock.so.vo.CreateDetailPickPlanVO;
import org.nodes.wms.core.outstock.so.vo.CreatePickPlanVO;
import org.nodes.wms.core.outstock.so.vo.PickPlanVO;
import org.nodes.wms.core.outstock.so.wrapper.PickPlanWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.List;

/**
 * 按件拣货 控制
 *
 * @Author zx
 * @Date 2020/3/4
 **/
@RestController
@AllArgsConstructor
@RequestMapping("/wms/outstock/pickplan")
@Api(value = "拣货计划表", tags = "拣货计划表")
public class PickPlanController extends BladeController {

	private IPickPlanService pickService;

	/**
	 * 按单分配-生成
	 *
	 * @param createPickPlanDTO 创建拣货计划参数
	 * @return
	 */
	@ApiLog("拣货计划-生成")
	@PostMapping("/create")
	@ApiOperation(value = "按单分配-生成", notes = "传入CreatePickPlanDTO")
	public R<CreatePickPlanVO> createPickPlan(
		@ApiParam(value = "主键集合", required = true) @Valid @RequestBody CreatePickPlanDTO createPickPlanDTO) {
		CreatePickPlanVO createPickPlanVO = SwitchAllocStrategyModeUtil
			.executeStrategy(ISwitchAllocStrategyMode.AllocTypeEnum.ORDER, createPickPlanDTO);
		return R.data(createPickPlanVO);
	}

	/**
	 * 按单分配-保存
	 *
	 * @return
	 */
	@ApiLog("拣货计划-保存")
	@PostMapping("/save")
	@ApiOperation(value = "按单分配-保存", notes = "传入需要保存的拣货计划")
	public R savePickPlan(@Valid @RequestBody SavePickPlanDTO savePickPlanDTO) {
		return R.data(pickService.savePickPlan(savePickPlanDTO));
	}

	/**
	 * 按单取消分配
	 *
	 * @param ids 多个订单主键ID
	 * @return
	 */
	@ApiLog("拣货计划-按单取消分配")
	@PostMapping("/rollback")
	@ApiOperation(value = "取消分配", notes = "传入Ids")
	public R rollback(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.data(pickService.rollback(Func.toLongList(ids)));
	}

	@ApiLog("拣货计划-明细分配")
	@PostMapping("/getDetailPickPlan")
	@ApiOperation(value = "出库单ID", notes = "出库单ID，多个用英文逗号分隔")
	public R<List<CreateDetailPickPlanVO>> getDetailPickPlan(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.data(pickService.getDetailPickPlan(Func.toLongList(ids)));
	}

	/**
	 * 分页 拣货计划表
	 */
	@GetMapping("/page")
	@ApiOperation(value = "拣货计划查看", notes = "传入pickPlan")
	public R<IPage<PickPlanVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<PickPlan> pages = pickService.page(Condition.getPage(query), Condition.getQueryWrapper(params, PickPlan.class));
		return R.data(PickPlanWrapper.build().pageVO(pages));
	}

	/**
	 * 撤销拣货
	 */
	@ApiLog("撤销拣货")
	@GetMapping("/cancelPick")
	@ApiOperation(value = "撤销拣货", notes = "")
	public R cancelPick(@NotBlank(message = "请选择要撤销到记录！") @RequestParam String ids){
		pickService.cancelPick(ids);
		return null;
	}

}
