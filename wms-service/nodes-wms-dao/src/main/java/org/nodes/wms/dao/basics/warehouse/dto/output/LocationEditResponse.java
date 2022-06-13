package org.nodes.wms.dao.basics.warehouse.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 库位编辑返回dto对象
 **/
@Data
public class LocationEditResponse {

	private static final long serialVersionUID = -2480210232385700445L;

	/**
	 * 库位id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 库房编码
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 库区下拉选择对象
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long zoneId;

	/**
	 * 库位类型
	 */
	private Integer locType;

	/**
	 * 库位种类
	 */
	private Integer locCategory;

	/**
	 * 库位处理
	 */
	private Integer locHandling;

	/**
	 * abc分类
	 */
	private Integer abc;

	/**
	 * 使用状态
	 */
	private Integer locFlag;

	/**
	 * 校验数位
	 */
	private String checkDigit;

	/**
	 * 线路顺序
	 */
	private Integer logicAllocation;

	/**
	 * 货架排
	 */
	private String locBank;

	/**
	 * 货架列
	 */
	private String locColumn;

	/**
	 * 上架顺序
	 */
	private Integer putOrder;

	/**
	 * 适用容器类型id
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long lpnTypeId;

	/**
	 * 高度
	 */
	private BigDecimal locHigh;

	/**
	 * 长度
	 */
	private BigDecimal locLength;

	/**
	 * 宽度
	 */
	private BigDecimal locWide;

	/**
	 * 容量
	 */
	private BigDecimal capacity;

	/**
	 * 载重量
	 */
	private BigDecimal loadWeight;

	/**
	 * 货架层
	 */
	private String locLevel;

	/**
	 * 最大存放件数
	 */
	private Integer itemNum;

	/**
	 * 最大存放托数
	 */
	private Integer trayNum;

	/**
	 * 混放货品
	 */
	private String locSkuMix;

	/**
	 * 混放批号
	 */
	private String locLotNoMix;
}
