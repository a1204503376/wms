package org.nodes.modules.wms.strategy;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

import org.nodes.wms.core.strategy.service.IMissionService;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.wms.core.strategy.entity.Mission;
import org.nodes.wms.core.strategy.vo.MissionVO;
import org.nodes.wms.core.strategy.wrapper.MissionWrapper;
import org.nodes.wms.core.strategy.dto.MissionDTO;
import org.springblade.core.boot.ctrl.BladeController;
import springfox.documentation.annotations.ApiIgnore;

/**
 *  控制器
 *
 * @author NodeX
 * @since 2021-05-24
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/strategy/mission")
@Api(value = "", tags = "接口")
public class MissionController extends BladeController {

	private IMissionService missionService;

	/**
	 * 详情
	 */
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入mission")
	public R<MissionVO> detail(MissionDTO mission) {
		Mission detail = (Mission) missionService.getOne(Condition.getQueryWrapper(mission));
		return R.data(MissionWrapper.build().entityVO(detail));
	}

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "列表", notes = "传入mission")
    public R<List<MissionVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<Mission> list = missionService.list(Condition.getQueryWrapper(params, Mission.class));
		return R.data(MissionWrapper.build().listVO(list));
    }

	/**
	 * 分页
	 */
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入mission")
	public R<IPage<MissionVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Mission> pages = missionService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Mission.class));
		return R.data(MissionWrapper.build().pageVO(pages));
	}


	/**
	 * 新增或修改
	 */
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入mission")
	public R submit(@Valid @RequestBody Mission mission) {
		return R.status(missionService.saveOrUpdate(mission));
	}


	/**
	 * 删除
	 */
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(missionService.removeByIds(Func.toLongList(ids)));
	}


}
