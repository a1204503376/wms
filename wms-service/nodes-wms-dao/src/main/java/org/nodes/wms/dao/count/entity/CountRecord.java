package org.nodes.wms.dao.count.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 盘点单记录表
 */
@Data
@TableName("wms_count_record")
public class CountRecord extends SkuLotBaseEntity implements Serializable {

	private static final long serialVersionUID = -8261165352293925484L;

	/**
	 * 盘点单记录ID
	 */
	@TableId(value = "wcr_id", type = IdType.ASSIGN_ID)
	private Long wcrId;
	/**
	 * 库房ID
	 */
	private Long whId;
	/**
	 * 盘点单ID
	 */
	private Long countBillId;
	/**
	 * 单据编码
	 */
	private String countBillNo;
	/**
	 * 库存ID
	 */
	private Long stockId;
	/**
	 * 库位ID
	 */
	private Long locId;
	/**
	 * 库位编码
	 */
	private String locCode;
	/**
	 * 任务分组编码
	 */
	private String taskGroup;
	/**
	 * 容器编码
	 */
	private String lpnCode;
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
	 * 盘点数量
	 */
	private BigDecimal countQty;
	/**
	 * 实际库存量
	 */
	private BigDecimal stockQty;
	/**
	 * 包装ID
	 */
	private Long wspId;
	/**
	 * 包装名称
	 */
	private String wspName;
	/**
	 * 计量单位名称
	 */
	private String wsuName;
	/**
	 * 层级
	 */
	private Integer skuLevel;
	/**
	 * 换算倍率
	 */
	private Integer convertQty;
	/**
	 * 盘点序列号
	 */
	private String serialNumber;
	/**
	 * 盘点时间
	 */
	private LocalDateTime procTime;
	/**
	 * 用户ID
	 */
	private Long userId;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 盘点状态
	 */
	private Integer recordState;
	/**
	 * 批次号
	 */
	private String lotNumber;

	/**
	 * 箱号
	 */
	private String boxCode;
}
