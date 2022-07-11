package org.nodes.wms.dao.basics.bom.dto.input;

import lombok.Data;

import java.io.Serializable;

/**
 * 物料清单查询详情请求对象
 */
@Data
public class FindSkuBomByIdRequset implements Serializable {
	private static final long serialVersionUID = 5307366094033593891L;
	/**
	 * 主键id
	 */
	private Long id;
}
