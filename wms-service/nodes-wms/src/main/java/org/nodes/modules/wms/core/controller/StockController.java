package org.nodes.modules.wms.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.modules.wms.core.service.StockService;
import org.nodes.modules.wms.core.service.impl.AccountsQueryUtil;
import org.nodes.wms.core.basedata.entity.SkuPackageDetail;
import org.nodes.wms.core.basedata.service.ISkuPackageDetailService;
import org.nodes.wms.core.stock.core.dto.StockDTO;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.stock.core.entity.StockDetail;
import org.nodes.wms.core.stock.core.service.IStockDetailService;
import org.nodes.wms.core.stock.core.service.IStockService;
import org.nodes.wms.core.stock.core.vo.AccountsVo;
import org.nodes.wms.core.stock.core.vo.ErpStockVO;
import org.nodes.wms.core.stock.core.vo.StockDetailVo;
import org.nodes.wms.core.stock.core.vo.StockVO;
import org.nodes.wms.core.stock.core.wrapper.StockWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 库存 控制器
 *
 * @author pengwei
 * @since 2019-12-24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/core/stock")
@Api(value = "库存", tags = "库存接口")
public class StockController extends BladeController {

	private IStockService stockService;
	@Autowired
	private StockService mStockService;
	private IStockDetailService stockDetailService;


	/**
	 * 分页查询库存
	 */
	@ApiLog("库存接口-库存分页")
	@GetMapping("/page")
	@ApiOperation(value = "库存分页", notes = "传入stock")
	public R<IPage<StockVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		ISkuPackageDetailService skuPackageDetailService = SpringUtil.getBean(ISkuPackageDetailService.class);
		IPage<Stock> page = stockService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Stock.class));
		List<StockVO> stockVOList = new ArrayList<>();
		if(Func.isNotEmpty(page.getRecords())){
			for(Stock stock : page.getRecords()){
				StockVO stockVO = StockWrapper.build().entityVO(stock);
				stockVOList.add(stockVO);
			}
		}
		return R.data(StockWrapper.build().pageVO(page).setRecords(stockVOList));
	}

	/**
	 * 库存锁定
	 *
	 * @param ids 库存ID
	 * @return
	 */
	@ApiLog("库存接口-库存锁定")
	@PostMapping("/lock")
	@ApiOperation(value = "库存锁定")
	public R lock(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.data(stockService.lock(Func.toLongList(ids)));
	}

	/**
	 * 库存解锁
	 *
	 * @param ids 库存ID
	 * @return
	 */
	@ApiLog("库存接口-库存解锁")
	@PostMapping("/unlock")
	@ApiOperation(value = "库存解锁")
	public R unlock(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.data(stockService.unlock(Func.toLongList(ids)));
	}

	/**
	 * 批次锁定
	 *
	 * @param ids 库存ID
	 * @return
	 */
	@ApiLog("库存接口-批次锁定")
	@PostMapping("/lock/lot")
	@ApiOperation(value = "批次锁定")
	public R lockByLot(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.data(stockService.lockByLot(Func.toLongList(ids)));
	}

	/**
	 * 内部转移
	 */
	@ApiLog("库存接口-内部转移")
	@PostMapping("/internalTransfer")
	@ApiOperation(value = "内部转移", notes = "传入库存集合")
	public R internalTransfer(@RequestBody List<Stock> stockList) {
		return R.data(this.mStockService.updateBatch(stockList));
	}

	/**
	 * 获取wms库存与erp库存比对结果
	 *
	 * @return
	 */
	@ApiLog("库存接口-获取wms库存与erp库存比对结果")
	@GetMapping("/erpCompare")
	@ApiOperationSupport(order = 5)
	@ApiOperation(value = "获取wms库存与erp库存比对结果")
	public R<List<ErpStockVO>> erpCompare() {
		return R.data(stockService.listErpCompare());
	}


	/**
	 * 台账查询
	 */
	@ApiLog("库存接口-台账查询")
	@PostMapping("/accounts")
	@ApiOperation(value = "台账查询", notes = "传入stock")
	public R<List<AccountsVo>> getAccounts(@NotBlank(message = "物品id不能为空！") String ids,
										   String startTime, String endTime, String group, AccountsVo accountsVo,String stockIds) {
		return R.data(AccountsQueryUtil.getAccounts(Func.toLongList(ids),
			startTime, endTime, group, accountsVo,stockIds));
	}

}

