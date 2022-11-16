package org.nodes.wms.dao.stock.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 库存结存实体
 */
@Data
@TableName("stock_balance")
public class StockBalance extends SkuLotBaseEntity implements Serializable {
	private static final long serialVersionUID = 4781570401404803872L;
	/**
	 * 库存ID
	 */
	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;
	/**
	 * 层级
	 */
	private Integer skuLevel;
	/**
	 * 包装名称
	 */
	private String wspName;
	/**
	 * 包装ID
	 */
	private Long wspId;
	/**
	 * 物品ID
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
	 * 计量单位编码
	 */
	private String wsuCode;
	/**
	 * 库房id
	 */
	private Long whId;
	/**
	 * 货主id
	 */
	private Long woId;
	/**
	 * 结存日期
	 */
	private Date balanceDate;
	/**
	 * 入库数量
	 */
	private BigDecimal inStockQty;
	/**
	 * 出库数量
	 */
	private BigDecimal outStockQty;
	/**
	 * 期初数量
	 */
	private BigDecimal openingQty;
	/**
	 * 结存数量
	 */
	private BigDecimal balanceQty;
	/**
	 * 库存状态
	 */
	private StockStatusEnum stockStatus;
	/**
	 * 批次号
	 */
	private String lotNumber;
	/**
	 * 自定义字段1
	 */
	private String udf1;

	/**
	 * 自定义字段2
	 */
	private String udf2;

	/**
	 * 自定义字段3
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
	/**
	 * 是否有序列号
	 */
	private Integer has_serial;
	/**
	 * 是否启用
	 */
	private Integer state;
}
