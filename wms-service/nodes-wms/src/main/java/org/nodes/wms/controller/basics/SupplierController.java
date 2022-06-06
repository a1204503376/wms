package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.suppliers.SupplierBiz;
import org.nodes.wms.dao.basics.suppliers.dto.input.*;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierSelectResponse;
import org.nodes.wms.dao.basics.suppliers.entities.Supplier;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * 供应商管理API
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "supplier")
public class SupplierController {
	private final SupplierBiz supplierBiz;

	@PostMapping("/page")
	public R<IPage<SupplierPageResponse>> page(Query query, @RequestBody SupplierPageQuery supplierPageQuery) {
		IPage<SupplierPageResponse> pageResponse = supplierBiz.getPage(Condition.getPage(query), supplierPageQuery);
		return R.data(pageResponse);
	}

	@ApiLog("供应商管理-新增")
	@PostMapping("/newSupplier")
	public R<String> newSupplier(@Valid @RequestBody AddSupplierRequest addSupplierRequest) {
		Supplier supplier = supplierBiz.newSupplier(addSupplierRequest);
		return R.success("新增成功，供应商编码:"+supplier.getCode());
	}

	@ApiLog("供应商管理-删除")
	@PostMapping("/remove")
	public R<Boolean> remove(@Valid @RequestParam RemoveRequest removeRequest){
		boolean state = supplierBiz.removeByIds(removeRequest);
		return R.status(state);
	}

	@PostMapping("/export")
	public void export(@RequestBody SupplierPageQuery supplierPageQuery, HttpServletResponse response){
		supplierBiz.exportSupplier(supplierPageQuery,response);
	}

	@GetMapping("/export-template")
	public void exportTemplate(HttpServletResponse response){
		List<SupplierImportRequest> importDataList = new ArrayList<>();
		ExcelUtil.export(response, "供应商", "供应商数据表", importDataList, SupplierImportRequest.class);
	}
	@ApiLog("供应商管理-导入")
	@PostMapping("/import-data")
	public R<String> importExcel(MultipartFile file){
		List<SupplierImportRequest> importDataList = ExcelUtil.read(file, SupplierImportRequest.class);
		boolean importFlag = supplierBiz.importExcel(importDataList);
		return importFlag ? R.success("导入成功") : R.fail("导入失败");
	}

	@PostMapping("/select")
	public R<List<SupplierSelectResponse>> getSupplierSelectResponseTop10List(@RequestBody SupplierSelectQuery supplierSelectQuery){
		List<SupplierSelectResponse> selectResponseList = supplierBiz.getSupplierSelectResponseTop10List(supplierSelectQuery);
		return R.data(selectResponseList);
	}
}
