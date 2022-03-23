package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 按箱移动返回VO
 * @Author zx
 * @Date 2020/7/30
 **/
@Data
public class StockMoveByBoxVO {
	/**
	 * 物品名称
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
	 * 移动数量
	 */
	@ApiModelProperty("移动数量")
	@JsonSerialize(using = ToStringSerializer.class)
	private BigDecimal moveQty;

	/**
	 * 单位名称
	 */
	@ApiModelProperty("单位名称")
	private String wsuName;

	/**
	 * 源库位编码
	 */
	@ApiModelProperty("源库位编码")
	private String sourceLocCode;
	/**
	 * 源库位ID
	 */
	@ApiModelProperty("源库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long sourceLocId;


	/**
	 * 批属性标签
	 */
	@ApiModelProperty("批属性标签列表")
	private List<SkuLotConfigVO> skuLotValList;

	/**
	 * 库存与包装列表
	 */
	@ApiModelProperty("库存与包装列表")
	private List<StockAndPackageVO> stockList;
}
