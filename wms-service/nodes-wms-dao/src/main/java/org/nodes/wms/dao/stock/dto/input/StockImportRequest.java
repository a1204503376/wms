package org.nodes.wms.dao.stock.dto.input;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存余额导入Excel模板类
 */
@Data
@ContentRowHeight(-1)
@HeadFontStyle(color = 0)
public class StockImportRequest implements Serializable {
	private static final long serialVersionUID = 1797880308486744488L;
	@ColumnWidth(15)
	@HeadFontStyle(color = 2)
	@ExcelProperty({"*物料编码"})
	private String skuCode;
	@ColumnWidth(20)
	@ExcelProperty({"库存状态\n 0:正常,1:冻结"})
	private Integer stockStatus;
	@ColumnWidth(15)
	@HeadFontStyle(color = 2)
	@ExcelProperty({"*库存数量"})
	private BigDecimal stockQty;
	@ColumnWidth(20)
	@HeadFontStyle(color = 2)
	@ExcelProperty({"*计量单位编码"})
	private String wsuCode;
	@ColumnWidth(20)
	@ExcelProperty({"箱码"})
	private String boxCode;
	@ColumnWidth(15)
	@ExcelProperty({"托盘号"})
	private String lpnCode;
	@ColumnWidth(15)
	@HeadFontStyle(color = 2)
	@ExcelProperty({"*库位编码"})
	private String locCode;
	@ColumnWidth(15)
	@HeadFontStyle(color = 2)
	@ExcelProperty({"*库房编码"})
	private String whCode;
	@ColumnWidth(45)
	@ExcelProperty({"序列号(以,分割)"})
	private String snCode;
	@ColumnWidth(15)
	@HeadFontStyle(color = 2)
	@ExcelProperty({"*货主编码"})
	private String ownerCode;
	@ColumnWidth(15)
	@ExcelProperty({"供应商编码"})
	private String supplierCode;
	@ColumnWidth(15)
	@ExcelProperty({"批属性1"})
	private String skuLot1;
	@ColumnWidth(15)
	@ExcelProperty({"批属性2"})
	private String skuLot2;
	@ColumnWidth(15)
	@ExcelProperty({"批属性3"})
	private String skuLot3;
	@ColumnWidth(15)
	@ExcelProperty({"批属性4"})
	private String skuLot4;
	@ColumnWidth(15)
	@ExcelProperty({"批属性5"})
	private String skuLot5;
	@ColumnWidth(15)
	@ExcelProperty({"批属性6"})
	private String skuLot6;
	@ColumnWidth(15)
	@ExcelProperty({"批属性7"})
	private String skuLot7;
	@ColumnWidth(15)
	@ExcelProperty({"批属性8"})
	private String skuLot8;
	@ColumnWidth(15)
	@ExcelProperty({"批属性9"})
	private String skuLot9;
	@ColumnWidth(15)
	@ExcelProperty({"批属性10"})
	private String skuLot10;
	@ColumnWidth(15)
	@ExcelProperty({"批属性11"})
	private String skuLot11;
	@ColumnWidth(15)
	@ExcelProperty({"批属性12"})
	private String skuLot12;
	@ColumnWidth(15)
	@ExcelProperty({"批属性13"})
	private String skuLot13;
	@ColumnWidth(15)
	@ExcelProperty({"批属性14"})
	private String skuLot14;
	@ColumnWidth(15)
	@ExcelProperty({"批属性15"})
	private String skuLot15;
	@ColumnWidth(15)
	@ExcelProperty({"批属性16"})
	private String skuLot16;
	@ColumnWidth(15)
	@ExcelProperty({"批属性17"})
	private String skuLot17;
	@ColumnWidth(15)
	@ExcelProperty({"批属性18"})
	private String skuLot18;
	@ColumnWidth(15)
	@ExcelProperty({"批属性19"})
	private String skuLot19;
	@ColumnWidth(15)
	@ExcelProperty({"批属性20"})
	private String skuLot20;
	@ColumnWidth(15)
	@ExcelProperty({"批属性21"})
	private String skuLot21;
	@ColumnWidth(15)
	@ExcelProperty({"批属性22"})
	private String skuLot22;
	@ColumnWidth(15)
	@ExcelProperty({"批属性23"})
	private String skuLot23;
	@ColumnWidth(15)
	@ExcelProperty({"批属性24"})
	private String skuLot24;
	@ColumnWidth(15)
	@ExcelProperty({"批属性25"})
	private String skuLot25;
	@ColumnWidth(15)
	@ExcelProperty({"批属性26"})
	private String skuLot26;
	@ColumnWidth(15)
	@ExcelProperty({"批属性27"})
	private String skuLot27;
	@ColumnWidth(15)
	@ExcelProperty({"批属性28"})
	private String skuLot28;
	@ColumnWidth(15)
	@ExcelProperty({"批属性29"})
	private String skuLot29;
	@ColumnWidth(15)
	@ExcelProperty({"批属性30"})
	private String skuLot30;
	@ColumnWidth(15)
	@ExcelProperty({"自定义字段1"})
	private String udf1;
	@ColumnWidth(15)
	@ExcelProperty({"自定义字段2"})
	private String udf2;
	@ColumnWidth(15)
	@ExcelProperty({"自定义字段3"})
	private String udf3;
	@ColumnWidth(15)
	@ExcelProperty({"自定义字段4"})
	private String udf4;
	@ColumnWidth(15)
	@ExcelProperty({"自定义字段5"})
	private String udf5;
}
