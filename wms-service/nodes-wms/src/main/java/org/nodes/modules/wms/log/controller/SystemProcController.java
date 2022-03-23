package org.nodes.modules.wms.log.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jodd.util.StringPool;
import lombok.AllArgsConstructor;
import org.nodes.modules.wms.log.vo.SystemProcVO;
import org.nodes.modules.wms.log.wrapper.SystemProcWrapper;
import org.nodes.wms.core.stock.core.entity.*;
import org.nodes.wms.core.stock.core.service.*;
import org.nodes.wms.core.stock.core.wrapper.*;
import org.nodes.wms.core.outstock.loading.service.ITruckSerialService;
import org.nodes.wms.core.log.system.entity.SystemProc;
import org.nodes.wms.core.log.system.service.ISystemProcService;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 系统日志 控制器
 *
 * @author NodeX
 * @since 2020-02-12
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/log/systemProc")
@Api(value = "系统日志", tags = "系统日志接口")
public class SystemProcController extends BladeController {

	private ISystemProcService systemProcService;
	private ISerialService serialService;
	private ISerialLogService serialLogService;
	private ILotService lotService;
	private ILotLogService lotLogService;
	private IStockOccupyService stockOccupyService;
	private IStockLogService stockLogService;
	private ITruckSerialService truckSerialService;

	/**
	 * 详情
	 */
	@ApiLog("系统日志接口-系统日志详情")
	@GetMapping("/detail")
	@ApiOperation(value = "系统日志详情", notes = "传入systemProc")
	public R<SystemProcVO> detail(SystemProc systemProc) {
		SystemProcVO detail = SystemProcWrapper.build().entityVO(
			systemProcService.getOne(Condition.getQueryWrapper(systemProc)));
		if (ObjectUtil.isNotEmpty(detail)) {
			detail.setSerialList(SerialWrapper.build().listVO(
				serialService.list(Condition.getQueryWrapper(new Serial()).lambda()
					.eq(Serial::getSystemProcId, detail.getSystemProcId())
				)
			));
			detail.setSerialLogList(SerialLogWrapper.build().listVO(
				serialLogService.list(Condition.getQueryWrapper(new SerialLog()).lambda()
					.eq(SerialLog::getSystemProcId, detail.getSystemProcId())
				)
			));
			detail.setLotList(LotWrapper.build().listVO(
				lotService.list(Condition.getQueryWrapper(new Lot()).lambda()
					.eq(Lot::getSystemProcId, detail.getSystemProcId())
				)
			));
			detail.setLotLogList(LotLogWrapper.build().listVO(
				lotLogService.list(Condition.getQueryWrapper(new LotLog()).lambda()
					.eq(LotLog::getSystemProcId, detail.getSystemProcId())
				)
			));
			detail.setStockOccupyList(StockOccupyWrapper.build().listVO(
				stockOccupyService.list(Condition.getQueryWrapper(new StockOccupy()).lambda()
					.eq(StockOccupy::getSystemProcId, detail.getSystemProcId())
				)
			));
			detail.setStockLogList(StockLogWrapper.build().listVO(
				stockLogService.list(Condition.getQueryWrapper(new StockLog()).lambda()
					.eq(StockLog::getSystemProcId, detail.getSystemProcId())
				)
			));
			detail.setTruckSerialList(truckSerialService.listBySystemProcId(detail.getSystemProcId()));
		}
		return R.data(detail);
	}

	/**
	 * 分页 系统日志
	 */
	@ApiLog("系统日志接口-系统日志分页")
	@GetMapping("/page")
	@ApiOperation(value = "系统日志分页", notes = "传入systemProc")
	public R<IPage<SystemProcVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SystemProc> pages = systemProcService.page(Condition.getPage(query),
			Condition.getQueryWrapper(params, SystemProc.class).lambda()
				.orderByDesc(SystemProc::getSystemProcId));
		return R.data(SystemProcWrapper.build().pageVO(pages));
	}

}
