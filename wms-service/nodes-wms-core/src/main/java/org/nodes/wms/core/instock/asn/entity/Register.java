
package org.nodes.wms.core.instock.asn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.mp.base.BaseEntity;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 到货登记实体类
 *
 * @author zhonglianshuai
 * @since 2020-04-07
 */
@Data
@TableName("asn_register")
@ApiModel(value = "Register对象", description = "到货登记")
public class Register extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 到货登记ID
	 */
	@TableId(type = IdType.ID_WORKER)
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "到货登记ID")
	private Long asnrId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@NotNull(message = "参数：whId 不能为空！")
	private Long whId;
	/**
	 * 收货单ID
	 */
	@ApiModelProperty(value = "收货单ID")
	@NotNull(message = "参数：asnBillId 不能为空！")
	private Long asnBillId;
	/**
	 * 送货牌ID
	 */
	@ApiModelProperty(value = "送货牌ID")
	private Long dcId;
	/**
	 * 月台ID
	 */
	@ApiModelProperty(value = "月台ID")
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
	 * 卸货司机
	 */
	@ApiModelProperty(value = "卸货司机")
	private String uninstallDriverName;
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
