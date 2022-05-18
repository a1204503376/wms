package org.nodes.wms.core.instock.asn.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 收货单明细表实体类
 *
 * @author zx
 * @since 2019-12-13
 */
@Data
@TableName("asn_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Detail对象", description = "收货单明细表")
public class AsnDetail extends SkuLotBaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	* 收货单明细ID
	*/
	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "收货单明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnDetailId;
	/**
	* 收货单ID
	*/
	@ApiModelProperty(value = "收货单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long asnBillId;
	/**
	* 单据编码
	*/

	/**
	 * 供应商id
	 */
	private Long supplierId;

	@ApiModelProperty(value = "单据编码")
	@NotNull(message = "单据编码不能为空")
	@Length(max = 60,message = "单据编码最大长度为60位")
	private String asnBillNo;
	/**
	* 订单行号
	*/
	@ApiModelProperty(value = "订单行号")
	@NotNull(message = "订单行号不能为空")
	@Length(max = 50,message = "订单行号最大长度为50位")
	private String asnLineNo;

	/**
	* 上位系统单据明细唯一标识
	*/
//	@ApiModelProperty(value = "上位系统单据明细唯一标识")
//	@Length(max = 30,message = "最大长度为30位")
//	private String asnBillDetailKey;
	/**
	* 物品ID
	*/
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull(message = "物品ID不能为空")
	private Long skuId;
	/**
	* 包装ID
	*/
	@ApiModelProperty(value = "包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@NotNull(message = "包装ID不能为空")
	private Long wspId;
	/**
	* 层级
	*/
	@ApiModelProperty(value = "层级")
	private Integer skuLevel;
	/**
	* 物品编码
	*/
	@ApiModelProperty(value = "物品编码")
	@NotNull(message = "物品编码不能为空")
	@Length(max = 30,message = "最大长度为30位")
	private String skuCode;
	/**
	* 物品名称
	*/
	@ApiModelProperty(value = "物品名称")
	@NotNull(message = "物品名称不能为空")
	@Length(max = 30,message = "最大长度为30位")
	private String skuName;
	/**
	* 规格
	*/
	@ApiModelProperty(value = "规格")
	@Length(max = 30,message = "最大长度为30位")
	private String skuSpec;
	/**
	* 换算倍率
	*/
	@ApiModelProperty(value = "换算倍率")
	private Integer convertQty;
	/**
	* 计量单位编码
	*/
	@ApiModelProperty(value = "计量单位编码")
	@Length(max = 30,message = "最大长度为30位")
	@NotNull(message = "计量单位编码不能为空")
	private String umCode;
	/**
	* 计量单位名称
	*/
	@ApiModelProperty(value = "计量单位名称")
	@NotNull(message = "计量单位名称不能为空")
	@Length(max = 30,message = "最大长度为30位")
	private String umName;
	/**
	* 基础计量单位编码
	*/
	@ApiModelProperty(value = "基础计量单位编码")
	@NotNull(message = "基础计量单位编码不能为空")
	@Length(max = 30,message = "最大长度为30位")
	private String baseUmCode;
	/**
	* 基础计量单位名称
	*/
	@ApiModelProperty(value = "基础计量单位名称")
	@NotNull(message = "基础计量单位名称不能为空")
	@Length(max = 30,message = "最大长度为30位")
	private String baseUmName;
	/**
	* 计划数量
	*/
	@ApiModelProperty(value = "计划数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	@NotNull(message = "计划数量不能为空")
	@Digits(integer = 20 , fraction = 6, message = "计划数量整数位长度不能超过20,小数位不能超过6:planQty")
	private BigDecimal planQty;
	/**
	* 实际数量
	*/
	@ApiModelProperty(value = "实际数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	@Digits(integer = 20 , fraction = 6, message = "实际数量整数位长度不能超过20,小数位不能超过6:scanQty")
	private BigDecimal scanQty;
	/**
	* 剩余数量
	*/
	@ApiModelProperty(value = "剩余数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	@Digits(integer = 20 , fraction = 6, message = "剩余数量整数位长度不能超过20,小数位不能超过6:surplusQty")
	private BigDecimal surplusQty;
	/**
	* 收货库房
	*/
//	@ApiModelProperty(value = "收货库房")
//	@Length(max = 30,message = "最大长度为30位")
//	private String incomeWhCode;
	/**
	* 单价
	*/
	@ApiModelProperty(value = "单价")
	private BigDecimal detailPrice;
	/**
	* 明细总金额
	*/
//	@ApiModelProperty(value = "明细总金额")
//	private BigDecimal detailAmount;
	/**
	* 上位系统备注
	*/
//	@ApiModelProperty(value = "上位系统备注")
//	@Length(max = 30,message = "最大长度为30位")
//	private String asnDetailRemark;
	/**
	* 接收状态
	*/
	@ApiModelProperty(value = "接收状态")
	private Integer detailStatus;
	/**
	 * 上位系统单据唯一标识
	 */
//	@ApiModelProperty(value = "上位系统单据唯一标识")
//	@Length(max = 30,message = "最大长度为30位")
//	private String billKey;
	/**
	 * 上位系统单编号
	 */
//	@ApiModelProperty(value = "上位系统单编号")
//	@Length(max = 30,message = "上位系统单编号最大长度为30位")
//	private String orderNo;
	/**
	 * 差异报告Id
	 */
//	@ApiModelProperty(value = "差异报告Id")
//	@JsonSerialize(using = ToStringSerializer.class)
//	private Long wcrepId;

	/**
	 * 序列号导入状态
	 */
	@ApiModelProperty(value = "序列号导入状态")
	private String importSn;
	/**
	 * 物品分类
	 */
//	@ApiModelProperty(value = "物品分类")
//	@TableField(exist = false)
//	private String skuType;
}
