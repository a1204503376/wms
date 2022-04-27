package org.nodes.wms.dao.basics.customers.dto.input;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 客户管理删除接收类
 **/
@Data
public class DeleteCustomerRequest {
	@NotNull(message = "ID不能为空")
	private List<Long> list;
}
