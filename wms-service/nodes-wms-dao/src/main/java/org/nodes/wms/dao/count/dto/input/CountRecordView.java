package org.nodes.wms.dao.count.dto.input;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 盘点单记录请求对象视图
 *
 * @author nodesc
 */
@Data
public class CountRecordView implements Serializable {
	private static final long serialVersionUID = -8047003397293895539L;
	/**
	 * 盘点单记录ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wcrId;

	/**
	 * 库房ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 盘点单ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long countBillId;

	/**
	 * 单据编码
	 */
	private String countBillNo;

	/**
	 * 库存ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;

	/**
	 * 库位ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
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
	@JsonSerialize(using = ToStringSerializer.class)
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
	@JsonSerialize(using = ToStringSerializer.class)
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

	/**
	 * 库房名称
	 */
	private String whName;

	/**
	 * 库区ID集合
	 */
	private List<Long> zoneId;

	/**
	 * 开始时间
	 */
	private String[] startTimeArray;

	/**
	 * 创建时间描述
	 */
	private String procTimeDesc;

	/**
	 * 层级名称
	 */
	private String skuLevelDesc;

	/**
	 * 盘点记录状态描述
	 */
	private String recordStateDesc;

	/**
	 * 盘点时间范围（json数组）
	 */
	private String procTimeArray;
}
