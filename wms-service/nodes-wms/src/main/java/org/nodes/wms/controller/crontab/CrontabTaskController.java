package org.nodes.wms.controller.crontab;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.biz.common.log.LogBiz;
import org.nodes.wms.dao.common.log.dto.input.LogPageQuery;
import org.nodes.wms.dao.common.log.dto.output.LogResponse;
import org.nodes.wms.dao.crontab.dto.CrontabTaskDTO;
import org.nodes.wms.dao.crontab.entity.CrontabTask;
import org.nodes.wms.biz.crontab.ICrontabTaskService;
import org.nodes.wms.dao.crontab.vo.CrontabTaskVO;
import org.nodes.wms.dao.crontab.wrapper.CrontabTaskWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import static org.nodes.wms.core.crontab.cache.CrontabTaskCache.CRONTAB_TASK_CACHE;

/**
 * 任务表 控制器
 *
 * @author NodeX
 * @since 2021-01-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/crontab/task")
@Api(value = "任务表", tags = "任务表接口")
public class CrontabTaskController extends BladeController {

	private final ICrontabTaskService taskService;
	private final LogBiz logBiz;

	/**
	 * 任务表详情
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "任务表详情", notes = "传入task")
	public R<CrontabTaskVO> detail(CrontabTaskDTO task) {
		CrontabTask detail = taskService.getOne(Condition.getQueryWrapper(task));
		return R.data(CrontabTaskWrapper.build().entityVO(detail));
	}

    /**
     * 任务表列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "任务表列表", notes = "传入task")
    public R<List<CrontabTaskVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<CrontabTask> list = taskService.list(Condition.getQueryWrapper(params, CrontabTask.class));
		return R.data(CrontabTaskWrapper.build().listVO(list));
    }

	/**
	 * 任务表分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "任务表分页", notes = "传入task")
	public R<IPage<CrontabTaskVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<CrontabTask> pages = taskService.page(Condition.getPage(query), Condition.getQueryWrapper(params, CrontabTask.class));
		return R.data(CrontabTaskWrapper.build().pageVO(pages));
	}

	/**
	 * 任务表新增
	 */
	@PostMapping("/newCrontabTask")
	@ApiOperation(value = "任务表新增或修改", notes = "传入task")
	public R newCrontabTask(@Valid @RequestBody CrontabTask crontabTask) {
		CacheUtil.clear(CRONTAB_TASK_CACHE);

		return R.status(taskService.newCrontabTask(crontabTask));
	}
	/**
	 * 任务表修改
	 */
	@PostMapping("/editCrontabTask")
	@ApiOperation(value = "任务表新增或修改", notes = "传入task")
	public R editCrontabTask(@Valid @RequestBody CrontabTask crontabTask) {
		CacheUtil.clear(CRONTAB_TASK_CACHE);

		return R.status(taskService.editCrontabTask(crontabTask));
	}

	/**
	 * 任务表删除
	 */
	@PostMapping("/remove")
	@ApiOperation(value = "任务表删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestBody List<Long> ids) {
		CacheUtil.clear(CRONTAB_TASK_CACHE);
		return R.status(taskService.deleteByIds(ids));
	}
    @GetMapping("/detailById")
	public  R<CrontabTaskVO>  detailById(Long id){
		CrontabTask detail =  taskService.getById(id);
		return R.data(CrontabTaskWrapper.build().entityVO(detail));
	}
	@PostMapping("/getLogById")
	public  R<Page<LogResponse>>  getLogById(@RequestBody  LogPageQuery logPageQuery, Query query){
		return R.data(logBiz.getPage(logPageQuery,query));
	}

}
