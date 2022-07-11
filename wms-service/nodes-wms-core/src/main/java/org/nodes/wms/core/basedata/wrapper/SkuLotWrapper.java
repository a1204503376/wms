package org.nodes.wms.core.basedata.wrapper;

import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import org.nodes.core.base.cache.ParamCache;
import org.nodes.wms.core.basedata.dto.SkuLotDTO;
import org.nodes.wms.dao.basics.owner.entities.Owner;
import org.nodes.wms.dao.basics.skulot.entities.SkuLot;
import org.nodes.wms.core.basedata.excel.SkuLotExcel;
import org.nodes.wms.core.basedata.service.IOwnerService;
import org.nodes.wms.core.basedata.vo.SkuLotVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.SpringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author chenhz
 * @program 批属性封装方法
 * @description 批属性分类实体封装VO
 * @create 20191205
 */
public class SkuLotWrapper extends BaseEntityWrapper<SkuLot, SkuLotVO> {
	/**
	 * 物品分类封装
	 */
	public static SkuLotWrapper build() {
		return new SkuLotWrapper();
	}
	/**
	 * 将批属性封装成map
	 *
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */
	public static Map<String, Object> skuLotToMapWithStock(Object obj) throws IllegalAccessException {
		Map<String, Object> map = new HashMap<>();
		List<Field> fields = ReflectionKit.getFieldList(obj.getClass());
		for (int i = 0; i < fields.size(); i++) {
			//设置是否允许访问，不是修改原来的访问权限修饰词。
			Field field = fields.get(i);
			field.setAccessible(true);
			if (field.getName().contains("skuLot")) {
				//正则判断是否是数字
				if (Pattern.compile("^[-\\+]?[\\d]*$").matcher(field.getName().substring(6)).matches()) {
					if (Integer.valueOf(field.getName().substring(6)) <= ParamCache.LOT_COUNT) {
						if(Func.isNotEmpty(field.get(obj))){
							//key为skuLot1-30
							map.put(field.getName(), field.get(obj));
						}else{
							map.put(field.getName(), "");
						}
					}
				}
			}
		}
		return map;
	}
	@Override
	public SkuLotVO entityVO(SkuLot skuLot) {
		SkuLotVO skuLotVO = BeanUtil.copy(skuLot, SkuLotVO.class);
		if (ObjectUtil.isNotEmpty(skuLotVO)) {
			//查询货主缓存信息根据货主ID获取货主名字
			IOwnerService ownerService = SpringUtil.getBean(IOwnerService.class);
			Owner owner = ownerService.getById(skuLotVO.getWoId());
			if (ObjectUtil.isNotEmpty(owner)) {
				skuLotVO.setOwnerName(owner.getOwnerName());
			}
		}
		return skuLotVO;
	}
	public SkuLotDTO entityDTO(SkuLotExcel skuLotExcel) {
		SkuLotDTO skuLotDTO = BeanUtil.copy(skuLotExcel, SkuLotDTO.class);
		return skuLotDTO;
	}
	/**
	 * 将批属性封装成map
	 *
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */
	public static Map<String, Object> skuLotToMap(Object obj) throws IllegalAccessException {
		Map<String, Object> map = new HashMap<>();
		Field[] fields = obj.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			//设置是否允许访问，不是修改原来的访问权限修饰词。
			fields[i].setAccessible(true);
			if (fields[i].getName().contains("skuLot")) {
				//正则判断是否是数字
				if (Pattern.compile("^[-\\+]?[\\d]*$").matcher(fields[i].getName().substring(6)).matches()) {
					if (Integer.valueOf(fields[i].getName().substring(6)) <= ParamCache.LOT_COUNT) {
						if(Func.isNotEmpty(fields[i].get(obj))){
							//key为skuLot1-30
							map.put(fields[i].getName(), fields[i].get(obj));
						}else{
							map.put(fields[i].getName(), "");
						}
					}
				}
			}
		}
		return map;
	}
	/**
	 * 将批属性封装成map
	 *
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 */
	public static Map<String, Object> skuLotToMap1(Object obj) throws IllegalAccessException {
		Map<String, Object> map = new HashMap<>();
		List<Field> fields = ReflectionKit.getFieldList(obj.getClass());
		for (int i = 0; i < fields.size(); i++) {
			//设置是否允许访问，不是修改原来的访问权限修饰词。
			fields.get(i).setAccessible(true);
			if (fields.get(i).getName().contains("skuLot")) {
				//正则判断是否是数字
				if (Pattern.compile("^[-\\+]?[\\d]*$").matcher(fields.get(i).getName().substring(6)).matches()) {
					if (Integer.valueOf(fields.get(i).getName().substring(6)) <= ParamCache.LOT_COUNT) {
						if(Func.isNotEmpty(fields.get(i).get(obj))){
							map.put(fields.get(i).getName(), fields.get(i).get(obj));
						}else{
							map.put(fields.get(i).getName(), "");
						}
					}
				}
			}
		}
		return map;
	}
	/**
	 * 给对象的批属性赋值
	 * @param i 索引
	 * @param skuLotValue 值
	 * @param o 要赋值对象
	 * @throws IllegalAccessException
	 */
	public void setSkuLotToObject(int i, String skuLotValue, Object o) throws Exception {
		Class<?> tClass = o.getClass();
		Field field = tClass.getDeclaredField("skuLot"+i);
		char[] tChars = field.getName().toCharArray();
		tChars[0] -= 32;
		String tMethodName = "set" + String.valueOf(tChars);
		Method method = tClass.getMethod(tMethodName, field.getType());
		method.invoke(o,skuLotValue);
	}
}
