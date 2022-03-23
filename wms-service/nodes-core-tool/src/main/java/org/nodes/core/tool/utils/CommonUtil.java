/*
 *      Copyright (c) 2018-2028, Nodes All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *
 *  Redistributions of source code must retain the above copyright notice,
 *  this list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *  Neither the name of the dreamlu.net developer nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  Author: Nodes
 */
package org.nodes.core.tool.utils;

import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springblade.core.tool.utils.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用工具类
 *
 * @author Nodes
 */
public class CommonUtil {
	/**
	 * 获取使用1900日期系统的Excel中的对应时间
	 *
	 * @param strDitNumber 时间单元格的值,如43900
	 * @return Date
	 */
	public static Date getDateTime(String strDitNumber) {
		Date d = null;

		if (ObjectUtil.isEmpty(strDitNumber) || !isNonNegativeNumber(strDitNumber)) {
			return d;
		}

		BigDecimal bd = new BigDecimal(strDitNumber);
		int days = bd.intValue();    //天数
		int mills = (int) Math.round(bd.subtract(new BigDecimal(days)).doubleValue() * 24 * 3600);

		//获取时间
		Calendar c = Calendar.getInstance();
		c.set(1900, 0, 1);    //采用Excel 1900日期系统
		c.add(Calendar.DATE, days - 2);
		int hour = mills / 3600;
		int minute = (mills - hour * 3600) / 60;
		int second = mills - hour * 3600 - minute * 60;
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, second);

		d = c.getTime();    //Date

//		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//		Timestamp t = Timestamp.valueOf(dateFormat.format(c.getTime()));	//Timestamp

		return d;
	}

	/**
	 * 判断字符串是否为非负数
	 *
	 * @param numStr 字符串
	 * @return 判断结果
	 */
	public static Boolean isNonNegativeNumber(String numStr) {
		Pattern pattern = Pattern.compile("[0-9]+\\.?[0-9]*");
		Matcher isNum = pattern.matcher(numStr);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}


	/**
	 * 对象是否为全空
	 * @param object
	 * @return
	 */
	public static boolean isAllNull(Object object) {
		if (null == object) {
			return true;
		}
		try {
			for (Field f : object.getClass().getDeclaredFields()) {
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
	 * 条件搜索日期时间去除单引号
	 * @param str
	 * @return
	 */
	public static String removeTimeQuotation(String str){
		if (StringUtil.isNotBlank(str)){
			return str.replaceAll("'","");
		}
		return "";
	}

}

