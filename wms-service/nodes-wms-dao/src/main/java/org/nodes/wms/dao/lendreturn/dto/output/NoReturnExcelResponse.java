package org.nodes.wms.dao.lendreturn.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 未归还列表导出Excel类
 */
@Data
@ColumnWidth(15)
public class NoReturnExcelResponse {

	/**
	 * 借出归还人姓名
	 */
	@ExcelProperty("借出人姓名")
	private String lendReturnName;

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
	 * 借出数量
	 */
	@ExcelProperty("借出数量")
	private BigDecimal lendQty;

	/**
	 * 归还数量
	 */
	@ExcelProperty("归还数量")
	private BigDecimal returnQty;

	/**
	 * 未归还的数量
	 */
	@ExcelProperty("未归还的数量")
	@JsonSerialize
	public BigDecimal noReturnQty;

	public BigDecimal getNoReturnQty() {
		return lendQty.subtract(returnQty);
	}

	public void setNoReturnQty(BigDecimal noReturnQty) {
		this.noReturnQty = noReturnQty;
	}

	/**
	 * 计量单位编码
	 */
	@ExcelProperty("计量单位编码")
	private String wsuCode;

	/**
	 * 计量单位名称
	 */
	@ExcelProperty("计量单位名称")
	private String wsuName;

	/**
	 * 序列号
	 */
	@ExcelProperty("序列号")
	private String snCode;

	@ExcelProperty("生产批次")
	private String skuLot1;

	@ExcelProperty("规格型号")
	private String skuLot2;
}
