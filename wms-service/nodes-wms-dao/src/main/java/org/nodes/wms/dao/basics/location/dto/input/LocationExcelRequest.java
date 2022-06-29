package org.nodes.wms.dao.basics.location.dto.input;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import lombok.Data;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库位 导入模板类
 **/
@Data
@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND,
	fillForegroundColor = 44)
@HeadFontStyle(fontName="等线" ,bold = false, fontHeightInPoints=16)
@HeadRowHeight(21)
@ColumnWidth(21)
public class LocationExcelRequest implements Serializable {

	private static final long serialVersionUID = 2982364877515483353L;

	/**
	 * 库位编码
	 */
	@ColumnWidth(21)
	@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND,
		fillForegroundColor = 10)
	@ExcelProperty("库位编码")
	private String locCode;

	/**
	 * 库房编码
	 */
	@ColumnWidth(21)
	@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND,
		fillForegroundColor = 10)
	@ExcelProperty("库房编码")
	private String whCode;

	/**
	 * 库区编码
	 */
	@ColumnWidth(21)
	@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND,
		fillForegroundColor = 10)
	@ExcelProperty("库区编码")
	private String zoneCode;

	/**
	 * 应用类型
	 */
	@ExcelProperty("应用类型")
	private Integer locType;

	/**
	 * 库位种类
	 */
	@ExcelProperty("库位种类")
	private Integer locCategory;

	/**
	 * 库位处理
	 */
	@ExcelProperty("库位处理")
	private Integer locHandling;

	/**
	 * 校验位数
	 */
	@ExcelProperty("校验数位")
	private String checkDigit;

	/**
	 * ABC分类
	 */
	@ExcelProperty("ABC分类")
	private Integer abc;

	/**
	 * 使用状态
	 */
	@ExcelProperty("使用状态")
	private Integer locFlag;

	/**
	 * 路线顺序
	 */
	@ExcelProperty("路线顺序")
	private Integer logicAllocation;

	/**
	 * 长
	 */
	@ExcelProperty("长")
	private BigDecimal locLength;

	/**
	 * 宽
	 */
	@ExcelProperty("宽")
	private BigDecimal locWide;

	/**
	 * 高
	 */
	@ExcelProperty("高")
	private BigDecimal locHigh;

	/**
	 * 货架层
	 */
	@ExcelProperty("货架层")
	private String locLevel;

	/**
	 * 货架列
	 */
	@ExcelProperty("货架列")
	private String locColumn;

	/**
	 * 货架排
	 */
	@ExcelProperty("货架排")
	private String locBank;

	/**
	 * 上架顺序
	 */
	@ExcelProperty("上架顺序")
	private Integer putOrder;

	/**
	 * 适用的容器类型
	 */
	@ExcelProperty("适用的容器类型")
	private String lpnTypeCode;

	/**
	 * 容量
	 */
	@ExcelProperty("容量")
	private BigDecimal capacity;

	/**
	 * 载重量
	 */
	@ExcelProperty("载重量")
	private BigDecimal loadWeight;

	/**
	 * 最大存放件数
	 */
	@ExcelProperty("最大存放件数")
	private Integer itemNum;

	/**
	 * 最大存放托数
	 */
	@ExcelProperty("最大存放托数")
	private Integer trayNum;

	/**
	 * 混放货品
	 */
	@ExcelProperty("混放货品")
	private String locSkuMix;

	/**
	 * 混放批号
	 */
	@ExcelProperty("混放批号")
	private String locLotNoMix;

	/**
	 * 是否启用
	 */
	@ColumnWidth(21)
	@HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND,
		fillForegroundColor = 10)
	@ExcelProperty("是否启用")
	private Integer status;
}
