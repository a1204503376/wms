
package org.nodes.wms.core.outstock.so.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springblade.core.tenant.mp.TenantEntity;

/**
 * 波次划分明细表实体类
 *
 * @author pengwei
 * @since 2020-02-10
 */
@Data
@TableName("so_wellen_detail")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "WellenDetail对象", description = "波次划分明细表")
public class WellenDetail extends TenantEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 波次明细ID
	 */
	@ApiModelProperty(value = "波次明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	@TableId(value = "wellen_detail_id", type = IdType.ASSIGN_ID)
	private Long wellenDetailId;
	/**
	 * 波次ID
	 */
	@ApiModelProperty(value = "波次ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long wellenId;
	/**
	 * 订单ID
	 */
	@ApiModelProperty(value = "订单ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soBillId;
	/**
	 * 订单编号
	 */
	@ApiModelProperty(value = "订单编号")
	private String soBillNo;
	/**
	 * 出库单明细ID
	 */
	@ApiModelProperty(value = "出库单明细ID")
	@JsonSerialize(using = ToStringSerializer.class)
	private Long soDetailId;
	/**
	 * 单据种类编码
	 */
	@ApiModelProperty(value = "单据种类编码")
	private String billTypeCd;

	/**
	 * 发货清单id
	 */
	@ApiModelProperty("发货清单id")
	private Long inventoryId;

}
