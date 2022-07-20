package org.nodes.wms.dao.picking.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页参数
 **/
@Data
public class FindAllPickingRequest implements Serializable {
	private static final long serialVersionUID = -2328658218530094518L;
    private String no;
	private String whId;
	private Integer billDetailState;
}
