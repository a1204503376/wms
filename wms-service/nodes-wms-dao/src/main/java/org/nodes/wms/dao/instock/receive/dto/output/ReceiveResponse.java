package org.nodes.wms.dao.instock.receive.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 收货单明细 返回前端视图类
 **/
@Data
public class ReceiveResponse implements Serializable {
	private static final long serialVersionUID = -1690606023140560102L;
	private ReceiveHeaderResponse receiveHeaderResponse;
	private List<ReceiveDetailResponse> detailList;
}
