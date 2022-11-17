
package org.nodes.modules.wms.strategy;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.nodes.wms.core.strategy.dto.OutstockDTO;
import org.nodes.wms.core.strategy.entity.Outstock;
import org.nodes.wms.core.strategy.service.IOutstockService;
import org.nodes.wms.core.strategy.vo.OutstockVO;
import org.nodes.wms.core.strategy.wrapper.OutstockWrapper;
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
 * 分配策略  控制器
 *
 * @author zhongls
 * @since 2019-12-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/strategy/outstock")
@Api(value = "分配策略", tags = "分配策略")
public class OutstockController extends BladeController {

	private IOutstockService outstockService;

	/**
	 * 根据分配策略ID获取详情
	 */
	@ApiLog("分配策略-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入Outstock的主键ssoId")
	public R<OutstockVO> detail(Long ssoId) {
		OutstockVO detail = outstockService.getOne(ssoId);
		return R.data(detail);
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	@ApiOperation(value = "分页", notes = "传入outstock")
	public R<List<OutstockVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<Outstock> list = outstockService.list(Condition.getQueryWrapper(params, Outstock.class));
		return R.data(OutstockWrapper.build().listVO(list));
	}

	/**
	 * 分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入outstock")
	public R<IPage<OutstockVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Outstock> pages = outstockService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Outstock.class));
		return R.data(OutstockWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改
	 */
	@ApiLog("分配策略-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入outstock")
	public R submit(@Valid @RequestBody OutstockDTO outstockDTO) {
		return R.status(outstockService.saveOrUpdate(outstockDTO));
	}


	/**
	 * 删除
	 */
	@ApiLog("分配策略-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(outstockService.removeByIds(Func.toLongList(ids)));
	}
}
