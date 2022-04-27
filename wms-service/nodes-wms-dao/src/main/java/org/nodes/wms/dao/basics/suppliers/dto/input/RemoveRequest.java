package org.nodes.wms.dao.basics.suppliers.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 删除请求参数
 **/
@Data
public class RemoveRequest implements Serializable {

	private static final long serialVersionUID = 1793457830002692743L;

	@NotNull(message = "供应商id不能为空")
	private List<Long> ids;
}
