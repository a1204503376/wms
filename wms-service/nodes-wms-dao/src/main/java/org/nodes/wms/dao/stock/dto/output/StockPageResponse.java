package org.nodes.wms.dao.stock.dto.output;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存分页返回前端response
 */
@Data
public class StockPageResponse implements Serializable {
	private static final long serialVersionUID = -4376432991216164312L;
	/**
	 * 库存主键id
	 */
	@ExcelIgnore
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 物品编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "物料编码"})
	private String skuCode;
	/**
	 * 库存状态
	 */
	@ExcelIgnore
	private StockStatusEnum stockStatus;
	/**
	 * 库存状态导出字段
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "库存状态"})
	private String stockStatusDesc;
	/**
	 * 生产批次
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "生产批次"})
	private String skuLot1;
	/**
	 * 上架数量
	 */
	@ExcelIgnore
	private BigDecimal stockQty;
	/**
	 * 下架数量
	 */
	@ExcelIgnore
	private BigDecimal pickQty;
	/**
	 * 占用数量
	 */
	@ExcelProperty
	private BigDecimal occupyQty;

	/**
	 * 库存余额
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "库存余额"})
	private BigDecimal stockBalance;
	/**
	 * 库存可用
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "库存可用"})
	private BigDecimal stockEnable;
	/**
	 * 计量单位编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "计量单位编码"})
	private String wsuCode;
	/**
	 * 库位编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "库位编码"})
	private String locCode;
	/**
	 * 库区编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "库区编码"})
	private String zoneCode;

	/**
	 * 箱号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "箱码"})
	private String boxCode;
	/**
	 * 托盘号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "托盘号"})
	private String lpnCode;
	/**
	 * 规格型号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "规格"})
	private String skuLot2;
	/**
	 * 收货日期
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "收货日期"})
	private String skuLot3;
	/**
	 * 专用客户
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "专用客户"})
	private String skuLot4;
	/**
	 * 钢背批次
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "钢背批次"})
	private String skuLot5;
	/**
	 * 摩擦块批次
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "摩擦块批次"})
	private String skuLot6;
	/**
	 * 产品标识代码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "产品标识"})
	private String skuLot7;
	/**
	 * CRCC
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "CRCC"})
	private String skuLot8;
	/**
	 * 库房ID
	 */
	@ExcelProperty
	private Long whId;
	/**
	 * 库房编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "库房编码"})
	private String whCode;
	/**
	 * 货主ID
	 */
	@ExcelProperty
	private Long woId;
	/**
	 * 货主编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "货主编码"})
	private String ownerCode;
	/**
	 * 货主名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "货主名称"})
	private String ownerName;
	/**
	 * 任务号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "任务号"})
	private String taskId;
	/**
	 * 最近入库时间(库存数量增加时更新)
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "入库时间"})
	private Date lastInTime;
	/**
	 * 最近出库时间(下架数量增加时更新)
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "出库时间"})
	private Date lastOutTime;

}