
package org.nodes.wms.core.outstock.loading.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 车次明细视图实体类PDA
 *
 * @author chz
 * @since 2020-03-03
 */
@Data
@ApiModel(value = "SoTruckDetailPdaVO对象", description = "车次明细")
public class SoTruckDetailPdaVO  {
	private static final long serialVersionUID = 1L;

	/**
	 * 车次明细ID
	 */
	@ApiModelProperty(value = "车次明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long truckDetailId;
	/**
	 * 车次ID
	 */
	@ApiModelProperty(value = "车次ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long truckId;
	/**
	 * 车次编号
	 */
	@ApiModelProperty(value = "车次编号")
	private String truckCode;
	/**
	 * 发货单ID
	 */
	@ApiModelProperty(value = "发货单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 容器编码
	 */
	@ApiModelProperty(value = "容器编码")
	private String lpnCode;
	/**
	 * 波次ID
	 */
	@ApiModelProperty(value = "波次ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wellenId;
	/**
	 * 运单编号
	 */
	@ApiModelProperty(value = "运单编号")
	private String truckDetailWaybill;
	/**
	 * 备注编码
	 */
	@ApiModelProperty(value = "备注编码")
	private String wmsRemarkCode;
	/**
	 * 库存明细VO
	 */
	@ApiModelProperty(value = "发货明细头表数据")
	private StockPdaVO stockPdaVO;

}
