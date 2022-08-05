package org.nodes.wms.dao.stock.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import org.nodes.wms.dao.common.esayExcel.EnumConverter;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 库存按序列号显示返回前端分页dto
 */
@Data
public class StockBySerialPageResponse implements Serializable {
	private static final long serialVersionUID = 6196607591505436862L;
	/**
	 * 序列号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "序列号"})
	private String serialNumber;
	/**
	 * 物品编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "物品编码"})
	private String skuCode;
	/**
	 * 库存状态
	 */
	@ColumnWidth(15)
	@ExcelProperty(value = {"库存信息", "库存状态"}, converter = EnumConverter.class)
	private StockStatusEnum stockStatus;


	/**
	 * 生产批次
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "生产批次"})
	private String skuLot1;
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
	 * 库房编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存信息", "库房编码"})
	private String whCode;
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
