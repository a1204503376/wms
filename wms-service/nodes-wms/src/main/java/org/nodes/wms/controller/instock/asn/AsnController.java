package org.nodes.wms.controller.instock.asn;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.application.AsnManageBiz;
import org.nodes.wms.biz.instock.asn.AsnBiz;
import org.nodes.wms.dao.instock.asn.dto.input.*;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailByEditResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * ASN单管理API
 *
 * @author 彭永程
 * @date 2022-04-22 9:22
 **/
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.WMS_ROOT_URL + "asn")
public class AsnController {

	private final AsnBiz asnBiz;

	private final AsnManageBiz asnManageBiz;

	@PostMapping("/page")
	public R<Page<PageResponse>> page(@RequestBody Query query, @RequestBody PageParamsQuery pageParamsQuery) {
		Page<PageResponse> asnPage = asnBiz.getPageAsnBill(Condition.getPage(query), pageParamsQuery);
		return R.data(asnPage);
	}

	@GetMapping("/detail")
	public R<AsnDetailResponse> getAsnContactDetail(@Valid @RequestParam AsnBillIdRequest asnBillIdRequest) {
		AsnDetailResponse asnDetailResponse = asnBiz.getAsnContactDetail(asnBillIdRequest);
		return R.data(asnDetailResponse);
	}

	@PostMapping("/add")
	public R<Boolean> add(@Valid @RequestParam AddAsnBillRequest addAsnBillRequest){
		boolean add = asnBiz.add(addAsnBillRequest);
		return R.status(add);
	}

	@GetMapping("/detailByEdit")
	public R<AsnDetailByEditResponse> detailByEdit(@Valid @RequestParam AsnBillIdRequest asnBillIdRequest){
		return R.data(asnBiz.getAsnHeaderAndAsnDetail(asnBillIdRequest));
	}

	@PostMapping("/edit")
	public R<Boolean> edit(@Valid @RequestBody EditAsnBillRequest editAsnBillRequest){
		boolean edit = asnBiz.edit(editAsnBillRequest);
 		return R.status(edit);
	}

	@PostMapping("/remove")
	public R<Boolean> remove(@Valid @RequestBody DeleteRequest deleteRequest) {
		boolean delete = asnManageBiz.remove(deleteRequest.getAsnBillIds());
		return R.status(delete);
	}

	@PostMapping("/export")
	public void export(@RequestBody PageParamsQuery pageParamsQuery, HttpServletResponse httpServletResponse){
		asnBiz.exportAsnBill(pageParamsQuery,httpServletResponse);
	}
}
