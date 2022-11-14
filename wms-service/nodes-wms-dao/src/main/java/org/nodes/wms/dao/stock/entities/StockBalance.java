package org.nodes.wms.dao.stock.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;

import java.io.Serializable;

/**
 * 库存结存实体
 */
@Data
@TableName("stock_balance")
public class StockBalance extends SkuLotBaseEntity implements Serializable {
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
	private Data balanceDate;
}
