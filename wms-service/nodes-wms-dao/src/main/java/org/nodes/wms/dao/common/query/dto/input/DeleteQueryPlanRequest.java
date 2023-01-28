package org.nodes.wms.dao.common.query.dto.input;


import lombok.Data;

/**
 * @author nodes
 * 删除查询方案Dto
 */
@Data
public class DeleteQueryPlanRequest {
	/**
	 * 被删除的ID
	 */
	private Long id;
}
