package org.nodes.wms.biz.basics.BarcodeRule;

import org.nodes.wms.dao.basics.BarcodeRule.dto.output.SysBarcodeRuleResponse;

import java.util.List;

/**
 * 编码规则Biz
 */
public interface SysBarcodeRuleBiz {
	/**
	 * 获取编码规则集合
	 * @return 编码规则集合
	 */
	List<SysBarcodeRuleResponse> getAllSysBarcodeRule();
}
