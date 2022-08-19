
package org.nodes.wms.core.count.entity;

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
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 盘点单明细表实体类
 *
 * @author chz
 * @since 2020-01-02
 */
@Data
@TableName("wms_count_detail")
@ApiModel(value = "CountDetail对象", description = "盘点单明细表")
public class CountDetail extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 盘点单明细ID
	 */
	@ApiModelProperty(value = "盘点单明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "count_detail_id", type = IdType.ASSIGN_ID)
	private Long countDetailId;
	/**
	 * 盘点单ID
	 */
	@ApiModelProperty(value = "盘点单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long countBillId;
	/**
	 * 单据编码
	 */
	@ApiModelProperty(value = "单据编码")
	private String countBillNo;
	/**
	 * 库位ID
	 */
	@ApiModelProperty(value = "库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 库位编码
	 */
	@ApiModelProperty(value = "库位编码")
	private String locCode;
	/**
	 * 货位状态
	 */
	@ApiModelProperty(value = "货位状态")
	private Integer locState;
	/**
	 * 物品ID
	 */
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 物品编码
	 */
	@ApiModelProperty(value = "物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ApiModelProperty(value = "物品名称")
	private String skuName;
	/**
	 * 任务分组编码
	 */
	@ApiModelProperty(value = "任务分组编码")
	private String taskGroup;
	/**
	 * 用户名称
	 */
	@ApiModelProperty(value = "用户名称")
	private String userName;
	/**
	 * 盘点时间
	 */
	@ApiModelProperty(value = "盘点时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime procTime;

	private String boxCode;


}
