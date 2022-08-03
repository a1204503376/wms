package org.nodes.wms.pdaController.user;

import lombok.AllArgsConstructor;
import org.nodes.core.constant.WmsApiPath;
import org.nodes.wms.biz.basics.BarcodeRule.SysBarcodeRuleBiz;
import org.nodes.wms.dao.basics.BarcodeRule.dto.output.SysBarcodeRuleResponse;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Pda查看编码规则的controller
 */
@RestController
@AllArgsConstructor
@RequestMapping(WmsApiPath.WMS_PDA_API)
public class PdaSysBarcodeRuleController {
	private final SysBarcodeRuleBiz sysBarcodeRuleBiz;

	@GetMapping("/findAllSysBarCodeRule")
	public R<List<SysBarcodeRuleResponse>> findAllSysBarCodeRule(){
		return R.data(sysBarcodeRuleBiz.getAllSysBarcodeRule());
	}
}
