package org.nodes.wms.core.outstock.inventory.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.outstock.inventory.entity.SoInventory;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.nodes.wms.core.outstock.so.vo.SoPickVO;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 发货清单主表
视图实体类
 *
 * @author NodeX
 * @since 2021-06-09
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SoInventoryVO对象", description = "发货清单主表 ")
public class SoInventoryVO extends SoInventory {
	private static final long serialVersionUID = 1L;

	/**
	 * 客户ID
	 */
	@ApiModelProperty(value = "客户ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@JsonProperty("cId")
	private Long cId;
	/**
	 * 承运商ID
	 */
	@ApiModelProperty(value = "承运商ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long expressId;
	/**
	 * 单据状态
	 */
	@ApiModelProperty(value = "单据状态名称")
	private String soBillStateName;
	/**
	 * 出库方式
	 */
	@ApiModelProperty(value = "出库方式名称")
	private String outstockTypeName;
	/**
	 * 同步状态
	 */
	@ApiModelProperty(value = "同步状态名称")
	private String syncStateName;
	/**
	 * 创建类型
	 */
	@ApiModelProperty(value = "创建类型名称")
	private String createTypeName;
	/**
	 * 货主名称
	 */
	@ApiModelProperty(value = "货主名称")
	private String ownerName;
	/**
	 * 单据类型名称
	 */
	@ApiModelProperty(value = "单据类型名称")
	private String billTypeName;
	/**
	 * 库房名称
	 */
	@ApiModelProperty(value = "库房名称")
	private String whName;
	/**
	 * 实际发货时间
	 */
	@DateTimeFormat(
		pattern = "yyyy-MM-dd"
	)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
	@ApiModelProperty(value = "实际发货时间")
	private Date outStockDateTime;

	/**
	 * 发货方式描述
	 */
	@ApiModelProperty(value = "发货方式描述")
	private String transportDesc;

	/**
	 * 库区类型描述
	 */
	@ApiModelProperty(value = "库区类型描述")
	private String zoneTypeDesc;

	/**
	 * 创建人名称
	 */
	@ApiModelProperty(value = "创建人名称")
	private String billCreator;

	/**
	 * 明细集合
	 */
	private List<SoPickVO> detailList;

	/**
	 * 报表名称
	 */
	@ApiModelProperty("报表名称")
	private String reportFileName;

	/**
	 * 发运状态描述
	 */
	@ApiModelProperty("发运状态描述")
	private String shipStateDesc;
}
