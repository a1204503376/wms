package org.nodes.wms.core.basedata.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author pengwei
 * @Date 2021/4/8 12:14
 * @Description 包装导出模板
 */
@Data
public class SkuPackageExcel {
	@ColumnWidth(15)
	@ExcelProperty(value = "包装名称")
	private String wspName;
	/**
	 * 托盘每层箱数
	 */
	@ColumnWidth(15)
	@ExcelProperty(value = "托盘每层箱数")
	private BigDecimal palletBoxLevel;
	/**
	 * 每托盘层数
	 */
	@ColumnWidth(15)
	@ExcelProperty(value = "每托盘层数")
	private BigDecimal palletLevel;
	/**
	 * 每托盘重量
	 */
	@ColumnWidth(15)
	@ExcelProperty(value = "每托盘重量")
	private BigDecimal lpnWeight;
	/**
	 * 每托盘高度
	 */
	@ColumnWidth(15)
	@ExcelProperty(value = "每托盘高度")
	private BigDecimal lpnHeight;
	/**
	 * 每托盘长度
	 */
	@ColumnWidth(15)
	@ExcelProperty(value = "每托盘长度")
	private BigDecimal lpnLength;
	/**
	 * 每托盘宽度
	 */
	@ColumnWidth(15)
	@ExcelProperty(value = "每托盘宽度")
	private BigDecimal lpnWidth;
	/**
	 * 包装规格
	 */
	@ColumnWidth(15)
	@ExcelProperty("包装规格")
	private String spec;


	@ColumnWidth(15)
	@ExcelProperty({"明细", "计量单位编码"})
	private String wsuCode;

	@ColumnWidth(15)
	@ExcelProperty({"明细", "计量单位名称"})
	private String wsuName;

	@ColumnWidth(15)
	@ExcelProperty({"明细", "层级"})
	private String skuLevelName;

	/**
	 * 换算倍数
	 */
	@ColumnWidth(15)
	@ExcelProperty({"明细", "换算倍数"})
	private Integer convertQty;
	/**
	 * 重量
	 */
	@ColumnWidth(15)
	@ExcelProperty({"明细", "重量"})
	private BigDecimal lpnWeight1;
	/**
	 * 长度
	 */
	@ColumnWidth(15)
	@ExcelProperty({"明细", "长度"})
	private BigDecimal lpnLength1;
	/**
	 * 宽度
	 */
	@ColumnWidth(15)
	@ExcelProperty({"明细",  "宽度"})
	private BigDecimal lpnWidth1;
	/**
	 * 高度
	 */
	@ColumnWidth(15)
	@ExcelProperty({"明细",  "高度"})
	private BigDecimal lpnHeight1;
	/**
	 * RFID筛选值
	 */
	@ColumnWidth(15)
	@ExcelProperty({"明细",  "RFID筛选值"})
	private String filterValue;
	/**
	 * RFID指示符位数
	 */
	@ColumnWidth(15)
	@ExcelProperty({"明细",  "RFID指示符位数"})
	private BigDecimal indicatorDigit;
	/**
	 * 物品规格
	 */
	@ColumnWidth(15)
	@ExcelProperty({"明细",  "物品规格"})
	private String skuSpec;
	/**
	 * 皮重
	 */
	@ColumnWidth(15)
	@ExcelProperty({"明细",  "皮重"})
	private String attribute2;
	/**
	 * 总重
	 */
	@ColumnWidth(15)
	@ExcelProperty({"明细",  "总重"})
	private String attribute3;

}
