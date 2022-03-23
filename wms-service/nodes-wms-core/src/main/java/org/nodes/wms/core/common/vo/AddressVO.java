
package org.nodes.wms.core.common.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.nodes.wms.core.common.entity.Address;

/**
 * 视图实体类
 *
 * @author pengwei
 * @since 2019-12-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AddressVO对象", description = "AddressVO对象")
public class AddressVO extends Address {
	private static final long serialVersionUID = 1L;

	/**
	 * 地址类型描述
	 */
	@ApiModelProperty("地址类型描述")
	public String addressTypeDesc;

	/**
	 * 默认标记（Boolean类型）
	 */
	@ApiModelProperty("默认标记（Boolean类型）")
	public boolean defaultFlagBoolean;

	@ApiModelProperty("默认标记（Boolean类型）")
	public String defaultFlagBooleanDesc;

}
