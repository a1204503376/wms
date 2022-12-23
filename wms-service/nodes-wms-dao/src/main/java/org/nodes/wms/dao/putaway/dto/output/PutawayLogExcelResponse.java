package org.nodes.wms.dao.putaway.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("putaway_log")
@ColumnWidth(15)
public class PutawayLogExcelResponse {
	private static final long serialVersionUID = 1248280286386004947L;

	/**
	 * 容器编码
	 */
	@ExcelProperty("容器编码")
	private String lpnCode;

	/**
	 * 目标库位编码
	 */
	@ExcelProperty("目标库位编码")
	private String targetLocCode;

	/**
	 * 用户编码
	 */
	@ExcelProperty("用户编码")
	private String userCode;

	/**
	 * 用户名称
	 */
	@ExcelProperty("用户名称")
	private String userName;

	/**
	 * 执行时间
	 */
	@ExcelProperty("执行时间")
	private Date aplTime;

	/**
	 * 箱码
	 */
	@ExcelProperty("箱码")
	private String boxCode;

	/**
	 * 物品编码
	 */
	@ExcelProperty("物品编码")
	private String skuCode;

	/**
	 * 数量
	 */
	@ExcelProperty("数量")
	private BigDecimal qty;

	/**
	 * 生产批次
	 */
	@ExcelProperty("生产批次")
	private String skuLot1;

	/**
	 * 规格型号
	 */
	@ExcelProperty("规格型号")
	private String skuLot2;
}
