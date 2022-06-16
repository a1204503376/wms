package org.nodes.modules.wms.basedata.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.basedata.excel.SkuUmExcel;
import org.nodes.wms.core.basedata.service.ISkuUmService;
import org.nodes.wms.core.basedata.vo.SkuUmVO;
import org.nodes.wms.core.basedata.wrapper.SkuUmWrapper;
import org.nodes.wms.dao.basics.sku.entities.SkuUm;
import org.springblade.core.boot.ctrl.BladeController;
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

/**
 * 计量单位 控制器
 *
 * @author zhongls
 * @since 2019-12-05
 */
@RestController
@AllArgsConstructor
@RequestMapping("wms/basedata/sku-um")
@Api(value = "计量单位", tags = "计量单位")
public class SkuUmController extends BladeController {

	private ISkuUmService skuUmService;
	/**
	 * 详情
	 */
	@ApiLog("计量单位-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入skuUm")
	public R<SkuUm> detail(SkuUm skuUm) {
		SkuUm detail = skuUmService.getOne(Condition.getQueryWrapper(skuUm));
		return R.data(detail);
	}

	/**
	 * 列表
	 */
	@ApiLog("计量单位-列表")
	@GetMapping("/list")
	@ApiOperation(value = "列表", notes = "传入skuUm")
	public R<List<SkuUmVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<SkuUm> list = skuUmService.list(Condition.getQueryWrapper(params, SkuUm.class));
		return R.data(SkuUmWrapper.build().listVO(list));
	}

	/**
	 * 分页
	 */
	@ApiLog("计量单位-分页")
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入skuUm")
	public R<IPage<SkuUmVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SkuUm> page = skuUmService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SkuUm.class));
		return R.data(SkuUmWrapper.build().pageVO(page));
	}

	/**
	 * 新增或修改
	 */
	@ApiLog("计量单位-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入skuUm")
	public R submit(@Valid @RequestBody SkuUm skuUm) {
		return R.status(skuUmService.saveOrUpdate(skuUm));
	}

	/**
	 * 删除
	 */
	@ApiLog("计量单位-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(skuUmService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 导出
	 */
	@ApiLog("计量单位-导出")
	@GetMapping("export")
	@ApiOperation(value = "导出", notes = "查询条件")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		skuUmService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@ApiLog("计量单位-导出模板")
	@GetMapping("export-template")
	@ApiOperation(value = "导出模板")
	public void exportTemplate(HttpServletResponse response) {
		List<SkuUmExcel> skuUmExportList = new ArrayList<>();
		ExcelUtil.export(response, "计量单位", "计量单位数据表", skuUmExportList, SkuUmExcel.class);
	}

	/**
	 * 导入验证
	 */
	@ApiLog("计量单位-导入验证")
	@PostMapping("import-valid")
	@ApiOperation(value = "导入验证")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(skuUmService.validExcel(ExcelUtil.read(file, SkuUmExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@ApiLog("计量单位-导入数据")
	@PostMapping("import-data")
	@ApiOperation(value = "导入数据")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(skuUmService.importData(dataVerifyList));
	}
}
