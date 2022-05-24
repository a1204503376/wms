package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;

import java.util.List;

/**
 * 编辑收货单接收前端数据类
 **/
@Data
public class EditReceiveRequest {
	private EditReceiveHeaderRequest editReceiveHeaderRequest;
	private List<EditReceiveDetailRequest> editReceiveDetailRequestList;
}
