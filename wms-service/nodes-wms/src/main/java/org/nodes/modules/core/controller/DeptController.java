package org.nodes.modules.core.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.service.IDeptService;
import org.nodes.core.base.vo.DeptVO;
import org.nodes.core.base.wrapper.DeptWrapper;
import org.nodes.core.constant.CommonConstant;
import org.nodes.core.tool.utils.NodesUtil;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.launch.constant.AppConstant;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.secure.utils.AuthUtil;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.constant.BladeConstant;
import org.springblade.core.tool.utils.Func;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.springblade.core.cache.constant.CacheConstant.SYS_CACHE;

/**
 * 控制器
 *
 * @author Nodes
 */
@RestController
@AllArgsConstructor
@RequestMapping(AppConstant.APPLICATION_SYSTEM_NAME + "/dept")
@Api(value = "机构", tags = "机构")
public class DeptController extends BladeController {

	private IDeptService deptService;

	/**
	 * 详情
	 */
	@ApiLog("机构-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入dept")
	public R<DeptVO> detail(Dept dept) {
		Dept detail = deptService.getOne(Condition.getQueryWrapper(dept));
		return R.data(DeptWrapper.build().entityVO(detail));
	}

	/**
	 * 列表
	 */
	@ApiLog("机构-列表")
	@GetMapping("/list")
	@ApiOperation(value = "列表", notes = "传入dept")
	public R<List<DeptVO>> list(@ApiIgnore @RequestParam Map<String, Object> params) {
		QueryWrapper<Dept> queryWrapper = Condition.getQueryWrapper(params, Dept.class);
		List<Dept> allList = deptService.list();
		List<Dept> deptList = deptService.list(queryWrapper);
		if (!BladeConstant.ADMIN_TENANT_ID.equals(AuthUtil.getTenantId())) {
			allList.removeIf(new Predicate<Dept>() {
				@Override
				public boolean test(Dept dept) {
					return !AuthUtil.getTenantId().equals(dept.getTenantId());
				}
			});
		}
		List<DeptVO> deptVOList = DeptWrapper.build().listVO(deptList);
		deptVOList.forEach(item -> {
			Integer hasChildren = allList.stream().filter(u -> {
				return item.getId().equals(u.getParentId());
			}).collect(Collectors.toList()).size();
			item.setHasChildren(hasChildren > 0);
			item.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
		});
		return R.data(deptVOList);
	}

	@ApiLog("机构-分页")
	@GetMapping("/page")
	public R<IPage<DeptVO>> page(@ApiIgnore @RequestParam Map<String, Object> params, Query query) {
		IPage<Dept> basePage = deptService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Dept.class)
			.lambda()
			.eq(Dept::getParentId, CommonConstant.TOP_PARENT_ID));
		IPage<DeptVO> page = DeptWrapper.build().pageVO(basePage);
		if (Func.isNotEmpty(page) && Func.isNotEmpty(page.getRecords())) {
			List<Dept> deptList = deptService.list(Condition.getQueryWrapper(new Dept())
				.lambda()
				.in(Dept::getParentId, NodesUtil.toList(page.getRecords(), Dept::getId)));
			page.getRecords().forEach(item->{
				long childrenSize = deptList.stream().filter(u->{
					return u.getParentId().equals(item.getId());
				}).count();
				item.setHasChildren(childrenSize > CommonConstant.TOP_PARENT_ID);
			});
		}
		return R.data(page);
	}

	/**
	 * 获取机构树形结构
	 */
	@ApiLog("机构-树形结构")
	@GetMapping("/tree")
	@ApiOperation(value = "树形结构", notes = "树形结构")
	public R<List<DeptVO>> tree(String tenantId, BladeUser bladeUser, Long id) {
		List<DeptVO> tree = deptService.tree(null, id);
		return R.data(tree);
	}

	/**
	 * 新增或修改
	 */
	@ApiLog("机构-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入dept")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	public R submit(@Valid @RequestBody Dept dept, BladeUser user) {
		if (Func.isEmpty(dept.getId())) {
			dept.setTenantId(user.getTenantId());
		}
		CacheUtil.clear(SYS_CACHE);
		return R.status(deptService.submit(dept));
	}

	/**
	 * 删除
	 */
	@ApiLog("机构-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	@CacheEvict(cacheNames = {SYS_CACHE}, allEntries = true)
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CacheUtil.clear(SYS_CACHE);
		return R.status(deptService.removeByIds(Func.toLongList(ids)));
	}


}
