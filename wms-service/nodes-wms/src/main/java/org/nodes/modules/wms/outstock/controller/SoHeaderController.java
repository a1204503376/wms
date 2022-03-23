package org.nodes.modules.wms.outstock.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.outstock.so.cache.SoCache;
import org.nodes.wms.core.outstock.so.dto.SoHeaderDTO;
import org.nodes.wms.core.outstock.so.dto.SoHeaderQueryDTO;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.service.ISoHeaderService;
import org.nodes.wms.core.outstock.so.vo.SoHeaderVO;
import org.nodes.wms.core.outstock.so.wrapper.SoHeaderWrapper;
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
 * 出库管理主 控制器
 *
 * @author zhonglianshuai
 * @since 2020-02-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/outstock/header")
@Api(value = "出库管理主接口", tags = "出库管理主接口")
public class SoHeaderController extends BladeController {

	private ISoHeaderService soHeaderService;

	/**
	 * 详情
	 */
	@ApiLog("出库管理主接口-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "出库单头表详情", notes = "传入出库单id,query")
	public R<SoHeaderVO> detail(@RequestParam Long soBillId) {
		return R.data(soHeaderService.getDetail(soBillId));
	}

	/**
	 * 出库单列表
	 */
	@ApiLog("出库管理主接口-列表")
	@GetMapping("/list")
	@ApiOperation(value = "列表", notes = "传入header")
	public R<List<SoHeaderVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<SoHeader> list = soHeaderService.list(Condition.getQueryWrapper(params, SoHeader.class));
		return R.data(SoHeaderWrapper.build().listVO(list));
	}

	/**
	 * 出库单分页
	 */
	@ApiLog("出库管理主接口-分页")
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入soHeader,query")
	public R<IPage<SoHeaderVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SoHeader> page = soHeaderService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SoHeader.class));
		return R.data(SoHeaderWrapper.build().pageVO(page));
	}

	/**
	 * 新增或修改
	 */
	@ApiLog("出库管理主接口-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入header")
	public R submit(@Valid @RequestBody SoHeaderDTO header) {
		return R.status(soHeaderService.saveOrUpdate(header));
	}


	/**
	 * 删除
	 */
	@ApiLog("出库管理主接口-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(soHeaderService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 获得出库单编号
	 *
	 * @return
	 */
	@ApiLog("出库管理主接口-获得出库单编号")
	@GetMapping("/getSoBillNo")
	@ApiOperation(value = "获得出库单编号", notes = "")
	public R<String> getSoBillNo() {
		return R.data(SoCache.getSoBillNo());
	}

	/**
	 * 获取出库单是否允许编辑
	 *
	 * @param soHeaderId 出库单ID
	 * @return true:允许编辑， false:不允许编辑
	 */
	@ApiLog("出库管理主接口-获取出库单是否允许编辑")
	@GetMapping("/canEdit")
	@ApiOperation(value = "获取出库单是否允许编辑", notes = "传入出库单ID, 获取出库单是否允许编辑")
	public R canEdit(@Valid @ApiParam(value = "入库单ID", required = true) @RequestParam Long soHeaderId) {
		return R.data(soHeaderService.canEdit(soHeaderId));
	}

	/**
	 * 取消订单
	 *
	 * @param ids 出库单ID
	 * @return
	 */
	@ApiLog("出库管理主接口-取消订单")
	@PostMapping("/cancel")
	@ApiOperation(value = "取消订单", notes = "传入订单主键ID")
	public R cancel(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.data(soHeaderService.cancel(Func.toLongList(ids)));
	}

	@ApiLog("出库单头表接口-完成订单")
	@PostMapping("/completed")
	@ApiOperation(value = "出库单ID", notes = "出库单ID，多个用英文逗号分隔")
	public R completed(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.data(soHeaderService.completed(Func.toLongList(ids)));
	}

	@ApiLog("出库单头表接口-完成出库")
	@PostMapping("/complated/outstock")
	@ApiOperation(value = "出库单ID", notes = "出库单ID，多个用英文逗号分隔")
	public R completedOutstock(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.data(soHeaderService.completedOutstock(Func.toLongList(ids)));
	}
}
