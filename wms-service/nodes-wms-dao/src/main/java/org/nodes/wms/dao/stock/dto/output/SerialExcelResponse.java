package org.nodes.wms.dao.stock.dto.output;

/**
 * 序列号导出Excel响应类
 **/

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import org.nodes.wms.dao.common.esayExcel.EnumConverter;
import org.nodes.wms.dao.stock.enums.SerialStateEnum;

import java.io.Serializable;

@Data
@ColumnWidth(15)
public class SerialExcelResponse implements Serializable {

	private static final long serialVersionUID = -843444599029741997L;

	/**
	 * 序列号id
	 */
	@ExcelIgnore
	private Long serialId;

	/**
	 * 序列号
	 */
	@ExcelProperty("序列号")
	private String serialNumber;

	/**
	 * 序列号状态
	 */
	@ExcelProperty(value = "序列号状态", converter = EnumConverter.class)
	private SerialStateEnum serialState;

	/**
	 * 入库次数
	 */
	@ExcelProperty("入库次数")
	private String instockNumber;

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
	 * 箱码
	 */
	@ExcelProperty("箱码")
	private String boxCode;

	/**
	 * lpn编码
	 */
	@ExcelProperty("lpn编码")
	private String lpnCode;
}
