
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

/**
 * 库区管理 实体类
 *
 * @author zhongls
 * @since 2019-12-06
 */
@Data
@TableName("wms_zone")
@ApiModel(value = "Zone对象", description = "Zone对象")
public class Zone extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 库区ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "zone_id", type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "库区ID")
	private Long zoneId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 库区编码
	 */
	@ApiModelProperty(value = "库区编码")
	@Length(min = 1, max = 30, message = "最小长度为1位，最大长度为30位")
	private String zoneCode;
	/**
	 * 库区名称
	 */
	@ApiModelProperty(value = "库区名称")
	@Length(min = 1, max = 50, message = "最小长度为1位，最大长度为50位")
	private String zoneName;
	/**
	 * 库区类型
	 */
	@ApiModelProperty(value = "库区类型")
	private Integer zoneType;
}
