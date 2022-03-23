
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
import org.hibernate.validator.constraints.Length;
import org.springblade.core.mp.base.BaseEntity;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 盘点单头表实体类
 *
 * @author NodeX
 * @since 2020-01-02
 */
@Data
@TableName("wms_count_header")
@ApiModel(value = "CountHeader对象", description = "盘点单头表")
public class CountHeader extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 盘点单id
	 */
	@ApiModelProperty(value = "盘点单id")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "count_bill_id", type = IdType.ASSIGN_ID)
	private Long countBillId;
	/**
	 * 盘点单编码
	 */
	@ApiModelProperty(value = "盘点单编码")
	@Length(max = 60, message = "盘点单编码最大长度为60位")
	private String countBillNo;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 盘点单状态
	 */
	@ApiModelProperty(value = "盘点单状态")
	private Integer countBillState;

	/**
	 * 盘点方式(0:按物品盘点, 1:按库位盘点)
	 */
	@ApiModelProperty(value = "盘点方式(0:按物品盘点, 1:按库位盘点)")
	private Integer countBy;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	@Length(max = 100, message = "备注最大长度为100位")
	private String countRemark;
	/**
	 * 盘点标签
	 */
	@ApiModelProperty(value = "盘点标签")
	private Integer countTag;

	/**
	 * 盘点模式
	 */
	@ApiModelProperty(value = "盘点模式")
	private String mode;
	/**h
	 * 单据创建人员名称
	 */
	@ApiModelProperty(value = "单据创建人员名称")
	private String creator;
	/**
	 * 同步状态
	 */
	@ApiModelProperty(value = "同步状态")
	private Integer syncState;
	/**
	 * 单据创建时间
	 */
	@ApiModelProperty(value = "单据创建时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime procTime;


}
