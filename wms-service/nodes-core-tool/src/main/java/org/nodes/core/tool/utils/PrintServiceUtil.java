package org.nodes.core.tool.utils;

import lombok.extern.slf4j.Slf4j;
import org.nodes.core.tool.config.PrintServiceConfiguration;
import org.springblade.core.http.util.HttpUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.tool.utils.Func;

import java.util.HashMap;
import java.util.Map;

/**
 * @author pengwei
 * @description Nodes打印服务 工具类
 * @since 2020-07-23
 */
@Slf4j
public class PrintServiceUtil {

	/**
	 * 打印服务URL
	 */
	public static String PrintURL = null;

	/**
	 * Http 请求头
	 */
	public static Map<String, String> HttpHeaders = new HashMap<>();

	static {
		// 设置 http 请求头
		HttpHeaders.put("accept", "*/*");
		HttpHeaders.put("connection", "Keep-Alive");
		HttpHeaders.put("Content-Type", "application/x-www-form-urlencoded");
	}

	/**
	 * 调用接口，打印指定标签
	 *
	 * @param labelId       标签名称
	 * @param parameterJson 参数Json字符串
	 */
	public static void print(String labelId, String parameterJson) {
		// 获取打印服务地址配置
		PrintURL = PrintServiceConfiguration.SERVICE_URL;
		if (Func.isEmpty(PrintURL)) {
			throw new ServiceException("系统设置未配置打印服务接口地址！");
		}
		// 封装请求信息
		Map<String, Object> httpParams = new HashMap<>();
		httpParams.put(PrintServiceConfiguration.KEY_LABEL_ID, labelId);
		httpParams.put(PrintServiceConfiguration.KEY_GROUP_ID, PrintServiceConfiguration.VALUE_GROUP_ID);
//		httpParams.put(
//			PrintServiceConfiguration.KEY_USER_UNIQUE_CODE,
//			PrintServiceConfiguration.VALUE_USER_UNIQUE_CODE);
		httpParams.put(PrintServiceConfiguration.KEY_PARAMETER_JSON, parameterJson);

		// 调用接口，请求打印
		String response = HttpUtil.post(PrintURL, HttpHeaders, httpParams);
		if (Func.isEmpty(response)) {
			log.error("[PrintServe]" + response);
			throw new ServiceException("打印异常：连接打印服务失败！");
		}
	}
}
