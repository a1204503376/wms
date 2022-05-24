package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 收货单删除接收前端id集合
 **/
@Data
public class DeleteReceiveIdRequest implements Serializable {
	private static final long serialVersionUID = 6974361554853687333L;
	private List<Long> receiveIdList;
}
