
package org.nodes.modules.wms.warehouse.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.warehouse.entity.WorkArea;
import org.nodes.wms.core.warehouse.service.IWorkAreaService;
import org.nodes.wms.core.warehouse.vo.WorkAreaVO;
import org.nodes.wms.core.warehouse.wrapper.WorkareaWrapper;
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
import java.util.Map;

/**
 * 工作区控制器
 *
 * @author liangmei
 * @since 2019-12-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/warehouse/workarea")
@Api(value = "工作区管理", tags = "工作区管理")
public class WorkAreaController extends BladeController {

	private IWorkAreaService workAreaService;

	/**
	 * 工作区详情
	 */
	@ApiLog("工作区-详情")
	@GetMapping("/detail")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "wwaId", value = "工作区ID", paramType = "query", dataType = "string")
	})
	@ApiOperation(value = "工作区详情", notes = "传入workArea")
	public R<WorkAreaVO> detail(@ApiIgnore @RequestParam Map<String, Object> workArea) {
		WorkArea detail = workAreaService.getOne(Condition.getQueryWrapper(workArea, WorkArea.class));
		return R.data(WorkareaWrapper.build().entityVO(detail));
	}

	/**
	 * 列表
	 */
	@ApiLog("工作区-列表")
	@GetMapping("/list")
	@ApiOperation(value = "工作区分页", notes = "传入workArea")
	public R<List<WorkAreaVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<WorkArea> list = workAreaService.list(Condition.getQueryWrapper(params, WorkArea.class));
		return R.data(WorkareaWrapper.build().listVO(list));
	}

	/**
	 * 自定义分页
	 */
	@ApiLog("工作区-分页")
	@GetMapping("/page")
	@ApiOperation(value = "工作区分页", notes = "传入workArea")
	public R<IPage<WorkAreaVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<WorkArea> pages = workAreaService.page(Condition.getPage(query), Condition.getQueryWrapper(params, WorkArea.class));
		return R.data(WorkareaWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改工作区
	 */
	@ApiLog("工作区域-提交")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改工作区", notes = "传入workArea")
	public R submit(@Valid @RequestBody WorkArea workArea) {
		return R.status(workAreaService.saveOrUpdate(workArea));
	}


	/**
	 * 删除工作区
	 */
	@ApiLog("工作区域-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除工作区", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(workAreaService.removeByIds(Func.toLongList(ids)));
	}


}
