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
import org.springblade.core.tool.utils.Func;
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

	/**
	 * 供应商：分页
	 */
	@PostMapping("/page")
	public R<IPage<SupplierPageResponse>> page(Query query, @RequestBody SupplierPageQuery supplierPageQuery) {
		IPage<SupplierPageResponse> pageResponse = supplierBiz.getPage(Condition.getPage(query), supplierPageQuery);
		return R.data(pageResponse);
	}

	/**
	 * 供应商：新增
	 */
	@ApiLog("供应商管理-新增")
	@PostMapping("/newSupplier")
	public R<String> newSupplier(@Valid @RequestBody AddOrEditSupplierRequest addOrEditSupplierRequest) {
		Supplier supplier = supplierBiz.newSupplier(addOrEditSupplierRequest);
		return R.success("新增成功，供应商编码:" + supplier.getCode());
	}

	/**
	 * 供应商：删除
	 */
	@ApiLog("供应商管理-删除")
	@PostMapping("/remove")
	public R<Boolean> remove(@Valid @RequestBody RemoveRequest removeRequest) {
		boolean state = supplierBiz.removeByIdList(removeRequest.getIdList());
		return R.status(state);
	}

	/**
	 * 供应商：服务端导出
	 */
	@PostMapping("/export")
	public void export(@RequestBody SupplierPageQuery supplierPageQuery, HttpServletResponse response) {
		supplierBiz.exportSupplier(supplierPageQuery, response);
	}

	/**
	 * 供应商：模板导出
	 */
	@GetMapping("/export-template")
	public void exportTemplate(HttpServletResponse response) {
		List<SupplierImportRequest> importDataList = new ArrayList<>();
		ExcelUtil.export(response, "供应商", "供应商数据表", importDataList, SupplierImportRequest.class);
	}

	/**
	 * 供应商：导入
	 */
	@ApiLog("供应商管理-导入")
	@PostMapping("/import-data")
	public R<String> importExcel(MultipartFile file) {
		boolean importFlag = supplierBiz.importExcel(file);
		return importFlag ? R.success("导入成功") : R.fail("导入失败");
	}

	/**
	 * 供应商组件：根据供应商编码或名称获取最近更新的10个供应商信息
	 */
	@PostMapping("/select")
	public R<List<SupplierSelectResponse>> getSupplierSelectResponseTop10List(@RequestBody SupplierSelectQuery supplierSelectQuery) {
		List<SupplierSelectResponse> selectResponseList = supplierBiz.getSupplierSelectResponseTop10List(supplierSelectQuery);
		return R.data(selectResponseList);
	}

	/**
	 * 供应商新增或修改：提供给外部系统的接口
	 */
	@ApiLog("供应商管理-新增或修改")
	@PostMapping("/save")
	public R<String> save(@Valid @RequestBody AddOrEditSupplierRequest addOrEditSupplierRequest) {
		Supplier supplier = supplierBiz.save(addOrEditSupplierRequest);
		return R.success(String.format("[%s]成功，供应商编码：[%s]"
			, (Func.isEmpty(supplier.getId()) ? "新增" : "修改"), supplier.getCode()));
	}
}
