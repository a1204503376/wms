package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.modules.wms.basedata.service.ISkuService;
import org.nodes.wms.biz.basics.sku.SkuBiz;
import org.nodes.wms.core.basedata.dto.SkuDTO;
import org.nodes.wms.dao.basics.sku.dto.input.SkuAddOrEditRequest;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.excel.SkuExcel;
import org.nodes.wms.core.basedata.vo.SkuVO;
import org.nodes.wms.core.basedata.wrapper.SkuWrapper;
import org.nodes.wms.dao.basics.sku.dto.input.SkuSelectQuery;
import org.nodes.wms.dao.basics.sku.dto.output.SkuSelectResponse;
import org.nodes.wms.dao.basics.sku.dto.input.SkuUmSelectQuery;
import org.nodes.wms.dao.basics.sku.dto.output.SkuUmSelectResponse;
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
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.nodes.wms.core.basedata.cache.SkuCache.SKU_CACHE;

/**
 * 物品 API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.SKU_URL)
public class SkuController {

	private final SkuBiz skuBiz;
	private final ISkuService skuService;

	/**
	 * 物品选择下拉框
	 * 展示最近10个物品
	 */
	@PostMapping("getSkuSelectResponseTop10List")
	public R<List<SkuSelectResponse>> getSkuSelectResponseTop10List(@RequestBody SkuSelectQuery skuSelectQuery) {
		return R.data(skuBiz.getSkuSelectResponseTop10List(skuSelectQuery));
	}


	/**
	 * 详情
	 */
	@GetMapping("/detail")
	public R<SkuVO> detail(@NotNull SkuDTO sku) {
		return R.data(skuService.getDetail(sku));
	}

	/**
	 * 物品列表查询
	 */
	@GetMapping("/list")
	public R<List<SkuVO>> list(@ApiIgnore @RequestParam HashMap<String, Object> params) {
		List<Sku> skuList = skuService.list(Condition.getQueryWrapper(params, Sku.class));
		return R.data(SkuWrapper.build().listVO(skuList));
	}

	/**
	 * 分页 物品
	 */
	@GetMapping("/page")
	public R<IPage<SkuVO>> page(@ApiIgnore @RequestParam HashMap<String, Object> params, Query query) {
		IPage<Sku> pages = skuService.page(Condition.getPage(query), Condition.getQueryWrapper(params, Sku.class));
		return R.data(SkuWrapper.build().pageVO(pages));
	}

	/**
	 * 新增或修改 物品
	 */
	@PostMapping("/submit")
	public R<Object> submit(@Valid @RequestBody SkuDTO sku) {
		CacheUtil.clear(SKU_CACHE);
		return R.status(skuService.saveOrUpdate(sku));
	}

	/**
	 * 编辑时验证 物品
	 */
	@GetMapping("/edit-valid")
	public R<Object> editValid(@Valid @ApiParam(value = "物品ID", required = true) @RequestParam Long skuId) {
		return R.status(skuService.editValid(skuId));
	}

	/**
	 * qualityDateType
	 * 删除 物品
	 */
	@PostMapping("/remove")
	public R<Object> remove(@ApiParam(value = "主键集合", required = true) @RequestParam String ids) {
		CacheUtil.clear(SKU_CACHE);
		return R.status(skuService.removeByIds(Func.toLongList(ids)));
	}

	/**
	 * 导出
	 */
	@GetMapping("export")
	public void export(@ApiIgnore @RequestParam HashMap<String, Object> params, HttpServletResponse response) {
		skuService.exportExcel(params, response);
	}

	/**
	 * 导出模板
	 */
	@GetMapping("export-template")
	public void exportTemplate(HttpServletResponse response) {
		List<SkuExcel> skuExportList = new ArrayList<>();
		ExcelUtil.export(response, "物品", "物品数据表", skuExportList, SkuExcel.class);
	}

	/**
	 * 导入验证
	 */
	@PostMapping("import-valid")
	public R<List<DataVerify>> importValid(MultipartFile file) {
		return R.data(skuService.validExcel(ExcelUtil.read(file, SkuExcel.class)));
	}

	/**
	 * 导入验证通过的数据
	 */
	@PostMapping("import-data")
	public R<Boolean> importData(@RequestBody List<DataVerify> dataVerifyList) {
		return R.data(skuService.importData(dataVerifyList));
	}

	/**
	 * 计量单位下拉组件数据源: 根据物品id查询所有计量单位
	 */
	@PostMapping("/findSkuUmSelectResponseListBySkuId")
	public R<List<SkuUmSelectResponse>> findSkuUmSelectResponseListBySkuId(@RequestBody SkuUmSelectQuery skuUmSelectQuery){
		List<SkuUmSelectResponse> umList = skuBiz.findSkuUmSelectResponseListBySkuId(skuUmSelectQuery.getSkuId());
		return R.data(umList);
	}

	@ApiLog("物品管理-新增或修改")
	@PostMapping("/save")
	public R<String> save(@Valid @RequestBody SkuAddOrEditRequest skuAddOrEditRequest) {
		Sku sku = skuBiz.save(skuAddOrEditRequest);
		return R.success(String.format("[%s]成功，物品编码：[%s]"
			, (Func.isEmpty(skuAddOrEditRequest.getSkuId()) ? "新增" : "修改"), sku.getSkuCode()));
	}
}
