package org.nodes.wms.core.instock.asn.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
/**
 * @Author wangYuNan
 * @Date 2021/7/1 13:14
 * @Description 序列号导出模板
 */
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SnExcel {
	/**
	 * 入库单编号
	 */
	@NotBlank(message = "入库单编号不能为空！")
	@ColumnWidth(15)
	@ExcelProperty("入库单编号")
	private String asnBillNo;
	/**
	 * 行号
	 */
	@NotBlank(message = "入库单编号不能为空！")
	@ColumnWidth(15)
	@ExcelProperty("行号")
	private String asnLineNo;
	/**
	 * 分组编号
	 */
	@ColumnWidth(15)
	@ExcelProperty("分组编号")
	private String groupNum;
	/**
	 * 序列号
	 */
	@NotBlank(message = "入库单编号不能为空！")
	@ColumnWidth(15)
	@ExcelProperty("序列号")
	private String snDetailCode;

}
