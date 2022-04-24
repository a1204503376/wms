package org.nodes.wms.dao.instock.receive.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.models.auth.In;
import lombok.Data;
import org.nodes.wms.dao.common.entitits.BaseSkuLotEntity;
import org.springblade.core.mp.base.BaseEntity;

import java.math.BigDecimal;

/**
 * 收货单明细表 实体类
 **/
@Data
@TableName("receive_detail")
public class ReceiveDetail extends BaseSkuLotEntity {
	/**
	 * 收货单明细 主键id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long receiveDetailId;

	/**
	 * 收货单id
	 */
	private Long receiveId;

	/**
	 * 收货单编码
	 */

	private String receiveNo;

	/**
	 * Asn单明细 id
	 */
	private Long asnDetailId;

	/**
	 * 订单行号
	 */
	private String lineNo;

	/**
	 * 接收状态
	 */
	private Integer detailStatus;

	/**
	 * 物品id
	 */
	private Long skuId;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品名称
	 */
	private String skuName;

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
	 * 包装id
	 */
	private Long wspId;

	/**
	 * 层级
	 */
	private Integer skuLevel;

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
	 * 库房id
	 */
	private Long whId;

	/**
	 * 库房编码
	 */
	private String whCode;

	/**
	 * 货主id
	 */
	private Long woId;

	/**
	 * 货主编码
	 */
	private String ownerCode;

	/**
	 * 单价
	 */
	private BigDecimal detailPrice;

	/**
	 * 明细总金额
	 */
	private BigDecimal detailAmount;

	/**
	 * 序列号导入状态(Y:已导入,N:未导入)
	 */
	private Character importSn;
}
