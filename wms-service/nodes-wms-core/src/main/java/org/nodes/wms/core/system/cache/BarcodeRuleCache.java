package org.nodes.wms.core.system.cache;

import org.nodes.wms.core.system.entity.BarcodeRule;
import org.nodes.wms.core.system.service.IBarcodeRuleService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 条码规则缓存
 *
 * @Author zx
 * @Date 2020/1/10
 **/
public class BarcodeRuleCache {

	/*static Map<Long, BarcodeRule> barcodeRuleMap = new HashMap<>();

	static IBarcodeRuleService barcodeRuleService;

	static {
		barcodeRuleService = SpringUtil.getBean(IBarcodeRuleService.class);

		barcodeRuleService.list().forEach(barcodeRule -> {
			barcodeRuleMap.put(barcodeRule.getSbrId(), barcodeRule);
		});
	}
	*//**
	 * 获取条码规则实体
	 *
	 * @param id 主键
	 * @return
	 *//*
	public static BarcodeRule getById(Long id) {
		return barcodeRuleMap.getOrDefault(id, null);
	}

	*//**
	 * 获取条码列表
	 *
	 * @return 参数列表
	 *//*
	public static List<BarcodeRule> list() {
		return new ArrayList<>(barcodeRuleMap.values());
	}

	*//**
	 * 添加 或者 更新 参数列表的缓存
	 *
	 * @param barcode 参数查询条件
	 *//*
	public static void saveOrUpdate(BarcodeRule barcode) {
		BarcodeRule findObj = barcodeRuleMap.getOrDefault(barcode.getSbrId(), null);
		if (Func.isEmpty(findObj)) {
			barcodeRuleMap.put(barcode.getSbrId(), barcode);
		} else {
			barcodeRuleMap.replace(barcode.getSbrId(), barcode);
		}
	}
	*//**
	 * 移除 参数列表中指定的数据
	 *
	 * @param id 参数 主键ID
	 *//*
	public static void remove(Long id) {
		barcodeRuleMap.remove(id);
	}
	*//**
	 * 移除 参数列表中指定的数据
	 *
	 * @param idList 参数 主键ID集合
	 *//*
	public static void remove(List<Long> idList) {
		for (Long id : idList) {
			remove(id);
		}
	}*/
}
