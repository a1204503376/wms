package org.nodes.wms.dao.instock.receive.dto.input;

import lombok.Data;
import org.nodes.wms.dao.basics.sku.dto.input.SkuSelectRequest;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 新建收货单明细接收类
 **/
@Data
public class NewReceiveDetailRequest implements Serializable {
	private static final long serialVersionUID = 8039767137023774339L;
	/**
	 * 订单行号
	 */
	private String lineNumber;
	/**
	 * 物品下拉框对象
	 */
	@NotNull(message = "物品ID不能为空")
	private SkuSelectRequest sku;
	/**
	 * 计划数量
	 */
	@NotNull(message = "计划数量不能为空")
	private BigDecimal planQty;
	/**
	 * 计量单位编码
	 */
	private String umCode;
	/**
	 * 计量单位名称
	 */
	private String umName;
	/**
	 * 规格
	 */
	private String skuSpec;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 生产批次
	 */
	private String skuLot1;
	/**
	 * 入库日期 （归还单的明细需要）
	 */
	private String skuLot3;
	/**
	 * 客户
	 */
	private String skuLot4;
	/**
	 * 钢背批次
	 */
	private String skuLot5;
	/**
	 * 摩擦块批次
	 */
	private String skuLot6;
	/**
	 * 摩擦块批次
	 */
	private String skuLot7;
	/**
	 * 适用速度等级
	 */
	private String skuLot8;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * Lpn
	 */
	private String lpnCode;
	/**
	 * 序列号
	 */
	private String snCode;
}
