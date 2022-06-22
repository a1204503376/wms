
package org.nodes.modules.wms.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springblade.core.log.exception.ServiceException;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSort;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.stock.core.dto.StockQueryDTO;
import org.nodes.wms.dao.stock.entities.Stock;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.vo.SerialVO;
import org.nodes.wms.core.count.dto.RandomCheckSubmitDTO;
import org.nodes.wms.core.count.dto.RandomCountDTO;
import org.nodes.wms.core.count.entity.CountHeader;
import org.nodes.wms.core.count.entity.CountRecord;
import org.nodes.wms.core.count.enums.StockCountStateEnum;
import org.nodes.wms.core.count.service.ICountHeaderService;
import org.nodes.wms.core.count.service.ICountRecordService;
import org.nodes.wms.core.count.vo.CountHeaderVO;
import org.nodes.wms.core.count.vo.CountRecordVO;
import org.nodes.wms.core.count.vo.RandomCountRltVO;
import org.nodes.wms.core.count.wrapper.CountRecordWrapper;
import org.nodes.wms.core.outstock.so.service.IPickPlanService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 出库(手持) 控制器
 *
 * @author xujy
 * @since 2020-02-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api/ApiPDA/StockCountPDA")
@ApiSort(1)
@Api(value = "手持接口", tags = "盘点接口")
public class StockCountPDAController extends BladeController {
	private ICountRecordService countRecordService;
	//拣货计划
	private IPickPlanService pickService;
	private ICountHeaderService countHeaderService;
	private IStockService stockService;
	/**
	 * 分页 盘点记录
	 */
	@ApiLog("PDA-分页盘点记录")
	@GetMapping("/countRecords")
	@ApiOperation(value = "分页", notes = "盘点单号")
	public R<IPage<CountRecordVO>> countRecords(CountRecord countRecordVO, org.springblade.core.mp.support.Query query) {
		IPage<CountRecord> pages = countRecordService.page(Condition.getPage(query), Condition.getQueryWrapper(countRecordVO)
			.lambda().orderByDesc(CountRecord::getWcrId));
		return R.data(CountRecordWrapper.build().pageVO(pages));
	}
	/**
	 * 随机盘点提交
	 */
	@ApiLog("PDA-随机盘点提交")
	@PostMapping("/randomCountSubmit")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "随机盘点提交", notes="传入参数RandomCountDTO")
	public R<RandomCountRltVO> randomCountSubmit(@Valid @RequestBody RandomCountDTO randomCountDTO){
		return R.data(countRecordService.randomCountSubmit(randomCountDTO));
	}
	/**
	 * 欣天新随机盘点提交
	 */
	@ApiLog("PDA-欣天新随机盘点提交")
	@PostMapping("/randomCheckSubmit")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "欣天新随机盘点提交", notes="传入参数RandomCountDTO")
	public R randomCheckSubmit(@Valid @RequestBody RandomCheckSubmitDTO randomCheckSubmitDTO){
		return R.status(countRecordService.randomCheckSubmit(randomCheckSubmitDTO));
	}
	/**
	 * 欣天新盘点任务提交
	 */
	@ApiLog("PDA-欣天新盘点任务提交")
	@PostMapping("/randomCheckTaskSubmit")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "欣天新盘点任务提交", notes="传入参数RandomCountDTO")
	public R randomCheckTaskSubmit(@Valid @RequestBody RandomCheckSubmitDTO randomCheckSubmitDTO){
		return R.status(countRecordService.randomCheckTaskSubmit(randomCheckSubmitDTO));
	}
	/**
	 * 单据盘点提交
	 */
	@ApiLog("PDA-单据盘点提交")
	@PostMapping("/billCountSubmit")
	@ApiOperationSupport(order = 9)
	@ApiOperation(value = "单据盘点提交", notes="传入参数RandomCountDTO")
	public R<RandomCountRltVO> billCountSubmit(@Valid @RequestBody RandomCountDTO randomCountDTO){
		return R.data(countRecordService.billCountSubmit(randomCountDTO));
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
	 * Pda获取盘点单详情
	 */
	@ApiLog("PDA-手持获取盘点单详情")
	@GetMapping("/detailPda")
	@ApiOperationSupport(order = 1)
	@ApiOperation(value = "手持获取盘点单详情", notes = "传入countHeader")
	public R<CountHeader> detailPda(String countBillNo) {
		if(Func.isNull(countBillNo))
			throw new ServiceException("单据编号不能为空");
		CountHeader one = countHeaderService.getCountHeaderByNo(countBillNo);
		if(Func.isEmpty(one)){
			throw new ServiceException("不存在此盘点单据！");
		}
		if(StockCountStateEnum.CREATE.getIndex().equals(one.getCountBillState())){
			throw new ServiceException("此盘点单据还未分派任务！");
		}
		if(StockCountStateEnum.INVALID.getIndex().equals(one.getCountBillState())){
			throw new ServiceException("此盘点单据已作废！");
		}
		if(StockCountStateEnum.COUNT_COMPLATED.getIndex().equals(one.getCountBillState())){
			throw new ServiceException("此盘点单据已完成！");
		}
		CountHeader head=new CountHeader();
		head.setCountBillNo(countBillNo);
		head.setCountBillState(StockCountStateEnum.COUNTING.getIndex());
		CountHeaderVO detail = countHeaderService.getDetailPda(head);
		return R.data(detail);
	}


	/**
	 * 完成库位信息
	 */
	@ApiLog("PDA-完成盘点单库位")
	@PostMapping("/completeTaskPda")
	@ApiOperationSupport(order = 10)
	@ApiOperation(value = "完成盘点单库位", notes = "传入LocId集合")
	public R<CountHeaderVO> completeTaskPda(@ApiParam(value = "主键集合", required = true) @RequestParam Long[] ids) {
		return R.data(countHeaderService.completeTaskPda(ids));
	}

	/**
	 * 获取库存信息
	 */
	@ApiLog("PDA-盘点查询库存详情")
	@PostMapping("/stockListPda")
	@ApiOperationSupport(order = 11)
	@ApiOperation(value = "盘点查询库存详情", notes = "")
	public R<List<Stock>> stockListPda(@RequestBody StockQueryDTO dto) {
		return R.data(stockService.list(dto));
	}
}
