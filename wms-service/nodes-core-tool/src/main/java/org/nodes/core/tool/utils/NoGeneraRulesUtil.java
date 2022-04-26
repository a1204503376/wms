package org.nodes.core.tool.utils;


import lombok.AllArgsConstructor;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.util.Date;

/**
 * NoGeneraRulesUtil
 * 编码生成工具类
 * generateCode 返回相对应的编码
 *
 * @author 王智勇
 * @date 2022-04-22 9:31
 */
@Component
@AllArgsConstructor
public class NoGeneraRulesUtil {
	private final NumberFormat numberFormat = NumberFormat.getNumberInstance();
	private static final String DEFAULT_RULE = "YYYYMMXXXXX";

	private RedisUtil redisUtil;

	/**
	 * 生成编码
	 *
	 * @param projectName 项目名称
	 * @param prefix      编码前缀
	 * @param rule        编码规则
	 * @return 编码
	 */
	public String generateCode(String projectName, String prefix, String rule) {
		//1生成 key WMS:NO:A20220422
		String redisKey = createRedisKey(projectName, prefix, rule);
		//2获取 编号 1
		Long serialNo = nextSerialNo(redisKey);
		//根据规则补0 返回编码
		return generateCodeFormat(redisKey, serialNo, rule);
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
	public String generateCodeBySkipExistNo(String projectName, String prefix, String rule, String existNo) {
		throw new ServiceException("暂不支持");
	}

	/**
	 * 生成RedisKey
	 *
	 * @param projectName 项目名
	 * @param prefix      前缀
	 * @param rule        规则
	 * @return RedisKey
	 */
	private String createRedisKey(String projectName, String prefix, String rule) {
		String dateString;
		//判断项目名称是否存在，不存在则使用默认的项目名
		if (!Func.isNotEmpty(projectName)) {
			projectName = "WMS";
		}
		//判断规则是否存在，不存在则使用默认的规则
		if (!Func.isNotEmpty(rule)) {
			rule = DEFAULT_RULE;
		}
		//获取日期格式规则
		String rules = rule.substring(0, rule.indexOf("X"));
		//判断当前规则生成对应的时间字符串
		dateString = DateUtil.format(new Date(), rules);
		//生成字符串模板Key  例如WMS:NO:A20220422
		return projectName + ":NO:" + prefix + dateString;

	}

	/**
	 * 下一个Redis里面存储的值,不存在则创建，默认为一。存在则加一
	 * @param key Redis的键
	 * @return Redis存储的数据
	 */
	private Long nextSerialNo(String key) {
		try {
			boolean exist = redisUtil.hasKey(key);
			if (!exist) {
				redisUtil.set(key, "1");
			} else {
				redisUtil.incr(key, 1L);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Long.parseLong(redisUtil.get(key).toString());
	}

	private String generateCodeFormat(String key, Long serialNo, String rule) {
		int ruleLenghts = rule.substring(rule.indexOf("X")).length();
		if (ruleLenghts == serialNo.toString().length()) {
			return key + serialNo;
		} else if (ruleLenghts < serialNo.toString().length()) {
			throw new ServiceException("超出格式限制");
		}
		//设置补0规则去掉,分割
		numberFormat.setGroupingUsed(false);
		//获取传过来的规则后面有几个X
		int length = ruleLenghts - serialNo.toString().length();
		//把要补全0的个数传输进框架自带的补0方法里
		numberFormat.setMinimumIntegerDigits(length);
		//生成要补全0的字符串
		String zeroFill = numberFormat.format(0L);
		return key + zeroFill + serialNo;
	}


}
