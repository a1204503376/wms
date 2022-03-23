
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
 * 工作任务包实体类
 *
 * @author pengwei
 * @since 2019-12-10
 */
@Data
@TableName("tsk_task_package")
@ApiModel(value = "TaskPackage对象", description = "工作任务包")
public class TaskPackage extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 工作任务包ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "ttp_id", type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "工作任务包ID")
	private Long ttpId;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库房ID")
	private Long whId;
	/**
	 * 任务编码(101:到货登记任务，102:入库清点)
	 */
	@ApiModelProperty(value = "任务编码(101:到货登记任务，102:入库清点)")
	private Integer taskTypeCd;
	/**
	 * 任务执行方式(90:协作模式，91:抢单模式，92:人工分派模式，93:自动分派模式，99:后台任务，00:不发布任务)
	 */
	@ApiModelProperty(value = "任务执行方式(90:协作模式，91:抢单模式，92:人工分派模式，93:自动分派模式，99:后台任务，00:不发布任务)")
	private Integer taskProcType;
	/**
	 * 任务数据ID(波次ID)
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "任务数据ID(波次ID)")
	private Long taskDataId;
	/**
	 * 任务包下发时间
	 */
	@ApiModelProperty(value = "任务包下发时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime ttpCreateTime;


}
