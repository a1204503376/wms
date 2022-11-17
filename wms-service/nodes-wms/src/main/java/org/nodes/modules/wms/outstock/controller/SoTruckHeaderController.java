
package org.nodes.modules.wms.outstock.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.outstock.loading.entity.SoTruckHeader;
import org.nodes.wms.core.outstock.loading.service.ISoTruckHeaderService;
import org.nodes.wms.core.outstock.loading.vo.SoTruckHeaderVO;
import org.nodes.wms.core.outstock.loading.wrapper.SoTruckHeaderWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * 车次头表 控制器
 *
 * @author chz
 * @since 2020-03-03
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/outstock/soTruckHeader")
@Api(value = "车次头表", tags = "车次头表接口")
public class SoTruckHeaderController extends BladeController {

	private ISoTruckHeaderService soTruckHeaderService;

	/**
	 * 详情
	 */
	@ApiLog("车次头表接口-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入soTruckHeader")
	public R<SoTruckHeaderVO> detail(SoTruckHeader soTruckHeader) {
		return R.data(soTruckHeaderService.getDetail(soTruckHeader));
	}

	/**
	 * 分页 车次头表
	 */
	@GetMapping("/list")
	@ApiOperation(value = "分页", notes = "传入soTruckHeader")
	public R<List<SoTruckHeaderVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<SoTruckHeader> list = soTruckHeaderService.list(Condition.getQueryWrapper(params, SoTruckHeader.class));
		return R.data(SoTruckHeaderWrapper.build().listVO(list));
	}

	/**
	 * 自定义分页 车次头表
	 */
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入soTruckHeader")
	public R<IPage<SoTruckHeaderVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SoTruckHeader> pages = soTruckHeaderService.page(
			Condition.getPage(query),
			Condition.getQueryWrapper(params, SoTruckHeader.class).lambda().orderByDesc(SoTruckHeader::getCreateTime));
		return R.data(SoTruckHeaderWrapper.build().pageVO(pages));
	}

	/**
	 * 新增 车次头表
	 */
	@ApiLog("车次头表接口-新增")
	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入soTruckHeader")
	public R save(@Valid @RequestBody SoTruckHeader soTruckHeader) {
		return R.status(soTruckHeaderService.save(soTruckHeader));
	}

	/**
	 * 修改 车次头表
	 */
	@ApiLog("车次头表接口-修改")
	@PostMapping("/update")
	@ApiOperation(value = "修改", notes = "传入soTruckHeader")
	public R update(@Valid @RequestBody SoTruckHeader soTruckHeader) {
		return R.status(soTruckHeaderService.updateById(soTruckHeader));
	}

	/**
	 * 新增或修改 车次头表
	 */
	@ApiLog("车次头表接口-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入soTruckHeader")
	public R submit(@Valid @RequestBody SoTruckHeader soTruckHeader) {
		return R.status(soTruckHeaderService.saveOrUpdate(soTruckHeader));
	}


	/**
	 * 删除 车次头表
	 */
	@ApiLog("车次头表接口-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(soTruckHeaderService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 确认发货
	 */
	@ApiLog("车次头表接口-确认发货")
	@PostMapping("/SaveconfirmSo")
	@ApiOperation(value = "确认发货", notes = "确认发货truckId")
	public R SaveconfirmSo(@ApiParam(value = "主键集合", required = true) @RequestParam String truckId) {
		return R.status(soTruckHeaderService.SaveconfirmSo(truckId));
	}
}
