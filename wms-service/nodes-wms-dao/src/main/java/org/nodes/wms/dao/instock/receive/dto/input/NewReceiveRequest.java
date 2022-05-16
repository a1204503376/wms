package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 新建收货单接收头表和明细参数
 **/
@Data
public class NewReceiveRequest implements Serializable {
	private static final long serialVersionUID = 676133696090864158L;
	private NewReceiveHeaderRequest newReceiveHeaderRequest;
	private List<ReceiveNewDetailRequest> receiveNewDetailRequestList;
}
