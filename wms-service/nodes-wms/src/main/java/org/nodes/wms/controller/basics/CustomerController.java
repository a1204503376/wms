package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.customer.CustomerBiz;
import org.nodes.wms.dao.basics.customer.dto.input.*;
import org.nodes.wms.dao.basics.customer.dto.input.CustomerPageQuery;
import org.nodes.wms.dao.basics.customer.dto.input.CustomerSelectQuery;
import org.nodes.wms.dao.basics.customer.dto.input.NewCustomerRequest;
import org.nodes.wms.dao.basics.customer.dto.input.DeleteCustomerRequest;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerSelectResponse;
import org.nodes.wms.dao.basics.customer.dto.output.CustomerResponse;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 *  客户管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL +"customer")
public class CustomerController {

	private  final CustomerBiz customerBiz;

	/**
	 * 客户管理分页查询
	 */
	@PostMapping("/page")
	public R<IPage<CustomerResponse>>  page(@RequestBody CustomerPageQuery customerPageQuery, Query query) {
		IPage<CustomerResponse> pages = customerBiz.getPage(customerPageQuery,query);
		return R.data(pages);
	}

	/**
	 * 客户管理新增
	 */
	@ApiLog("客户管理-新增")
	@PostMapping("/newCustomer")
	public R<Boolean> newCustomer(@RequestBody NewCustomerRequest newCustomerRequest) {
		return R.status(customerBiz.newCustomers(newCustomerRequest));
	}

	/**
	 * 客户管理删除
	 */
	@ApiLog("客户管理-逻辑删除")
	@PostMapping("/delete")
	public R<Boolean> delete(@RequestBody DeleteCustomerRequest deleteRequest) {
		System.out.println(deleteRequest);
		return R.status(customerBiz.remove(deleteRequest));
	}

	/**
	 * 导出
	 */
	@PostMapping("export")
	public void export( @RequestBody CustomerPageQuery customerPageQuery, HttpServletResponse response) {
		customerBiz.exportExcel(customerPageQuery, response);
	}

	@GetMapping("/export-template")
	public void exportTemplate(HttpServletResponse response){
		List<CustomerImportRequest> importExcelList = new ArrayList<>();
		ExcelUtil.export(response, "客户", "客户数据表", importExcelList, CustomerImportRequest.class);
	}

	@ApiLog("客户管理-导入")
	@PostMapping("/import-data")
	public R<String> importData(MultipartFile file){
		List<CustomerImportRequest> importDataList = ExcelUtil.read(file, CustomerImportRequest.class);
		boolean importFlag = customerBiz.importExcel(importDataList);
		return importFlag ? R.success("导入成功") : R.fail("导入失败");
	}


	/**
	 * 获取客户下拉列表最近10条数据
	 */
	@PostMapping("getCustomerSelectResponseTop10List")
	public R<List<CustomerSelectResponse>> getCustomerSelectResponseTop10List(@RequestBody CustomerSelectQuery customerSelectQuery) {
		return R.data(customerBiz.getCustomerSelectResponseTop10List(customerSelectQuery));
	}
	@ApiLog("客户管理-新增或修改")
	@PostMapping("/saveOrUpdate")
	public R<String> saveOrUpdate(@Valid @RequestBody NewCustomerRequest newCustomerRequest) {
		String msg  = customerBiz.saveOrUpdate(newCustomerRequest);
		return R.success(msg);
	}
}
