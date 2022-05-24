package org.nodes.wms.controller.instock.asn;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.core.tool.validation.Update;
import org.nodes.wms.biz.application.AsnManageBiz;
import org.nodes.wms.biz.instock.asn.AsnBiz;
import org.nodes.wms.dao.instock.asn.dto.input.AddOrEditAsnBillRequest;
import org.nodes.wms.dao.instock.asn.dto.input.AsnBillIdRequest;
import org.nodes.wms.dao.instock.asn.dto.input.DeleteRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnBillByEditResponse;
import org.nodes.wms.dao.instock.asn.dto.output.AsnBillViewResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.nodes.wms.dao.instock.asn.entities.AsnHeader;
import org.springblade.core.log.annotation.ApiLog;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	public R<Page<PageResponse>> page(Query query, @RequestBody PageParamsQuery pageParamsQuery) {
		Page<PageResponse> asnPage = asnBiz.getPageAsnBill(Condition.getPage(query), pageParamsQuery);
		return R.data(asnPage);
	}

	@PostMapping("/detail")
	public R<AsnBillViewResponse> asnBillViewDetail(@Valid @RequestBody AsnBillIdRequest asnBillIdRequest) {
		AsnBillViewResponse asnBillViewResponse = asnBiz.findAsnBillViewDetailByAsnBillId(asnBillIdRequest.getAsnBillId());
		return R.data(asnBillViewResponse);
	}

	@ApiLog("ASN单管理-新增")
	@PostMapping("/add")
	public R<String> add(@Valid @RequestBody AddOrEditAsnBillRequest addOrEditAsnBillRequest){
		AsnHeader asnHeader = asnBiz.add(addOrEditAsnBillRequest);
		return R.success("新增ASN单成功，ASN单编码: "+asnHeader.getAsnBillNo());
	}

	@PostMapping ("/detailByEdit")
	public R<AsnBillByEditResponse> detailByEdit(@Valid @RequestBody AsnBillIdRequest asnBillIdRequest){
		return R.data(asnBiz.findAsnHeaderAndAsnDetail(asnBillIdRequest.getAsnBillId()));
	}

	@ApiLog("ASN单管理-编辑")
	@PostMapping("/edit")
	public R<String> edit(@Validated({ Update.class }) @RequestBody AddOrEditAsnBillRequest addOrEditAsnBillRequest){
		AsnHeader asnHeader = asnBiz.edit(addOrEditAsnBillRequest);
 		return R.success("修改ASN单成功，ASN单编码: "+asnHeader.getAsnBillNo());
	}

	@ApiLog("ASN单管理-删除")
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
