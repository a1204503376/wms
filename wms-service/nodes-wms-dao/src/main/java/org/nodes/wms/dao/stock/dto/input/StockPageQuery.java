package org.nodes.wms.dao.stock.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存分页查询参数dto
 **/
@Data
public class StockPageQuery implements Serializable {
	private static final long serialVersionUID = 1002953447166258080L;
	/**
	 * 物品编码
	 */
	private List<Long> skuIds;
	/**
	 * 生产批次
	 */
	private String skuLot1;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 库存状态
	 */
	private List<Integer> stockStatusList;
	/**
	 * /**
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
	private String skuLot2;
	/**
	 * 收货日期开始
	 */
	private String receiveTimeBegin;
	/**
	 * 收货日期结束
	 */
	private String receiveTimeEnd;
	/**
	 * 专用客户
	 */
	private String skuLot4;
	/**
	 * 钢背批次
	 */
	private String skuLot5;
	/**
	 * 摩擦块批次
	 */
	private String skuLot6;
	/**
	 * 产品标识代码
	 */
	private String skuLot7;
	/**
	 * CRCC
	 */
	private String skuLot8;
	/**
	 * 库房编码
	 */
	private List<Long> whIdList;
	/**
	 * 货主
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;
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
	/**
	 * 是否按箱显示
	 */
	private Boolean isShowByBox;
}
