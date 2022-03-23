package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.basedata.vo.SkuPackageDetailViewVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * PDA用物品移动显示VO
 *
 * @Author zx
 * @Date 2020/3/10
 **/
@Data
public class StockSkuMoveVO {

	/**
	 * 源库位编码
	 */
	@ApiModelProperty(value = "源库位编码")
	private String sourceLocCode;

	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
	private String skuName;

	/**
	 * 规格
	 */
	@ApiModelProperty("规格")
	private String skuSpec;

	/**
	 * 数量
	 */
	@ApiModelProperty("数量")
	private BigDecimal stockQty;
	/**
	 * 数量名
	 */
	@ApiModelProperty("数量名")
	private String stockQtyName;

	/**
	 * 库房id
	 */
	@ApiModelProperty("库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 库房编码
	 */
	@ApiModelProperty("库房编码")
	private String whCode;
	/**
	 * 仓库名称
	 */
	@ApiModelProperty(value = "仓库名称")
	private String whName;

	/**
	 * 物品库存批次列表
	 */
	@ApiModelProperty(value = "物品库存批次列表")
	private List<SkuStockLotMoveVO> skuStockLotMoveVOs;

	/**
	 * 包装明细列表
	 */
	@ApiModelProperty("包装明细列表")
	private List<SkuPackageDetailViewVO> packageDetails;

	/**
	 * 默认包装明细
	 */
	@ApiModelProperty("默认包装明细")
	private SkuPackageDetailViewVO defaultPackageDetail;




}
