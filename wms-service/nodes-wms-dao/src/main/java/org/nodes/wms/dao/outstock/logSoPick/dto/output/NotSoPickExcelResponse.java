package org.nodes.wms.dao.outstock.logSoPick.dto.output;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLot;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 未发货记录响应类
 **/
@Data
@ColumnWidth(15)
public class NotSoPickExcelResponse extends BaseSkuLot implements Serializable {

	private static final long serialVersionUID = -1629114343276457635L;

	/**
	 * 发货单编码
	 */
	@ExcelProperty("发货单编码")
	@ColumnWidth(20)
	private String soBillNo;

	/**
	 * 单据类型
	 */
	@ExcelProperty("单据类型")
	private String billTypeName;

	/**
	 * 上游编码
	 */
	@ExcelProperty("上游编码")
	private String orderNo;

	/**
	 * 行号
	 */
	@ExcelProperty("行号")
	private String soLineNo;

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
	 * 计量单位
	 */
	@ExcelProperty("计量单位")
	private String umName;

	/**
	 * 计划数量
	 */
	@ExcelProperty("计划数量")
	private BigDecimal planQty;

	/**
	 * 实收数量
	 */
	@ExcelProperty("实收数量")
	private BigDecimal scanQty;

	/**
	 * 剩余数量
	 */
	@ExcelProperty("剩余数量")
	private BigDecimal surplusQty;

	/**
	 * 创建人
	 */
	@ExcelProperty("创建人")
	private String createUser;

	/**
	 * 创建时间
	 */
	@ExcelProperty("创建时间")
	@ColumnWidth(25)
	private Date createTime;
}
