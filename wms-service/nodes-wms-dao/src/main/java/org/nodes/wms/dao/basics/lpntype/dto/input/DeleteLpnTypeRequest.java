package org.nodes.wms.dao.basics.lpntype.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 承运商管理删除接收类
 **/
@Data
public class DeleteLpnTypeRequest implements Serializable {
	private static final long serialVersionUID = 1968426677200511256L;
	private List<Long> list;
}
