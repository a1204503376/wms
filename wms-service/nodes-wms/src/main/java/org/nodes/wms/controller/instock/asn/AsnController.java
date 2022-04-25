package org.nodes.wms.controller.instock.asn;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.application.AsnManageBiz;
import org.nodes.wms.biz.instock.asn.AsnBiz;
import org.nodes.wms.dao.instock.asn.dto.input.DeleteRequest;
import org.nodes.wms.dao.instock.asn.dto.input.PageParamsQuery;
import org.nodes.wms.dao.instock.asn.dto.output.AsnDetailResponse;
import org.nodes.wms.dao.instock.asn.dto.output.PageResponse;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.mp.support.Query;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.*;

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
	public R<Object> page(Query query, @RequestParam PageParamsQuery pageParamsQuery) {
		Page<PageResponse> asnPage = asnBiz.getPageAsnBill(Condition.getPage(query), pageParamsQuery);
		return R.data(asnPage);
	}

	@GetMapping("/detail")
	public R<Object> getAsnContactDetail(@RequestParam DeleteRequest deleteRequest) {
		AsnDetailResponse asnDetailResponse = asnBiz.getAsnContactDetail(deleteRequest);
		return R.data(asnDetailResponse);
	}

	@GetMapping("/delete")
	public R<Object> remove(@RequestParam DeleteRequest deleteRequest) {
		boolean delete = asnManageBiz.remove(deleteRequest.getAsnBillId());
		return R.status(delete);
	}
}
