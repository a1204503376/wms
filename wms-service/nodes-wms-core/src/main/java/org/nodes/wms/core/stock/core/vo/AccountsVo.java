package org.nodes.wms.core.stock.core.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 台账类
 */
@Data
@ApiModel("台账类")
public class AccountsVo {
	/**
	 * 库存id
	 */
//	@JsonIgnore
//	@ApiModelProperty("库存id")
//	private String stockId;
	/**
	 * 物料编号
	 */
//	@ExcelColumnName("物料编号")
//	@ApiModelProperty("物料编号")
//	private String skuCode;
	/**
	 * 物料名称
	 */
//	@ExcelColumnName("物料名称")
//	@ApiModelProperty("物料名称")
//	private String skuName;
	/**
	 * 包装
	 */
//	@ExcelColumnName("包装")
//	@ApiModelProperty("包装")
//	private String wspName;
	/**
	 * 计量单位
	 */
//	@ExcelColumnName("计量单位")
//	@ApiModelProperty("计量单位")
//	private String wsuName;
	/**
	 * 仓库
	 */
//	@ExcelColumnName("仓库")
//	@ApiModelProperty("仓库")
//	private String whName;
	/**
	 * 库区
	 */
//	@ExcelColumnName("库区")
//	@ApiModelProperty("库区")
//	private String zoneName;
	/**
	 * 库位
	 */
//	@ExcelColumnName("库位")
//	@ApiModelProperty("库位")
//	private String locCode;
	/**
	 * 库存
	 */
//	@ExcelColumnName("库存")
//	@ApiModelProperty("库存")
//	@JsonSerialize(using = BigDecimalSerializer.class)
//	private BigDecimal stockQty;
	/**
	 * 单据编号
	 */
	@ExcelProperty("单据编号")
	@ApiModelProperty("单据编号")
	private String billNo;
	/**
	 * 操作人员
	 */
	@ExcelProperty("操作人员")
	@ApiModelProperty("操作人员")
	private String optUser;
	/**
	 * 操作数量
	 */
	@ExcelProperty("操作数量")
	@ApiModelProperty("操作数量")
	private BigDecimal optQty;
	/**
	 * 操作时间
	 */
	@ExcelProperty("操作时间")
	@ApiModelProperty("操作时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:dd")
	private String optTime;
	/**
	 * 业务类型
	 */
	@ExcelProperty("业务类型")
	@ApiModelProperty("业务类型")
	private String optType;

	private Boolean hasChildren;

	private String uuid;
	/**
	 * 批次号
	 */
//	@ExcelColumnName("批次号")
//	@ApiModelProperty("批次号")
//	private String lotNumber;
	/**
	 * 货主
	 */
//	@ExcelColumnName("货主")
//	@ApiModelProperty("货主")
//	private String ownerName;
}
