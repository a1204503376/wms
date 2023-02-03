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
	 * 货架列
	 */
	private String locColumn;

	/**
	 * 货架排
	 */
	private String locBank;

	/**
	 * 适用的容器类型
	 */
	private Long lpnTypeId;

	/**
	 * 适用容器名称
	 */
	private String lpnTypeCode;
}
