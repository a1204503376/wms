package org.nodes.wms.dao.instock.receive.dto.output;


import lombok.Data;

import java.io.Serializable;

/**
 * 收货响应对象
 **/
@Data
public class PdaByPcsReceiveResponse implements Serializable {
	private static final long serialVersionUID = -4983690992277737954L;
	//当前收货是否完成
	private Boolean currentReceivieIsAccomplish;
	//全部收货是否完成
	private Boolean allReceivieIsAccomplish;
}
