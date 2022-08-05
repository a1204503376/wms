package org.nodes.wms.dao.count.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;

import java.math.BigDecimal;

/**
 * 盘点差异报告表实体类
 */
@Data
@TableName("wms_count_report")
public class CountReport extends SkuLotBaseEntity {

	private static final long serialVersionUID = 6164233380209953512L;
	/**
	 * 盘点单明细ID
	 */
	@TableId(value = "wcrep_id", type = IdType.ASSIGN_ID)
	private Long wcrepId;
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
	 * 系统数量
	 */
	private BigDecimal wmsQty;
	/**
	 * 盘点数量
	 */
	private BigDecimal countQty;
	/**
	 * 计量单位名称
	 */
	private String wsuName;
	/**
	 * 包装ID
	 */
	private Long wspId;
	/**
	 * 序列号
	 */
	private String serialNumber;
	/**
	 * 系统批次号
	 */
	private String systemLot;
	/**
	 * 盘点批次号
	 */
	private String countLot;
	/**
	 * 库位标志
	 */
	private Integer locFlag;


}
