package org.nodes.wms.core.basedata.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @description 呆滞物品信息实体
 *
 * @author pengwei
 * @since 2020-06-29
 */
@Data
public class IdleSkuInfoVO {

	/**
	 * 物品ID
	 */
	@ApiModelProperty("物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

	/**
	 * 物品编码
	 */
	@ApiModelProperty("物品编码")
	private String skuCode;

	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
	private String skuName;

	/**
	 * 上一次出库时间
	 */
	@ApiModelProperty("上一次出库时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime lastOutstockTime;
}
