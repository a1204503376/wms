package org.nodes.core.tool.utils;


import lombok.AllArgsConstructor;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.ObjectUtil;
import org.springframework.stereotype.Component;
import java.text.NumberFormat;
import java.util.Date;

/**
 *  CodeGeneraRulesUtil
 *  编码生成工具类
 *  generateCode 返回相对应的编码
 *  @author 王智勇
 *  @date 2022-04-22 9:31
 */
@Component
@AllArgsConstructor
public class NOGeneraRulesUtil {
	private final NumberFormat numberFormat = NumberFormat.getNumberInstance();
    private static final String DEFAULT_RULE ="YYYYMMXXXXX";

    private RedisUtil redisUtil;

	/**
	 * 生成编码
	 * @param projectName 项目名称
	 * @param prefix 编码前缀
	 * @param rule 编码规则
	 * @return 编码
	 */
	public String generateCode(String projectName, String prefix, String rule) {
		String redisKey = createRedisKey(projectName, prefix, rule);
		long serialNo = nextSerialNo(redisKey);
		String no = generateCode(redisKey, serialNo, rule);
		return no;
	}

	/**
	 * 生成新的编码，跳过已经存在
	 * @param projectName 项目名称
	 * @param prefix 编码前缀
	 * @param rule 编码规则
	 * @param existNo 已存在的编码
	 * @return 编码
	 */
	public String generateCodeBySkipExistNo(String projectName, String prefix, String rule, String existNo) {
//		String incrCode = incrCode(projectName, singleCode, rule);
//		redisUtil.set(incrCode, delta);
//		return incrCode;
		throw new ServiceException("暂不支持");
	}

	private String createRedisKey(String projectName, String prefix, String rule){
		return "";
	}

	private long nextSerialNo(String key){
		return 0L;
	}

	private String generateCode(String key, long serialNo, String rule){
		return "";
	}


	private String incrCode(String projectName, String singleCode, String rule){
		String dateString;
		//  入参 projectName---项目名称  singleCode---编码前缀     rule---规则
		if(!ObjectUtil.isNotEmpty(projectName))
		{
			projectName="WMS";
		}
		//入参 rule---规则 如果为空 那么就默认他是
		if(!ObjectUtil.isNotEmpty(rule))
		{
			rule= DEFAULT_RULE;
		}

		//设置补0规则去掉,分割
		numberFormat.setGroupingUsed(false);

		//获取日期格式规则
		String rules = rule.substring(0, rule.indexOf("X"));

		//获取传过来的规则后面有几个X
		int length = rule.substring(rule.indexOf("X")).length() - 1;

		//把要补全0的个数传输进框架自带的补0方法里
		numberFormat.setMinimumIntegerDigits(length);

		//生成要补全0的字符串
		String zeroFill = numberFormat.format(0L);

		//判断当前规则生成对应的时间字符串
		dateString = DateUtil.format(new Date(), rules);

		//生成字符串模板Key  例如WMS:NO:A20220422
       return projectName + ":NO:" + singleCode + dateString + zeroFill;
	}
}
