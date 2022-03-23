package org.nodes.wms.core.outstock.so.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * @program: purefamily_wms_java
 * @description: 波次分配参数
 * @author: pengwei
 * @create: 2021-07-21 18:05
 **/
@Data
public class CreatePickPlanByWellenDTO {

	@ApiModelProperty("波次id")
	private Long wellenId;

	@ApiModelProperty("库位id")
	private Long locId;

	@ApiModelProperty("分配类型")
	private Integer type;

	@ApiModelProperty("周转方式")
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.PATTERN_DATE)
	private Integer turnoverItem;

	@ApiModelProperty("批次范围")
	private LocalDate[] lotRange;

	@ApiModelProperty("优先库区id")
	private Long[] zoneIds;

	@ApiModelProperty("批次号")
	private String lotNumbers;

	@ApiModelProperty("库位编码")
	private String locCodes;
}
