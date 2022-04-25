package org.nodes.wms.dao.basics.suppliers.dto.input;

import lombok.Data;

import java.util.List;

/**
 * 删除请求参数类
 *
 * @author 彭永程
 * @date 2022-04-21 14:17
 **/
@Data
public class DeleteSuppliersRequest {
	private List<Long> ids;
}