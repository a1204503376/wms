package org.nodes.wms.dao.basics.carriers.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 承运商返回前端视图类
 **/
@Data
public class CarrierExcelResponse implements Serializable {

	private static final long serialVersionUID = -7014238386876740344L;
	/**
	 * 承运商ID
	 */
	@ColumnWidth(15)
	@ExcelProperty({"承运商表ID"})
	private Long id;
	/**
	 * 承运商编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"承运商编码"})
	private String code;
	/**
	 * 承运商名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"承运商名称"})
	private String name;
	/**
	 * 承运商简称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"承运商简称"})
	private String  simpleName;
	/**
	 * 货主ID
	 */
	private String  woId;
	/**
	 * 备注
	 */
	@ColumnWidth(15)
	@ExcelProperty({"备注"})
	private String  remark;
	/**
	 * 业务状态
	 */
	private Integer status;
}
