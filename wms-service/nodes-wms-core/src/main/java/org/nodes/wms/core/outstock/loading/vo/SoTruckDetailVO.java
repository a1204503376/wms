
package org.nodes.wms.core.outstock.loading.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.stock.core.vo.StockVO;
import org.nodes.wms.core.outstock.loading.entity.SoTruckDetail;
import org.nodes.wms.core.outstock.so.vo.SoDetailVO;
import org.nodes.wms.core.outstock.so.vo.SoHeaderVO;

import java.math.BigDecimal;
import java.util.List;

/**
 * 车次明细视图实体类
 *
 * @author chz
 * @since 2020-03-03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SoTruckDetailVO对象", description = "车次明细")
public class SoTruckDetailVO extends SoTruckDetail {
	private static final long serialVersionUID = 1L;
	/**
	 * 发货信息头表数据
	 */
	@ApiModelProperty(value = "发货信息头表数据")
	private List<SoHeaderVO> soHeaderVOList;
	/**
	 * 发货明细头表数据
	 */
	@ApiModelProperty(value = "发货明细头表数据")
	private List<SoDetailVO> soDetailVOList;

	/**
	 * 库存明细VO
	 */
	@ApiModelProperty(value = "发货明细头表数据")
	private StockVO stockVO;

	/**
	 * 出库单编码
	 */
	@ApiModelProperty("出库单编码")
	private String soBillNo;

	/**
	 * 波次编码
	 */
	@ApiModelProperty("波次编码")
	private String wellenNo;

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
	 * 装车数量
	 */
	@ApiModelProperty("装车数量")
	private BigDecimal stockQty;
	/**
	 * 计量单位名称
	 */
	@ApiModelProperty("计量单位名称")
	private String umName;
	/**
	 * 批次号
	 */
	@ApiModelProperty("批次号")
	private String lotNumber;

	/**
	 * 物品批属性1
	 */
	@ApiModelProperty(value = "物品批属性1")
	private String skuLot1;
	/**
	 * 物品批属性2
	 */
	@ApiModelProperty(value = "物品批属性2")
	private String skuLot2;
	/**
	 * 物品批属性3
	 */
	@ApiModelProperty(value = "物品批属性3")
	private String skuLot3;
	/**
	 * 物品批属性4
	 */
	@ApiModelProperty(value = "物品批属性4")
	private String skuLot4;
	/**
	 * 物品批属性5
	 */
	@ApiModelProperty(value = "物品批属性5")
	private String skuLot5;
	/**
	 * 物品批属性6
	 */
	@ApiModelProperty(value = "物品批属性6")
	private String skuLot6;
	/**
	 * 物品批属性7
	 */
	@ApiModelProperty(value = "物品批属性7")
	private String skuLot7;
	/**
	 * 物品批属性8
	 */
	@ApiModelProperty(value = "物品批属性8")
	private String skuLot8;
	/**
	 * 物品批属性
	 */
	@ApiModelProperty(value = "物品批属性")
	private String skuLot9;
	/**
	 * 物品批属性10
	 */
	@ApiModelProperty(value = "物品批属性10")
	private String skuLot10;
	/**
	 * 物品批属性11
	 */
	@ApiModelProperty(value = "物品批属性11")
	private String skuLot11;
	/**
	 * 物品批属性12
	 */
	@ApiModelProperty(value = "物品批属性12")
	private String skuLot12;
	/**
	 * 物品批属性13
	 */
	@ApiModelProperty(value = "物品批属性13")
	private String skuLot13;
	/**
	 * 物品批属性14
	 */
	@ApiModelProperty(value = "物品批属性14")
	private String skuLot14;
	/**
	 * 物品批属性15
	 */
	@ApiModelProperty(value = "物品批属性15")
	private String skuLot15;
	/**
	 * 物品批属性16
	 */
	@ApiModelProperty(value = "物品批属性16")
	private String skuLot16;
	/**
	 * 物品批属性17
	 */
	@ApiModelProperty(value = "物品批属性17")
	private String skuLot17;
	/**
	 * 物品批属性18
	 */
	@ApiModelProperty(value = "物品批属性18")
	private String skuLot18;
	/**
	 * 物品批属性19
	 */
	@ApiModelProperty(value = "物品批属性19")
	private String skuLot19;
	/**
	 * 物品批属性20
	 */
	@ApiModelProperty(value = "物品批属性20")
	private String skuLot20;
	/**
	 * 物品批属性21
	 */
	@ApiModelProperty(value = "物品批属性21")
	private String skuLot21;
	/**
	 * 物品批属性22
	 */
	@ApiModelProperty(value = "物品批属性22")
	private String skuLot22;
	/**
	 * 物品批属性23
	 */
	@ApiModelProperty(value = "物品批属性23")
	private String skuLot23;
	/**
	 * 物品批属性24
	 */
	@ApiModelProperty(value = "物品批属性24")
	private String skuLot24;
	/**
	 * 物品批属性25
	 */
	@ApiModelProperty(value = "物品批属性25")
	private String skuLot25;
	/**
	 * 物品批属性26
	 */
	@ApiModelProperty(value = "物品批属性26")
	private String skuLot26;
	/**
	 * 物品批属性27
	 */
	@ApiModelProperty(value = "物品批属性27")
	private String skuLot27;
	/**
	 * 物品批属性28
	 */
	@ApiModelProperty(value = "物品批属性28")
	private String skuLot28;
	/**
	 * 物品批属性29
	 */
	@ApiModelProperty(value = "物品批属性29")
	private String skuLot29;
	/**
	 * 物品批属性30
	 */
	@ApiModelProperty(value = "物品批属性30")
	private String skuLot30;
}
