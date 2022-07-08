package org.nodes.wms.core.outstock.so.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;
import org.nodes.wms.dao.basics.skulot.entities.SkuLot;

import java.math.BigDecimal;
import java.util.List;

/**
 * 按件拣货展示VO
 *
 * @Author zx
 * @Date 2020/3/4
 **/
@Data
public class PickSkuVO {

	/**
	 * 拣货计划ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "拣货计划ID")
	private Long pickPlanId;

	/**
	 * 计划数量
	 */
	@ApiModelProperty(value = "物品总数量 : 计划总数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal planCountQty;

	/**
	 * 实绩数量
	 */
	@ApiModelProperty(value = "物品已收数量 : 实绩总数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal realCountQty;

	/**
	 * 计划总数量名
	 */
	@ApiModelProperty(value = "物品总数量名 : 计划总数量名")
	private String planQtyName;

	/**
	 * 实绩总数量名
	 */
	@ApiModelProperty(value = "物品已收数量名 : 实绩总数量名")
	private String realQtyName;

	/**
	 * 物品ID
	 */
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;


	@ApiModelProperty(value = "批次号")
	@JsonSerialize(using = ToStringSerializer.class)
	private String lotNumber;
	/**
	 * 物品名称
	 */
	@ApiModelProperty(value = "物品名称")
	private String skuName;
	/**
	 * 物品编码
	 */
	@ApiModelProperty(value = "物品编码")
	@JsonSerialize(using = ToStringSerializer.class)
	private String skuCode;
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
	 * 规格
	 */
	@ApiModelProperty("规格")
	private String skuSpec;

	/**
	 *  批属性
	 */
	@ApiModelProperty("批属性")
	private SkuLotBaseEntity skuLot;

	/**
	 *  批属性便签
	 */
	@ApiModelProperty("批属性标签")
	private SkuLot skuLotValue;


	/**
	 * 是否为序列号
	 */
	@ApiModelProperty(value = "是否为序列号")
	private int isSn;
	/**
	 * 序列号列表
	 */
	@ApiModelProperty(value = "序列号列表")
	private List<String> serialList;

	/**
	 * 包装明细信息
	 */
	@ApiModelProperty("包装明细信息")
	private List<SkuPackageDetailViewVO> packageDetails;
	/**
	 * 默认包装明细
	 */
	@ApiModelProperty("默认包装明细")
	private SkuPackageDetailViewVO defaultPackageDetail;

	/**
	 * 拣货人
	 */
	@ApiModelProperty("拣货人")
	private String userName;

	/**
	 * 包装ID
	 */
	@ApiModelProperty("包装ID")
	private Long wspId;

	/**
	 * 物品待拣计划总数
	 */
	@ApiModelProperty("物品待拣计划总数")
	private BigDecimal totalPlanQty;

	/**
	 * 物品已拣总数
	 */
	@ApiModelProperty("物品待拣计划总数")
	private BigDecimal totalScanQty;
	@ApiModelProperty("基础计量单位")
	private String baseUm;
}
