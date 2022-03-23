package org.nodes.modules.wms.crontab;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.crontab.dto.SchemeDTO;
import org.nodes.wms.core.crontab.entity.Scheme;
import org.nodes.wms.core.crontab.service.ISchemeService;
import org.nodes.wms.core.crontab.vo.SchemeVO;
import org.nodes.wms.core.crontab.wrapper.SchemeWrapper;
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

import static org.nodes.wms.core.crontab.cache.SchemeCache.SCHEME_CACHE;

/**
 * 任务计划表 控制器
 *
 * @author NodeX
 * @since 2021-01-22
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/crontab/scheme")
@Api(value = "任务计划表", tags = "任务计划表接口")
public class SchemeController extends BladeController {

	private ISchemeService schemeService;

	/**
	 * 任务计划表详情
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "任务计划表详情", notes = "传入scheme")
	public R<SchemeVO> detail(SchemeDTO scheme) {
		Scheme detail = schemeService.getOne(Condition.getQueryWrapper(scheme));
		return R.data(SchemeWrapper.build().entityVO(detail));
	}

    /**
     * 任务计划表列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "任务计划表列表", notes = "传入scheme")
    public R<List<SchemeVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<Scheme> list = schemeService.list(Condition.getQueryWrapper(params, Scheme.class));
		return R.data(SchemeWrapper.build().listVO(list));
    }

	/**
	 * 任务计划表分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "任务计划表分页", notes = "传入scheme")
	public R<IPage<SchemeVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Scheme> pages = schemeService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Scheme.class));
		return R.data(SchemeWrapper.build().pageVO(pages));
	}


	/**
	 * 任务计划表新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperation(value = "任务计划表新增或修改", notes = "传入scheme")
	public R submit(@Valid @RequestBody Scheme scheme) {
		CacheUtil.clear(SCHEME_CACHE);
		return R.status(schemeService.saveOrUpdate(scheme));
	}


	/**
	 * 任务计划表删除
	 */
	@PostMapping("/remove")
	@ApiOperation(value = "任务计划表删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CacheUtil.clear(SCHEME_CACHE);
		return R.status(schemeService.removeByIds(Func.toLongList(ids)));
	}


}
