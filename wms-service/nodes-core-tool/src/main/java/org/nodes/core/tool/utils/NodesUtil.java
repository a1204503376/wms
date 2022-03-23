package org.nodes.core.tool.utils;


import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringPool;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.util.CollectionUtils;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author pengwei
 * @program Ems_Core
 * @description 节点通通用工具类
 * @since 2020-08-18
 */
public class NodesUtil {

	/**
	 * 用 obj2 比较 obj1 ，obj2 属性为空，则忽略
	 *
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 所有属性是否相等（相等：true， 不相等：false）
	 */
	public static Boolean compare(Object obj1, Object obj2) {
		return compare(obj1, obj2, false);
	}

	/**
	 * 用 obj2 比较 obj1 ，obj2 属性为空，则忽略
	 *
	 * @param obj1       对象1
	 * @param obj2       对象2
	 * @param superClass 是否比较基类的字段
	 * @return 所有属性是否相等（相等：true， 不相等：false）
	 */
	public static Boolean compare(Object obj1, Object obj2, boolean superClass) {
		Boolean result = false;

		if (Func.isEmpty(obj1) || Func.isEmpty(obj2)) {
			// 其中一个对象为空的情况下
			return Func.isEmpty(obj1) && Func.isEmpty(obj2);
		}
		// 获取两个对象所有字段
		List<Field> fieldList1 = getFieldList(obj1.getClass());
		List<Field> fieldList2 = getFieldList(obj2.getClass());

		// 循环 obj1 的所有字段
		for (Field field1 : fieldList1) {

			Field field2 = fieldList2.stream().filter(item -> {
				return item.getName().equals(field1.getName());
			}).findFirst().orElse(null);

			if (Func.isEmpty(field2)) {
				// 字段没有找到就跳过
				continue;
			}
			Object value1 = ReflectionKit.getFieldValue(obj1, field1.getName());
			Object value2 = ReflectionKit.getFieldValue(obj2, field2.getName());

			if (Func.isEmpty(value2)) {
				// obj2 的属性值位空时，则跳过
				result = true;
				continue;
			}
			if (!value2.equals(value1)) {
				result = false;
				break;
			} else {
				result = true;
			}
		}

		return result;
	}

	public static <T> List<Field> getFieldList(Class<T> clazz) {
		List<Field> fieldList = new ArrayList<>();
		Class superClass = clazz;
		while (Func.isNotEmpty(superClass)) {
			// 来源对象属性值
			fieldList.addAll(Arrays.asList(superClass.getDeclaredFields()));
			// 获取父级
			superClass = superClass.getSuperclass();
		}
		fieldList.removeIf(new Predicate<Field>() {
			@Override
			public boolean test(Field field) {
				// 移除静态字段
				return Modifier.isStatic(field.getModifiers());
			}
		});
		return fieldList;
	}

	/**
	 * 获取对象中指定属性的值
	 *
	 * @param obj          对象
	 * @param propertyName 属性名称
	 * @return 指定对象属性的值
	 */
	public static Object getFieldValue(Object obj, String propertyName) {
		if (Func.isEmpty(obj)) {
			return null;
		}
		Field field = getFieldList(obj.getClass()).stream().filter(u -> {
			return u.getName().equals(propertyName);
		}).findFirst().orElse(null);
		if (Func.isNotEmpty(field)) {
			try {
				field.setAccessible(true);
				return field.get(obj);
			} catch (Exception e) {
				throw new ServiceException(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * 拼接集合中的指定属性，以英文逗号分隔
	 *
	 * @param coll     对象集合
	 * @param property 属性名称
	 * @return 拼接后的字符串
	 */
	@Deprecated
	public static String join(@Nullable Collection<?> coll, String property) {
		return join(coll, property, StringPool.COMMA);
	}

	/**
	 * 拼接集合中的指定属性
	 *
	 * @param coll     对象集合
	 * @param property 属性名称
	 * @param delim    分隔符号
	 * @return 拼接后的字符串
	 */
	@Deprecated
	public static String join(@Nullable Collection<?> coll, String property, String delim) {

		if (CollectionUtils.isEmpty(coll)) {
			return StringPool.EMPTY;
		}

		StringBuffer buffer = new StringBuffer();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {

			Object value = ReflectionKit.getFieldValue(it.next(), property);
			if (Func.isEmpty(value) || buffer.indexOf(value.toString()) > -1) {
				continue;
			}
			if (Func.isNotEmpty(value)) {
				buffer.append(value.toString());
			}
			if (it.hasNext()) {
				buffer.append(delim);
			}
		}
		String result = buffer.toString();
		if (buffer.charAt(buffer.length() - 1) == ',') {
			result = buffer.substring(0, buffer.length() - 1);
		}

		return result;
	}

	/**
	 * 将集合中对象的某个属性转为集合
	 *
	 * @param coll     集合
	 * @param function 集合中对象的属性名称
	 * @return 集合
	 */
	public static <T, U> String join (@Nullable Collection<?> coll, Function<U, T> function) {
		return join(coll, function, StringPool.COMMA);
	}

	/**
	 * 将集合中对象的某个属性转为集合
	 *
	 * @param coll     集合
	 * @param function 集合中对象的属性名称
	 * @return 集合
	 */
	public static <T, U> String join(@Nullable Collection<?> coll, Function<U, T> function, String delim) {
		List<T> arrayList = new ArrayList<>();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			T value = function.apply((U) it.next());
			if (Func.isNotEmpty(value) && !arrayList.contains(value)) {
				arrayList.add(value);
			}
		}

		return Func.join(arrayList, delim);
	}

	/**
	 * 将集合中对象的某个属性转为字符串集合
	 *
	 * @param coll     集合
	 * @param property 集合中对象的属性名称
	 * @return 字符串集合
	 * @deprecated 2020-09-03弃用，请使用 toList 方法
	 */
	@Deprecated
	public static List<String> toStrList(@Nullable Collection<?> coll, String property) {

		List<String> arrayList = new ArrayList<>();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {

			Object value = ReflectionKit.getFieldValue(it.next(), property);
			if (Func.isNotEmpty(value) && !arrayList.contains(value)) {
				arrayList.add(value.toString());
			}
		}

		return arrayList;
	}

	/**
	 * 将集合中对象的某个属性转为Long集合
	 *
	 * @param coll     集合
	 * @param property 集合中对象的属性名称
	 * @return Long集合
	 * @deprecated 2020-09-03弃用，请使用 toList 方法
	 */
	@Deprecated
	public static List<Long> toLongList(@Nullable Collection<?> coll, String property) {
		List<Long> arrayList = new ArrayList<>();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {

			Object value = ReflectionKit.getFieldValue(it.next(), property);
			if (Func.isNotEmpty(value) && !arrayList.contains(value)) {
				arrayList.add(Long.valueOf(value.toString()));
			}
		}

		return arrayList;
	}

	/**
	 * 将集合中对象的某个属性转为集合
	 *
	 * @param coll     集合
	 * @param property 集合中对象的属性名称
	 * @param clazz    返回集合类型
	 * @return 集合
	 */
	@Deprecated
	public static <T> List<T> toList(@Nullable Collection<?> coll, String property, Class<T> clazz) {
		List<T> arrayList = new ArrayList<>();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {

			Object value = ReflectionKit.getFieldValue(it.next(), property);
			if (Func.isNotEmpty(value) && !arrayList.contains(value)) {
				arrayList.add((T) value);
			}
		}

		return arrayList;
	}

	/**
	 * 将集合中对象的某个属性转为集合
	 *
	 * @param coll     集合
	 * @param function 集合中对象的属性名称
	 * @return 集合
	 */
	public static <T, U> List<T> toList(@Nullable Collection<?> coll, Function<U, T> function) {
		List<T> arrayList = new ArrayList<>();
		Iterator<?> it = coll.iterator();
		while (it.hasNext()) {
			T value = function.apply((U) it.next());
			if (Func.isNotEmpty(value) && !arrayList.contains(value)) {
				arrayList.add(value);
			}
		}

		return arrayList;
	}

	/**
	 * 对象是否为全空
	 *
	 * @param object
	 * @return
	 */
	public static boolean isAllNull(Object object) {
		if (null == object) {
			return true;
		}
		try {
			List<Field> fieldList = ReflectionKit.getFieldList(object.getClass());
			for (Field f : fieldList) {
				if (Modifier.isStatic(f.getModifiers())) {
					// 跳过静态字段
					continue;
				}
				f.setAccessible(true);
				Object obj = f.get(object);
				if (Func.isNotEmpty(obj) && Func.isNotEmpty(obj.toString().trim())) {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 根据运算符 operator 比较两个值
	 *
	 * @param value1   值1
	 * @param value2   值2
	 * @param operator 运算符索引[字典code=sku_lot_operation]
	 *                 0:=
	 *                 1:>
	 *                 2:<
	 *                 3:>=
	 *                 4:<=
	 *                 5:like
	 *                 6:不等于空
	 * @return 条件是否成立
	 */
	public static boolean match(String value1, String value2, int operator) {
		switch (operator) {
			case 0:
				return value1.compareTo(value2) == 0;
			case 1:
				return value1.compareTo(value2) == 1;
			case 2:
				return value1.compareTo(value2) == -1;
			case 3:
				return value1.compareTo(value2) >= 0;
			case 4:
				return value1.compareTo(value2) <= 0;
			case 5:
				return value1.indexOf(value2) > -1;
			case 6:
				return StringUtil.isNotBlank(value1);
			default:
				return true;
		}
	}
}
