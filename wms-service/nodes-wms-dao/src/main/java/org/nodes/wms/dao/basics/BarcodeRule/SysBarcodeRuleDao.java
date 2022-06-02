package org.nodes.wms.dao.basics.BarcodeRule;

import org.nodes.wms.dao.basics.BarcodeRule.entities.SysBarcodeRule;

import java.util.List;

/**
 * 编码规则Dao
 */
public interface SysBarcodeRuleDao {
	/**
	 * 返回编码规则集合
	 * @return List集合
	 */
    List<SysBarcodeRule> getSysBarcodeRules();
}
