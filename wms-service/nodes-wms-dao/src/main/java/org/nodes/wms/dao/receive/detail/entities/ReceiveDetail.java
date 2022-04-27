package org.nodes.wms.dao.receive.detail.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.Data;
import org.nodes.wms.dao.common.entitits.BaseSkuLotEntity;

import java.math.BigDecimal;

/**
 * 收货明细单表 实体类
 **/
@Data
@TableName("receive_detail")
public class ReceiveDetail extends BaseSkuLotEntity {
	/**
	 * 收货明细单主键id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long receiveDetailId;
	/**
	 * 收货单ID
	 */
	private Long receiveId;

	/**
	 * 收货单单据编码
	 */
	private Long receiveNo;
	/**
	 * ASN单明细id
	 */
	private Long asnDetailId;
	/**
	 * 订单行号
	 */
	private String  lineNo;
	/**
	 * 接收状态
	 */
	private Integer  detailStatus;
	/**
	 * 物品ID
	 */
	private Long  skuId;
	/**
	 * 物品编码
	 */
	private String  skuCode;
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
	 * 备注
	 */
	private String remark;
	/**
	 * 包装id
	 */
	private  Long  wspId;
	/**
	 * 层级
	 */
	private Integer skuLevel;
	/**
	 * 规格
	 */
	private BigDecimal skuSpec;
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
	 *  基础计量单位编码
	 */
	private String baseUmCode;
	/**
	 * 基础计量单位名称
	 */
	private  String baseUmName;
	/**
	 * 库房ID
	 */
	private Long whId;
	/**
	 *  库房编码
	 */
	private  String whCode;
	/**
	 * 货主ID
	 */
	private Long woId;
	/**
	 * 货主编码
	 */
	private String	ownerCode;
	/**
	 *  单价
	 */
	private BigDecimal detailPriceDecimal;
	/**
	 * 明细总金额
	 */
	private BigDecimal	detailAmount;
	/**
	 * 序列号导入状态  Y:已导入N：未导入
	 */
	private String importSn;
}
