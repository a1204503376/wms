
package org.nodes.wms.core.outstock.loading.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springblade.core.tenant.mp.TenantEntity;

import java.io.Serializable;

/**
 * 车次明细实体类
 *
 * @author chz
 * @since 2020-03-03
 */
@Data
@ApiModel(value = "SoTruckDetail对象", description = "车次明细")
public class SoTruckDetail extends TenantEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 车次明细ID
	 */
	@ApiModelProperty(value = "车次明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "truck_detail_id", type = IdType.ASSIGN_ID)
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


}
