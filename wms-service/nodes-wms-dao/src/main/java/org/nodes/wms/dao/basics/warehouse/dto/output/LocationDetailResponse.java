package org.nodes.wms.dao.basics.warehouse.dto.output;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库区详情返回dto对象
 **/
@Data
public class LocationDetailResponse implements Serializable {

	private static final long serialVersionUID = 1011909727744315333L;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 库房名称
	 */
	private String whName;

	/**
	 * 库区名称
	 */
	private String zoneName;

	/**
	 * 库位类型名称
	 */
	private String locType;

	/**
	 * 库位种类名称
	 */
	private String locCategory;

	/**
	 * 库位处理名称
	 */
	private String locHandling;

	/**
	 * abc分类名称
	 */
	private String abc;

	/**
	 * 使用状态名称
	 */
	private String locFlag;

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
	 * 适用容器类型编码
	 */
	private String lpnTypeCode;

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
