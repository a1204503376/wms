package org.nodes.wms.biz.basics.BarcodeRule.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.wms.biz.basics.BarcodeRule.SysBarcodeRuleBiz;
import org.nodes.wms.dao.basics.BarcodeRule.SysBarcodeRuleDao;
import org.nodes.wms.dao.basics.BarcodeRule.dto.output.SysBarcodeRuleResponse;
import org.nodes.wms.dao.basics.BarcodeRule.entities.SysBarcodeRule;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 编码规则Biz实现
 */
@Service
@RequiredArgsConstructor
public class SysBarcodeRuleBizImpl implements SysBarcodeRuleBiz {
	private final SysBarcodeRuleDao sysBarcodeRuleDao;

	/**
	 * 获取编码规则集合
	 * @return 编码规则集合
	 */
	@Override
	public List<SysBarcodeRuleResponse> getAllSysBarcodeRule() {
		List<SysBarcodeRule> sysBarcodeRules = sysBarcodeRuleDao.getSysBarcodeRules();
		return BeanUtil.copy(sysBarcodeRules,SysBarcodeRuleResponse.class);
	}
}
