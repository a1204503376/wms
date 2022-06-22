package org.nodes.wms.dao.common.log.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 请求日志 分页参数
 **/
@Data
public class LogApiPageQuery implements Serializable {

	private static final long serialVersionUID = -2609846204282004507L;

	/**
	 * 请求URI
	 */
	private String requestUri;

	/**
	 * 日志标题
	 */
	private String title;

	/**
	 * 操作方式
	 */
	private String	method;

	/**
	 * 创建时间开始
	 */
	private Date createTimeBegin;

	/**
	 * 创建时间结束
	 */
	private Date createTimeEnd;
}
