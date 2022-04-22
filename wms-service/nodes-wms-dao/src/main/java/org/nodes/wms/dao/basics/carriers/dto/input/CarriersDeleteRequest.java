package org.nodes.wms.dao.basics.carriers.dto.input;

import lombok.Data;

import java.util.List;
/**
 * 承运商管理删除接收类
 **/
@Data
public class CarriersDeleteRequest {
	private List<Long> list;
}
