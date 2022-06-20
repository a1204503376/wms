package org.nodes.wms.dao.system.updateVer.dto.output;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 系统版本分页查询请求dto类
 **/
@Data
public class UpdateVerPageResponse implements Serializable {

	private static final long serialVersionUID = -2602127440080436011L;

	/**
	 * 系统版本id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ExcelIgnore
	private Long suvId;

	/**
	 * 版本号数值
	 */
	@ExcelProperty("版本号数值")
	@ColumnWidth(15)
	private String verNum;

	/**
	 * 版本号名称
	 */
	@ExcelProperty("版本号名称")
	@ColumnWidth(15)
	private String verName;

	/**
	 * 更新地址
	 */
	@ExcelProperty("更新地址")
	@ColumnWidth(50)
	private String updateUrl;

}
