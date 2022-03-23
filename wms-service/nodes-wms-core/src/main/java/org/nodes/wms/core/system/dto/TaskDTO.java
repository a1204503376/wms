package org.nodes.wms.core.system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.system.entity.TaskPackage;
import org.nodes.wms.core.system.enums.TaskTypeEnum;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * author: pengwei
 * date: 2021/4/19 12:12
 * description: TaskDTO
 */
@Data
public class TaskDTO {

	/**
	 * 任务包
	 */
	@ApiModelProperty("任务包")
	private TaskPackage taskPackage;
	/**
	 * 任务类型
	 */
	@NotNull(message = "参数: taskType 不能为空")
	@ApiModelProperty("任务类型")
	private Integer taskType;
	/**
	 * 库房ID
	 */
	@NotNull(message = "参数: whId 不能为空")
	@ApiModelProperty("库房ID")
	private Long whId;
	/**
	 * 单据类型编码
	 */
	@NotNull(message = "参数: billTypeCd 不能为空")
	@ApiModelProperty("单据类型编码")
	private String billTypeCd;
	/**
	 * 单据ID
	 */
	@ApiModelProperty("单据ID")
	private Long billId;
	/**
	 * 单据编码
	 */
	@ApiModelProperty("单据编码")
	private String billNo;
	/**
	 * 任务明细数量
	 */
	@ApiModelProperty("任务明细数量")
	private Integer taskQty;
	/**
	 * 任务分派时间
	 */
	@ApiModelProperty("任务分派时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime allotTime;
	/**
	 * 任务优先时间
	 */
	@ApiModelProperty("任务优先时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime confirmDate;
	/**
	 * 备注
	 */
	@ApiModelProperty("备注")
	private String remark;
}
