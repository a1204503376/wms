package org.nodes.wms.dao.stock.entities;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;

import java.math.BigDecimal;

/**
 * 库存占用实体类
 **/
@Data
@TableName("wms_stock_occupy")
public class StockOccupy extends BaseSkuLotEntity {

	private static final long serialVersionUID = -4812391426520687409L;

	/**
	 * 库存占用id
	 */
	private Long wsoId;

	/**
	 * 占用数量
	 */
	private BigDecimal occupyQty;

	/**
	 * 占用类型
	 */
	private Integer occupyType;

	/**
	 * 来源单据编码
	 */
	private String sourceBillNo;

	/**
	 * 来源单据id
	 */
	private Long sourceBillId;

	/**
	 * 来源明细ID,可能是拣货计划id或盘点单明细id
	 */
	private Long sourceDetailId;

	/**
	 * 库存id
	 */
	private Long stockId;

	/**
	 * 层级
	 */
	private Integer skuLevel;

	/**
	 * 包装名称
	 */
	private String wspName;

	/**
	 * 包装id
	 */
	private Long wspId;

	/**
	 * 物品id
	 */
	private Long skuId;

	/**
	 * 物品名称
	 */
	private String skuName;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 库位id
	 */
	private Long locId;

	/**
	 * 库区编码
	 */
	private String zoneCode;

	/**
	 * 库区id
	 */
	private Long zoneId;

	/**
	 * 库房名称
	 */
	private String whName;

	/**
	 * 库房id
	 */
	private Long whId;

	/**
	 * 货主id
	 */
	private Long woId;

	/**
	 * 版本号
	 */
	private Integer version;
}
