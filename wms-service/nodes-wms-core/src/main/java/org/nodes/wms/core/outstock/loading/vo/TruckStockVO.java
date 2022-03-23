package org.nodes.wms.core.outstock.loading.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.nodes.wms.core.outstock.loading.entity.TruckStock;

/**
 * 视图实体类
 *
 * @author pengwei
 * @since 2020-04-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "TruckStockVO对象", description = "TruckStockVO对象")
public class TruckStockVO extends TruckStock {
	private static final long serialVersionUID = 1L;

	/**
	 * 车次编号
	 */
	@ApiModelProperty("车次编号")
	private String truckCode;
	/**
	 * 车次状态
	 */
	@ApiModelProperty("车次状态")
	private String truckStateDesc;
	/**
	 * 车牌号
	 */
	@ApiModelProperty("车牌号")
	private String plateNumber;
	/**
	 * 司机
	 */
	@ApiModelProperty("司机")
	private String driverName;
	/**
	 * 电话
	 */
	@ApiModelProperty("电话")
	private String driverPhone;
	/**
	 * 库房
	 */
	@ApiModelProperty("库房")
	private String whName;
	/**
	 * 库区
	 */
	@ApiModelProperty("库区")
	private String zoneName;
	/**
	 * 库位
	 */
	@ApiModelProperty("库位")
	private String locCode;
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
	 * 单位
	 */
	@ApiModelProperty("单位")
	private String umName;
}
