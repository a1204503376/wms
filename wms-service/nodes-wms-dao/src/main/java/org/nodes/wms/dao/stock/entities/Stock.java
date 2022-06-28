
package org.nodes.wms.dao.stock.entities;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.entity.SkuLotBaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存实体类
 *
 * @author pengwei
 * @since 2019-12-24
 */
@Data
@TableName("wms_stock")
@EqualsAndHashCode(callSuper = true)
public class Stock extends SkuLotBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String STOCK_STATUS = "stock_status";

	/**
	 * 库存ID
	 */
	@TableId(value = "stock_id", type = IdType.ASSIGN_ID)
	private Long stockId;

	/**
	 * 库房ID
	 */
	private Long whId;
	/**
	 * 库房名称
	 */
	private String whName;
	/**
	 * 库区ID
	 */
	private Long zoneId;
	/**
	 * 库区名称
	 */
	private String zoneName;
	/**
	 * 库位ID
	 */
	private Long locId;
	/**
	 * 库位编码
	 */
	private String locCode;
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
	 * 包装ID
	 */
	private Long wspId;
	/**
	 * 包装名称
	 */
	private String wspName;
	/**
	 * 层级
	 */
	private Integer skuLevel;
	/**
	 * 库存数量
	 */
	private BigDecimal stockQty;
	/**
	 * 分配数量
	 */
	private BigDecimal occupyQty;

	/**
	 * 下架数量
	 */
	private BigDecimal pickQty;
	/**
	 * 盘点占用数量
	 */
	@TableField(exist = false)
	private BigDecimal countOccupyQty;
	/**
	 * 库存状态
	 */
	private Integer stockStatus;
	/**
	 * 最近入库时间(库存数量增加时更新)
	 */
	private LocalDateTime lastInTime;
	/**
	 * 最近出库时间(下架数量增加时更新)
	 */
	private LocalDateTime lastOutTime;
	/**
	 * 批次号
	 */
	private String lotNumber;

	/**
	 * 货主ID
	 */
	private Long woId;

	/**
	 * 有效截止日期
	 */
	private String validTime;

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

}
