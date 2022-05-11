package org.nodes.wms.dao.basics.customer.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 客户管理删除接收类
 **/
@Data
public class DeleteCustomerRequest implements Serializable {
	private static final long serialVersionUID = 2538985370152501433L;
	@NotNull(message = "ID不能为空")
	private List<Long> ids;
}
