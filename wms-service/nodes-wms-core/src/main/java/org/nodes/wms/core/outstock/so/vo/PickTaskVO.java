package org.nodes.wms.core.outstock.so.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 按件拣货任务VO
 *
 * @Author zx
 * @Date 2020/3/6
 **/
@Data
public class PickTaskVO {

	/**
	 * 单据总数
	 */
	@ApiModelProperty(value = "单据进度:总数")
	private Integer count;

	/**
	 * 单据完成数
	 */
	@ApiModelProperty(value = "单据进度:完成数")
	private Integer finish;

	/**
	 * 标题编码
	 */
	@ApiModelProperty(value = "标题编码")
	private String titleCode;

	/**
	 * 拣货计划集合
	 */
	@ApiModelProperty(value = "拣货计划集合")
	private List<PickSkuVO> pickPlans;

	/**
	 * 上位系统单号
	 */
	@ApiModelProperty(value = "上位系统单号")
	private String orderNo;

	/**
	 * 上位系统单号
	 */
	@ApiModelProperty(value = "出库单编号")
	private String sobillNo;

	@JsonProperty("cName")
	@ApiModelProperty("客户名称")
	private String cName;

	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@ApiModelProperty(value = "发货完成时间")
	private LocalDateTime transportDate;
}
