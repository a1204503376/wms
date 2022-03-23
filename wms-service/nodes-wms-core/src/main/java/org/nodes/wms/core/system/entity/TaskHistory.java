
package org.nodes.wms.core.system.entity;

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
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 任务履历实体类
 *
 * @author pengwei
 * @since 2019-12-10
 */
@Data
@TableName("tsk_task_history")
@ApiModel(value = "TaskHistory对象", description = "任务履历")
public class TaskHistory extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 任务履历ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "task_history_id", type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "任务履历ID")
	private Long taskHistoryId;
	/**
	 * 任务ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "任务ID")
	private Long taskId;
	/**
	 * 工作任务包ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "工作任务包ID")
	private Long ttpId;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库房ID")
	private Long whId;
	/**
	 * 任务编码
	 */
	@ApiModelProperty(value = "任务编码")
	private Integer taskTypeCd;
	/**
	 * 任务执行方式
	 */
	@ApiModelProperty(value = "任务执行方式")
	private Integer taskProcType;
	/**
	 * 单据种类编码
	 */
	@ApiModelProperty(value = "单据种类编码")
	private String billTypeCd;
	/**
	 * 单据ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "单据ID")
	private Long billId;
	/**
	 * 单据编码
	 */
	@ApiModelProperty(value = "单据编码")
	private String billNo;
	/**
	 * 工作区ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "工作区ID")
	private Long wwaId;
	/**
	 * 用户ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "用户ID")
	private Long userId;
	/**
	 * 用户编码
	 */
	@ApiModelProperty(value = "用户编码")
	private String userCode;
	/**
	 * 用户名称
	 */
	@ApiModelProperty(value = "用户名称")
	private String userName;
	/**
	 * 数量
	 */
	@ApiModelProperty(value = "数量")
	private Integer taskQty;
	/**
	 * 任务分组编码
	 */
	@ApiModelProperty(value = "任务分组编码")
	private String taskGroup;
	/**
	 * 任务描述
	 */
	@ApiModelProperty(value = "任务描述")
	private String taskRemark;
	/**
	 * 任务分派时间
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "任务分派时间")
	private LocalDateTime allotTime;
	/**
	 * 任务优先时间
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "任务优先时间")
	private LocalDateTime confirmDate;
	/**
	 * 任务开始执行的时间
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "任务开始执行的时间")
	private LocalDateTime beginTime;
	/**
	 * 任务关闭时间
	 */
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "任务关闭时间")
	private LocalDateTime closeTime;


}
