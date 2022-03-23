
package org.nodes.wms.core.count.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.count.entity.CountDetail;
import org.nodes.wms.core.count.entity.CountHeader;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 盘点单头表视图实体类
 *
 * @author NodeX
 * @since 2020-01-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "CountHeaderVO对象", description = "盘点单头表")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountHeaderVO extends CountHeader {
	private static final long serialVersionUID = 1L;

	/**
	 * 搜索方式(0:abc盘点, 1:动碰盘点, 2:指定盘点)
	 */
	@NotNull
	@ApiModelProperty("搜索方式(0:abc盘点, 1:动碰盘点, 2:指定盘点)")
	private Integer searchType;
	/**
	 * 库房名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;

	/**
	 * 盘点单状态描述
	 */
	@ApiModelProperty(value = "盘点单状态描述")
	private String countBillStateName;

	/**
	 * 盘点类型
	 */
	@ApiModelProperty(value = "盘点类型")
	private String countTagName;

	/**
	 * 库区ID数组
	 */

	@ApiModelProperty(value = "库区ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private List<Long> zoneId;

	/**
	 * 同步状态描述
	 */
	@ApiModelProperty("同步状态描述")
	private String syncStateDesc;

	/**
	 * 库位起始ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库位起始ID")
	private Long locId;
	/**
	 * 库位起始编码
	 */
	@ApiModelProperty(value = "库位起始")
	private String locIdCode;
	/**
	 * 库位结束ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库位结束ID")
	private Long locIdEnd;
	/**
	 * 库位结束编码
	 */
	@ApiModelProperty(value = "库位结束")
	private String locIdEndCode;

	/**
	 * 货主ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "货主ID")
	private Long woId;

	/**
	 * 物品ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "物品ID")
	private Long skuId;

	/**
	 * 开始时间
	 */
	@ApiModelProperty(value = "开始时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime startTime;

	/**
	 * 结束时间
	 */
	@ApiModelProperty(value = "结束时间")
	private String endTime;

	/**
	 * 是否空货位
	 */
	@ApiModelProperty(value = "是否空货位")
	private Boolean checked;

	/**
	 * 货位编码
	 */
	@ApiModelProperty(value = "结束时间")
	private String locCode;
	/**
	 * 用户名称
	 */
	@ApiModelProperty(value = "用户名称")
	private String userName;
	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	private Integer locState;
	/**
	 * 状态描述
	 */
	@ApiModelProperty(value = "状态描述")
	private String locStatusDesc;
	/**
	 * 分配波次
	 */
	@ApiModelProperty(value = "分配波次")
	private Integer countTask;

	/**
	 * 盘点明细
	 */
	@ApiModelProperty(value = "盘点明细")
	private List<CountDetail> countDetailList;
	/**
	 * 盘点明细
	 */
	@ApiModelProperty(value = "盘点明细")
	private List<CountDetailVO> countDetailVOList;

	/**
	 * 差异报告
	 */
	@ApiModelProperty(value = "差异报告")
	private List<CountReportVO> countReportVOList;

	/**
	 * 盘点记录
	 */
	@ApiModelProperty(value = "盘点记录")
	private List<CountRecordVO> countRecordVOList;

	/**
	 * 上次盘点时间
	 */
	@ApiModelProperty("上次盘点时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime lastLocCountDate;

	/**
	 * ABC 分类
	 */
	@ApiModelProperty("ABC 分类")
	private Integer abc;

	/**
	 * ABC 分类描述
	 */
	@ApiModelProperty("ABC 分类描述")
	private String abcDesc;

	/**
	 * 变动类型
	 */
	@ApiModelProperty("变动类型")
	private List<Integer> changeType;

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


}
