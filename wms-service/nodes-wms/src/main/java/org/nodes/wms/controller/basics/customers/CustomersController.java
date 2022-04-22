package org.nodes.wms.controller.basics.customers;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.customers.CustomersBiz;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersPageQuery;
import org.nodes.wms.dao.basics.customers.dto.input.CustomersRequest;
import org.nodes.wms.dao.basics.customers.dto.input.DeleteRequest;
import org.nodes.wms.dao.basics.customers.dto.output.CustomersResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
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
	@GetMapping("/page")
	public R<Object> page(@RequestParam CustomersPageQuery customersPageQuery, Query query) {
		IPage<CustomersResponse> pages = customersBiz.page(Condition.getPage(query),customersPageQuery);

		return R.data(pages);
	}
	/**
	 * 客户管理新增
	 */
	@ApiLog("客户管理-新增")
	@PostMapping("/save")
	public R save(@RequestParam CustomersRequest customersRequest) {
		int count = customersBiz.save(customersRequest);
		return count>0?R.success("提交成功"):R.fail("失败");
	}

	/**
	 * 客户管理修改
	 */
	@ApiLog("客户管理-修改")
	@PostMapping("/update")
	public R update(@RequestParam CustomersRequest customersRequest) {
		int count = customersBiz.update(customersRequest);
		return count>0?R.success("修改成功"):R.fail("修改失败");
	}
	/**
	 * 客户管理删除
	 */
	@ApiLog("客户管理-逻辑删除")
	@PostMapping("/delete")
	public R delete(@RequestParam DeleteRequest deleteRequest) {
		int count = customersBiz.delete(deleteRequest);
		return count>0?R.success("删除成功"):R.fail("删除失败");
	}
}
