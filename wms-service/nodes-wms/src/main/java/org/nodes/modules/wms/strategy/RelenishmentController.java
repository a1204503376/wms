
package org.nodes.modules.wms.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.strategy.dto.RelenishmentDTO;
import org.nodes.wms.core.strategy.entity.Relenishment;
import org.nodes.wms.core.strategy.service.IRelenishmentService;
import org.nodes.wms.core.strategy.vo.RelenishmentVO;
import org.nodes.wms.core.strategy.wrapper.RelenishmentWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

/**
 * 补货策略控制器
 *
 * @author liangmei
 * @since 2019-12-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/strategy/relenishment")
@Api(value = "补货策略", tags = "补货策略")
public class RelenishmentController extends BladeController {

	private IRelenishmentService relenishmentService;

	/**
	 * 补货策略详情
	 */
	@ApiLog("补货策略-补货策略详情")
	@GetMapping("/detail")
	@ApiOperation(value = "补货策略详情", notes = "传入instock")
	public R<RelenishmentVO> detail(Relenishment instock) {
		RelenishmentVO detail = RelenishmentWrapper.build().entityVO(relenishmentService.getOne(Condition.getQueryWrapper(instock)));
		return R.data(detail);
	}

	/**
	 * 获取补货策略列表
	 */
	@GetMapping("/list")
	@ApiOperation(value = "补货策略分页", notes = "传入instock")
	public R<List<Relenishment>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<Relenishment> instockList = relenishmentService.list(Condition.getQueryWrapper(params, Relenishment.class));
		return R.data(instockList);
	}

	/**
	 * 补货策略自定义分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "补货策略分页", notes = "传入instock")
	public R<IPage<RelenishmentVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Relenishment> pages = relenishmentService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Relenishment.class));
		return R.data(RelenishmentWrapper.build().pageVO(pages));
	}

	/**
	 * 新增补货策略
	 */
	@ApiLog("补货策略-新增补货策略")
	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入instock")
	public R save(@Valid @RequestBody RelenishmentDTO instock) {
		return R.status(relenishmentService.save(instock));
	}

	/**
	 * 修改补货策略
	 */
	@ApiLog("补货策略-修改补货策略")
	@PostMapping("/update")
	@ApiOperation(value = "修改补货策略", notes = "传入instock")
	public R update(@Valid @RequestBody RelenishmentDTO instock) {
		return R.status(relenishmentService.updateById(instock));
	}

	/**
	 * 新增或修改补货策略
	 */
	@ApiLog("补货策略-新增或修改补货策略")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改补货策略", notes = "传入instock")
	public R submit(@Valid @RequestBody RelenishmentDTO instock) {
		return R.status(relenishmentService.saveOrUpdate(instock));
	}


	/**
	 * 删除补货策略
	 */
	@ApiLog("补货策略-删除补货策略")
	@PostMapping("/remove")
	@ApiOperation(value = "删除补货策略", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(relenishmentService.removeByIds(Func.toLongList(ids)));
	}
}
