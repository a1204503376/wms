package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.basics.bom.WmsSkuBomBiz;
import org.nodes.wms.dao.basics.bom.dto.input.DeleteSkuBomByIdsRequest;
import org.nodes.wms.dao.basics.bom.dto.input.FindSkuBomByIdRequset;
import org.nodes.wms.dao.basics.bom.dto.input.WmsSkuBomPageQuery;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomExcelResponse;
import org.nodes.wms.dao.basics.bom.dto.output.WmsSkuBomResponse;
import org.nodes.wms.dao.basics.bom.entites.SkuBom;
import org.nodes.wms.dao.basics.bom.dto.input.SkuBomAddOrEditRequest;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.utils.Func;
import org.springframework.web.bind.annotation.*;
import org.springblade.core.tool.api.R;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "WmsSkuBom")
public class WmsSkuBomController {
	private final WmsSkuBomBiz skuBomBiz;

	/**
	 * 分页查询
	 *
	 * @param query           底部分页参数
	 * @param skuBomPageQuery 查询条件
	 * @return 分页数据
	 */
	@PostMapping("/findAllWmsSkuBom")
	public R<IPage<WmsSkuBomResponse>> findAllWmsSkuBom(@RequestBody WmsSkuBomPageQuery skuBomPageQuery, Query query) {
		Page<WmsSkuBomResponse> skuBomPage = skuBomBiz.getSkuBomPage(query, skuBomPageQuery);
		return R.data(skuBomPage);
	}

	/**
	 * 物料清单导出
	 */
	@PostMapping("/excel")
	public void excel(@ApiIgnore @RequestBody HashMap<String, Object> params, HttpServletResponse response) {
		skuBomBiz.exportExcel(params, response);
	}

	@ApiLog("物品清单管理-新增或修改")
	@PostMapping("/save")
	public R<String> save(@Valid @RequestBody SkuBomAddOrEditRequest skuBomAddOrEditRequest) {
		SkuBom skuBom = skuBomBiz.save(skuBomAddOrEditRequest);
		return R.success(String.format("%s物品清单成功",
			Func.isEmpty(skuBomAddOrEditRequest.getId()) ? "新增" : "修改"));
	}

	/**
	 * 物品清单管理-删除
	 *
	 * @param request 物料清单删除请求对象
	 * @return 是否成功
	 */
	@ApiLog("物品清单管理-删除")
	@PostMapping("/delete")
	public R delete(@RequestBody DeleteSkuBomByIdsRequest request) {
		return R.status(skuBomBiz.deleteSkuBomByIds(request));
	}


	/**
	 * 物品清单管理-查询物料清单详情
	 *
	 * @param request 物料清单ById查询请求对象
	 * @return 是否成功
	 */
	@PostMapping("/selectSkuBomById")
	public R<SkuBom> selectSkuBomById(@RequestBody FindSkuBomByIdRequset request) {
		return R.data(skuBomBiz.findSkuBomById(request.getId()));
	}

	/**
	 * 导出 导入模板
	 *
	 * @param response 响应对象
	 * @return 是否成功
	 */
	@ApiLog("物品清单管理-导出 导入模板")
	@GetMapping("/export-template")
	public void selectSkuBomById(HttpServletResponse response) {
		List<WmsSkuBomExcelResponse> excelResponses = new ArrayList<>();
		ExcelUtil.export(response, excelResponses, WmsSkuBomExcelResponse.class);
	}

	@ApiLog("客户管理-导入")
	@PostMapping("/import-data")
	public R<String> importData(MultipartFile file) {
		List<WmsSkuBomExcelResponse> importDataList = ExcelUtil.read(file, WmsSkuBomExcelResponse.class);
		boolean importFlag = skuBomBiz.importExcel(importDataList);
		return importFlag ? R.success("导入成功") : R.fail("导入失败");
	}

}
