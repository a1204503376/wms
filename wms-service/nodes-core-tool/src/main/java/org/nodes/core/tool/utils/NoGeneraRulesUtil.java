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
	private static final String DEFAULT_PROJECT_NAME = "WMS";
	private static final String SERIAL_PLACEHOLDER = "X";

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
		if (Func.isEmpty(prefix)){
			throw new ServiceException("编码生成失败,编码前缀不能为空");
		}
		if (Func.isEmpty(projectName)){
			projectName = DEFAULT_PROJECT_NAME;
		}
		if (Func.isEmpty(rule)){
			rule = DEFAULT_RULE;
		}

		String noSubject = generateNoSubject(prefix, rule);
		Long serialNo = nextSerialNo(projectName, noSubject);
		return generateCodeFormat(noSubject, serialNo, rule);
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
	 * 获取编码的主体部分（由前缀和日期组成）
	 * @return
	 */
	private String generateNoSubject(String prefix, String rule){
		if (rule.indexOf(SERIAL_PLACEHOLDER) < 0){
			throw new ServiceException("编码生成失败，编码规则不符合规定，要求必须存在序列且序号是在编码的结束部分");
		}

		//获取日期格式规则
		String rules = rule.substring(0, rule.indexOf(SERIAL_PLACEHOLDER));
		//判断当前规则生成对应的时间字符串
		String dateString = DateUtil.format(new Date(), rules);
		return String.format("%s%s", prefix, dateString);
	}

	/**
	 * 同一类型的编码序号递增
	 * @param projectName
	 * @param noSubject
	 * @return
	 */
	private long nextSerialNo(String projectName, String noSubject) {
		String redisKey = String.format("%s:NO:%s", projectName, noSubject);

		return redisUtil.incr(redisKey, 1L);
	}

	/**
	 * 生成编码，编码格式：编码前缀（固定） + 日期 + 序号
	 * @param noSubject 编码主体
	 * @param serialNo 序号
	 * @param rule 编码规则
	 * @return
	 */
	private String generateCodeFormat(String noSubject, Long serialNo, String rule) {
		// 根据规则解析序号的长度
		int legthOfSerial = rule.substring(rule.indexOf("X")).length();
		// 格式化序号
		numberFormat.setMinimumIntegerDigits(legthOfSerial);
		numberFormat.setGroupingUsed(false);
		String serialFormat = numberFormat.format(serialNo);
		// 生成编码
		String result = String.format("%s%s", noSubject, serialFormat);
		return result;
	}

}
