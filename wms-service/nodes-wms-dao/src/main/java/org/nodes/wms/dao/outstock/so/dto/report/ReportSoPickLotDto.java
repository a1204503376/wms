package org.nodes.wms.dao.outstock.so.dto.report;

import lombok.Data;
import org.springblade.core.tool.utils.ConvertUtil;

import java.math.BigDecimal;

/**
 * 导出销售发货记录(批次)列表Dto类
 **/
@Data
public class ReportSoPickLotDto {
	/**
	 * 发货单id
	 */
	private Long soBillId;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 产品分类
	 */
	private String skuName;
	/**
	 * 生产日期/批次
	 */
	private String skuLot9;
	/**
	 * 单位
	 */
	private String wsuName;
	/**
	 * 数量
	 */
	private BigDecimal qty;

	public void setQty(BigDecimal qty) {
		this.qty = ConvertUtil.convert(qty.stripTrailingZeros().toPlainString(), BigDecimal.class);
	}

	/**
	 * 钢背
	 */
	private String skuLot5;

	/**
	 * 生产批次
	 */
	private String skuLot1;
	/**
	 * 型号
	 */
	private String skuLot2;
}
