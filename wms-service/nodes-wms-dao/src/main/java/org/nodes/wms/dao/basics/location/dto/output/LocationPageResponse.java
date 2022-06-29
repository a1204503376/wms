package org.nodes.wms.dao.basics.location.dto.output;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 库位分页查询数据返回dto
 **/
@Data
@ColumnWidth(15)
public class LocationPageResponse implements Serializable {

	private static final long serialVersionUID = 1943526496365753566L;

	/**
	 * 库位主键id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ExcelIgnore
	private Long locId;

	/**
	 * 库位编码
	 */
	@ExcelProperty("库位编码")
	private String locCode;

	/**
	 * 库房名称
	 */
	@ExcelProperty("库房")
	private String whName;

	/**
	 * 库区名称
	 */
	@ExcelProperty("库区")
	private String zoneName;

	/**
	 * 库区类型名称
	 */
	@ExcelProperty("库位类型")
	private String locType;

	/**
	 * 库位种类名称
	 */
	@ExcelProperty("库位种类")
	private String locCategory;

	/**
	 * 库位处理名称
	 */
	@ExcelProperty("库位处理")
	private String locHandling;

	/**
	 * 使用状态
	 */
	@ExcelProperty("使用状态")
	private String locFlag;

	/**
	 * abc分类名称
	 */
	@ExcelProperty("ABC分类")
	private String abc;

	/**
	 * 路线顺序
	 */
	@ExcelProperty("路线顺序")
	private Integer logicAllocation;

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
	 * 适用的容器类型编码
	 */
	@ExcelProperty("适用的容器类型")
	private String lpnTypeCode;

	/**
	 * 是否启用
	 */
	@ExcelProperty("是否启用")
	private String status;
}
