package org.nodes.wms.dao.instock.receive.header.dto.output;

import lombok.Data;
import org.nodes.wms.dao.instock.receive.detail.dto.output.ReceiveDetailResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 收货单明细 返回前端视图类
 **/
@Data
public class ReceiveHeaderDetailResponse implements Serializable {
	private static final long serialVersionUID = -1690606023140560102L;
	private ReceiveHeaderResponse receiveHeaderResponse;
	private List<ReceiveDetailResponse> detailList;
}
