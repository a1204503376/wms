package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.suppliers.SuppliersBiz;
import org.nodes.wms.dao.basics.suppliers.dto.input.DeleteSuppliersRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.SuppliersPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.input.SuppliersRequest;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

/**
 * 供应商管理API
 *
 * @author 彭永程
 * @date 2022-04-20 10:47
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping("/suppliers")
public class SuppliersController {
	private final SuppliersBiz suppliersBiz;

	@GetMapping("/page")
	public R<Object> list(Query query, @RequestParam SuppliersPageQuery suppliersPageQuery) {
		IPage<?> suppliersList = suppliersBiz.getPageSuppliers(Condition.getPage(query), suppliersPageQuery);
		return R.data(suppliersList);
	}

	@ApiLog("新增供应商")
	@PostMapping("/save")
	public R<Object> save(@RequestParam SuppliersRequest suppliersRequest) {
		boolean state = suppliersBiz.saveSuppliers(suppliersRequest);
		return R.status(state);
	}

	@ApiLog("修改供应商")
	@PostMapping("/update")
	public R<Object> update(@RequestParam SuppliersRequest suppliersRequest) {
		boolean state = suppliersBiz.updateSuppliersById(suppliersRequest);
		return R.status(state);
	}

	@ApiLog("删除供应商")
	@GetMapping("/delete")
	public R delete(@RequestParam DeleteSuppliersRequest deleteSuppliersRequest){
		boolean state = suppliersBiz.removeSuppliersByIds(deleteSuppliersRequest);
		return R.status(state);
	}
}
