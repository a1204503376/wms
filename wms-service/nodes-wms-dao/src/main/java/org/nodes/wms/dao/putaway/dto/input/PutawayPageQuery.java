package org.nodes.wms.dao.putaway.dto.input;

import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;

import java.io.Serializable;

/**
 * 上级记录查询请求对象
 *
 * @author Nodesc
 */
@Data
public class PutawayPageQuery extends BaseSkuLot implements Serializable {
	private static final long serialVersionUID = -1322402255947546514L;
	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 容器编码
	 */
	private String lpnCode;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 目标库位编码
	 */
	private String targetLocCode;
}
