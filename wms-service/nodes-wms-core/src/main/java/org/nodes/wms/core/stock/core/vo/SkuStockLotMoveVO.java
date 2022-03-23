package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.core.basedata.vo.SkuLotConfigVO;
import org.nodes.wms.core.basedata.vo.SkuPackageDetailViewVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 物品库存VO
 *
 * @Author zx
 * @Date 2020/3/11
 **/
@Data
public class SkuStockLotMoveVO {


	/**
	 * 库存ID
	 */
	@ApiModelProperty("库存ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long stockId;
	/**
	 * 源库位编码
	 */
	@ApiModelProperty(value = "源库位编码")
	private String sourceLocCode;
	/**
	 * 源容器编码
	 */
	@ApiModelProperty(value = "源容器编码")
	private String sourceLpnCode;
	/**
	 * 物品名称
	 */
	@ApiModelProperty("物品名称")
	private String skuName;

	/**
	 * 物品ID
	 */
	@ApiModelProperty("物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

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
	 * 库房id
	 */
	@ApiModelProperty("库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;

	/**
	 * 批次号
	 */
	@ApiModelProperty("批次号")
	private String lotNumber;

	/**
	 * 货主姓名
	 */
	@ApiModelProperty("货主姓名")
	private String ownerName;
	/**
	 *  批属性
	 */
	@ApiModelProperty("批属性")
	private List<SkuLotConfigVO> skuLotList;

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
