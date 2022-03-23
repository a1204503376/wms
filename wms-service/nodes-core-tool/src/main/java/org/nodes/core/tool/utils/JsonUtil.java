package org.nodes.core.tool.utils;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringPool;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;

/**
 * @author pengwei
 * @description Json 工具类
 * @since 2020-07-16
 */
public class JsonUtil {

	@Data
	public static class SerialInfo {

		private String date;

		private String skuCode;

		private Integer serialNo;
	}

	public static Map<String, Integer> SerialNoMap = new HashMap<>();
	// key=客户编码
	public static Map<String, List<SerialInfo>> SerialInfoMap = new HashMap<>();
	// key=skuCode, value={当天日期, 生成日期}
	public static Map<String, Map<String, String>> dateMap = new HashMap<>();

	public static Random random = new Random();

	public static int Index = 1;

	public static String bindData(@NotNull String sourceJson, @NotNull Object obj, String cCode, String skuCode) {
		return bindData(sourceJson, obj, cCode, skuCode, null);
	}

	/**
	 * 根据 json 的配置，解析并绑定数据，返回一个新的字符串
	 *
	 * @param sourceJson json配置
	 * @param obj        数据对象, 可以为单个对象，也可以为多个对象的集合
	 * @return 绑定后的字符串
	 */
	public static String bindData(@NotNull String sourceJson, @NotNull Object obj, String cCode, String skuCode,
								  Integer index) {
		if (Func.isEmpty(sourceJson)) {
			return StringPool.EMPTY;
		}
		// 解析 json
		List<Object> barcodeItems = org.springblade.core.tool.jackson.JsonUtil.readList(sourceJson, Object.class);
		// 解析dataSource
		Map<Object, Field[]> dataSource = getMap(obj);
		// 替换数据内容
		if (Func.isEmpty(barcodeItems)) {
			return StringPool.EMPTY;
		}
		StringBuffer sb = new StringBuffer();
		for (Object item : barcodeItems) {
			if (item instanceof String) {
				// 如果是字符串，直接拼接
				sb.append(item.toString());
			} else {
				// 除了字符串就是对象
				if (Func.isEmpty(dataSource)) {
					continue;
				}
				// 从对象中取对应的属性值，并拼接
				LinkedHashMap<String, Object> props = (LinkedHashMap<String, Object>) item;
				Object propObj = props.get("prop");
				if (propObj instanceof String) {
					boolean isFind = false;
					for (Object objKey : dataSource.keySet()) {
						String prop = Func.toStr(props.get("prop"));
						if (prop.indexOf(".") > -1) {
							String[] array = Func.split(prop, ".");
							if (Func.isEmpty(array) || array.length != 2) {
								continue;
							}
							if (!objKey.getClass().getSimpleName().equals(array[0])) {
								continue;
							}
							prop = array[1];
						}
						List<Field> fields = NodesUtil.getFieldList(objKey.getClass());
						if (Func.isEmpty(fields)) {
							continue;
						}
						for (Field field : fields) {
							if (Func.isEmpty(prop)) {
								continue;
							}
							if (!field.getName().equals(prop)) {
								continue;
							}
							try {
								field.setAccessible(true);
								Object result = field.get(objKey);
								if (result instanceof BigDecimal) {
									result = BigDecimalUtil.globeScale((BigDecimal) result);
								}
								if (Func.isNotEmpty(result)) {
									String value = result.toString();
									if (props.containsKey("format")) {
										if (props.getOrDefault("type", StringPool.EMPTY).equals("string")) {
											String currentFormat = props.getOrDefault("currentFormat", StringPool.EMPTY).toString();
											String format = props.getOrDefault("format", "yyyyMMdd").toString();
											Date date = DateUtil.parse(value, currentFormat);

											value = DateUtil.format(date, format);
										}
									}
									if (props.containsKey("startIndex")) {
										int startIndex = Func.toInt(props.get("startIndex"));
										int endIndex = Func.toInt(props.get("endIndex"));
										if (endIndex < 0) {
											value = value.substring(startIndex);
										} else {
											value = value.substring(startIndex, endIndex);
										}
									}
									sb.append(value);
									isFind = true;
									break;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (isFind) {
							break;
						} else if (prop.equals("index")) {
							Object propLength = props.get("length");
							Integer len = Func.isEmpty(propLength) ? 0 : Func.toInt(propLength);
							NumberFormat numberFormat = NumberFormat.getNumberInstance();
							numberFormat.setMinimumIntegerDigits(len);
							numberFormat.setGroupingUsed(false);
							if (props.containsKey("type") && Func.toStr(props.get("type")).equals("sku")) {
								// 清旧数据
								for (String key : SerialInfoMap.keySet()) {
									List<SerialInfo> serialInfoList = SerialInfoMap.get(key);
									serialInfoList.removeIf(new Predicate<SerialInfo>() {
										@Override
										public boolean test(SerialInfo serialInfo) {
											return !serialInfo.date.equals(DateUtil.formatDate(DateUtil.now()));
										}
									});
								}
								SerialInfo serialInfo = null;
								if (SerialInfoMap.containsKey(cCode)) {
									List<SerialInfo> serialInfoList = SerialInfoMap.get(cCode);
									serialInfo = serialInfoList.stream().filter(u -> {
										return u.skuCode.equals(skuCode)
											&& u.date.equals(DateUtil.formatDate(DateUtil.now()));
									}).findFirst().orElse(null);
									if (Func.isEmpty(serialInfo)) {
										serialInfo = new SerialInfo();
										serialInfo.date = DateUtil.formatDate(DateUtil.now());
										serialInfo.skuCode = skuCode;
										serialInfo.serialNo = serialInfoList.size() + 1;
										serialInfoList.add(serialInfo);
									}
									SerialInfoMap.replace(cCode, serialInfoList);
								} else {
									serialInfo = new SerialInfo();
									serialInfo.date = DateUtil.formatDate(DateUtil.now());
									serialInfo.skuCode = skuCode;
									serialInfo.serialNo = 1;
									List<SerialInfo> serialInfoList = new ArrayList<>();
									serialInfoList.add(serialInfo);
									SerialInfoMap.put(cCode, serialInfoList);
								}
								if (Func.toBoolean(props.getOrDefault("fixed", false))) {
									if (len != 0) {
										sb.append(numberFormat.format(serialInfo.serialNo));
									} else {
										sb.append(serialInfo.serialNo);
									}
								} else {
									if (SerialNoMap.containsKey(cCode + skuCode)) {
										Integer serialNo = SerialNoMap.get(cCode + skuCode);
										serialNo++;
										SerialNoMap.replace(cCode + skuCode, serialNo);
									} else {
										SerialNoMap.put(cCode + skuCode, 1);
									}
									if (len != 0) {
										sb.append(numberFormat.format(SerialNoMap.get(cCode + skuCode)));
									} else {
										sb.append(SerialNoMap.get(cCode + skuCode));
									}
								}
							} else {
								if (len != 0) {
									sb.append(numberFormat.format(index));
								} else {
									sb.append(index);
								}
							}
							break;
						} else if (prop.equals("function")) {
							if (Func.toStr(props.get("name")).equals("now")) {
								LocalDateTime now = LocalDateTime.now();
								int diff = 0;
								if (props.containsKey("max") && props.containsKey("min")) {
									diff = 0 - RandomUtils.nextInt(
										Func.toInt(props.get("min")),
										Func.toInt(props.get("max")));
								}
								now = now.plusDays(diff);
								String date = DateUtil.format(now, Func.toStr(props.get("format")));
								String finalDate = date;
								if (diff != 0) {
									List<String> dateList = new ArrayList<>();
									for (Map<String, String> values : dateMap.values()) {
										dateList.addAll(values.values());
									}
									while (dateList.contains(date)) {
										diff = 0 - RandomUtils.nextInt(
											Func.toInt(props.get("min")),
											Func.toInt(props.get("max")));
										date = DateUtil.format(now.plusDays(diff), Func.toStr(props.get("format")));
									}
								}

								if (Func.isNotEmpty(skuCode) && props.containsKey("type") &&
									Func.toStr(props.get("type")).equals("sku")) {
									String currentDate = DateUtil.formatDate(DateUtil.now());
									if (dateMap.containsKey(skuCode)) {
										Map<String, String> map = dateMap.get(skuCode);
										if (!map.containsKey(currentDate)) {
											map.clear();

											dateMap.replace(skuCode, new HashMap() {{
												put(currentDate, finalDate);
											}});
										} else {
											date = map.get(currentDate);
										}
									} else {
										dateMap.put(skuCode, new HashMap() {{
											put(currentDate, finalDate);
										}});
									}
								}
								sb.append(date);
							} else if (Func.toStr(props.get("name")).equals("week")) {
								Calendar calendar = Calendar.getInstance();
								calendar.setFirstDayOfWeek(Calendar.MONDAY);//设置星期一为一周开始的第一天
								calendar.setMinimalDaysInFirstWeek(4);//可以不用设置
								calendar.setTimeInMillis(System.currentTimeMillis());//获得当前的时间戳
								int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
								sb.append(weekOfYear);
							}
							break;
						} else if (prop.equals("ascii")) {
							sb.append(((char) Func.toInt(props.get("text"))) + "");
							break;
						}
					}
				} else {
					sb.append(bindData(org.springblade.core.tool.jackson.JsonUtil.toJson(propObj),
						obj, cCode, skuCode));
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 转换key:value 返回Map
	 *
	 * @param sourceJson
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> bindDataMap(@NotNull String sourceJson, @NotNull Object obj) {
		Map<String, Object> map = new HashMap<>();

		// json格式 [{"label":"企业编码","prop":"enterpriseCode"},"abcd"]
		List<Object> items = org.springblade.core.tool.jackson.JsonUtil.readList(sourceJson, Object.class);

		// 解析dataSource
		Map<Object, Field[]> dataSource = getMap(obj);
		if (Func.isNotEmpty(items)) {

			for (Object item : items) {
				// item格式 {"label":"企业编码","prop":"enterpriseCode"}
				if (item instanceof String) {
					continue;
				}
				// prop label=企业编码,prop=enterpriseCode
				Map<String, Object> props = (Map<String, Object>) item;
				// 标签中的Prop
				for (String key : props.keySet()) {
					if (!key.equals("prop")) {
						continue;
					}
					boolean isFind = false;
					for (Object objKey : dataSource.keySet()) {
						String prop = Func.toStr(props.get("prop"));
						if (prop.indexOf(".") > -1) {
							String[] array = Func.split(prop, ".");
							if (Func.isEmpty(array) || array.length != 2) {
								continue;
							}
							if (objKey.getClass().getSimpleName().equals(array[0])) {
								prop = array[1];
							}
						}
						List<Field> fields = NodesUtil.getFieldList(objKey.getClass());
						if (Func.isEmpty(fields)) {
							continue;
						}
						for (Field field : fields) {
							if (!field.getName().equals(prop)) {
								continue;
							}
							try {
								field.setAccessible(true);
								Object value = field.get(objKey);
								Annotation annotation = field.getAnnotation(JsonSerialize.class);
								if (Func.isNotEmpty(annotation)) {
									value = BigDecimalUtil.globeScale((BigDecimal) value);
								}
								map.put(field.getName(), value);
								if (Func.isNotEmpty(value)) {
									isFind = true;
								}
								break;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						if (isFind) {
							break;
						}
					}
				}
			}
		}
		return map;
	}

	/**
	 * 转换值 返回字符串
	 *
	 * @param json
	 * @param dataSource
	 * @return
	 */
	public static String bindDataString(String json, List<Object> dataSource) throws Exception {
		String result = "";
		if (Func.isEmpty(json)) throw new ApiException("标签字段内容不能为空");
		if (Func.isEmpty(dataSource)) throw new ApiException("参数不能为空");
		// json格式 [{"label":"企业编码","prop":"enterpriseCode"},"abcd"]
		List<Object> items = org.springblade.core.tool.jackson.JsonUtil
			.readList(json, Object.class);
		if (Func.isNotEmpty(items)) {
			for (Object item : items) {
				//item格式 {"label":"企业编码","prop":"enterpriseCode"}
				if (!(item instanceof String)) {
					//prop label=企业编码,prop=enterpriseCode
					Map<String, Object> propObj = (Map<String, Object>) item;
					//标签中的Prop
					p:
					for (String key : propObj.keySet()) {
						if (key.equals("prop")) {

							for (Object data : dataSource) {
								// 获取 原类 所有get/set 方法
								Method[] methods = data.getClass().getMethods();

								for (Method method : methods) {
									//propObj.get(key) = enterpriseCode
									char[] sChars = propObj.get(key).toString().toCharArray();
									sChars[0] -= 32;
									//标签内容中的porp名拼接get方法 getEnterpriseCode
									String labelFieldMethod = "get" + String.valueOf(sChars);
									if (method.getName().equals(labelFieldMethod)) {
										// 对象中的值 企业编码
										Object sFieldValue = method.invoke(data);
										result += sFieldValue + ":";
										//如果有则继续下一个prop
										continue p;
									}
								}
							}
						}
					}
				}
			}
		}
		result = result.substring(0, result.length() - 1);
		return result;
	}

	/**
	 * 获取指定对象的所有方法
	 *
	 * @param obj 对象、集合、数组
	 * @return
	 */
	private static Map<Object, Field[]> getMap(Object obj) {
		// 解析dataSource
		Map<Object, Field[]> dataSource = new HashMap<>();
		if (obj.getClass().isArray()) {
			Array array = (Array) obj;
			for (int i = 0; i < Array.getLength(obj); i++) {
				Object item = Array.get(array, i);
				if (Func.isNotEmpty(item)) {
					dataSource.put(item, getFieldArray(item));
				}
			}
		} else if (obj instanceof Collection) {
			Collection collection = (Collection) obj;
			for (Object item : collection) {
				if (Func.isNotEmpty(item)) {
					dataSource.put(item, getFieldArray(item));
				}
			}
		} else {
			if (Func.isNotEmpty(obj)) {
				dataSource.put(obj, getFieldArray(obj.getClass()));
			}
		}
		return dataSource;
	}

	private static Field[] getFieldArray(Object obj) {
		List<Field> fieldList = NodesUtil.getFieldList(obj.getClass());
		Field[] fieldArray = new Field[fieldList.size()];
		return fieldList.toArray(fieldArray);
	}
}
