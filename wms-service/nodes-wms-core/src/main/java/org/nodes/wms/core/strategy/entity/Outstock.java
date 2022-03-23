
package org.nodes.wms.core.strategy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.core.tool.utils.StringPool;

/**
 * 分配策略 实体类
 *
 * @author zhongls
 * @since 2019-12-10
 */
@Data
@TableName("st_outstock")
@ApiModel(value = "Outstock对象", description = "Outstock对象")
public class Outstock extends TenantEntity {

	private static final long serialVersionUID = 1L;
	public static final String DATA_TYPE = StringPool.ONE;

	/**
	 * 分配策略ID
	 */
	@ApiModelProperty(value = "分配策略ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "sso_id", type = IdType.ASSIGN_ID)
	private Long ssoId;
	/**
	 * 分配策略编码
	 */
	@ApiModelProperty(value = "分配策略编码")
	private String ssoCode;
	/**
	 * 分配策略名称
	 */
	@ApiModelProperty(value = "分配策略名称")
	private String ssoName;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 是否为系统级数据(0否，1是）
	 */
	@ApiModelProperty("是否为系统more(0否，1是）")
	private Integer isDefault;
}
