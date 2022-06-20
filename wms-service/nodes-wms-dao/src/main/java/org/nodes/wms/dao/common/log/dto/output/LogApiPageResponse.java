package org.nodes.wms.dao.common.log.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class LogApiPageResponse implements Serializable {

	private static final long serialVersionUID = 5629993679472575066L;

	/**
	 * 日志标题
	 */
	@ColumnWidth(15)
	@ExcelProperty({"请求日志", "日志标题"})
	private String title;

	/**
	 * 请求URI
	 */
	@ColumnWidth(15)
	@ExcelProperty({"请求日志", "请求uri"})
	private String requestUri;

	/**
	 * 操作方式
	 */
	@ColumnWidth(15)
	@ExcelProperty({"请求日志", "操作方式"})
	private String	method;

	/**
	 *方法类
	 */
	@ExcelProperty({"请求日志", "方法类"})
	private String	methodClass;

	/**
	 * 方法名
	 */
	@ExcelProperty({"请求日志", "方法名"})
	private String	methodName;

	/**
	 * 服务器名
	 */
	@ColumnWidth(15)
	@ExcelProperty({"请求日志", "服务器名"})
	private String serverHost;

	/**
	 * 服务器IP地址
	 */
	@ColumnWidth(15)
	@ExcelProperty({"请求日志", "服务器IP"})
	private String	serverIp;


	/**
	 * 操作提交的数据
	 */
	@ExcelProperty({"请求日志", "提交数据"})
	private String params;

	/**
	 * 服务器名
	 */
	@ColumnWidth(15)
	@ExcelProperty({"请求日志", "服务器名"})
	private String data;

	/**
	 * 创建者
	 */
	@ExcelProperty({"请求日志", "创建者"})
	private String createBy;

	/**
	 * 创建时间
	 */
	@ExcelProperty({"请求日志", "创建时间"})
	private Date createTime;
}
