package org.nodes.wms.core.outstock.sales.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModelProperty;
import org.nodes.wms.core.outstock.so.vo.SoDetailVO;
import org.nodes.wms.core.outstock.sales.entity.SalesHeader;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.annotations.ApiModel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 销售单主表
视图实体类
 *
 * @author NodeX
 * @since 2021-05-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SalesHeaderVO对象", description = "销售单主表 ")
public class SalesHeaderVO extends SalesHeader {
	private static final long serialVersionUID = 1L;

	/**
	 * 销售单明细
	 */
	@ApiModelProperty("销售单明细")
	private List<SalesDetailVO> detailList = new ArrayList<>();

	/**
	 * 客户ID
	 */
	@ApiModelProperty(value = "客户ID")
	@JsonSerialize(using = ToStringSerializer.class)
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
	 * 出库单明细
	 */
	@ApiModelProperty(value = "出库单明细")
	private IPage<SoDetailVO> soDetailIPage;
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
	 * 报表名称
	 */
	@ApiModelProperty("报表名称")
	private String reportFileName;

	/**
	 * 发运状态描述
	 */
	@ApiModelProperty("发运状态描述")
	private String shipStateDesc;

	/**
	 * 过账状态
	 */
	@ApiModelProperty("过账状态")
	private String postStateCd;
	/**
	 * 过账人
	 */
	@ApiModelProperty("过账人")
	private String postUserCd;
}
