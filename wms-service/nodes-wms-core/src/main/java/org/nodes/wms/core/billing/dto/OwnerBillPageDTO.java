package org.nodes.wms.core.billing.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * author: pengwei
 * date: 2021/7/2 16:24
 * description: OwnerBillPageDTO
 */
@Data
public class OwnerBillPageDTO {

	/**
	 * 货主id
	 */
	@ApiModelProperty("货主id")
	private Long woId;
	/**
	 * 开始日期
	 */
	@ApiModelProperty("开始日期")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.PATTERN_DATE)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private LocalDate beginDate;
	/**
	 * 结束日期
	 */
	@ApiModelProperty("结束日期")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.PATTERN_DATE)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private LocalDate endDate;
}
