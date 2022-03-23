package org.nodes.modules.core.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.base.entity.Region;
import org.nodes.core.base.service.IRegionService;
import org.nodes.core.base.vo.DictVO;
import org.nodes.core.base.vo.RegionVO;
import org.nodes.core.base.wrapper.DictWrapper;
import org.nodes.core.base.wrapper.RegionWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.launch.constant.AppConstant;
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
 * 区域列表
 */
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/region")
@Api(value = "区域管理",tags = "区域管理")
public class RegionController extends BladeController {

	private IRegionService regionService;

	/**
	 * 详情
	 */
	@ApiLog("区域-获取区域详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入区域")
	public R<Region> detail(Region region) {
		return R.data(regionService.getOne(Condition.getQueryWrapper(region)));
	}

	/**
	 * 列表
	 * @param params
	 * @return
	 */
	@ApiLog("区域-获取区域列表")
	@GetMapping("/list")
	@ApiOperation(value = "列表",notes = "传入区域")
	public R<List<RegionVO>> list(@ApiIgnore @RequestParam Map<String, Object> params){
		List<Region> listAll = regionService.list();
		List<Region> lists = regionService.list(Condition.getQueryWrapper(params,Region.class));

		List<RegionVO> list = RegionWrapper.build().listRegionVO(lists);

		for (RegionVO item : list) {
			Long count = listAll.stream().filter(u -> {
				return item.getCode().equals(u.getParentCode());
			}).count();
			item.setHasChildren(count > 0L);
		}
		return R.data(list);
	}

	/**
	 * 分页
	 */
	@ApiLog("区域-分页")
	@GetMapping("/page")
	public R<IPage<RegionVO>> page(@ApiIgnore @RequestParam Map<String, Object> params, Query query){
		IPage<Region> pages = regionService.page(Condition.getPage(query),Condition.getQueryWrapper(params,Region.class));
		IPage<RegionVO> pageVO = RegionWrapper.build().pageVO(pages);
		List<Region> list = regionService.list();
		for (RegionVO item : pageVO.getRecords()) {
			Long count = list.stream().filter(u -> {
				return item.getCode().equals(u.getParentCode());
			}).count();
			item.setHasChildren(count > 0L);
		}
		return R.data(pageVO);
	}

	/**
	 * 新增
	 */
	@ApiLog("区域-新增区域")
	@PostMapping("/save")
	@ApiOperation(value = "新增",notes = "传入区域")
	public R save(@Valid @RequestBody Region region){
		return R.status(regionService.save(region));
	}

	/**
	 *修改
	 */
	@ApiLog("区域-修改区域")
	@PostMapping("/update")
	@ApiOperation(value = "修改",notes = "传入区域")
	public R update(@Valid @RequestBody Region region){
		return R.status(regionService.updateById(region));
	}

	/**
	 * 新增或修改
	 */
	@ApiLog("区域-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改",notes = "传入区域")
	public R submit(@Valid @RequestBody Region region){
		return R.status(regionService.saveOrUpdate(region));
	}

	/**
	 * 删除 区域
	 */
	@ApiLog("区域-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除",notes = "传入codes")
	public R remove (@ApiParam(value = "主键集合",required = true) @RequestParam String ids){
		return R.status(regionService.removeByIds(Func.toStrList(ids)));
	}

}


