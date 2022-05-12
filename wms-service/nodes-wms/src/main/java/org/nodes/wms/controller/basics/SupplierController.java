package org.nodes.wms.controller.basics;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.basics.suppliers.SupplierBiz;
import org.nodes.wms.dao.basics.suppliers.dto.input.AddSupplierRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.RemoveRequest;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierPageQuery;
import org.nodes.wms.dao.basics.suppliers.dto.input.SupplierSelectQuery;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierPageResponse;
import org.nodes.wms.dao.basics.suppliers.dto.output.SupplierSelectResponse;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

/**
 * 供应商管理API
 *
 * @author 彭永程
 * @date 2022-04-20 10:47
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
	public R<Boolean> newSupplier(@Valid @RequestBody AddSupplierRequest addSupplierRequest) {
		boolean state = supplierBiz.newSupplier(addSupplierRequest);
		return R.status(state);
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

	@PostMapping("/select")
	public R<List<SupplierSelectResponse>> getSupplierSelectResponseTop10List(@RequestBody SupplierSelectQuery supplierSelectQuery){
		List<SupplierSelectResponse> selectResponseList = supplierBiz.getSupplierSelectResponseTop10List(supplierSelectQuery);
		return R.data(selectResponseList);
	}
}
