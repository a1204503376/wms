package org.nodes.wms.dao.putaway.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.wms.dao.common.skuLot.BaseSkuLotEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("putaway_log")
public class PutawayLog extends BaseSkuLotEntity {
	/**
	 * 主键id
	 */
	@TableId(type = IdType.ASSIGN_ID)
	private Long aplId;
	/**
	 * 容器编码
	 */
	private String lpnCode;
	/**
	 * 目标库位编码
	 */
	private String targetLocCode;
	/**
	 * 上架策略id
	 */
	private Long ssiId;
	/**
	 * 上架策略编码
	 */
	private String ssiCode;
	/**
	 * 上架策略名称
	 */
	private String ssiName;
	/**
	 * 库房id
	 */
	private Long whId;
	/**
	 * 处理顺序
	 */
	private Integer ssidProcOrder;
	/**
	 * 上架计算代码
	 */
	private Integer instockFunction;
	/**
	 * 用户编码
	 */
	private String userCode;
	/**
	 * 用户名称
	 */
	private String userName;
	/**
	 * 执行时间
	 */
	private LocalDateTime aplTime;
	/**
	 * 执行内容
	 */
	private String aplProcLog;
	/**
	 * 执行顺序
	 */
	private Integer executeOrder;
	/**
	 * 是否成功
	 */
	private Integer isSuccess;

	/**
	 * 箱码
	 */
	private String boxCode;

	/**
	 * 物品编号
	 */
	private Long skuId;

	/**
	 * 物品编码
	 */
	private String skuCode;

	/**
	 * 物品编码
	 */
	private String skuName;

	/**
	 * 数量
	 */
	private BigDecimal qty;

	/**
	 * 包装ID
	 */
	private Long wspId;

	/**
	 * 层级
	 */
	private Integer skuLevel;

	/**
	 * 规格
	 */
	private String skuSpec;

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
	 * 基础计量单位编码
	 */
	private String baseUmCode;

	/**
	 * 基础计量单位名称
	 */
	private String baseUmName;

	/**
	 * 库房编码
	 */
	private String whCode;

	/**
	 * 货主ID
	 */
	private Long woId;

	/**
	 * 货主编码
	 */
	private String ownerCode;
}
