
package org.nodes.wms.core.outstock.loading.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 车次头表视图实体类PDA
 *
 * @author chz
 * @since 2020-03-03
 */
@Data
@ApiModel(value = "SoTruckHeaderVO对象", description = "车次头表")
public class SoTruckHeaderPdaVO{
	private static final long serialVersionUID = 1L;
	/**
	 * 车次ID
	 */
	@ApiModelProperty(value = "车次ID")
	@JsonSerialize(using = ToStringSerializer.class)
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
	 * 所属单位
	 */
	@ApiModelProperty(value = "所属单位")
	private String truckCompany;
	/**
	 * 装车时间
	 */
	@ApiModelProperty(value = "装车时间")
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
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
	 * 任务ID
	 */
	@ApiModelProperty(value = "任务ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long taskId;
	/**
	 * 用户名称
	 */
	@ApiModelProperty(value = "用户名称")
	private String userName;
	/**
	 * 发货人
	 */
	@ApiModelProperty(value = "发货人")
	private String consigner;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 库存明细VO
	 */
	@ApiModelProperty(value = "发货明细头表数据")
	private List<StockPdaVO> stockPdaVOList;

	/**
	 * 客户名称
	 */
	@JsonProperty("cName")
	@ApiModelProperty(value = "客户名称")
	private String cName;
	/**
	 * 收货人地址
	 */
	@ApiModelProperty(value = "收货人地址")
	private String address;
	/**
	 * 收获联系人
	 */
	@ApiModelProperty(value = "收获联系人")
	private String contact;
	/**
	 * 收货人联系电话
	 */
	@ApiModelProperty(value = "收货人联系电话")
	private String phone;

	/**
	 * 发货单ID
	 */
	@ApiModelProperty(value = "发货单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;

}
