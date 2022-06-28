package org.nodes.wms.dao.instock.receive.dto.output;


import lombok.Data;

import java.io.Serializable;

/**
 * 收货响应对象
 **/
@Data
public class PdaByPcsReceiveResponse implements Serializable {
	//当前收货是否完成
	private String currentReceivieIsAccomplish;
	//全部收货是否完成
	private String allReceivieIsAccomplish;
}
