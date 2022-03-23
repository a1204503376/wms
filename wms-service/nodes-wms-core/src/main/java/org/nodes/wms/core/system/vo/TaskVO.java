
package org.nodes.wms.core.system.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.system.entity.Task;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 视图实体类
 *
 * @author pengwei
 * @since 2019-12-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TaskVO对象", description = "TaskVO对象")
public class TaskVO extends Task {
	private static final long serialVersionUID = 1L;

	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
	/**
	 * 库位名称
	 */
	@ApiModelProperty("库位名称")
	private String locName;
	/**
	 * 任务类型描述
	 */
	@ApiModelProperty("任务类型描述")
	private String taskTypeDesc;
	/**
	 * 任务执行方式描述
	 */
	@ApiModelProperty("任务执行方式描述")
	private String taskProcTypeDesc;
	/**
	 * 单据种类描述
	 */
	@ApiModelProperty("单据种类描述")
	private String billTypeDesc;
	/**
	 * 是否已分配
	 */
	@ApiModelProperty("是否已分配")
	private String isAllot;
	/**
	 * 计划量
	 */
	@ApiModelProperty("计划量")
	private Integer planQty;
	/**
	 * 已完成量
	 */
	@ApiModelProperty("已完成量")
	private Integer realQty;
	/**
	 * 任务量描述（已完成量/计划量）
	 */
	@ApiModelProperty("任务量描述（已完成量/计划量）")
	private String qtyDesc = String.format("%s/%s", realQty, planQty);

	/**
	 * 任务明细
	 */
	@ApiModelProperty("任务明细")
	private Object detail;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@JsonProperty("cName")
	@ApiModelProperty("客户名称")
	private String cName;

	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "发货完成时间")
	private LocalDateTime transportDate;

	@JsonProperty("orderNo")
	@ApiModelProperty("T100单号")
	private String orderNo;

	@JsonProperty("sobillNo")
	@ApiModelProperty("wms单号")
	private String sobillNo;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@ApiModelProperty("盘点方式描述")
	private String countByDesc;

}
