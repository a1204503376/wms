
package org.nodes.wms.core.strategy.entity;

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
import org.springblade.core.tool.utils.StringPool;

/**
 * 上架策略实体类
 *
 * @author liangmei
 * @since 2019-12-09
 */
@Data
@TableName("st_instock")
@ApiModel(value = "Instock对象", description = "Instock对象")
public class Instock extends TenantEntity {

	private static final long serialVersionUID = 1L;
	public static final String DATA_TYPE = StringPool.ONE;

	/**
	 * 上架策略ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "上架策略ID")
	@TableId(value = "ssi_id", type = IdType.ASSIGN_ID)

	private Long ssiId;
	/**
	 * 上架策略编码
	 */
	@ApiModelProperty(value = "上架策略编码")
	@Length(max = 50, message = "编码过长")
	private String ssiCode;
	/**
	 * 上架策略名称
	 */
	@ApiModelProperty(value = "上架策略名称")
	private String ssiName;
	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "库房ID")
	private Long whId;
	/**
	 * 是否为系统级数据(0否，1是）
	 */
	@ApiModelProperty("是否为系统more(0否，1是）")
	private Integer isDefault;
}
