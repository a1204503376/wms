package org.nodes.wms.dao.receive.header.dto.output;

import lombok.Data;
import org.nodes.wms.dao.receive.detail.dto.output.DetailResponse;

import java.io.Serializable;
import java.util.List;

/**
 * 收货单明细 返回前端视图类
 **/
@Data
public class HeaderDetailResponse implements Serializable {
	private HeaderResponse headerResponse;
	private List<DetailResponse> detailList;
}
