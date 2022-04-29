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
	 * 货主名称
	 */
	@ColumnWidth(16)
	@ExcelProperty({"货主名称"})
	private String  ownerName;
	/**
	 * 备注
	 */
	@ColumnWidth(15)
	@ExcelProperty({"备注"})
	private String  remark;
	/**
	 * 创建人
	 */
	@ColumnWidth(15)
	@ExcelProperty({"创建人"})
	private String createUser;
	/**
	 * 创建时间
	 */
	@ColumnWidth(18)
	@ExcelProperty({"创建时间"})
	private Date createTime;
	/**
	 * 更新人
	 */
	@ColumnWidth(15)
	@ExcelProperty({"更新人"})
	private String updateUser;
	/**
	 * 更新时间
	 */
	@ColumnWidth(18)
	@ExcelProperty({"更新时间"})
	private Date updateTime;

	/**
	 * 业务状态
	 */
	@ColumnWidth(15)
	@ExcelProperty({"业务状态"})
	private String status;
}
