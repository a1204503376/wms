package org.nodes.wms.dao.putaway.dto.input;

import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;

import java.io.Serializable;
import java.util.List;

/**
 * 上架记录查询请求对象
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
	private List<Long> skuIdList;

	/**
	 * 目标库位编码
	 */
	private String targetLocCode;

	/**
	 * 生产批次
	 */
	private String skuLot1;

	/**
	 * 规格型号
	 */
	private String skuLot2;
}
