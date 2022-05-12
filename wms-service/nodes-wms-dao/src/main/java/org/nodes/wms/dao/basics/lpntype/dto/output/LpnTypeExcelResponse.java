package org.nodes.wms.dao.basics.lpntype.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 容器管理返回对象
 */
@Data
public class LpnTypeExcelResponse implements Serializable {
	private static final long serialVersionUID = -5339953896430012558L;
	/**
	 * 容器类型编码
	 */
	@ColumnWidth(15)
	@ExcelProperty({"容器类型编码"})
	private String code;
	/**
	 * 容器类型(1:箱,2:托)
	 */
	@ColumnWidth(15)
	@ExcelProperty({"容器类型"})
	private String type;
	/**
	 * 容器编码生成规则
	 */
	@ColumnWidth(15)
	@ExcelProperty({"容器编码生成规则"})
	private String lpnNoRule;
	/**
	 * 容器重量(KG)
	 */
	@ColumnWidth(15)
	@ExcelProperty({"容器重量"})
	private BigDecimal weight;
	/**
	 * 创建人
	 */
	@ColumnWidth(15)
	@ExcelProperty({"创建人"})
	private String createUser;
	/**
	 * 创建时间
	 */
	@ColumnWidth(15)
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
	@ColumnWidth(15)
	@ExcelProperty({"更新时间"})
	private Date updateTime;

}
