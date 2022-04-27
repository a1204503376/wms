package org.nodes.wms.dao.receive.header.dto.input;

import lombok.Data;
import org.nodes.wms.dao.receive.detail.dto.input.NewDetailRequest;

import java.util.List;

/**
 * 新建收货单接收头表和明细参数
 **/
@Data
public class NewReceiveRequest {
	private NewHeaderRequest newHeaderRequest;
	private List<NewDetailRequest> newDetailRequests;
}
