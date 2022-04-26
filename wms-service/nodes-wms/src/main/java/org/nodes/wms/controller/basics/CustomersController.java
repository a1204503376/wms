package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.customers.CustomersBiz;
import org.nodes.wms.dao.basics.customers.dto.input.CustomerPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.newCustomerRequest;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteCustomerRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;
/**
 *  客户管理API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL +"customers")
public class CustomersController {
	private  final CustomersBiz customersBiz;
	/**
	 * 客户管理分页查询
	 */
	@GetMapping("/queryPage")
	public R<IPage<CustomersResponse>>  page(@RequestParam CustomerPageQuery customerPageQuery, Query query) {
		IPage<CustomersResponse> pages = customersBiz.getPage(customerPageQuery,query);
		return R.data(pages);
	}
	/**
	 * 客户管理新增
	 */
	@ApiLog("客户管理-新增")
	@PostMapping("/add")
	public R<Boolean> newCustomer(@RequestParam newCustomerRequest newCustomerRequest) {
		return R.status(customersBiz.newCustomers(newCustomerRequest));
	}

	/**
	 * 客户管理删除
	 */
	@ApiLog("客户管理-逻辑删除")
	@PostMapping("/delete")
	public R<Boolean> delete(@RequestParam DeleteCustomerRequest deleteRequest) {
		return R.status(customersBiz.remove(deleteRequest));
	}
}
