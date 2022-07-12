package org.nodes.wms.dao.stock.dto.input;

import lombok.Data;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 库存分页查询参数dto
 **/
@Data
public class StockPageQuery implements Serializable {
	private static final long serialVersionUID = 1002953447166258080L;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 生产批次
	 */
	private String SkuLot1;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 库存状态
	 */
	private StockStatusEnum stockStatus;
	/**
	 * 库区编码
	 */
	private String zoneCode;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 托盘号
	 */
	private String lpnCode;
	/**
	 * 规格型号
	 */
	private String SkuLot2;
	/**
	 * 收货日期
	 */
	private String SkuLot3;
	/**
	 * 专用客户
	 */
	private String SkuLot4;
	/**
	 * 钢背批次
	 */
	private String SkuLot5;
	/**
	 * 摩擦块批次
	 */
	private String SkuLot6;
	/**
	 * 产品标识代码
	 */
	private String SkuLot7;
	/**
	 * CRCC
	 */
	private String SkuLot8;
	/**
	 * 库房编码
	 */
	private String whCode;
	/**
	 * 货主编码
	 */
	private Long ownerCode;
	/**
	 * 最近入库时间开始
	 */
	private LocalDateTime lastInTimeBegin;
	/**
	 * 最近入库时间结束
	 */
	private LocalDateTime lastInTimeEnd;
	/**
	 * 最近出库时间开始
	 */
	private LocalDateTime lastOutTimeBegin;
	/**
	 * 最近出库时间结束
	 */
	private LocalDateTime lastOutTimeEnd;
	/**
	 * 任务号
	 */
	private String taskId;
}
