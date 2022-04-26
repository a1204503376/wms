package org.nodes.wms.biz.common.utils;


import lombok.AllArgsConstructor;
import org.nodes.core.base.entity.Param;
import org.nodes.core.base.service.IParamService;
import org.nodes.core.tool.config.NoBillFinals;
import org.nodes.core.tool.utils.NoGeneraRulesUtil;
import org.nodes.wms.biz.common.config.WMSAppConfig;
import org.springframework.stereotype.Component;


/**
 * NoGeneratorUtil
 * 编码生成  返回相对应的编码
 * @author 王智勇
 * @date 2022-04-22 17:31
 */
@Component
@AllArgsConstructor
public class NoGeneratorUtil {

	private NoGeneraRulesUtil noGeneraRulesUtil;
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
	private final static String PREFIX_SALE = "SA";

	/**
	 * 发货单编码
	 */
	private final static String PREFIX_SO = "SO";

	/**
	 * 发运单编码
	 */
	private final static String PREFIX_SHIP = "SH";



	/**
	 * ASN单据编码生成
	 */
	public String createAsnBillNo() {
		return createNo(PREFIX_ASN, NoBillFinals.RULE_ASN);
	}

	/**
	 * 收货单编码生成
	 */
	public String createReceiveBillNo() {
		return createNo(PREFIX_RECEIVE,NoBillFinals.RULE_RECEIVE);
	}

	/**
	 * 销售单编码生成
	 */
	public String createSaleBillNo() {
		return createNo(PREFIX_SALE,NoBillFinals.RULE_SALE);
	}

	/**
	 * 发货单编码生成
	 */
	public String createSoBillNo() {
		return createNo(PREFIX_SO,NoBillFinals.RULE_SO);
	}

	/**
	 * 发运单编码生成
	 */
	public String createShipBillNo() {
		return createNo(PREFIX_SHIP,NoBillFinals.RULE_SHIP);
	}

	/**
	 * 通用编码生成
	 * @param prefix 前缀
	 * @param paramKeyOfRule 编码规则参数key
	 * @return 编码
	 */
	private String createNo(String prefix, String paramKeyOfRule){
		Param param = paramService.selectByKey(paramKeyOfRule);
		String role = param.getParamValue();
		return noGeneraRulesUtil.generateCode(wmsAppConfig.getProjectName(),prefix,role);
	}



}
