package org.nodes.wms.dao.common.log.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 请求日志实体
 */
@Data
@TableName("blade_log_api")
public class LogApi extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -8093031380497680467L;

	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;

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
	 * 方法类
	 */
	private String	methodClass;

	/**
	 * 方法名
	 */
	private String	methodName;

	/**
	 * 服务器名
 	 */
	private String serverHost;

	/**
	 * 服务器IP地址
	 */
	private String	serverIp;

	/**
	 * 操作提交的数据
	 */
	private String params;

	/**
	 * 请求参数大小
	 */
	private BigDecimal paramsSize;

	/**
	 * 响应的数据
	 */
	private String data;

	/**
	 * 响应数据的大小
	 */
	private BigDecimal dataSize;

	/**
	 * 租户id
	 */
	private String tenantId;

	/**
	 * 服务id
	 */
	private String serviceId;

	/**
	 * 服务器环境
	 */
	private String env;

	/**
	 * 日志类型
	 */
	private String type;

	/**
	 * 用户代理
	 */
	private String	userAgent;

	/**
	 * 操作IP地址
	 */
	private String remoteIp;

	/**
	 * 执行时间
	 */
	private String time;

	/**
	 * 创建者
	 */
	private String createBy;

	/**
	 * 创建时间
	 */
	private Date createTime;
}
