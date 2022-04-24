package org.nodes.wms.dao.basics.customers.dto.input;

import lombok.Data;

import java.util.List;

/**
 * 客户管理删除接收类
 **/
@Data
public class DeleteCustomersRequest {
	private List<Long> list;
}
