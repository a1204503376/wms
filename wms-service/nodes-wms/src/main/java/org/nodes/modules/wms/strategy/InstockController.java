
package org.nodes.modules.wms.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.strategy.dto.InstockDTO;
import org.nodes.wms.core.strategy.entity.Instock;
import org.nodes.wms.core.strategy.service.IInstockService;
import org.nodes.wms.core.strategy.vo.InstockVO;
import org.nodes.wms.core.strategy.wrapper.InstockWrapper;
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
 * 上架策略控制器
 *
 * @author liangmei
 * @since 2019-12-09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/strategy/instock")
@Api(value = "上架策略", tags = "上架策略")
public class InstockController extends BladeController {

	private IInstockService instockService;

	/**
	 * 上架策略详情
	 */
	@ApiLog("上架策略-上架策略详情")
	@GetMapping("/detail")
	@ApiOperation(value = "上架策略详情", notes = "传入instock")
	public R<InstockVO> detail(Instock instock) {
		InstockVO detail = InstockWrapper.build().entityVO(instockService.getOne(Condition.getQueryWrapper(instock)));
		return R.data(detail);
	}

	/**
	 * 获取上架策略列表
	 */
	@ApiLog("上架策略-获取上架策略列表")
	@GetMapping("/list")
	@ApiOperation(value = "上架策略分页", notes = "传入instock")
	public R<List<Instock>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<Instock> instockList = instockService.list(Condition.getQueryWrapper(params, Instock.class));
		return R.data(instockList);
	}

	/**
	 * 上架策略自定义分页
	 */
	@ApiLog("上架策略-上架策略自定义分页")
	@GetMapping("/page")
	@ApiOperation(value = "上架策略分页", notes = "传入instock")
	public R<IPage<InstockVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Instock> pages = instockService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Instock.class));
		return R.data(InstockWrapper.build().pageVO(pages));
	}

	/**
	 * 新增上架策略
	 */
	@ApiLog("上架策略-新增上架策略")
	@PostMapping("/save")
	@ApiOperation(value = "新增", notes = "传入instock")
	public R save(@Valid @RequestBody InstockDTO instock) {
		return R.status(instockService.save(instock));
	}

	/**
	 * 修改上架策略
	 */
	@ApiLog("上架策略-修改上架策略")
	@PostMapping("/update")
	@ApiOperation(value = "修改上架策略", notes = "传入instock")
	public R update(@Valid @RequestBody InstockDTO instock) {
		return R.status(instockService.updateById(instock));
	}

	/**
	 * 新增或修改上架策略
	 */
	@ApiLog("上架策略-新增或修改上架策略")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改上架策略", notes = "传入instock")
	public R submit(@Valid @RequestBody InstockDTO instock) {
		return R.status(instockService.saveOrUpdate(instock));
	}


	/**
	 * 删除上架策略
	 */
	@ApiLog("上架策略-删除上架策略")
	@PostMapping("/remove")
	@ApiOperation(value = "删除上架策略", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(instockService.removeByIds(Func.toLongList(ids)));
	}
}
