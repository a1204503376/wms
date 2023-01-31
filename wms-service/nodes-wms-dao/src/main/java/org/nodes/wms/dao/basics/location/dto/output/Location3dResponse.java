package org.nodes.wms.dao.basics.location.dto.output;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Location3dResponse 3d库位模型相应类
 **/
@Data
public class Location3dResponse {

	/**
	 * 库位ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;

	/**
	 * 库区ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long zoneId;

	/**
	 * 库房ID
	 */
	private Long whId;

	/**
	 * 库位编码
	 */
	private String locCode;

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
	 * 分配区
	 */
	private String allocationZone;

	/**
	 * 使用状态,值参考字典中的LOC_FLAG
	 */
	private Integer locFlag;

	/**
	 * ABC分类
	 */
	private Integer abc;

	/**
	 * 库存差异是否自动冻结货位
	 */
	private String lockFlag;

	/**
	 * 状态
	 */
	private Integer locStatus;

	/**
	 * 路线顺序
	 */
	private Integer logicAllocation;

	/**
	 * 长
	 */
	private BigDecimal locLength;

	/**
	 * 宽
	 */
	private BigDecimal locWide;

	/**
	 * 高
	 */
	private BigDecimal locHigh;

	/**
	 * 货架层
	 */
	private String locLevel;

	/**
	 * X坐标
	 */
	private Integer xCode;

	/**
	 * Y坐标
	 */
	private Integer yCode;

	/**
	 * Z坐标
	 */
	private Integer zCode;

	/**
	 * 校验位数
	 */
	private String checkDigit;

	/**
	 * 货架列
	 */
	private String locColumn;

	/**
	 * 货架排
	 */
	private String locBank;

	/**
	 * 上架顺序
	 */
	private Integer putOrder;

	/**
	 * 适用的容器类型
	 */
	private Long lpnTypeId;

	/**
	 * 定方位
	 */
	private String orientation;

	/**
	 * 容量
	 */
	private BigDecimal capacity;

	/**
	 * 载重量
	 */
	private BigDecimal loadWeight;

	/**
	 * 最大存放件数
	 */
	private Integer itemNum;

	/**
	 * 最大存放托数
	 */
	private Integer trayNum;

	/**
	 * 盘点单编码
	 */
	private String countBillNo;

	/**
	 * 库位共享宽度
	 */
	private String shareWidth;

	/**
	 * 库位共享重量
	 */
	private String shareWeight;
}
