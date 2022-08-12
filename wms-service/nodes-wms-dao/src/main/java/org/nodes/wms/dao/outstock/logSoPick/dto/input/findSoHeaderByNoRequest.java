package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import lombok.Data;
import org.nodes.wms.dao.outstock.so.enums.SoBillStateEnum;

import java.io.Serializable;
import java.util.List;

/**
 * 分页参数
 **/
@Data
public class findSoHeaderByNoRequest implements Serializable {
	private static final long serialVersionUID = -2328658218530094518L;
    private String no;
	private String whId;
	private List<SoBillStateEnum> soBillState;
}
