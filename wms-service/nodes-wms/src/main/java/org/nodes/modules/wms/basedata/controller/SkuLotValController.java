
package org.nodes.modules.wms.basedata.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotVal;
import org.nodes.wms.core.basedata.excel.SkuLotValExcel;
import org.nodes.wms.core.basedata.service.ISkuLotValService;
import org.nodes.wms.core.basedata.vo.SkuLotValVO;
import org.nodes.wms.core.basedata.wrapper.SkuLotValWrapper;
import org.springblade.core.boot.ctrl.BladeController;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springblade.core.tool.utils.Func;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 批属性验证 控制器
 *
 * @author chenhz
 * @since 2019-12-18
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/basedata/skulotval")
@Api(value = "批属性验证", tags = "批属性验证接口")
public class SkuLotValController extends BladeController {

	private ISkuLotValService skuLotValService;

	/**
	 * 批属性验证详情
	 */
	@ApiLog("批属性验证接口-批属性验证详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入skuLotVal")
	public R<SkuLotValVO> detail(SkuLotVal skuLotVal) {
		/*if (Func.isEmpty(skuLotVal.getWslvId())) {
			return null;
		} else {
			SkuLotVal detail = skuLotValService.getOne(Condition.getQueryWrapper(skuLotVal));
			return R.data(detail);
		}*/
		SkuLotVal detail = skuLotValService.getOne(Condition.getQueryWrapper(skuLotVal));
		return R.data(SkuLotValWrapper.build().entityVO(detail));
	}

	/**
	 * 分页 批属性验证
	 */
	@GetMapping("/list")
	@ApiOperation(value = "列表", notes = "传入skuLotVal")
	public R<List<SkuLotValVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<SkuLotVal> skuLotList = skuLotValService.list(Condition.getQueryWrapper(params, SkuLotVal.class));
		return R.data(SkuLotValWrapper.build().listVO(skuLotList));
	}

	/**
	 * 自定义分页 批属性验证
	 */
	@GetMapping("/page")
	@ApiOperation(value = "分页", notes = "传入skuLotVal")
	public R<IPage<SkuLotValVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SkuLotVal> pages = skuLotValService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SkuLotVal.class));
		return R.data(SkuLotValWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改 批属性验证
	 */
	@ApiLog("批属性验证接口-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入skuLotVal")
	public R submit(@Validated @RequestBody SkuLotVal skuLotVal) {
		return R.status(skuLotValService.saveOrUpdate(skuLotVal));
	}


	/**
	 * 删除 批属性验证
	 */
	@ApiLog("批属性验证接口-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "传入ids")
	public R remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		return R.status(skuLotValService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 导出
	 */
	@ApiLog("批属性验证-导出")
	@GetMapping("export")
	@ApiOperation(value = "导出", notes = "查询条件")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		skuLotValService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@ApiLog("批属性验证-导出模板")
	@GetMapping("export-template")
	@ApiOperation(value = "导出模板")
	public void exportTemplate(HttpServletResponse response) {
		List<SkuLotValExcel> skuLotValExportList = new ArrayList<>();
		ExcelUtil.export(response, "批属性验证", "批属性验证数据表", skuLotValExportList, SkuLotValExcel.class);
	}

	/**
	 * 导入验证
	 */
	@ApiLog("批属性验证-导入验证")
	@PostMapping("import-valid")
	@ApiOperation(value = "导入验证")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(skuLotValService.validExcel(ExcelUtil.read(file, SkuLotValExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@ApiLog("批属性验证-导入数据")
	@PostMapping("import-data")
	@ApiOperation(value = "导入数据")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(skuLotValService.importData(dataVerifyList));
	}
}
