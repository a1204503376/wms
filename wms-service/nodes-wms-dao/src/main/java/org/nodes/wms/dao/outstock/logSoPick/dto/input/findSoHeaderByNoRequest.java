package org.nodes.wms.dao.outstock.logSoPick.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页参数
 **/
@Data
public class findSoHeaderByNoRequest implements Serializable {
	private static final long serialVersionUID = -2328658218530094518L;
    private String no;
	private String whId;
	private Integer billDetailState;
}
