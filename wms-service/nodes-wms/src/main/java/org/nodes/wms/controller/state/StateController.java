package org.nodes.wms.controller.state;

import lombok.RequiredArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;
import org.nodes.wms.dao.application.dto.output.AuditLogTypeResponse;
import org.nodes.wms.dao.application.dto.output.LpnTypeResponse;
import org.nodes.wms.dao.application.dto.output.StateGeneralResponse;
import org.nodes.wms.dao.basics.lpntype.enums.LpnTypeEnum;
import org.nodes.wms.dao.common.log.enumeration.AuditLogType;
import org.nodes.wms.dao.instock.asn.enums.AsnBillStateEnum;
import org.nodes.wms.dao.instock.asn.enums.InStorageTypeEnum;
import org.nodes.wms.dao.outstock.so.dto.output.SoBillStateResponse;
import org.nodes.wms.dao.stock.dto.output.StockLogTypeResponse;
import org.nodes.wms.dao.stock.dto.output.StockStatusResponse;
import org.nodes.wms.dao.stock.enums.StockLogTypeEnum;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
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

	/**
	 * @return 获取容器类型
	 */
	@PostMapping("getLpnTypeState")
	public R<List<LpnTypeResponse>> getLpnTypeState(){
		return R.data(LpnTypeEnum.getList());
	}

	@PostMapping("getAuditLogTypeState")
	public R<List<AuditLogTypeResponse>> getAuditLogType()
	{
		return R.data(AuditLogType.getList());
	}

	@PostMapping("getStockLogType")
	public R<List<StockLogTypeResponse>> getStockLogType(){
		return R.data(StockLogTypeEnum.getList());
	}

	@GetMapping("/getSoBillState")
	public R<List<SoBillStateResponse>> getSoBillState(){
		return R.data(SoBillStateEnum.getList());
	}
	@GetMapping("/getStockStatus")
	public R<List<StockStatusResponse>> getStockStatus(){
		return R.data(StockStatusEnum.getList());
	}
}
