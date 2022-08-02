package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;

import java.util.List;

/**
 * 库存按序列号显示查询dto
 */
@Data
public class StockBySerialPageQuery {
	/**
	 * 物品编码
	 */
	private List<Long> skuIds;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 生产批次
	 */
	private String skuLot1;
	/**
	 * 库位id集合
	 */
	private List<Long> locIdList;
	/**
	 * 序列号
	 */
	private String serial;
	/**
	 * 序列号范围开始
	 */
	private String serialBegin;
	/**
	 * 序列号范围结束
	 */
	private String serialEnd;
}







