package org.nodes.modules.wms.crontab;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.crontab.dto.CrontabTaskDTO;
import org.nodes.wms.core.crontab.entity.CrontabTask;
import org.nodes.wms.core.crontab.service.ICrontabTaskService;
import org.nodes.wms.core.crontab.vo.CrontabTaskVO;
import org.nodes.wms.core.crontab.wrapper.CrontabTaskWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
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

	private ICrontabTaskService taskService;

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
	 * 任务表新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperation(value = "任务表新增或修改", notes = "传入task")
	public R submit(@Valid @RequestBody CrontabTask crontabTask) {
		CacheUtil.clear(CRONTAB_TASK_CACHE);
		return R.status(taskService.saveOrUpdate(crontabTask));
	}


	/**
	 * 任务表删除
	 */
	@PostMapping("/remove")
	@ApiOperation(value = "任务表删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestBody List<Long> ids) {
		CacheUtil.clear(CRONTAB_TASK_CACHE);
		return R.status(taskService.removeByIds(ids));
	}
    @GetMapping("/detailById")
	public  R<CrontabTaskVO>  detailById(Long id){
		CrontabTask detail =  taskService.getById(id);
		return R.data(CrontabTaskWrapper.build().entityVO(detail));
	}

}
