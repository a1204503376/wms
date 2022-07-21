package org.nodes.wms.dao.outstock.so.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;

import java.math.BigDecimal;

/**
 * 发货单明细实体类
 **/
@Data
@TableName("so_detail")
public class SoDetail extends BaseSkuLotEntity {

	private static final long serialVersionUID = -4265265920441708082L;


	/**
	 * 发货单明细id
	 */
	@TableId("so_detail_id")
	private Long soDetailId;

	/**
	 * 发货单id
	 */
	private Long soBillId;

	/**
	 * 单据编码
	 */
	private String soBillNo;

	/**
	 * 行号
	 */
	private String soLineNo;

	/**
	 * 单据种类编码
	 */
	private String billTypeCd;

	/**
	 * 上位系统单据唯一标识
	 */
	private String billDetailKey;

	/**
	 * 物品id
	 */
	private Long skuId;

	/**
	 * 包装id
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
	 * 物品编码
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
	 * 实收数量
	 */
	private BigDecimal scanQty;

	/**
	 * 剩余数量
	 */
	private BigDecimal surplusQty;

	/**
	 * 批次号
	 */
	private String lotNumber;

	/**
	 * 发货库房
	 */
	private String pickWhCode;

	/**
	 * 单价
	 */
	private BigDecimal detailPrice;

	/**
	 * 明细总金额
	 */
	private BigDecimal detailAmount;

	/**
	 * 单据状态 TODO 定义为枚举
	 */
	private Integer billDetailState;

	/**
	 * 波次id
	 */
	private Long wellenId;

	/**
	 * 差异报告id
	 */
	private Long wcrepId;

	/**
	 * 是否允许替代（0：不允许，1：允许）
	 */
	private Integer allowReplace;

	/**
	 * 上位系统单据唯一标识
	 */
	private String billKey;

	/**
	 * 上位系统编号
	 */
	private String orderNo;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 备用字段1
	 */
	private String attribute1;

	/**
	 * 备用字段2
	 */
	private String attribute2;

	/**
	 * 备用字段3
	 */
	private String attribute3;

	/**
	 * 备用字段4
	 */
	private String attribute4;

	/**
	 * 备用字段5
	 */
	private String attribute5;

	/**
	 * 备用字段6
	 */
	private String attribute6;

	/**
	 * 备用字段7
	 */
	private String attribute7;

	/**
	 * 备用字段8
	 */
	private String attribute8;

	/**
	 * 备用字段9
	 */
	private String attribute9;

	/**
	 * 备用字段10
	 */
	private String attribute10;

	/**
	 * 备用字段11
	 */
	private String attribute11;

	/**
	 * 备用字段12
	 */
	private String attribute12;

	/**
	 * 备用字段13
	 */
	private String attribute13;

	/**
	 * 备用字段14
	 */
	private String attribute14;

	/**
	 * 备用字段15
	 */
	private String attribute15;

	/**
	 * 备用字段16
	 */
	private String attribute16;

	/**
	 * 备用字段17
	 */
	private String attribute17;

	/**
	 * 备用字段18
	 */
	private String attribute18;

	/**
	 * 备用字段19
	 */
	private String attribute19;

	/**
	 * 备用字段20
	 */
	private String attribute20;

	/**
	 * 备用字段21
	 */
	private String attribute21;

	/**
	 * 备用字段22
	 */
	private String attribute22;

	/**
	 * 备用字段23
	 */
	private String attribute23;

	/**
	 * 备用字段24
	 */
	private String attribute24;

	/**
	 * 备用字段25
	 */
	private String attribute25;

	/**
	 * 备用字段26
	 */
	private String attribute26;

	/**
	 * 备用字段27
	 */
	private String attribute27;

	/**
	 * 备用字段28
	 */
	private String attribute28;

	/**
	 * 备用字段29
	 */
	private String attribute29;

	/**
	 * 备用字段30
	 */
	private String attribute30;
}
