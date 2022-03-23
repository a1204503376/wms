package org.nodes.wms.core.log.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统日志实体类
 *
 * @author NodeX
 * @since 2020-02-12
 */
@Data
@TableName("log_system_proc")
@ApiModel(value = "SystemProc对象", description = "系统日志")
public class SystemProc extends TenantEntity implements Serializable  {

	private static final long serialVersionUID = 1L;

	/**
	 * 系统日志ID
	 */

	@TableId(type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "系统日志ID")
	private Long systemProcId;
	/**
	 * 业务发生时间
	 */
	@ApiModelProperty(value = "业务发生时间")
	private Date procTime;
	/**
	 * 业务种类
	 */
	@ApiModelProperty(value = "业务种类")
	private String procType;
	/**
	 * 系统日志内容
	 */
	@ApiModelProperty(value = "系统日志内容")
	private String systemProcLog;
	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long userId;
	/**
	 * 用户名称
	 */
	@ApiModelProperty(value = "用户名称")
	private String userName;
	/**
	 * 数据键
	 */
	@ApiModelProperty(value = "数据键")
	private String dataId;
	/**
	 * 数据项目
	 */
	@ApiModelProperty(value = "数据项目")
	private Integer dataItem;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	@TableField(exist = false)
	@ApiModelProperty("业务触发时间范围")
	private String triggerRange;
}
