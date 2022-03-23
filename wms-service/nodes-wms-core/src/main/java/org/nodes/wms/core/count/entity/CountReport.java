
package org.nodes.wms.core.count.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.nodes.core.tool.entity.SkuLotBaseEntity;
import org.nodes.core.tool.jackson.BigDecimalSerializer;

import java.math.BigDecimal;

/**
 * 盘点差异报告表实体类
 *
 * @author chz
 * @since 2020-02-20
 */
@Data
@TableName("wms_count_report")
@ApiModel(value = "CountReport对象", description = "盘点差异报告表")
public class CountReport extends SkuLotBaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 盘点单明细ID
	 */
	@ApiModelProperty(value = "盘点单明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "wcrep_id", type = IdType.ASSIGN_ID)
	private Long wcrepId;
	/**
	 * 库房ID
	 */
	@ApiModelProperty(value = "库房ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long whId;
	/**
	 * 盘点单ID
	 */
	@ApiModelProperty(value = "盘点单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long countBillId;
	/**
	 * 单据编码
	 */
	@ApiModelProperty(value = "单据编码")
	private String countBillNo;
	/**
	 * 库存ID
	 */
	@ApiModelProperty(value = "库存ID")
	private Long stockId;
	/**
	 * 库位ID
	 */
	@ApiModelProperty(value = "库位ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long locId;
	/**
	 * 库位编码
	 */
	@ApiModelProperty(value = "库位编码")
	@ExcelProperty("库位")
	private String locCode;
	/**
	 * 容器编码
	 */
	@ApiModelProperty(value = "容器编码")
	private String lpnCode;
	/**
	 * 物品ID
	 */
	@ApiModelProperty(value = "物品ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;
	/**
	 * 物品编码
	 */
	@ApiModelProperty(value = "物品编码")
	@ExcelProperty("物品编码")
	private String skuCode;
	/**
	 * 物品名称
	 */
	@ApiModelProperty(value = "物品名称")
	@ExcelProperty("物品名称")
	private String skuName;
	/**
	 * 系统数量
	 */
	@ApiModelProperty(value = "系统数量")
	@ExcelProperty("系统数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal wmsQty;
	/**
	 * 盘点数量
	 */
	@ApiModelProperty(value = "盘点数量")
	@ExcelProperty("盘点数量")
	@JsonSerialize(using = BigDecimalSerializer.class)
	private BigDecimal countQty;
	/**
	 * 计量单位名称
	 */
	@ApiModelProperty(value = "计量单位名称")
	@ExcelProperty("计量单位")
	private String wsuName;
	/**
	 * 包装ID
	 */
	@ApiModelProperty(value = "包装ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wspId;
	/**
	 * 序列号
	 */
	@ApiModelProperty(value = "序列号")
	@ExcelProperty("序列号")
	private String serialNumber;
	/**
	 * 系统批次号
	 */
	@ApiModelProperty(value = "系统批次号")
	@ExcelProperty("系统批次")
	private String systemLot;
	/**
	 * 盘点批次号
	 */
	@ApiModelProperty(value = "盘点批次号")
	@ExcelProperty("盘点批次")
	private String countLot;
	/**
	 * 库位标志
	 */
	@ApiModelProperty(value = "库位标志")
	private Integer locFlag;


}
