package org.nodes.modules.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;

import javax.validation.Valid;


import lombok.extern.slf4j.Slf4j;
import org.nodes.core.base.dto.NodesCurdColumnDTO;
import org.nodes.core.base.entity.NodesCurdColumn;
import org.nodes.core.base.service.INodesCurdColumnService;
import org.nodes.core.base.vo.NodesCurdColumnVO;
import org.nodes.core.base.wrapper.NodesCurdColumnWrapper;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springblade.core.boot.ctrl.BladeController;

import java.util.List;

/**
 * 列显隐表 控制器
 *
 * @author wangYuNan
 * @since 2021-07-06
 */
@RestController
@AllArgsConstructor
@RequestMapping("/nodesCurdColumn")
@Api(value = "列显隐表", tags = "列显隐表接口")
public class NodesCurdColumnController extends BladeController {

	private INodesCurdColumnService nodesCurdColumnService;

	/**
	 * 列显隐表详情
	 */
	@ApiLog("列显隐--详情")
	@GetMapping("/detail")
	@ApiOperation(value = "列显隐表详情", notes = "传入nodesCurdColumn")
	public R<NodesCurdColumnVO> detail(NodesCurdColumnDTO nodesCurdColumn) {
		NodesCurdColumn detail = nodesCurdColumnService.getOne(Condition.getQueryWrapper(nodesCurdColumn));
		return R.data(NodesCurdColumnWrapper.build().entityVO(detail));
	}

	/**
	 * 列显隐表列表
	 */
	@ApiLog("列显隐--列表")
	@GetMapping("/list")
	@ApiOperation(value = "列显隐表列表", notes = "传入nodesCurdColumn")
	public R<List<NodesCurdColumnVO>> list(BladeUser bladeUser) {
		List<NodesCurdColumn> list = nodesCurdColumnService.list(bladeUser);
		if (Func.isEmpty(list)){
			return R.data(null);
		}
		return R.data(NodesCurdColumnWrapper.build().listVO(list));
	}

	/**
	 * 列显隐表分页
	 */
	@ApiLog("列显隐--分页")
	@GetMapping("/page")
	@ApiOperation(value = "列显隐表分页", notes = "传入nodesCurdColumn")
	public R<IPage<NodesCurdColumnVO>> page(NodesCurdColumnDTO nodesCurdColumn, Query query) {
		IPage<NodesCurdColumn> pages = nodesCurdColumnService.page(Condition.getPage(query), Condition.getQueryWrapper(nodesCurdColumn));
		return R.data(NodesCurdColumnWrapper.build().pageVO(pages));
	}


	/**
	 * 列显隐表新增或修改
	 */
	@ApiLog("列显隐--新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "列显隐表新增或修改", notes = "传入nodesCurdColumn")
	public R submit(@Valid @RequestBody NodesCurdColumnDTO nodesCurdColumn, BladeUser bladeUser) {
		return R.status(nodesCurdColumnService.saveOrUpdate(nodesCurdColumn,bladeUser));
	}


	/**
	 * 列显隐表删除
	 */
	@ApiLog("列显隐--删除")
	@PostMapping("/remove")
	@ApiOperation(value = "列显隐表删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(nodesCurdColumnService.removeByIds(Func.toLongList(ids)));
	}

}
