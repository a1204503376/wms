package org.nodes.wms.core.utils;

import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.core.constant.DictConstant;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.utils.NodesUtil;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.dao.basics.sku.entities.Sku;
import org.nodes.wms.core.basedata.entity.SkuLot;
import org.nodes.wms.core.basedata.entity.SkuLotVal;
import org.nodes.wms.dao.basics.sku.enums.SkuLotEditTypeEnum;
import org.nodes.wms.dao.basics.sku.enums.SkuLotMaskEnum;
import org.nodes.wms.core.basedata.service.ISkuLotService;
import org.nodes.wms.core.basedata.service.ISkuLotValService;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.SpringUtil;
import org.springblade.core.tool.utils.StringUtil;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: WmsCore
 * @description: 物品批属性工具类
 * @author: pengwei
 * @create: 2020-12-18 14:24
 **/
public class SkuLotUtil {

	/**
	 * 根据批属性规则填充批属性
	 *
	 * @param sku     物品对象
	 * @param entity  待填充的批属性对象
	 * @param source1 批属性数据源对象1
	 */
	public static void fill(@NotNull Sku sku, @NotNull SkuLotBaseEntity entity, SkuLotBaseEntity source1,
							SkuLotBaseEntity source2) {
		// 获取批属性、批属性验证
		ISkuLotService skuLotService = SpringUtil.getBean(ISkuLotService.class);
		SkuLot skuLot = skuLotService.getById(sku.getWslId());
		ISkuLotValService skuLotValService = SpringUtil.getBean(ISkuLotValService.class);
		SkuLotVal skuLotVal = skuLotValService.getById(sku.getWslvId());
		if (Func.isEmpty(skuLot) || Func.isEmpty(skuLotVal)) {
			return;
		}
		// 循环批属性，给批属性赋值
		for (int i = 1; i <= ParamCache.LOT_COUNT; i++) {
			Integer editType = skuLotVal.skuLotEditTypeGet(i);
			if (Func.isEmpty(editType)
				|| editType.equals(SkuLotEditTypeEnum.DEFAULT.getIndex())
				|| editType.equals(SkuLotEditTypeEnum.USER_INPUT.getIndex())) {
				// 复制批属性
				if (Func.isNotEmpty(source1) && source1.skuLotChk(i)) {
					entity.skuLotSet(i, source1.skuLotGet(i));
				} else if (Func.isNotEmpty(source2) && source2.skuLotChk(i)) {
					entity.skuLotSet(i, source2.skuLotGet(i));
				}
				continue;
			}
			Integer skuLotMaskVal = skuLotVal.skuLotMaskGet(i);
			if (Func.isEmpty(skuLotMaskVal)) {
				continue;
			}
			// 过滤 dict.remark 为空的选项
			String dictValue = DictCache.getValue(DictConstant.SKU_LOT_VAL, skuLotMaskVal);
			if (Func.isEmpty(dictValue)) {
				continue;
			}
			// 获取格式化规则，用于下面判断是否为日期时间类型
			String skuLotMixMask = skuLotVal.skuLotMixMaskGet(i);
			// 判断 dict.remark 中是否存在. 如果存在还需要找其他对象类
			if (dictValue.contains(StringPool.DOT)) {
				// TODO 彭伟：找其他对象属性，后期处理（欣天新用不着）
			} else {
				if (Func.isNotEmpty(skuLotMixMask) && skuLotMixMask.toLowerCase().contains("yy")) {
					// 日期类型
					try {
						SimpleDateFormat dateFormat = new SimpleDateFormat(skuLotMixMask);
						dateFormat.setLenient(false);
						String dateStr = null;
						if (SkuLotMaskEnum.SYSTEM_TIME.getIndex().equals(skuLotMaskVal)) {
							// 系统时间
							if (SkuLotEditTypeEnum.AUTO_EDIT.getIndex().equals(editType)
								&& Func.isNotEmpty(source2)) {
								dateStr = dateFormat.format(dateFormat.parse(source2.skuLotGet(i)));
							} else {
								dateStr = dateFormat.format(new Date());
							}
						} else {
							// 字段时间
							Object value = NodesUtil.getFieldValue(source1, dictValue);
							dateStr = dateFormat.format(value);
						}
						entity.skuLotSet(i, dateStr);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					// 填充字段
					Object value = NodesUtil.getFieldValue(source1, dictValue);
					entity.skuLotSet(i, Func.toStr(value));
				}
			}

		}
	}

	public static Map<String, String> createSqlSkuLotMap(Class classe, Object obg) {
		Map<String, String> map = new HashMap<>();
		List<Field> fieldList = NodesUtil.getFieldList(classe);
		fieldList.forEach(field -> {
			field.setAccessible(true);
			try {
				String name = field.getName();
				if (name.contains("skuLot")) {
					String value = (String) field.get(obg);
					int index = Integer.valueOf(name.substring(6, name.length()));
					if (index <= ParamCache.LOT_COUNT) {
						String underline = StringUtil.humpToUnderline(name);
						map.put(underline, value);
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		});
		return map;
	}

	public static Map<String, String> createSqlSkuLotMap1(Class classe, Object obg) {
		Map<String, String> map = new HashMap<>();
		List<Field> fieldList = NodesUtil.getFieldList(classe);
		fieldList.forEach(field -> {
			field.setAccessible(true);
			try {
				String name = field.getName();
				if (name.contains("skuLot")) {
					String value = (String) field.get(obg);
					int index = Integer.valueOf(name.substring(6, name.length()));
					if (index > 1 && index <= ParamCache.LOT_COUNT) {
						String underline = StringUtil.humpToUnderline(name);
						map.put(underline, value);
					}
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		});
		return map;
	}
}
