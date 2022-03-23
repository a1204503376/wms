package org.nodes.modules.wms.outstock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import org.nodes.wms.core.outstock.sales.entity.SalesHeader;
import org.nodes.wms.core.outstock.sales.wrapper.SalesHeaderWrapper;
import org.nodes.wms.core.outstock.so.cache.SoCache;
import org.nodes.wms.core.outstock.sales.dto.SalesHeaderQueryDTO;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.outstock.sales.vo.SalesHeaderVO;
import org.nodes.wms.core.outstock.sales.dto.SalesHeaderDTO;
import org.nodes.wms.core.outstock.sales.service.ISalesHeaderService;
import org.springblade.core.boot.ctrl.BladeController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 销售单主表
 控制器
 *
 * @author NodeX
 * @since 2021-05-31
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/outstock/sales-header")
@Api(value = "销售单主表", tags = "销售单主表接口")
public class SalesHeaderController extends BladeController {

	private ISalesHeaderService salesHeaderService;

	/**
	 * 销售单主表详情
	 */
	@ApiLog("销售单-详细")
	@GetMapping("/detail")
	@ApiOperation(value = "销售单主表详情", notes = "传入salesHeader")
	public R<SalesHeaderVO> detail(SalesHeaderDTO salesHeader) {
		return R.data(salesHeaderService.getDetail(salesHeader.getSoBillId()));
	}

    /**
     * 销售单主表列表
     */
	@ApiLog("销售单-列表")
    @GetMapping("/list")
    @ApiOperation(value = "销售单主表列表", notes = "传入salesHeader")
    public R<List<SalesHeaderVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<SalesHeader> list = salesHeaderService.list(Condition.getQueryWrapper(params, SalesHeader.class));
		return R.data(SalesHeaderWrapper.build().listVO(list));
    }

	/**
	 * 销售单主表分页
	 */
	@ApiLog("销售单-分页")
	@GetMapping("/page")
	@ApiOperation(value = "销售单主表分页", notes = "传入salesHeader")
	public R<IPage<SalesHeaderVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SalesHeader> page = salesHeaderService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SalesHeader.class));
		return R.data(SalesHeaderWrapper.build().pageVO(page));
	}


	/**
	 * 销售单主表新增或修改
	 */
	@ApiLog("销售单-提交")
	@PostMapping("/submit")
	@ApiOperation(value = "销售单主表新增或修改", notes = "传入salesHeader")
	public R submit(@Valid @RequestBody SalesHeaderDTO salesHeader) {
		return R.status(salesHeaderService.saveOrUpdate(salesHeader));
	}


	/**
	 * 销售单主表删除
	 */
	@ApiLog("销售单-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "销售单主表删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(salesHeaderService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 获得销售单单编号
	 *
	 * @return
	 */
	@ApiLog("销售单-编号")
	@GetMapping("/getSalesBillNo")
	@ApiOperation(value = "获得销售单编号", notes = "")
	public R<String> getSoBillNo() {
		return R.data(SoCache.getSalesBillNo());
	}

	/**
	 * 创建出库单
	 * @param salesHeader 销售单
	 * @return 是否成功
	 */
	@ApiLog("销售单-创建出库单")
	@PostMapping("/create-so")
	@ApiOperation(value = "根据销售订单生成出库单", notes = "salesHeader")
	public R createSo(@Valid @RequestBody SalesHeaderDTO salesHeader) {
		return R.status(salesHeaderService.createSo(salesHeader));
	}

}
