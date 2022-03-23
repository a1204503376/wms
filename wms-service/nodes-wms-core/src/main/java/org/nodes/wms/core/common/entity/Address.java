
package org.nodes.wms.core.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springblade.core.tenant.mp.TenantEntity;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 地址实体类
 *
 * @author pengwei
 * @since 2019-12-06
 */
@Data
@TableName("pub_address")
@ApiModel(value = "Address对象", description = "Address对象")
public class Address extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 地址ID
	 */
	@ApiModelProperty(value = "地址ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "pa_id", type = IdType.ASSIGN_ID)
	private Long paId;
	/**
	 * 数据ID
	 */
	@ApiModelProperty(value = "数据ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long dataId;
	/**
	 * 地址数据类型
	 */
	@NotNull(message = "地址数据类型不能为空")
	@ApiModelProperty(value = "地址数据类型", required = true)
	private Integer addressDataType;
	/**
	 * 地址类型
	 */

	@ApiModelProperty(value = "地址类型", required = true)
	private Integer addressType;
	/**
	 * 地址
	 */
	@ApiModelProperty(value = "地址")
	@Length(max = 1000, message = "字符长度不能超过1000")
	private String address;
	/**
	 * 经度
	 */
	@ApiModelProperty(value = "经度")
	private BigDecimal longitude;
	/**
	 * 纬度
	 */
	@ApiModelProperty(value = "纬度")
	private BigDecimal latitude;
	/**
	 * 默认试用标记
	 */
	@ApiModelProperty(value = "默认试用标记")
	private Integer defaultFlag;
}
