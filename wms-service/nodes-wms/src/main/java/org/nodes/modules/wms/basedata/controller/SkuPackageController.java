package org.nodes.modules.wms.basedata.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.basedata.dto.SkuPackageDTO;
import org.nodes.wms.dao.basics.sku.entities.SkuPackage;
import org.nodes.wms.core.basedata.excel.SkuPackageExcel;
import org.nodes.wms.core.basedata.service.ISkuPackageService;
import org.nodes.wms.core.basedata.vo.SkuPackageVO;
import org.nodes.wms.core.basedata.wrapper.SkuPackageWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.cache.utils.CacheUtil;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.nodes.wms.core.basedata.cache.SkuPackageCache.SKU_PACKAGE_CACHE;

/**
 * 包装 控制器
 *
 * @author
 * @since 2019-12-10
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/basedata/skupackage")
@Api(value = "包装接口", tags = "包装接口")
public class SkuPackageController extends BladeController {

	private ISkuPackageService skuPackageService;
	/**
	 * 详情
	 */
	@ApiLog("包装接口-包装详情")
	@GetMapping("/detail")
	@ApiOperation(value = "包装详情", notes = "传入skuPackage")
	public R<SkuPackageVO> detail(SkuPackageVO skuPackage) {
		if (skuPackage.getWspId() != null && skuPackage.getWspId() > 0) {
			SkuPackage detail = skuPackageService.getOne(Condition.getQueryWrapper(skuPackage));
			return R.data(SkuPackageWrapper.build().entityVO(detail));
		} else {
			return null;
		}
	}

	/**
	 * 包装列表
	 */
	@ApiLog("包装接口-包装列表")
	@GetMapping("/list")
	@ApiOperation(value = "包装列表", notes = "传入skuPackage")
	public R<List<SkuPackageVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<SkuPackage> list = skuPackageService.list(Condition.getQueryWrapper(params, SkuPackage.class));
		return R.data(SkuPackageWrapper.build().listVO(list));
	}

	/**
	 * 包装分页
	 */
	@ApiLog("包装接口-包装分页")
	@GetMapping("/page")
	@ApiOperation(value = "包装分页", notes = "传入SkuPackageDTO")
	public R<IPage<SkuPackageVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SkuPackage> pages = skuPackageService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SkuPackage.class));
		return R.data(SkuPackageWrapper.build().pageVO(pages));
	}

	/**
	 * 包装新增或修改
	 */
	@ApiLog("包装接口-包装新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "包装新增或修改", notes = "传入skuPackage")
	public R submit(@Valid @RequestBody SkuPackageDTO skuPackageDTO) {
		CacheUtil.clear(SKU_PACKAGE_CACHE);
		return R.status(skuPackageService.saveOrUpdate(skuPackageDTO));
	}


	/**
	 * 包装批量删除
	 */
	@ApiLog("包装接口-包装批量删除")
	@PostMapping("/remove")
	@ApiOperation(value = "包装批量删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CacheUtil.clear(SKU_PACKAGE_CACHE);
		return R.status(skuPackageService.removeByIds(Func.toLongList(ids)));
	}
	/**
	 * 导出
	 */
	@ApiLog("包装管理-导出")
	@GetMapping("export")
	@ApiOperation(value = "导出", notes = "查询条件")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		skuPackageService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@ApiLog("包装管理-导出模板")
	@GetMapping("export-template")
	@ApiOperation(value = "导出模板")
	public void exportTemplate(HttpServletResponse response) {
		List<SkuPackageExcel> skuPackageExcels = new ArrayList<>();
		ExcelUtil.export(response, "物品", "物品数据表", skuPackageExcels, SkuPackageExcel.class);
	}

	/**
	 * 导入验证
	 */
	@ApiLog("包装管理-导入验证")
	@PostMapping("import-valid")
	@ApiOperation(value = "导入验证")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(skuPackageService.validExcel(ExcelUtil.read(file, SkuPackageExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@ApiLog("包装管理-导入数据")
	@PostMapping("import-data")
	@ApiOperation(value = "导入数据")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(skuPackageService.importData(dataVerifyList));
	}
}
