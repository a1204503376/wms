package org.nodes.wms.core.warehouse.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocationExcel {
	/**
	 * 库位编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库位编码"})
	private String locCode;
	/**
	 * 库区编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库区编码"})
	private String zoneCode;
	/**
	 * 库区名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库区名称"})
	private String zoneName;
	/**
	 * 仓库编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库房编码"})
	private String whCode;
	/**
	 * 仓库名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库房名称"})
	private String whName;
	/**
	 * 库位类型
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库位类型"})
	private Integer locType;
	/**
	 * 库位种类
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库位种类"})
	private Integer locCategory;
	/**
	 * 库位处理
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库位处理"})
	private Integer locHandling;
	/**
	 * 分配区
	 */
	@ColumnWidth(15)
	@ExcelProperty({"分配区"})
	private String allocationZone;

	/**
	 * ABC分类
	 */
	@ColumnWidth(15)
	@ExcelProperty({"ABC分类"})
	private Integer abc;

	/**
	 * 状态
	 */
	@ColumnWidth(15)
	@ExcelProperty({"状态"})
	private Integer locFlag;
	/**
	 * 库存差异是否自动冻结货位
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库存差异是否自动冻结货位"})
	private String lockFlag;
	/**
	 * 状态
	 */
	@ColumnWidth(15)
	@ExcelProperty({"状态"})
	private Integer locStatus;
	/**
	 * 路线顺序
	 */
	@ColumnWidth(15)
	@ExcelProperty({"路线顺序"})
	private Integer logicAllocation;
	/**
	 * 长
	 */
	@ColumnWidth(15)
	@ExcelProperty({"长"})
	private BigDecimal locLength;
	/**
	 * 宽
	 */
	@ColumnWidth(15)
	@ExcelProperty({"宽"})
	private BigDecimal locWide;
	/**
	 * 高
	 */
	@ColumnWidth(15)
	@ExcelProperty({"高"})
	private BigDecimal locHigh;
	/**
	 * 货架层
	 */
	@ColumnWidth(15)
	@ExcelProperty({"货架层"})
	private String locLevel;
	/**
	 * X坐标
	 */
	@ColumnWidth(15)
	@ExcelProperty({"X坐标"})
	private Integer xCode;
	/**
	 * Y坐标
	 */
	@ColumnWidth(15)
	@ExcelProperty({"Y坐标"})
	private Integer yCode;
	/**
	 * Z坐标
	 */
	@ColumnWidth(15)
	@ExcelProperty({"Z坐标"})
	private Integer zCode;
	/**
	 * 定方位
	 */
	@ColumnWidth(15)
	@ExcelProperty({"定方位"})
	private String orientation;
	/**
	 * 容量
	 */
	@ColumnWidth(15)
	@ExcelProperty({"容量"})
	private BigDecimal capacity;
	/**
	 * 载重量
	 */
	@ColumnWidth(15)
	@ExcelProperty({"载重量"})
	private BigDecimal loadWeight;
	/**
	 * 最大存放件数
	 */
	@ColumnWidth(15)
	@ExcelProperty({"最大存放件数"})
	private Integer itemNum;
	/**
	 * 最大存放托数
	 */
	@ColumnWidth(15)
	@ExcelProperty({"最大存放托数"})
	private Integer trayNum;
	/**
	 * 库位共享宽度
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库位共享宽度"})
	private String shareWidth;
	/**
	 * 库位共享重量
	 */
	@ColumnWidth(15)
	@ExcelProperty({"库位共享重量"})
	private String shareWeight;
	/**
	 * 混放货品
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放货品"})
	private String locSkuMix;
	/**
	 * 混放批号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批号"})
	private String locLotNoMix;
	/**
	 * 混放批属性1
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性1"})
	private String locLotMix1;
	/**
	 * 混放批属性2
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性2"})
	private String locLotMix2;
	/**
	 * 混放批属性3
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性3"})
	private String locLotMix3;
	/**
	 * 混放批属性4
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性4"})
	private String locLotMix4;
	/**
	 * 混放批属性5
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性5"})
	private String locLotMix5;
	/**
	 * 混放批属性6
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性6"})
	private String locLotMix6;
	/**
	 * 混放批属性7
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性7"})
	private String locLotMix7;
	/**
	 * 混放批属性8
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性8"})
	private String locLotMix8;
	/**
	 * 混放批属性9
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性9"})
	private String locLotMix9;
	/**
	 * 混放批属性10
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性10"})
	private String locLotMix10;
	/**
	 * 混放批属性11
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性11"})
	private String locLotMix11;
	/**
	 * 混放批属性12
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性12"})
	private String locLotMix12;
	/**
	 * 混放批属性13
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性13"})
	private String locLotMix13;
	/**
	 * 混放批属性14
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性14"})
	private String locLotMix14;
	/**
	 * 混放批属性15
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性15"})
	private String locLotMix15;
	/**
	 * 混放批属性16
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性16"})
	private String locLotMix16;
	/**
	 * 混放批属性17
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性17"})
	private String locLotMix17;
	/**
	 * 混放批属性18
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性18"})
	private String locLotMix18;
	/**
	 * 混放批属性19
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性19"})
	private String locLotMix19;
	/**
	 * 混放批属性20
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性20"})
	private String locLotMix20;
	/**
	 * 混放批属性21
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性21"})
	private String locLotMix21;
	/**
	 * 混放批属性22
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性22"})
	private String locLotMix22;
	/**
	 * 混放批属性23
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性23"})
	private String locLotMix23;
	/**
	 * 混放批属性24
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性24"})
	private String locLotMix24;
	/**
	 * 混放批属性25
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性25"})
	private String locLotMix25;
	/**
	 * 混放批属性26
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性26"})
	private String locLotMix26;
	/**
	 * 混放批属性27
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性27"})
	private String locLotMix27;
	/**
	 * 混放批属性28
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性28"})
	private String locLotMix28;
	/**
	 * 混放批属性29
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性29"})
	private String locLotMix29;
	/**
	 * 混放批属性30
	 */
	@ColumnWidth(15)
	@ExcelProperty({"混放批属性30"})
	private String locLotMix30;

}
