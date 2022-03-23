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
public class SkuTypeExcel {
	@ColumnWidth(15)
	@ExcelProperty(value = "分类名称")
	private String typeName;

	@ColumnWidth(15)
	@ExcelProperty(value = "分类编码")
	private String typeCode;

	@ColumnWidth(15)
	@ExcelProperty(value = "货主编码")
	private String woCode;

	@ColumnWidth(15)
	@ExcelProperty(value = "货主名称")
	private String woName;

	@ColumnWidth(15)
	@ExcelProperty(value = "上级分类")
	private String typePreName;

	@ColumnWidth(15)
	@ExcelProperty(value = "绩效系数")
	private BigDecimal gradeNum;

	@ColumnWidth(15)
	@ExcelProperty(value = "备注")
	private String typeRemark;

}
