package org.nodes.wms.dao.common.log.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 日志分页列表返货前端视图图
 */
@Data
public class LogActionExcelResponse implements Serializable {

	private static final long serialVersionUID = 8658238171114149673L;

	/**
	 * 操作人员账号
	 */
	@ColumnWidth(15)
	@ExcelProperty({"操作人员账号"})
	private String userAccount;
	/**
	 * 操作人员真实名称
	 */
	@ColumnWidth(15)
	@ExcelProperty({"操作人员真实名称"})
	private String userRealName;
	/**
	 * 操作类型
	 */
	@ColumnWidth(15)
	@ExcelProperty({"操作类型"})
	private String type;
	/**
	 * 目标单据id,可能为空
	 */
	@ColumnWidth(15)
	@ExcelProperty({"单据ID"})
	private Long billId;
	/**
	 * 目标单据编码,可能为空
	 */
	@ColumnWidth(15)
	@ExcelProperty({"单据编码"})
	private String billNo;
	/**
	 * 操作内容
	 */
	@ColumnWidth(15)
	@ExcelProperty({"操作内容"})
	private String log;
	/**
	 * 创建时间
	 */
	@ColumnWidth(15)
	@ExcelProperty({"创建时间"})
	private Date createTime;
	/**
	 * 创建人
	 */
	@ColumnWidth(15)
	@ExcelProperty({"创建人"})
	private String createUser;
	/**
	 * 更新时间
	 */
	@ColumnWidth(15)
	@ExcelProperty({"更新时间"})
	private Date updateTime;
	/**
	 * 更新人
	 */
	@ColumnWidth(15)
	@ExcelProperty({"更新人"})
	private String updateUser;
}
