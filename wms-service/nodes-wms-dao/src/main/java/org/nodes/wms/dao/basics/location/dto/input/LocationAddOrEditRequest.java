package org.nodes.wms.dao.basics.location.dto.input;

import lombok.Data;
import org.nodes.core.tool.validation.Update;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库位新增请求dto
 **/
@Data
public class LocationAddOrEditRequest implements Serializable {

	private static final long serialVersionUID = -6793501619868182722L;

	/**
	 * 库位id
	 */
	@NotNull(message = "库位id不能为空",groups = Update.class)
	private Long locId;

	/**
	 * 库位编码
	 */
	@NotNull(message = "库位编码不能为空")
	private String locCode;

	/**
	 * 库房编码
	 */
	@NotNull(message = "库房不能为空")
	private Long whId;

	/**
	 * 库区下拉选择对象
	 */
	@NotNull(message = "库区不能为空")
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
