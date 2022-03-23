package org.nodes.wms.core.strategy.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.utils.StringPool;
import org.nodes.wms.core.stock.core.vo.StockVO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * author: pengwei
 * date: 2021/5/26 14:01
 * description: InstockExecuteVO
 */
@Data
public class RelenishmentExecuteVO {

	public final static RelenishmentExecuteVO EMPTY = new RelenishmentExecuteVO();

	/**
	 * 补货策略ID
	 */
	@ApiModelProperty("补货策略ID")
	private Long ssiId;
	/**
	 * 补货策略编码
	 */
	@ApiModelProperty("补货策略编码")
	private String ssiCode;
	/**
	 * 补货策略名称
	 */
	@ApiModelProperty("补货策略名称")
	private String ssiName;

	private Integer ssidProcOrder;

	private Integer relenishmentFunction;

	private String relenishmentFunctionDesc;

	/**
	 * 策略执行是否成功
	 */
	@ApiModelProperty("策略执行是否成功")
	private Boolean success;
	/**
	 * 执行消息
	 */
	@ApiModelProperty("执行消息")
	private String msg;
	/**
	 * 库区编码
	 */
	@ApiModelProperty("库区编码")
	private String zoneCode = StringPool.UNKNOWN;
	/**
	 * 库区名称
	 */
	@ApiModelProperty("库区名称")
	private String zoneName = StringPool.UNKNOWN;
	/**
	 * 目标库位ID
	 */
	@ApiModelProperty("目标库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long targetLocId;
	/**
	 * 目标库位编码
	 */
	@ApiModelProperty("目标库位编码")
	private String targetLocCode;

	/**
	 * 目标库位编码
	 */
	@ApiModelProperty("目标库位编码")
	private Boolean isSaveZoneAndLoc = true;
	/**
	 * 库位编码
	 */
	@ApiModelProperty("库位编码")
	private String locCode;
	/**
	 * 库位状态
	 */
	@ApiModelProperty("库位状态")
	private Integer locStatus;
	/**
	 * 库位状态描述
	 */
	@ApiModelProperty("库位状态描述")
	private String locStatusName;
	/**
	 * 库房ID
	 */
	@ApiModelProperty("库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 库房名称
	 */
	@ApiModelProperty("库房名称")
	private String whName;
	/**
	 * 物品种类数量
	 */
	@ApiModelProperty("物品种类数量")
	private Integer typeCount;
	/**
	 * 库存集合
	 */
	@ApiModelProperty("库存集合")
	private List<StockVO> stockLocList = new ArrayList<>();
	/**
	 * 库存ID
	 */
	@ApiModelProperty("库存ID")
	private Long stockId;
	/**
	 * 库存可用量
	 */
	@ApiModelProperty("库存可用量")
	private BigDecimal planCount;
}
