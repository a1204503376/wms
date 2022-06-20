package org.nodes.wms.dao.common.log.dto.input;

import lombok.Data;

import java.util.Date;

/**
 * 异常日志 分页参数
 **/
@Data
public class LogErrorPageQuery {
	/**
	 * 操作方式
	 */
	private String	method;
	/**
	 * 请求URI
	 */
	private String requestUri;
	/**
	 * 创建时间开始
	 */
	private Date createTimeBegin;
	/**
	 * 创建时间结束
	 */
	private Date createTimeEnd;
}
