package org.nodes.wms.dao.instock.asn.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;

import java.math.BigDecimal;

/**
 * ASN单据明细表 实体
 */
@Data
@TableName("asn_detail")
public class AsnDetail extends BaseSkuLotEntity {

	private static final long serialVersionUID = -8540649823396732815L;

	/**
	 * ASN单明细id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long asnDetailId;

	/**
	 * ASN单id
	 */
	private Long asnBillId;

	/**
	 * 供应商id
	 */
	private Long supplierId;

	/**
	 * 单据编码
	 */
	private String asnBillNo;

	/**
	 * 订单行号
	 */
	private String asnLineNo;

	/**
	 * 物品ID
	 */
	private Long skuId;

	/**
	 * 包装ID
	 */
	private Long wspId;

	/**
	 * 层级
	 */
	private Integer skuLevel;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 规格
	 */
	private String skuSpec;

	/**
	 * 换算倍率
	 */
	private Integer convertQty;

	/**
	 * 计量单位编码
	 */
	private String umCode;

	/**
	 * 计量单位名称
	 */
	private String umName;

	/**
	 * 基础计量单位编码
	 */
	private String baseUmCode;

	/**
	 * 基础计量单位名称
	 */
	private String baseUmName;

	/**
	 * 计划数量
	 */
	private BigDecimal planQty;

	/**
	 * 实际数量
	 */
	private BigDecimal scanQty;

	/**
	 * 剩余数量
	 */
	private BigDecimal surplusQty;

	/**
	 * 库房id
	 */
	private Long whId;

	/**
	 * 单价
	 */
	private BigDecimal detailPrice;

	/**
	 * 明细总金额
	 */
	private BigDecimal detailAmount;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 接收状态
	 */
	private Integer detailStatus;

	/**
	 * 序列号导入状态
	 */
	private String importSn;
}
