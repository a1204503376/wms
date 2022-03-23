package org.nodes.modules.wms.statistics;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.utils.CommonUtil;
import org.nodes.wms.core.stock.core.vo.LocRateRltVO;
import org.nodes.wms.core.stock.core.vo.UnsafeStockSkuVO;
import org.nodes.wms.core.system.vo.TaskVO;
import org.nodes.wms.core.outstock.so.vo.OutstockSkuRltVO;
import org.nodes.wms.statistics.cache.StatisticsCache;
import org.nodes.wms.statistics.dto.IdleSkuDTO;
import org.nodes.wms.statistics.service.IStatisticsService;
import org.nodes.wms.statistics.vo.*;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 统计控制器
 * @create 20200403
 */
@RestController
@AllArgsConstructor
@RequestMapping("statistics")
@Api(value = "查询统计", tags = "查询统计接口")
public class StatisticsController {

	private IStatisticsService statisticsService;

	/**
	 * 获取出库订单量
	 *
	 * @return
	 */
	@ApiLog("查询统计接口-获取出库订单量")
	@GetMapping("/soBillCount")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "获取出库订单量")
	public R<BillCountRltVO> soBillCount() {
		return R.data(statisticsService.soBillCount());
	}

	/**
	 * 获取入库订单量
	 *
	 * @return
	 */
	@ApiLog("查询统计接口-获取入库订单量")
	@GetMapping("/asnBillCount")
	@ApiOperationSupport(order = 2)
	@ApiOperation(value = "获取入库订单量")
	public R<BillCountRltVO> asnBillCount() {
		return R.data(statisticsService.asnBillCount());
	}

	/**
	 * 获取缺货的SKU数量
	 *
	 * @return
	 */
	@ApiLog("查询统计接口-获取缺货的SKU数量")
	@GetMapping("/stockoutSkuCount")
	@ApiOperationSupport(order = 3)
	@ApiOperation(value = "获取缺货的SKU数量")
	public R<SkuCountRltVO> stockoutSkuCount() {
		return R.data(statisticsService.outstockSkuCount());
	}

	/**
	 * 获取呆滞物品信息
	 *
	 * @return
	 */
	@ApiLog("查询统计接口-获取呆滞物品信息")
	@GetMapping("/idleSkuInfo")
	@ApiOperationSupport(order = 4)
	@ApiOperation(value = "获取呆滞物品信息")
	public R<IdleSkuRltVO> idleSkuInfo(IdleSkuDTO idleSkuDTO) {
		return R.data(statisticsService.getIdleSkuInfo(idleSkuDTO));
	}

	/**
	 * 获取一周内订单特征
	 *
	 * @return
	 */
	@ApiLog("查询统计接口-获取一周内订单特征")
	@GetMapping("/billTrait")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "获取一周内订单特征")
	public R<BillTraitRltVO> billTrait() {
		return R.data(statisticsService.getBillTrait());
	}

	/**
	 * 获取物品当前库存信息
	 *
	 * @return
	 */
	@ApiLog("查询统计接口-获取库存物品数量信息")
	@GetMapping("/stockSkuCount")
	@ApiOperationSupport(order = 7)
	@ApiOperation(value = "获取库存物品数量信息")
	public R<StockSkuCountRltVO> stockSkuCount() {
		return R.data(statisticsService.getStockSkuCount());
	}

	/**
	 * 获取库位占用信息
	 *
	 * @return
	 */
	@ApiLog("查询统计接口-获取库位占用信息")
	@GetMapping("/locOccupyInfo")
	@ApiOperationSupport(order = 8)
	@ApiOperation(value = "获取库位占用信息")
	public R<LocOccupyRltVO> locOccupyInfo() {
		return R.data(statisticsService.getLocOccupyInfo());
	}

	/**
	 * 获取出库频率最高的前十个SKU
	 *
	 * @return
	 */
	@ApiLog("查询统计接口-获取出库频率最高的前十个SKU")
	@GetMapping("/outstockRate")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "获取出库频率最高的前十个SKU")
	public R<List<OutstockSkuRltVO>> outstockRate() {
		return R.data(statisticsService.getOutstockSku());
	}

	/**
	 * 获取库位使用频率
	 *
	 * @return
	 */
	@ApiLog("查询统计接口-获取库位使用频率")
	@GetMapping("/locRate")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "获取库位使用频率")
	public R<List<LocRateRltVO>> getLocRate() {
		return R.data(statisticsService.getLocRate());
	}

	/**
	 * 获取未执行任务信息
	 *
	 * @return
	 */
	@ApiLog("查询统计接口-获取未执行任务信息")
	@GetMapping("/unExecTaskInfo")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "获取未执行任务信息")
	public R<List<TaskVO>> getUnExecTaskInfo() {
		return R.data(statisticsService.unExecTaskInfo());
	}

	/**
	 * 获取不在安全库存内的物品列表
	 *
	 * @return 不在安全库存内的物品列表
	 */
	@ApiLog("查询统计接口-获取不在安全库存内的物品列表")
	@GetMapping("/getUnsafeStockSkuList")
	@ApiOperationSupport(order = 12)
	@ApiOperation(value = "获取不在安全库存内的物品列表")
	public R<List<UnsafeStockSkuVO>> getUnsafeStockSkuList() {
		return R.data(statisticsService.getUnsafeStockSkuList());
	}

	/**
	 * 获取临期物品列表
	 *
	 * @return 临期物品列表
	 */
	@ApiLog("查询统计接口-获取临期物品列表")
	@GetMapping("/getAdventSkuList")
	@ApiOperationSupport(order = 13)
	@ApiOperation(value = "获取临期物品列表")
	public R<List<AdventSkuVO>> getAdventSkuList() {
		return R.data(statisticsService.getAdventSkuList());
	}

	/**
	 * 获取物品出库总数
	 *
	 * @return 所有物品出库总数
	 */
	@ApiLog("查询统计接口-获取物品出库总数")
	@GetMapping("/getSkuOutstockSummary")
	@ApiOperationSupport(order = 14)
	@ApiOperation(value = "获取物品出库总数")
	public R<List<SkuOutstockSummaryVO>> getSkuOutstockSummary() {
		return R.data(statisticsService.getSkuOutstockSummary());
	}

	/**
	 * 获取所有统计
	 *
	 * @return
	 */
	@ApiLog("查询统计接口-获取所有查询统计")
	@GetMapping("/all")
	@ApiOperationSupport(order = 100)
	@ApiOperation(value = "获取所有查询统计")
	public R<Object> all() {
		return R.data(statisticsService.all());
	}
}
