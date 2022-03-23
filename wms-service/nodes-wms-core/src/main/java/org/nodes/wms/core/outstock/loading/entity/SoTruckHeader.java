
package org.nodes.wms.core.outstock.loading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 车次头表实体类
 *
 * @author chz
 * @since 2020-03-03
 */
@Data
@ApiModel(value = "SoTruckHeader对象", description = "车次头表")
public class SoTruckHeader extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 车次ID
	 */
	@ApiModelProperty(value = "车次ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "truck_id", type = IdType.ASSIGN_ID)
	private Long truckId;
	/**
	 * 车次编号
	 */
	@ApiModelProperty(value = "车次编号")
	private String truckCode;
	/**
	 * 车次状态
	 */
	@ApiModelProperty(value = "车次状态")
	private Integer truckState;
	/**
	 * 车牌号
	 */
	@ApiModelProperty(value = "车牌号")
	private String plateNumber;
	/**
	 * 司机
	 */
	@ApiModelProperty(value = "司机")
	private String driverName;
	/**
	 * 电话
	 */
	@ApiModelProperty(value = "电话")
	private String driverPhone;
	/**
	 * 装车时间
	 */
	@ApiModelProperty(value = "装车时间")
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime truckDate;
	/**
	 * 运单编号
	 */
	@ApiModelProperty(value = "运单编号")
	private String truckHeaderWaybill;
	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String truckRemark;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;


}
