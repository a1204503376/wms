package org.nodes.wms.dao.common.log.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.util.Date;
@Data
public class LogErrorPageResponse {
	/**
	 * 服务器名
	 */
	@ColumnWidth(15)
	@ExcelProperty({"异常日志", "服务器名"})
	private String serverHost;
	/**
	 * 服务器IP地址
	 */
	@ColumnWidth(15)
	@ExcelProperty({"异常日志", "服务器IP"})
	private String	serverIp;
	/**
	 * 系统环境
	 */
	@ColumnWidth(15)
	@ExcelProperty({"异常日志", "系统环境"})
	private String env;
	/**
	 * 操作方式
	 */
	@ColumnWidth(15)
	@ExcelProperty({"异常日志", "操作方式"})
	private String	method;
	/**
	 * 请求URI
	 */
	@ColumnWidth(15)
	@ExcelProperty({"异常日志", "请求uri"})
	private String requestUri;
	/**
	 * 用户代理
	 */
	@ColumnWidth(15)
	@ExcelProperty({"异常日志", "用户代理"})
	private String	userAgent;
	/**
	 * 堆栈
	 */
	@ColumnWidth(15)
	@ExcelProperty({"异常日志", "堆栈"})
	private String stackTrace;
	/**
	 * 异常名
	 */
	@ColumnWidth(15)
	@ExcelProperty({"异常日志", "异常名称"})
	private String	exceptionName;
	/**
	 * 异常信息
	 */
	@ColumnWidth(15)
	@ExcelProperty({"异常日志", "异常信息"})
	private String message;
	/**
	 * 错误行数
	 */
	@ExcelProperty({"异常日志", "错误行数"})
	private Integer	lineNumber;
	/**
	 *方法类
	 */
	@ExcelProperty({"异常日志", "方法类"})
	private String	methodClass;
	/**
	 * 文件名
	 */
	@ExcelProperty({"异常日志", "文件名"})
	private String fileName;
	/**
	 * 方法名
	 */
	@ExcelProperty({"异常日志", "方法名"})
	private String	methodName;
	/**
	 * 操作提交的数据
	 */
	@ExcelProperty({"异常日志", "提交数据"})
	private String params;
	/**
	 * 创建者
	 */
	@ExcelProperty({"异常日志", "创建者"})
	private String createBy;
	/**
	 * 创建时间
	 */
	@ExcelProperty({"异常日志", "创建时间"})
	private Date createTime;
}
