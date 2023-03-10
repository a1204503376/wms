package org.nodes.wms.dao.stock.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;
import org.nodes.wms.dao.stock.enums.StockStatusEnum;

import java.math.BigDecimal;

/**
 * 库存日志实体类
 **/
@Data
@TableName("wms_stock_log")
public class StockLog extends BaseSkuLotEntity {

	private static final long serialVersionUID = 1057813814281261519L;

	/**
	 * 库存日志id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long wlslId;

	/**
	 * 库存日志类型
	 */
	private String logType;

	/**
	 * 来源的单据id
	 */
	private Long sourceBillId;

	/**
	 * 来源的单据编码
	 */
	private String sourceBillNo;

	/**
	 * 来源单据行号
	 */
	private String lineNo;

	/**
	 * 本次操作的待上架数量
	 */
	private BigDecimal currentStayStockQty;

	/**
	 * 本次操作的上架数量
	 */
	private BigDecimal currentStockQty;

	/**
	 * 本次操作的下架数量
	 */
	private BigDecimal currentPickQty;

	/**
	 * 当前操作的占用量
	 */
	private BigDecimal currentOccupyQty;

	/**
	 * 库存id
	 */
	private Long stockId;

	/**
	 * 库存状态（0正常，1冻结）
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
	 * 待上架数量(操作之后)
	 */
	private BigDecimal stayStockQty;

	/**
	 * 上架数量(操作之后)
	 */
	private BigDecimal stockQty;

	/**
	 * 下架数量(操作之后)
	 */
	private BigDecimal pickQty;

	/**
	 * 占用量(操作之后)
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
	 * 消息
	 */
	private String msg;
	/**
	 * 计量单位编码
	 */
	private String wsuCode;
	/**
	 * 落放id
	 */
	private String dropId;

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
}
