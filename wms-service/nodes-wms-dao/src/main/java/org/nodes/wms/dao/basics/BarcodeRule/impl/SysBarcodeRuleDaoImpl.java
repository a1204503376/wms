package org.nodes.wms.dao.basics.BarcodeRule.impl;

import org.nodes.wms.dao.basics.BarcodeRule.SysBarcodeRuleDao;
import org.nodes.wms.dao.basics.BarcodeRule.entities.SysBarcodeRule;
import org.nodes.wms.dao.basics.BarcodeRule.mapper.SysBarcodeRuleMapper;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 编码规则Dao实现
 */
@Repository
public class SysBarcodeRuleDaoImpl extends BaseServiceImpl<SysBarcodeRuleMapper, SysBarcodeRule> implements SysBarcodeRuleDao {
	@Override
	public List<SysBarcodeRule> getSysBarcodeRules() {
		return super.list();
	}
}
