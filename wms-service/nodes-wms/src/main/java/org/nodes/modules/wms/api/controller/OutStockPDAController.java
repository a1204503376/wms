
package org.nodes.modules.wms.api.controller;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.instock.asn.vo.BoxItemVo;
import org.nodes.wms.core.instock.asn.vo.LpnItemVo;
import org.nodes.wms.core.outstock.so.entity.PickPlan;
import org.nodes.wms.core.stock.core.vo.SerialVO;
import org.nodes.wms.core.instock.asn.service.IAsnHeaderService;
import org.nodes.wms.core.instock.asn.vo.InStockSubmitVO;
import org.nodes.wms.core.outstock.so.entity.SoPick;
import org.nodes.wms.core.outstock.so.service.IPickByBoxService;
import org.nodes.wms.core.outstock.so.service.IPickPlanService;
import org.nodes.wms.core.outstock.so.service.ISoPickService;
import org.nodes.wms.core.outstock.so.vo.PickLogVO;
import org.nodes.wms.core.outstock.so.vo.PickSkuVO;
import org.nodes.wms.core.outstock.so.vo.PickTaskSubmitVO;
import org.nodes.wms.core.outstock.so.vo.PickTaskVO;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 出库(手持) 控制器
 *
 * @author xujy
 * @since 2020-02-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/ApiPDA/OutStock")
@ApiSort(1)
@Api(value = "手持接口", tags = "手持出库接口")
public class OutStockPDAController extends BladeController {
	//拣货计划
	private IPickPlanService pickService;
	//拣货记录
	private ISoPickService soPickService;
    //出库
	private IAsnHeaderService asnHeaderService;
	//按箱拣货
	private IPickByBoxService pickByBoxService;

	@ApiLog("PDA-托盘转移")
	@PostMapping(value = "/submitMOVE")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "托盘转移", notes = "传入stock")
	public R submitMOVE (@RequestBody InStockSubmitVO inStockSubmitVO) {
		return R.status(asnHeaderService.submitMove(inStockSubmitVO));
	}
	/**
	 * 按托拣货-根据托盘号获取信息
	 *
	 * @param lpnCode
	 * @param pickPlanId
	 * @return
	 */
	@ApiLog("PDA-根据托盘号获取信息")
	@GetMapping(value = "/getInfoByLpnCode")
	@ApiOperation(value = "根据箱号获取推荐信息", notes = "lpnCode")
	public R<LpnItemVo> getInfoByLpnCode(@RequestParam String lpnCode,@RequestParam String pickPlanId,@RequestParam BigDecimal qty) {
		return R.data(pickService.getInfoByLpnCode(lpnCode,pickPlanId,qty));
	}
	/**
	 * 获得拣货信息
	 */
	@ApiLog("PDA-获得拣货信息")
	@GetMapping("/getPickInfo")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "wellenNo", value = "波次编码", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "taskId", value = "任务ID", paramType = "query", dataType = "string")
	})
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "获得拣货信息", notes = "传入参数")
	public R<PickTaskVO> getPickInfo(@ApiIgnore @RequestParam Map<String,String> param) {
		return R.data(pickService.getByTaskId(param));
	}


	/**
	 * 提交拣货信息
	 */
	@ApiLog("PDA-提交拣货信息")
	@PostMapping("/submitPickInfo")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "提交拣货信息", notes = "传入拣货信息")
	public R submitPickInfo(@Valid @RequestBody PickTaskSubmitVO pickTaskSubmitVO) {
		return R.data(pickService.submitPickInfo(pickTaskSubmitVO));
	}
	/**
	 * 提交拣货信息
	 */
	@ApiLog("PDA-按托拣货提交")
	@PostMapping("/submitTrayPickInfo")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "按托拣货提交", notes = "按托拣货提交")
	public R submitTrayPickInfo(@RequestBody PickTaskSubmitVO pickTaskSubmitVO) {
		return R.data(pickService.submitTrayPickInfo(pickTaskSubmitVO));
	}
	/**
	 * 提交拣货信息
	 */
	@ApiLog("PDA-按单拣货提交")
	@PostMapping("/submitPickInfoWithOrder")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "提交拣货信息", notes = "传入拣货信息")
	public R submitPickInfoWithOrder(@RequestBody PickTaskSubmitVO pickTaskSubmitVO) {
		return R.data(pickService.submitPickInfoWithOrder(pickTaskSubmitVO));
	}
	/**
	 * 查询拣货记录列表
	 * @param
	 * @return
	 */
	@ApiLog("PDA-拣货记录列表")
	@GetMapping("/getSopickLog")
	@ApiOperationSupport(order = 4)
	@ApiImplicitParams({
		@ApiImplicitParam(name = "wellenNo", value = "波次编码", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "taskId", value = "任务ID", paramType = "query", dataType = "string")
	})
	@ApiOperation(value = "拣货记录列表", notes = "传入参数")
	public R<Map<String, Object>> getListById(@ApiIgnore @RequestParam Map<String,String> param) {

		String wellenNo = param.get("wellenNo");
//		String taskId = param.get("taskId");

		SoPick soPick = new SoPick();
//		if (Func.isNotEmpty(taskId)){
//			soPick.setTaskId(Long.parseLong(taskId));
//		}
		if (Func.isNotEmpty(wellenNo)){
			soPick.setWellenNo(Long.parseLong(wellenNo));
		}

		List<SoPick> list = soPickService.list(Condition.getQueryWrapper(soPick));

		Map<String, Object> result = new HashMap<>();
		List<PickLogVO> pickLog = soPickService.getPickLog(list);
		if(Func.isNotEmpty(pickLog)){
			Map<String, BigDecimal> collect1 = pickLog.stream().collect(Collectors.groupingBy(PickLogVO::getSkuCode))
				.entrySet()
				.stream().collect(Collectors.toMap(Map.Entry::getKey, m -> m.getValue()
					.stream()
					.filter(d -> Objects.nonNull(d.getRealCountQty()))
					.map(PickLogVO::getRealCountQty).reduce(BigDecimal.ZERO, BigDecimal::add)));

			Map<String, BigDecimal> collect = pickLog.stream().collect(Collectors.groupingBy(PickLogVO::getSkuCode)).entrySet()
				.stream().collect(Collectors.toMap(Map.Entry::getKey, m -> m.getValue().stream()
			.collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(PickLogVO::getPlanCountQty))), ArrayList::new))
				.stream()
					.filter(d -> Objects.nonNull(d.getPlanCountQty()))
					.map(PickLogVO::getPlanCountQty).reduce(BigDecimal.ZERO, BigDecimal::add))
			);

			pickLog.forEach(pickLogVO->{
				pickLogVO.setTotalPlanQty(collect.get(pickLogVO.getSkuCode()));
				pickLogVO.setTotalScanQty(collect1.get(pickLogVO.getSkuCode()));
			});
		}
		result.put("pickPlans", pickLog);
		return R.data(result);
	}

	/**
	 * 容器序列号验证
	 */
	@ApiLog("PDA-容器序列号验证")
	@GetMapping("/isHasSerial")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "容器序列号验证", notes = "传入容器编码，物品ID，序列号")
	public R isHasSerial(SerialVO serialVO) {
		return R.status(pickService.lpnHasSerial(serialVO));
	}


	/**
	 * 按箱拣货获得拣货信息
	 */
	@ApiLog("PDA-按箱拣货获得拣货信息")
	@GetMapping("/getPickInfoByBox")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "wellenNo", value = "波次编码", paramType = "query", dataType = "string")
	})
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "按箱拣货获得拣货信息", notes = "传入波次编码")
	public R<PickTaskVO> getPickInfoByBox(@ApiIgnore @RequestParam Map<String,String> param) {
		return R.data(pickByBoxService.getByTaskId(param));
	}

	/**
	 * 按单拣货获得拣货信息
	 */
	@ApiLog("PDA-按单拣货获得拣货信息")
	@GetMapping("/getPickInfoByOrder")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "wellenNo", value = "波次编码", paramType = "query", dataType = "string"),
		@ApiImplicitParam(name = "soBillNo", value = "出库单编码", paramType = "query", dataType = "string")
	})
	@ApiOperationSupport(order = 6)
	@ApiOperation(value = "按单拣货获得拣货信息", notes = "传入波次编码")
	public R<List<PickPlan>> getPickInfoByOrder(@ApiIgnore @RequestParam Map<String,String> param) {
		return R.data(pickByBoxService.getPickInfoByOrder(param));
	}

	/**
	 * 按箱拣货获得拣货信息
	 */
	@ApiLog("PDA-按箱拣货获得物品已拣总数量")
	@PostMapping("/getTotalScanQtyBySku")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "按箱拣货获得物品已拣总数量", notes = "传入波次编码")
	public R<PickSkuVO> getTotalScanQtyBySku(@RequestParam String wellenNo, @RequestParam  String skuCode){
		return R.data(pickByBoxService.getTotalScanQtyBySku(wellenNo,skuCode));
	}

	/**
	 * 按箱拣货提交拣货信息
	 */
	@ApiLog("PDA-按箱拣货提交拣货信息")
	@PostMapping("/submitPickInfoByBox")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "按箱拣货提交拣货信息", notes = "传入拣货信息")
	public R submitPickInfoByBox(@Valid @RequestBody PickTaskSubmitVO pickTaskSubmitVO) {
		return R.data(pickByBoxService.submitPickInfo(pickTaskSubmitVO));
	}


}
