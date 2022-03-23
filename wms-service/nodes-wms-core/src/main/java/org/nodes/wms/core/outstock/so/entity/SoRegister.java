package org.nodes.wms.core.outstock.so.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

import java.io.Serializable;

/**
 * 装车登记实体类
 *
 * @author zhongls
 * @since 2020-05-07
 */
@Data
@TableName("truck_register")
@ApiModel(value = "Register对象", description = "装车登记")
public class SoRegister extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	* 到货登记ID
	*/
		@ApiModelProperty(value = "到货登记ID")
		@JsonSerialize(using = ToStringSerializer.class)
		@TableId(value = "trr_id", type = IdType.ASSIGN_ID)
		private Long trrId;
	/**
	* 库房ID
	*/
		@ApiModelProperty(value = "库房ID")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long whId;
	/**
	* 装车牌
	*/
		@ApiModelProperty(value = "装车牌")
		@JsonSerialize(using = ToStringSerializer.class)
		private String loadId;
	/**
	* 月台ID
	*/
		@ApiModelProperty(value = "月台ID")
		@JsonSerialize(using = ToStringSerializer.class)
		private Long spiId;
	/**
	* 月台编号
	*/
		@ApiModelProperty(value = "月台编号")
		private String platformCode;
	/**
	* 月台名称
	*/
		@ApiModelProperty(value = "月台名称")
		private String platformName;
	/**
	* 司机名称
	*/
		@ApiModelProperty(value = "司机名称")
		private String driverName;
	/**
	* 联系电话
	*/
		@ApiModelProperty(value = "联系电话")
		private String driverPhone;
	/**
	* 车牌号
	*/
		@ApiModelProperty(value = "车牌号")
		private String plateNumber;

}
