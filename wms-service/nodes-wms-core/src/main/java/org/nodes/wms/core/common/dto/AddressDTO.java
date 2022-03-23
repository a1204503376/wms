
package org.nodes.wms.core.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.common.entity.Address;

import javax.validation.constraints.NotBlank;

/**
 * 数据传输对象实体类
 *
 * @author NodeX
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AddressDTO extends Address {
	private static final long serialVersionUID = 1L;

	@NotBlank(message = "地址类型不能为空")
    private String addressTypeDesc;
}
