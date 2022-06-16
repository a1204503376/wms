
package org.nodes.wms.core.stock.core.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.nodes.wms.dao.basics.sku.entities.SkuPackageDetail;
import org.nodes.wms.core.stock.core.entity.Serial;
import org.nodes.wms.core.stock.core.entity.Stock;
import org.nodes.wms.core.warehouse.entity.Location;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存视图实体类
 *
 * @author pengwei
 * @since 2019-12-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "StockVO对象", description = "库存")
public class StockVO extends Stock {
	private static final long serialVersionUID = 1L;

	/**
	 * 库存状态说明
	 */
	@ApiModelProperty("库存状态说明")
	private String stockStatusDesc;
	/**
	 * 库存状态说明
	 */
	@ApiModelProperty("库位状态")
	private String lockFlag;
	/**
	 * 是否序列号
	 */
	@ApiModelProperty("是否序列号")
	private Integer isSn;
	/**
	 * 可用量
	 */
	@ApiModelProperty(value = "可用量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal availableQty;

	/**
	 * 序列号
	 */
	@ApiModelProperty("序列号")
	private List<Serial> serialList;

	/**
	 * 货主名称
	 */
	@ApiModelProperty("货主名称")
	private String ownerName;

	/**
	 * 物品总数量-给手持用LPN
	 */
	@ApiModelProperty("物品总数量")
	private BigDecimal skuCount;

	/**
	 * 库房名称-给手持用LPN
	 */
	@ApiModelProperty("库房名称")
	private String whName;

	/**
	 * 库位名称-给手持用LPN
	 */
	@ApiModelProperty("库位编码")
	private String locCode;


	/**
	 * 包装规格描述-给手持用LPN
	 */
	@ApiModelProperty("包装规格描述")
	private String skuSpec;


	/**
	 * 所属库区
	 */
	@ApiModelProperty("所属库区")
	private String zoneName;

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
	 * 计量单位
	 */
	@ApiModelProperty("计量单位")
	private String wsuName;

	/**
	 * 实际库存数量
	 */
	@ApiModelProperty("实际库存数量")
	private BigDecimal realQty;

	/**
	 * 分配总量
	 */
	@ApiModelProperty("分配总量")
	private BigDecimal allotQty;

	/**
	 * 盘点占用总量
	 */
	@ApiModelProperty("盘点占用总量")
	private BigDecimal checkOccupyQty;



	/**
	 * 库存实际数量
	 */
	@ApiModelProperty("库存实际数量")
	private BigDecimal actualQty;
	/**
	 * 物品分类编码
	 */
	@ApiModelProperty("物品分类编码")
	private String skuTypeCd;
	/**
	 * 物品分类名称
	 */
	@ApiModelProperty("物品分类名称")
	private String skuTypeName;
	/**
	 * 是否选中
	 */
	@ApiModelProperty("物品分类名称")
	private Boolean CheckStock;

	/**
	 * 包装名称
	 */
	@ApiModelProperty("包装名称")
	private String wspName;
	/**
	 * 批次状态
	 */
	@ApiModelProperty("批次状态")
	private Integer lotStatus;
	/**
	 * 批次状态描述
	 */
	@ApiModelProperty("批次状态描述")
	private String lotStatusDesc;
	/**
	 * 库位信息
	 */
	@ApiModelProperty("库位信息")
	private Location location;
	/**
	 * 物品可用库存汇总
	 */
	@ApiModelProperty("物品可用库存汇总")
	private String totalQty;

	/**
	 * 库龄
	 */
	@ApiModelProperty("库龄")
	private String kuLing;

	/**
	 * 临期相差天数
	 */
	@ApiModelProperty("临期相差天数")
	private String validDay;

	/**
	 * 当前库存的包装明细
	 */
	@ApiModelProperty("当前库存对应的包装明细")
	private List<SkuPackageDetail> skuPackageDetailList;
}
