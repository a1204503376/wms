package org.nodes.wms.dao.outstock.so.dto.output;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 出库单导出Excel响应类
 **/
@Data
@ColumnWidth(15)
public class SoHeaderExcelResponse implements Serializable {

	private static final long serialVersionUID = 7539146158533443755L;

	/**
	 * 发货单编码
	 */
	@ExcelProperty("发货单编码")
	private String soBillNo;

	/**
	 * 上游编码
	 */
	@ExcelProperty("上游编码")
	private String orderNo;

	/**
	 * 单据类型名称
	 */
	@ExcelProperty("单据类型")
	private String billTypeName;

	/**
	 * 单据状态
	 */
	@ExcelProperty("单据状态")
	@ExcelIgnore
	private String soBillState;

	/**
	 * 客户编码
	 */
	@ExcelProperty("客户编码")
	private String customerCode;

	/**
	 * 客户名称
	 */
	@ExcelProperty("客户名称")
	private String customerName;

	/**
	 * 库房名称
	 */
	@ExcelProperty("库房名称")
	private String whName;

	/**
	 * 创建时间
	 */
	@ExcelProperty("创建时间")
	private Date createTime;

	/**
	 * 创建人
	 */
	@ExcelProperty("创建人")
	private String createUser;

	/**
	 * 发货单备注
	 */
	@ExcelProperty("发货单备注")
	private String soBillRemark;
}
