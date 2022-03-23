
package org.nodes.modules.wms.basedata.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.wms.core.basedata.dto.SkuLotDTO;
import org.nodes.wms.core.basedata.entity.SkuLot;
import org.nodes.wms.core.basedata.excel.SkuLotExcel;
import org.nodes.wms.core.basedata.service.ISkuLotService;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.basedata.vo.SkuLotVO;
import org.nodes.wms.core.basedata.wrapper.SkuLotWrapper;
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
 * 物品批属性 控制器
 *
 * @author Chenhz
 * @program 物品批属性
 * @description 物品批属性控制器
 * @since 2019-12-05
 */
@RestController
@AllArgsConstructor
@RequestMapping("/wms/basedata/skulot")
@Api(value = "物品批属性", tags = "物品批属性接口")
public class SkuLotController extends BladeController {

	private ISkuLotService skuLotService;

	/**
	 * 详情
	 */
	@ApiLog("物品批属性接口-详情")
	@GetMapping("/detail")
	@ApiOperation(value = "详情", notes = "传入物品批属性skuLot")
	public R<SkuLotVO> detail(SkuLot skuLot) {

		SkuLot detail = skuLotService.getOne(Condition.getQueryWrapper(skuLot));
		return R.data(SkuLotWrapper.build().entityVO(detail));
	}

	/**
	 * 列表查询数据 物品批属性
	 */
	@ApiLog("物品批属性接口-列表查询数据")
	@GetMapping("/list")
	@ApiOperation(value = "列表查询数据", notes = "传入物品批属性skuLot")
	public R<List<SkuLotVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<SkuLot> skuLotList = skuLotService.list(Condition.getQueryWrapper(params, SkuLot.class));
		return R.data(SkuLotWrapper.build().listVO(skuLotList));
	}

	/**
	 * 分页查询数据 物品批属性
	 */
	@ApiLog("物品批属性接口-分页查询数据")
	@GetMapping("/page")
	@ApiOperation(value = "分页查询数据", notes = "传入物品批属性skuLot")
	public R<IPage<SkuLotVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<SkuLot> pages = skuLotService.page(Condition.getPage(query), Condition.getQueryWrapper(params, SkuLot.class));
		return R.data(SkuLotWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改 物品批属性
	 */
	@ApiLog("物品批属性接口-新增或修改")
	@PostMapping("/submit")
	@ApiOperation(value = "新增或修改", notes = "传入物品批属性skuLot")
	public R submit(@Validated @RequestBody SkuLot skuLot) {
		return R.data(skuLotService.saveOrUpdate(skuLot));
	}


	/**
	 * 删除 物品批属性
	 */
	@ApiLog("物品批属性接口-删除")
	@PostMapping("/remove")
	@ApiOperation(value = "删除", notes = "物品批属性设置ID")
	public R remove(@ApiParam(value = "wslId", required = true) @RequestParam String ids) {

		return R.status(skuLotService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 导出
	 */
	@ApiLog("批属性-导出")
	@GetMapping("export")
	@ApiOperation(value = "导出", notes = "查询条件")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		skuLotService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@ApiLog("批属性-导出模板")
	@GetMapping("export-template")
	@ApiOperation(value = "导出模板")
	public void exportTemplate(HttpServletResponse response) {
		List<SkuLotExcel> skuLotExportList = new ArrayList<>();
		ExcelUtil.export(response, "批属性", "批属性数据表", skuLotExportList, SkuLotExcel.class);
	}

	/**
	 * 导入验证
	 */
	@ApiLog("批属性-导入验证")
	@PostMapping("import-valid")
	@ApiOperation(value = "导入验证")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(skuLotService.validExcel(ExcelUtil.read(file, SkuLotExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@ApiLog("批属性-导入数据")
	@PostMapping("import-data")
	@ApiOperation(value = "导入数据")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(skuLotService.importData(dataVerifyList));
	}

	/**
	 * 获取批属性配置
	 *
	 * @param skuId 物品ID
	 * @return 批属性配置
	 */
	@ApiLog("批属性-配置")
	@GetMapping("config")
	@ApiOperation(value = "批属性配置")
	public R<List<SkuLotConfigVO>> config(@RequestParam Long skuId) {
		return R.data(skuLotService.getConfig(skuId));
	}
}
