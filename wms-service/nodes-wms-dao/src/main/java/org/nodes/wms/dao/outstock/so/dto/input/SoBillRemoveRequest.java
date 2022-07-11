package org.nodes.wms.dao.outstock.so.dto.input;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 发货单删除请求类
 **/
@Data
public class SoBillRemoveRequest implements Serializable {

	private static final long serialVersionUID = -6114305364454272380L;

	private List<Long> idList;
}
