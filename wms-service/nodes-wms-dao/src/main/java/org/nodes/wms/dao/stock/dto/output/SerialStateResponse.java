package org.nodes.wms.dao.stock.dto.output;

import lombok.Data;
import org.nodes.wms.dao.application.dto.output.ElementUiSelectResponse;

import java.io.Serializable;

/**
 * 序列号状态响应对象
 **/
@Data
public class SerialStateResponse extends ElementUiSelectResponse<Integer> implements Serializable {

	private static final long serialVersionUID = -3151882454835328079L;


}
