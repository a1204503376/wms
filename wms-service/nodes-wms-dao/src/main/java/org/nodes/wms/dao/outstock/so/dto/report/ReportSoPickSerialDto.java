package org.nodes.wms.dao.outstock.so.dto.report;

import lombok.Data;
import org.springblade.core.tool.utils.ConvertUtil;

import java.math.BigDecimal;

/**
 * 导出销售发货记录(批次)列表Dto类
 **/
@Data
public class ReportSoPickSerialDto {
	/**
	 * 发货单id
	 */
	private Long soBillId;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 数量
	 */
	private BigDecimal qty;
	public void setQty(BigDecimal qty) {
		this.qty = ConvertUtil.convert(qty.stripTrailingZeros().toPlainString(), BigDecimal.class);
	}
	/**
	 * 辅助代码(序列号)
	 */
	private String snCode;
	/**
	 * 钢背
	 */
	private String skuLot5;
	/**
	 * 生产日期(入库日期)
	 */
	private String skuLot9;
}
