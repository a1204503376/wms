package org.nodes.wms.dao.crontab.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;

/**
 * 任务表实体类
 *
 * @author NodeX
 * @since 2021-01-22
 */
@Data
@TableName("crontab_task")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Task对象", description = "任务表")
public class CrontabTask extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;

	/**
	 * 任务名
	 */
	@ApiModelProperty(value = "任务名")
	private String crontabTaskName;
	/**
	 * 0为外部调用
	 */
	@ApiModelProperty(value = "0为外部调用")
	private Integer appid;
	/**
	 * url,如果是内部调用，请填写path即可，http_method=0时，请填写绝对路径
	 */
	@ApiModelProperty(value = "url,如果是内部调用，请填写path即可，http_method=0时，请填写绝对路径")
	private String url;
	/**
	 * 请求方式 0-执行urll绝对路径里的程序 1-GET 2-POST
	 */
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	@ApiModelProperty(value = "请求方式 0-执行urll绝对路径里的程序 1-GET 2-POST")
	private String httpMethod;
	/**
	 * 请求参数
	 */
	@ApiModelProperty(value = "请求参数")
	@TableField(updateStrategy = FieldStrategy.IGNORED)
	private String params;
	/**
	 * 启动状态
	 */
	private Integer enabled;
	/**
	 * 请求方法
	 */
	private String method;
	/**
	 * cron表达式
	 */
	private String cron;
}
