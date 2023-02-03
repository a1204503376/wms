package org.nodes.wms.dao.stock.dto.output;

import lombok.Data;

import java.io.Serializable;

/**
 * 复核页面请求对象
 **/
@Data
public class FindBoxCountResponse implements Serializable {
	private static final long serialVersionUID = -860868604598028351L;
	/**
	 * 拣货计划总数
	 */
	private Integer pickPlanQty;

	/**
	 * 已经拣货量
	 */
	private Integer pickedQty;

	/**
	 * 已经复核量
	 */
	private Integer reviewedQty;
}
