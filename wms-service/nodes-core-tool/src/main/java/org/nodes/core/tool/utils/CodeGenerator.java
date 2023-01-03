package org.nodes.core.tool.utils;


import lombok.AllArgsConstructor;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * NoGeneraRulesUtil
 * 通用编码生成工具类
 * generateCode 返回相对应的编码
 *
 * @author 王智勇
 * @date 2022-04-22 9:31
 */
@Component
@AllArgsConstructor
public class CodeGenerator {
	private final NumberFormat numberFormat = NumberFormat.getNumberInstance();
	private static final String DEFAULT_RULE = "yyyyMMXXXXX";
	private static final String DEFAULT_PROJECT_NAME = "WMS";
	private static final String SERIAL_PLACEHOLDER = "X";
	private final RedisUtil redisUtil;

	/**
	 * 生成编码,编码有前缀+日期+序号组成。其中日期和序号支持规则定义,具体是：Y（年）M（月） X（序号个数）
	 *
	 * @param projectName 项目名称，非必填
	 * @param type        编码类型，必填，存在redis中分类，不参与编码生成，要求编码类型+编码前缀要全局唯一,否则会导致串号
	 * @param prefix      编码前缀，必填
	 * @param rule        编码规则，非必填，默认为：YYYYMMXXXXX
	 * @param year        编码时间：年
	 * @param month       编码时间：月
	 * @return 编码
	 */
	public String generateCode(String projectName, String type, String prefix, String rule, String year, String month) {
		AssertUtil.notEmpty(type, "编码生成失败，编码类型必填，且编码类型+编码前缀要求全局唯一");
		AssertUtil.notNull(prefix, "编码生成失败，编码前缀必填，且编码类型+编码前缀要求全局唯一");
		if (!rule.endsWith(SERIAL_PLACEHOLDER)) {
			throw new ServiceException("编码生成失败，编码规则中不存在序号X");
		}

		if (Func.isEmpty(projectName)) {
			projectName = DEFAULT_PROJECT_NAME;
		}
		if (Func.isEmpty(rule)) {
			rule = DEFAULT_RULE;
		}
		// 生成redis中的key=项目名称：编码类型：编码前缀：时间部分
		String time;
		if (year == null && month == null) {
			time = parseTime(rule);
		} else {
			time = parseTime(rule, year, month);
		}
		String redisKey = getRedisKey(projectName, type, prefix, time);
		// 根据key获取下一个序号
		Long serialNo = nextSerialNo(redisKey);
		// 组合编码
		return generateCodeFormat(rule, prefix, time, serialNo);
	}

	private String parseTime(String rule, String year, String month) {
		String timeFormat = rule.substring(0, rule.indexOf(SERIAL_PLACEHOLDER));
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
		Date date;
		try {
			date = simpleDateFormat.parse(year + month);
		} catch (ParseException e) {
			throw new ServiceException("字符串转换时间异常, 格式错误！");
		}
		return DateUtil.format(date, timeFormat);
	}

	private String generateCodeFormat(String rule, String prefix, String time, Long serialNo) {
		// 根据规则解析序号的长度
		int serialLength = rule.substring(rule.indexOf("X")).length();
		// 格式化序号
		numberFormat.setMinimumIntegerDigits(serialLength);
		numberFormat.setGroupingUsed(false);
		String serialNoString = numberFormat.format(serialNo);
		// 生成编码
		return String.format("%s%s%s", prefix, time, serialNoString);
	}

	private Long nextSerialNo(String redisKey) {
		return redisUtil.incr(redisKey, 1L);
	}

	private String getRedisKey(String projectName, String type, String prefix, String timeOfRedisKey) {
		return String.format("%s:%s:%s:%s", projectName, type, prefix, timeOfRedisKey);
	}

	private String parseTime(String rule) {
		String timeFormat = rule.substring(0, rule.indexOf(SERIAL_PLACEHOLDER));
		return DateUtil.format(new Date(), timeFormat);
	}

	/**
	 * 生成新的编码，跳过已经存在
	 *
	 * @param projectName 项目名称
	 * @param prefix      编码前缀
	 * @param rule        编码规则
	 * @param existNo     已存在的编码
	 * @return 编码
	 */
	public String generateCodeBySkipExist(String projectName, String prefix, String rule, String existNo) {
		throw new ServiceException("暂不支持");
	}

}
