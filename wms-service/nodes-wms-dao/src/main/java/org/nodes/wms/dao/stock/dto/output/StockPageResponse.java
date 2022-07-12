package org.nodes.wms.dao.stock.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存分页返回前端response
 */
@Data
public class StockPageResponse implements Serializable {
	private static final long serialVersionUID = -4376432991216164312L;
	/**
	 * 库存主键id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 物品编码
	 */
	private String skuCode;
	/**
	 * 库存状态
	 */
	private StockStatusEnum stockStatus;
	/**
	 * 生产批次
	 */
	private String SkuLot1;
	/**
	 * 上架数量
	 */
	private BigDecimal stockQty;
	/**
	 * 下架数量
	 */
	private BigDecimal pickQty;
	/**
	 * 占用数量
	 */
	private BigDecimal occupyQty;

	/**
	 * 库存余额
	 */
	private BigDecimal stockBalance;
	/**
	 * 库存可用
	 */
	private BigDecimal stockEnable;
	/**
	 * 计量单位编码
	 */
	private String wsuCode;
	/**
	 * 库位编码
	 */
	private String locCode;
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
	 * 库房ID
	 */
	private Long whId;
	/**
	 * 库房编码
	 */
	private String whCode;
	/**
	 * 货主ID
	 */
	private Long woId;
	/**
	 * 货主编码
	 */
	private Long ownerCode;
	/**
	 * 货主名称
	 */
	private Long ownerName;
	/**
	 * 最近入库时间(库存数量增加时更新)
	 */
	private LocalDateTime lastInTime;
	/**
	 * 最近出库时间(下架数量增加时更新)
	 */
	private LocalDateTime lastOutTime;
	/**
	 * 任务号
	 */
	private String taskId;
}
