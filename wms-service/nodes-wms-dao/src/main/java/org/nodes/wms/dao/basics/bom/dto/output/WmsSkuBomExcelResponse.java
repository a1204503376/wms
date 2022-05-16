package org.nodes.wms.dao.basics.bom.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分页返回
 */
@Data
public class WmsSkuBomExcelResponse implements Serializable {
	private static final long serialVersionUID = -5115983650541946050L;
	/**
	 * 组合物品编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"组合物品编码"})
	private String joinSkuCode;
	/**
	 * 组合物品名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"组合物品名称"})
	private String joinSkuName;
	/**
	 * 物品编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"物品编码"})
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"物品名称"})
	private String skuName;
	/**
	 * 组合数量
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ColumnWidth(15)
	@ExcelProperty({"组合数量"})
	private BigDecimal qty;
	/**
	 * 主单位编码、物品单位编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"物品单位编码"})
	private String wsuCode;
	/**
	 * 组合物品单位编码 数据库备注组合单位编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"组合物品单位编码"})
	private String joinWsuCode;
	/**
	 * 货主名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"货主名称"})
	private String ownerName;
	/**
	 * 更新时间
	 */
	@ColumnWidth(15)
	@ExcelProperty({"更新时间"})
	private Date updateTime;
	/**
	 * 更新人
	 */
	@ColumnWidth(15)
	@ExcelProperty({"更新人"})
	private String updateUser;
}
