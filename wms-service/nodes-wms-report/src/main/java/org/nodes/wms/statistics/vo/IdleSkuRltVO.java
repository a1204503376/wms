package org.nodes.wms.statistics.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.warehouse.entity.Location;
import org.nodes.wms.core.basedata.vo.IdleSkuInfoVO;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author pengwei
 * @program WmsCore
 * @description 呆滞物品信息
 * @create 20200407
 */
@Data
public class IdleSkuRltVO {

	/**
	 * 当前呆滞物品数量
	 */
	@ApiModelProperty("当前呆滞物品数量")
	private Integer currSkuCount;

	/**
	 * 当前呆滞物品列表
	 */
	@ApiModelProperty("当前呆滞物品列表")
	private List<IdleSkuInfoVO> currSkuList;

	/**
	 * 上一时间段呆滞物品数量
	 */
	@ApiModelProperty("上一时间段呆滞物品数量")
	private Integer lastSkuCount;

	/**
	 * 上一时间段呆滞物品列表
	 */
	@ApiModelProperty("上一时间段呆滞物品列表")
	private List<IdleSkuInfoVO> lastSkuList;

	/**
	 * 比率
	 */
	@ApiModelProperty("比率")
	private Integer rate;

	/**
	 * 呆滞物品占用库位
	 */
	@ApiModelProperty("呆滞物品占用库位")
	private List<Location> occupyLocList;

	/**
	 * 呆滞物品占用的库位数量
	 */
	@ApiModelProperty("呆滞物品占用的库位数量")
	private Integer occupyLocCount;

	/**
	 * 当前开始时间
	 */
	@ApiModelProperty("开始时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime currBeginTime;

	/**
	 * 当前结束时间
	 */
	@ApiModelProperty("结束时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime currEndTime;

	/**
	 * 上一次开始时间
	 */
	@ApiModelProperty("上一次开始时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime lastBeginTime;

	/**
	 * 上一次结束时间
	 */
	@ApiModelProperty("上一次结束时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime lastEndTime;
}
