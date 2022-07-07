package org.nodes.wms.dao.instock.receive.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.wms.dao.instock.receive.enums.ReceiveDetailStatusEnum;

import java.math.BigDecimal;

/**
 * lpn表 实体类
 **/
@Data
@TableName("receive_detail_lpn")
public class ReceiveDetailLpn extends SkuLotBaseEntity {
	private static final long serialVersionUID = -8534916597428803794L;
	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 收货单头表id
	 */
	private Long receiveHeaderId;
	/**
	 * 收货单明细id
	 */
	private Long receiveDetailId;
	/**
	 * 接收状态
	 */
	private ReceiveDetailStatusEnum detailStatus;
	/**
	 * 供应商id
	 */
	private Long supplierId;
	/**
	 * 物品id
	 */
	private Long skuId;
	/**
	 * 物品编码
	 */
   private  String skuCode;
	/**
	 * 物品名称
	 */
	private String skuName;
	/**
	 * 托盘号
	 */
	private String lpnCode;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 序列号
	 */
	private String snCode;
	/**
	 * 计划数量
	 */
	private BigDecimal planQty;
	/**
	 * 实收数量
	 */
	private BigDecimal scanQty;
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
	private  String skuSpec;
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
	 * 库房id
	 */
	private Long whId;
	/**
	 * 库房编码
	 */
	private String whCode;
	/**
	 * 基础计量单位编码
	 */
	private String baseUmCode;
	/**
	 * 基础计量单位名称
	 */
	private String baseUmName;
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
}
