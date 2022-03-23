
package org.nodes.wms.core.warehouse.entity;

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

import java.math.BigDecimal;

/**
 * 容器实体类
 *
 * @author liangmei
 * @since 2019-12-16
 */
@Data
@TableName("wms_lpn")
@ApiModel(value = "Lpn对象", description = "Lpn对象")
public class Lpn extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 容器id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "容器id")
	@TableId(value = "lpn_id", type = IdType.ASSIGN_ID)
	private Long lpnId;
	/**
	 * 容器编码
	 */
	@ApiModelProperty(value = "容器编码")
	@Length(min = 1, max = 30, message = "最小长度为1位，最大长度为30位")
	private String lpnCode;
	/**
	 * 容器名称
	 */
	@ApiModelProperty(value = "容器名称")
	@Length(min = 1, max = 50, message = "最小长度为1位，最大长度为50位")
	private String lpnName;
	/**
	 * 容器类型
	 */
	@ApiModelProperty(value = "容器类型")
	private Integer lpnType;
	/**
	 * 容器的重量
	 */
	@ApiModelProperty(value = "容器的重量")
	private BigDecimal lpnWeight;
}
