package org.nodes.wms.controller.state;

import lombok.RequiredArgsConstructor;
import org.nodes.core.tool.constant.WmsApiPath;
import org.nodes.wms.biz.instock.asn.enums.AsnBillStateEnum;
import org.nodes.wms.biz.instock.asn.enums.InStorageTypeEnum;
import org.nodes.wms.dao.application.dto.output.StateGeneralResponse;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 业务状态API
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(WmsApiPath.STATE_URL)
public class StateController {

	/**
	 * 获取ASN单状态
	 */
	@PostMapping("getAsnBillState")
	public R<List<StateGeneralResponse>> getAsnBillState() {
		return R.data(AsnBillStateEnum.getList());
	}

	/**
	 * 获取入库方式
	 */
	@PostMapping("getStorageMethod")
	public R<List<StateGeneralResponse>> getStorageMethod() {
		return R.data(InStorageTypeEnum.getList());
	}
}
