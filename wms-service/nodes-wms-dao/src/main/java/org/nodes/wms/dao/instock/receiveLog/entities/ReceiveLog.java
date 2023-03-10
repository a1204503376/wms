package org.nodes.wms.dao.instock.receiveLog.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.math.BigDecimal;

/**
 * 收货单 清点表
 **/
@Data
@TableName("receive_log")
public class ReceiveLog extends BaseSkuLotEntity {
	private static final long serialVersionUID = 6947408810253759891L;
	/**
	 * 清点表 主键ID
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 收货单id
	 */
	private Long receiveId;
	/**
	 * 收货单据编码
	 */
	private String receiveNo;
	/**
	 * ASN单主键ID
	 */
	private Long asnBillId;
	/**
	 * ASN单据编码
	 */
	private String asnBillNo;
	/**
	 * 收货单明细ID
	 */
	private Long receiveDetailId;
	/**
	 * 供应商id
	 */
	private Long supplierId;
	/**
	 * 订单行号
	 */
	private String lineNo;
	/**
	 * 数量
	 */
	private BigDecimal qty;
	/**
	 * lpn编码
	 */
	private String lpnCode;
	/**
	 * 箱码
	 */
	private String boxCode;
	/**
	 * 序列号,多个序列号，分割
	 */
	private String snCode;
	/**
	 * 库位id
	 */
	private Long locId;
	/**
	 * 库位编码
	 */
	private String locCode;
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
	 * 包装id
	 */
	private Long wspId;
	/**
	 * 包装层级
	 */
	private Integer skuLevel;
	/**
	 * 规格
	 */
	private String skuSpec;
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
	 * 计量单位编码
	 */
	private String wsuCode;
	/**
	 * 库存状态
	 */
	private StockStatusEnum stockStatus;
	/**
	 * 清点记录id
	 */
	private String cancelLogId;
	/**
	 * 自定义字段1
	 */
	private String udf1;
	/**
	 * 自定义字段2
	 */
	private String udf2;
	/**
	 * 自定义字段3：标记库存是否撤销收货
	 */
	private String udf3;
	/**
	 * 自定义字段4
	 */
	private String udf4;
	/**
	 * 自定义字段5
	 */
	private String udf5;
}

