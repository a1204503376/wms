package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.suppliers.SuppliersBiz;
import org.nodes.wms.dao.basics.suppliers.dto.input.DeleteSuppliersRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.input.AddSupplierRequest;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.context.annotation.Primary;
import org.springframework.web.bind.annotation.*;

/**
 * 供应商管理API
 *
 * @author 彭永程
 * @date 2022-04-20 10:47
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "/suppliers")
@Primary
public class SuppliersController {
	private final SuppliersBiz suppliersBiz;

	@PostMapping("/page")
	public R<IPage<SupplierPageResponse>> page(Query query, @RequestParam SupplierPageQuery supplierPageQuery) {
		IPage<SupplierPageResponse> pageResponse = suppliersBiz.getPage(Condition.getPage(query), supplierPageQuery);
		return R.data(pageResponse);
	}

	@ApiLog("供应商管理-新增")
	@PostMapping("/newSupplier")
	public R<Boolean> newSupplier(@RequestParam AddSupplierRequest addSupplierRequest) {
		Boolean state = suppliersBiz.newSupplier(addSupplierRequest);
		return R.status(state);
	}

	@ApiLog("供应商管理-删除")
	@GetMapping("/remove")
	public R<Boolean> remove(@RequestParam DeleteSuppliersRequest deleteSuppliersRequest){
		Boolean state = suppliersBiz.removeByIds(deleteSuppliersRequest);
		return R.status(state);
	}
}
