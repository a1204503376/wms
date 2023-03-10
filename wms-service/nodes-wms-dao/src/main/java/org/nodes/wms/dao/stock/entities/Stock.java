package org.nodes.wms.dao.stock.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.nodes.core.constant.WmsAppConstant;
import org.nodes.core.tool.utils.BigDecimalUtil;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存，库存中数量保存的是基础计量单位
 *
 * @author nodesc
 */
@Data
@TableName("wms_stock")
@EqualsAndHashCode(callSuper = true)
public class Stock extends SkuLotBaseEntity implements Serializable {

	private static final long serialVersionUID = 5537284380775132619L;

	/**
	 * 库存ID
	 */
	@TableId(value = "stock_id", type = IdType.ASSIGN_ID)
	private Long stockId;

	/**
	 * 库存状态, 系统冻结的库存不能移动
	 */
	private StockStatusEnum stockStatus;
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
	 * 待上架数量
	 */
	private BigDecimal stayStockQty;
	/**
	 * 上架数量
	 */
	private BigDecimal stockQty;
	/**
	 * 下架数量
	 */
	private BigDecimal pickQty;
	/**
	 * 占用数量
	 */
	private BigDecimal occupyQty;
	/**
	 * 箱号
	 */
	private String boxCode;
	/**
	 * 托盘号
	 */
	private String lpnCode;
	/**
	 * 库位ID
	 */
	private Long locId;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 库区ID
	 */
	private Long zoneId;
	/**
	 * 库区编码
	 */
	private String zoneCode;
	/**
	 * 库房ID
	 */
	private Long whId;
	/**
	 * 库房名称
	 */
	private String whCode;
	/**
	 * 货主ID
	 */
	private Long woId;
	/**
	 * 最近入库时间(库存数量增加时更新)
	 */
	private LocalDateTime lastInTime;
	/**
	 * 最近出库时间(下架数量增加时更新)
	 */
	private LocalDateTime lastOutTime;
	/**
	 * 批次号,不再使用
	 */
	private String lotNumber;
	/**
	 * 计量单位编码
	 */
	private String wsuCode;
	/**
	 * 乐观锁
	 * 支持的数据类型只有:int,Integer,long,Long,Date,Timestamp,LocalDateTime
	 * 整数类型下 newVersion = oldVersion + 1
	 * newVersion 会回写到 entity 中
	 * 仅支持 updateById(id) 与 update(entity, wrapper) 方法
	 * 在 update(entity, wrapper) 方法下, wrapper 不能复用!!!
	 */
	@Version
	private Integer version;

	/**
	 * 落放id
	 */
	private String dropId;

	/**
	 * 是否有序列号,1有序列号
	 */
	private Integer hasSerial;

	/**
	 * 自定义字段1
	 */
	@TableField(value = "udf1")
	private String udf1;

	/**
	 * 自定义字段2
	 */
	@TableField(value = "udf2")
	private String udf2;

	/**
	 * 自定义字段3
	 */
	@TableField(value = "udf3")
	private String udf3;

	/**
	 * 自定义字段4
	 */
	@TableField(value = "udf4")
	private String udf4;

	/**
	 * 自定义字段5
	 */
	@TableField(value = "udf5")
	private String udf5;

	/**
	 * 库存余额,序列化成json为stockBalance
	 *
	 * @return 库存余额
	 */
	@JsonSerialize
	public BigDecimal getStockBalance() {
		return getStockQty().subtract(getPickQty());
	}

	/**
	 * 库存可用量,序列化成json为stockEnable
	 *
	 * @return 库存可用量
	 */
	@JsonSerialize
	public BigDecimal getStockEnable() {
		return getStockQty().subtract(getPickQty()).subtract(getOccupyQty());
	}

	@JsonSerialize
	public boolean isSerial() {
		return WmsAppConstant.TRUE_DEFAULT.equals(this.hasSerial);
	}

	@JsonSerialize
	public String getHasSerialString() {
		return WmsAppConstant.TRUE_DEFAULT.equals(this.hasSerial) ? "是" : "否";
	}

	/**
	 * 判断是否是空库存，空库存是指上架量和下架量相等
	 *
	 * @return true：空库存
	 */
	public boolean isEmptyStock() {
		return BigDecimalUtil.eq(stockQty, pickQty);
	}

	/**
	 * 判断是否有可用的库存，库存余额大于0，但库存可用等于0
	 *
	 * @return true：没有可用的库存
	 */
	public boolean isNotEnable() {
		return BigDecimalUtil.gt(stockQty, pickQty)
			&& BigDecimalUtil.eq(getStockEnable(), BigDecimal.ZERO);
	}
}
