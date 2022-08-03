package org.nodes.wms.biz.task.util;

import org.nodes.wms.dao.application.dto.scheduling.SchedulingGlobalResponse;

/**
 * 请求调度系统API 接口
 **/
public interface SendToScheduleUtil {

	/**
	 * post请求
	 *
	 * @param url 请求地址
	 * @param request 请求参数
	 *
	 * @return 请求调度系统全局返回对象
	 */
	SchedulingGlobalResponse sendPost(String url, Object request);
}
