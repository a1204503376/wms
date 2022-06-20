package org.nodes.wms.dao.common.log.entities;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

import java.io.Serializable;
import java.util.Date;
import java.math.BigDecimal;

/**
 * 异常日志实体
 * @author 彭浩
 */
@Data
@TableName("blade_log_error")
public class LogError extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -8093031380497680467L;

	/**
	 * 主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 租户id
	 */
	private String  tenantId;
	/**
	 * 服务ID
	 */
	private String serviceId;
	/**
	 * 服务器名
 	 */
	private String serverHost;
	/**
	 * 服务器IP地址
	 */
	private String	serverIp;
	/**
	 * 系统环境
	 */
	private String env;
	/**
	 * 操作方式
	 */
	private String	method;
	/**
	 * 请求URI
	 */
	private String requestUri;
	/**
	 * 用户代理
	 */
	private String	userAgent;
	/**
	 * 堆栈
	 */
	private String stackTrace;
	/**
	 * 异常名
	 */
	private String	exceptionName;
	/**
	 * 异常信息
	 */
	private String message;
	/**
	 * 错误行数
	 */
	private Integer	lineNumber;
	/**
	 * 操作IP地址
	 */
	private String remoteIp;
	/**
	 *方法类
	 */
	private String	methodClass;
	/**
	 * 文件名
	 */
	private String fileName;
	/**
	 * 方法名
	 */
	private String	methodName;
	/**
	 * 操作提交的数据
	 */
	private String params;
	/**
	 * 操作提交的数据大小
	 */
	private BigDecimal paramsSize;
	/**
	 * 创建者
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date create_time;

}
