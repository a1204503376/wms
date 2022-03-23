package org.nodes.modules.wms.strategy;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.strategy.entity.Wellen;
import org.nodes.wms.core.strategy.vo.WellenVO;
import org.nodes.wms.core.strategy.wrapper.WellenWrapper;
import org.nodes.wms.core.strategy.dto.WellenDTO;
import org.nodes.wms.core.strategy.service.IWellenService;
import org.springblade.core.boot.ctrl.BladeController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * 波次策略 控制器
 *
 * @author wangYN
 * @since 2021-05-26
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/strategy/wellen")
@Api(value = "波次策略", tags = "波次策略接口")
public class WellenController extends BladeController {

	private IWellenService wellenService;

	/**
	 * 波次策略详情
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "波次策略详情", notes = "传入wellen")
	public R<WellenVO> detail(WellenDTO wellen) {
		return R.data(wellenService.getDetail(wellen));
	}

	/**
	 * 波次策略列表
	 */
	@GetMapping("/list")
	@ApiOperation(value = "波次策略列表", notes = "传入wellen")
	public R<List<WellenVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<Wellen> list = wellenService.list(Condition.getQueryWrapper(params, Wellen.class));
		return R.data(WellenWrapper.build().listVO(list));
	}

	/**
	 * 波次策略分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "波次策略分页", notes = "传入wellen")
	public R<IPage<WellenVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Wellen> pages = wellenService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Wellen.class));
		return R.data(WellenWrapper.build().pageVO(pages));
	}


	/**
	 * 波次策略新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperation(value = "波次策略新增或修改", notes = "传入wellen")
	public R submit(@Valid @RequestBody WellenDTO wellen) {
		return R.status(wellenService.saveOrUpdate(wellen));
	}


	/**
	 * 波次策略删除
	 */
	@PostMapping("/remove")
	@ApiOperation(value = "波次策略删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(wellenService.removeByIds(Func.toLongList(ids)));
	}


}
