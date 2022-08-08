package org.nodes.wms.dao.stock.dto.output;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.nodes.wms.dao.common.esayExcel.EnumConverter;
import org.nodes.wms.dao.stock.enums.SerialStateEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 序列号分页查询请求类
 **/
@Data
@ColumnWidth(15)
public class SerialLogExcelResponse implements Serializable {

	private static final long serialVersionUID = 1045322215797138984L;

	/**
	 * 序列号id
	 */
	@ExcelIgnore
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wlsnlId;

	/**
	 * 序列号
	 */
	@ExcelProperty("序列号")
	private String serialNumber;

	/**
	 * 物品编码
	 */
	@ExcelProperty("物品编码")
	private String skuCode;

	/**
	 * 物品名称
	 */
	@ExcelProperty("物品名称")
	private String skuName;

	/**
	 * 库位
	 */
	@ExcelProperty("库位")
	private String locCode;

	/**
	 * 箱码
	 */
	@ExcelProperty("箱码")
	private String boxCode;

	/**
	 * lpn编码
	 */
	@ExcelProperty("lpn编码")
	private String lpnCode;

	/**
	 * 批次
	 */
	@ExcelProperty("批次")
	private String lotNumber;

	/**
	 * 入库日期
	 */
	@ExcelProperty("入库日期")
	@ColumnWidth(20)
	private Date createTime;

	/**
	 * 序列号状态
	 */
	@ExcelProperty(value = "序列号状态", converter = EnumConverter.class)
	private SerialStateEnum serialState;
}
