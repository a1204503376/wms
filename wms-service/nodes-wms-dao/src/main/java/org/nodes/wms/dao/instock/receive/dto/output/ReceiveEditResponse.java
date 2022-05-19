package org.nodes.wms.dao.instock.receive.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 编辑收货单返回前端视图类
 **/
@Data
public class ReceiveEditResponse implements Serializable {
	private static final long serialVersionUID = -2742813345998722216L;
	private ReceiveHeaderEditResponse receiveHeaderResponse;
	private List<ReceiveDetailEditResponse> receiveDetailResponseList;
}
