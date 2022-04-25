package org.nodes.wms.biz.common.utils;


import lombok.AllArgsConstructor;
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.service.IParamService;
import org.nodes.core.tool.utils.NOGeneraRulesUtil;
import org.nodes.wms.biz.common.config.WMSAppConfig;
import org.springblade.core.tool.utils.DateUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.core.tool.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * DocumentCodeUtil
 * 编码生成 跟数据库对接判断是否存在类
 * asnDocumentCode 返回相对应的编码
 *
 * @author 王智勇
 * @date 2022-04-22 17:31
 */
@Component
@AllArgsConstructor
public class NoGeneratorUtil {

	private NOGeneraRulesUtil NOGeneraRulesUtil;
	private IParamService paramService;
	private WMSAppConfig wmsAppConfig;

	/**
	 * ASN单编码
	 */
	private final static String PREFIX_ASN = "A";

	/**
	 * 收货单编码
	 */
	private final static String PREFIX_RECEIVE = "R";

	/**
	 * 销售单编码
	 */
	private final static String CODE_SALE = "SA";

	/**
	 * 发货单编码
	 */
	private final static String CODE_SO = "SO";

	/**
	 * 发运单编码
	 */
	private final static String CODE_SHIP = "SH";

	/**
	 * ASN单据编码生成规则
	 */
	private final static String RULE_ASN = "NoRuleOfASN";

	/**
	 * 收货单编码生成规则
	 */
	private final static String RULE_RECEIVE = "NoRuleOfReceive";

	/**
	 * 销售单编码生成规则
	 */
	private final static String RULE_SALE = "NoRuleOfSale";

	/**
	 * 发货单编码生成规则
	 */
	private final static String RULE_SO = "NoRuleOfSO";

	/**
	 * 参数key：发运单编码生成规则
	 */
	private final static String RULE_SHIP = "NoRuleOfShip";


	/**
	 * ASN单据编码生成
	 */
	public String createAsnBillNo() {
		String no = createNo(PREFIX_ASN, RULE_ASN);
		return no;
	}

	/**
	 * 收货单编码生成
	 */
	public String createReceiveNo() {
		String code="A202204240000398";
		return documentCode(RULE_RECEIVE, PREFIX_RECEIVE,code);
	}

	/**
	 * 销售单编码生成
	 */
	public String saleDocumentCode() {
		String code="wms33:NO:A202204240000398";
		return documentCode(RULE_SALE, CODE_SALE,code);
	}

	/**
	 * 发货单编码生成
	 */
	public String soDocumentCode() {
		String code="wms33:NO:A202204240000398";
		return documentCode(RULE_SO, CODE_SO,code);
	}

	/**
	 * 发运单编码生成
	 */
	public String shipDocumentCode() {
		String code="wms33:NO:A202204240000398";
		return documentCode(RULE_SHIP, CODE_SHIP,code);
	}

	private String createNo(String prefix, String paramKeyOfRule){

		return "";
	}


	/**
	 * 通用编码生成
	 */
	private String documentCode(String key, String singleCode,String code) {

		//2、获取当前单据规则
//		Param param = paramService.selectByKey(key);
//		String role = param.getParamValue();
//
//
//		//生成在最新的编码格式
//		generateCode = NOGeneraRulesUtil.generateCode(wmsAppConfig.getProjectName(), singleCode, role);
//		//3、查询当前生成的单据编码是否Redis跟数据库同步 如果同步直接返回
//		if(StringUtil.equals(generateCode,code)){
//			//2、将单据编码返回给用户
//			return generateCode;
//		}
//		//如果不同步 将Redis里面的替换成MySQL的编号
//		//获取日期格式规则
//		String rules = role.substring(0,role.indexOf("X"));
//		//判断当前规则生成对应的时间字符串
//		String format = DateUtil.format(new Date(), rules);
//
//		int lengthyCode = generateCode.lastIndexOf(format) + format.length();
//		int codeSize = Integer.parseInt(code.substring(lengthyCode));
//
//		if(StringUtil.equals(code.substring(0,lengthyCode),generateCode.substring(0,lengthyCode)))
//		{
//			if(Integer.parseInt(generateCode.substring(generateCode.lastIndexOf(format)+format.length()))<= codeSize) {
//			     NOGeneraRulesUtil.setCode(wmsAppConfig.getProjectName(), singleCode, role, Integer.toString(codeSize));
//				return NOGeneraRulesUtil.generateCode(wmsAppConfig.getProjectName(),singleCode,role);
//
//
//		}
//		}
//		//2、将单据编码返回给用户
//		return generateCode;

		return "";
	}


}
