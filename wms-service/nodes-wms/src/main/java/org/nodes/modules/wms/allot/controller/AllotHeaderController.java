
package org.nodes.modules.wms.allot.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.allot.cache.AllotCache;
import org.nodes.wms.core.allot.dto.AllotHeaderDTO;
import org.nodes.wms.core.allot.entity.AllotHeader;
import org.nodes.wms.core.allot.service.IAllotHeaderService;
import org.nodes.wms.core.allot.vo.AllotHeaderVO;
import org.nodes.wms.core.allot.wrapper.AllotHeaderWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;

/**
 * 调拨单头表 控制器
 *
 * @author Wangjw
 * @since 2020-01-23
 */
@RestController
//@AllArgsConstructor
@RequestMapping("/wms/allot/allotheader")
@Api(value = "调拨单头表", tags = "调拨单头表接口")
public class AllotHeaderController extends BladeController {

	@Autowired
	private IAllotHeaderService allotHeaderService;

	/**
	 * 详情
	 */
	@ApiLog("调拨单头表接口-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入allotHeaderId")
	public R<AllotHeaderVO> detail(@RequestParam Long allotBillId) {
		return R.data(allotHeaderService.getDetail(allotBillId));
	}

	/**
	 * 自定义分页 调拨单头表
	 */
	@ApiLog("调拨单头表接口-分页")
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入allotHeader")
	public R<IPage<AllotHeaderVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<AllotHeader> pages = allotHeaderService.page(Condition.getPage(query),
			Condition.getQueryWrapper(params, AllotHeader.class).lambda().orderByDesc(AllotHeader::getCreateTime));
		return R.data(AllotHeaderWrapper.build().pageVO(pages));
	}

	/**
	 * 新增 调拨单头表
	 */
	@ApiLog("调拨单头表接口-新增")
	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入allotHeader")
	public R save(@Valid @RequestBody AllotHeaderDTO allotHeaderDTO) {
		return R.status(allotHeaderService.save(allotHeaderDTO));
	}

	/**
	 * 修改 调拨单头表
	 */
	@ApiLog("调拨单头表接口-修改")
	@PostMapping("/update")
	@ApiOperation(value = "修改", notes = "传入allotHeader")
	public R update(@Valid @RequestBody AllotHeaderDTO allotHeaderDTO) {
		return R.status(allotHeaderService.updateById(allotHeaderDTO));
	}

	/**
	 * 新增或修改 调拨单头表
	 */
	@ApiLog("调拨单头表接口-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入allotHeader")
	public R submit(@Valid @RequestBody AllotHeaderDTO allotHeaderDTO) {
		return R.status(allotHeaderService.saveOrUpdate(allotHeaderDTO));
	}


	/**
	 * 删除 调拨单头表
	 */
	@ApiLog("调拨单头表接口-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(allotHeaderService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 获得调拨单编号
	 */
	@ApiLog("调拨单头表接口-获得调拨单编号")
	@GetMapping("/getAllotBillNo")
	@ApiOperation(value = "获得调拨单编号", notes = "")
	public R<String> getAllotBillNo() {
		return R.data(AllotCache.getAllotBillNo());
	}

	/**
	 * 取消 调拨单头表
	 */
	@ApiLog("调拨单头表接口-取消")
	@PostMapping("/cancel")
	@ApiOperation(value = "取消", notes = "传入ids")
	public R cancel(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(allotHeaderService.cancel(Func.toLongList(ids)));
	}

	/**
	 * 调拨单 是否允许编辑
	 * @param allotBillId
	 * @return
	 */
	@ApiLog("调拨单头表接口-获取调拨单是否允许编辑")
	@GetMapping("/canEdit")
	@ApiOperation(value = "获取调拨单是否允许编辑", notes = "传入调拨单ID, 获取调拨单是否允许编辑")
	public R canEdit(@Valid @ApiParam(value = "入库单ID", required = true) @RequestParam Long allotBillId) {
		return R.data(allotHeaderService.canEdit(allotBillId));
	}
}
