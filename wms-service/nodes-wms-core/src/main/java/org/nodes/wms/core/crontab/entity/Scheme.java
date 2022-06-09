package org.nodes.wms.core.crontab.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务计划表实体类
 *
 * @author NodeX
 * @since 2021-01-22
 */
@Data
@TableName("crontab_scheme")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Scheme对象", description = "任务计划表")
public class Scheme extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	@JsonSerialize(using = ToStringSerializer.class)
	private Long id;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 年
	 */
	@ApiModelProperty(value = "年")
	private Integer years;
	/**
	 * 月
	 */
	@ApiModelProperty(value = "月")
	private Integer months;
	/**
	 * 日
	 */
	@ApiModelProperty(value = "日")
	private Integer days;
	/**
	 * 周,0表示星期天，1表示周一，-1表示忽略
	 */
	@ApiModelProperty(value = "周,0表示星期天，1表示周一，-1表示忽略")
	private Integer weekdays;
	/**
	 * 时
	 */
	@ApiModelProperty(value = "时")
	private Integer hours;
	/**
	 * 分
	 */
	@ApiModelProperty(value = "分")
	private Integer minutes;
	/**
	 * 秒
	 */
	@ApiModelProperty(value = "秒")
	private Integer seconds;
	/**
	 * 时间范围，单位秒
	 */
	@ApiModelProperty(value = "时间范围，单位秒")
	private Integer secondRange;
	/**
	 * 任务id
	 */
	@ApiModelProperty(value = "任务id")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long crontabTaskId;
	/**
	 * 规定时间内必须执行次数
	 */
	@ApiModelProperty(value = "规定时间内必须执行次数")
	private Integer mustExecTimes;
	/**
	 * 当天执行次数
	 */
	@ApiModelProperty(value = "当天执行次数")
	private Integer todayExecTimes;
	/**
	 * 上次执行时间
	 */
	@ApiModelProperty("上次执行时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime lastExecTime;

	private Integer status;

}
