package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 补货任务明细返回VO
 * @Author zx
 * @Date 2020/8/4
 **/
@Data
public class StockReplenishmentDetailVO {
	/**
	 * 补货任务明细ID
	 */
	@ApiModelProperty("补货任务明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long transferDetailId;
	/**
	 * 物品ID
	 */
	@ApiModelProperty("物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 物品编码
	 */
	@ApiModelProperty("物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
	private String skuName;

	/**
	 * 源库位编码
	 */
	@ApiModelProperty("源库位编码")
	private String sourceLocCode;

	/**
	 * 规格
	 */
	@ApiModelProperty("规格")
	private String skuSpec;

	/**
	 * 计划数量
	 */
	@ApiModelProperty("计划数量")
	private BigDecimal planQty;

	/**
	 * 补货数量
	 */
	@ApiModelProperty("补货数量")
	private BigDecimal transQty;
	/**
	 * 货主ID
	 */
	@ApiModelProperty("货主ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long woId;
	/**
	 * 包装ID
	 */
	@ApiModelProperty("包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;

	/**
	 * 单位名称
	 */
	@ApiModelProperty("单位名称")
	private String wsuName;

	/**
	 * 批属性标签列表
	 */
	@ApiModelProperty("批属性标签列表")
	private List<SkuLotConfigVO> skuLotValList;

	/**
	 * 目标库区集合
	 */
	@ApiModelProperty("目标库区集合")
	private List<String> locationList;


}
